package com.kelvinapps.rxfirebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kelvinapps.rxfirebase.exceptions.rxFirebaseDataCastException;
import com.kelvinapps.rxfirebase.exceptions.rxFirebaseDataException;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Created by Nick Moskalenko on 15/05/2016.
 */
public class rxFirebaseDatabase {

    public static <T> Observable<T> observeSingleValue(@NonNull final Query query, @NonNull final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        T value = dataSnapshot.getValue(clazz);
                        if (value != null) {
                            subscriber.onNext(value);
                        } else {
                            subscriber.onError(new rxFirebaseDataCastException("unable to cast firebase data response to " + clazz.getSimpleName()));
                        }

                        subscriber.onCompleted();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        subscriber.onError(new rxFirebaseDataException(error));
                    }
                });

            }
        });
    }

    public static <T> Observable<List<T>> observeValuesList(final Query query, final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<List<T>>() {
            @Override
            public void call(final Subscriber<? super List<T>> subscriber) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<T> items = new ArrayList<T>();
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            T value = childSnapshot.getValue(clazz);
                            if (value == null) {
                                subscriber.onError(new rxFirebaseDataCastException("unable to cast firebase data response to " + clazz.getSimpleName()));
                            } else {
                                items.add(value);
                            }
                        }
                        subscriber.onNext(items);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        subscriber.onError(new rxFirebaseDataException(error));
                    }
                });
            }

        });
    }

    public static <T> Observable<rxFirebaseChildEvent<T>> observeChildrenEvents(@NonNull final Query ref, @NonNull final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<rxFirebaseChildEvent<T>>() {
            @Override
            public void call(final Subscriber<? super rxFirebaseChildEvent<T>> subscriber) {
                final ChildEventListener childEventListener =
                        ref.addChildEventListener(new ChildEventListener() {

                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                                subscriber.onNext(
                                        new rxFirebaseChildEvent<T>(dataSnapshot.getValue(clazz),
                                                previousChildName,
                                                rxFirebaseChildEvent.EventType.ADDED));
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                                subscriber.onNext(
                                        new rxFirebaseChildEvent<T>(dataSnapshot.getValue(clazz),
                                                previousChildName,
                                                rxFirebaseChildEvent.EventType.CHANGED));
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                subscriber.onNext(
                                        new rxFirebaseChildEvent<T>(dataSnapshot.getValue(clazz),
                                                rxFirebaseChildEvent.EventType.REMOVED));
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                                subscriber.onNext(
                                        new rxFirebaseChildEvent<T>(dataSnapshot.getValue(clazz),
                                                previousChildName,
                                                rxFirebaseChildEvent.EventType.MOVED));
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                subscriber.onError(new rxFirebaseDataException(error));
                            }
                        });

                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        ref.removeEventListener(childEventListener);
                    }
                }));
            }
        });
    }

}
