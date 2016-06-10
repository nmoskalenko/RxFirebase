package com.kelvinapps.rxfirebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kelvinapps.rxfirebase.exceptions.RxFirebaseDataCastException;
import com.kelvinapps.rxfirebase.exceptions.RxFirebaseDataException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Created by Nick Moskalenko on 15/05/2016.
 */
public class RxFirebaseDatabase {

    @NonNull
    public static <T> Observable<T> observeSingleValue(@NonNull final Query query, @NonNull final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        T value = dataSnapshot.getValue(clazz);
                        if (value != null) {
                            if (!subscriber.isUnsubscribed()) {
                                subscriber.onNext(value);
                            }
                        } else {
                            if (!subscriber.isUnsubscribed()) {
                                subscriber.onError(new RxFirebaseDataCastException("unable to cast firebase data response to " + clazz.getSimpleName()));
                            }
                        }

                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(new RxFirebaseDataException(error));
                        }
                    }
                });

            }
        });
    }

    @NonNull
    public static <T> Observable<T> observeValues(@NonNull final Query query, @NonNull final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            T value = childSnapshot.getValue(clazz);
                            if (value == null) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onError(new RxFirebaseDataCastException("unable to cast firebase data response to " + clazz.getSimpleName()));
                                }
                            } else {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onNext(value);
                                }
                            }
                        }
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(new RxFirebaseDataException(error));
                        }
                    }
                });
            }

        });
    }

    @NonNull
    public static <T> Observable<List<T>> observeValuesList(@NonNull final Query query, @NonNull final Class<T> clazz) {
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
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onError(new RxFirebaseDataCastException("unable to cast firebase data response to " + clazz.getSimpleName()));
                                }
                            } else {
                                items.add(value);
                            }
                        }

                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(items);
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(new RxFirebaseDataException(error));
                        }
                    }
                });
            }

        });
    }

    @NonNull
    public static <T> Observable<Map<String, T>> observeValuesMap(@NonNull final Query query, @NonNull final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<Map<String, T>>() {
            @Override
            public void call(final Subscriber<? super Map<String, T>> subscriber) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, T> items = new HashMap<String, T>();
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            T value = childSnapshot.getValue(clazz);
                            if (value == null) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onError(new RxFirebaseDataCastException("unable to cast firebase data response to " + clazz.getSimpleName()));
                                }
                            } else {
                                items.put(childSnapshot.getKey(), value);
                            }
                        }

                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(items);
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(new RxFirebaseDataException(error));
                        }
                    }
                });
            }

        });
    }

    @NonNull
    public static <T> Observable<RxFirebaseChildEvent<T>> observeChildrenEvents(@NonNull final Query ref, @NonNull final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<RxFirebaseChildEvent<T>>() {
            @Override
            public void call(final Subscriber<? super RxFirebaseChildEvent<T>> subscriber) {
                final ChildEventListener childEventListener =
                        ref.addChildEventListener(new ChildEventListener() {

                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onNext(
                                            new RxFirebaseChildEvent<T>(dataSnapshot.getValue(clazz),
                                                    previousChildName,
                                                    RxFirebaseChildEvent.EventType.ADDED));
                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onNext(
                                            new RxFirebaseChildEvent<T>(dataSnapshot.getValue(clazz),
                                                    previousChildName,
                                                    RxFirebaseChildEvent.EventType.CHANGED));
                                }
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onNext(
                                            new RxFirebaseChildEvent<T>(dataSnapshot.getValue(clazz),
                                                    RxFirebaseChildEvent.EventType.REMOVED));
                                }
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onNext(
                                            new RxFirebaseChildEvent<T>(dataSnapshot.getValue(clazz),
                                                    previousChildName,
                                                    RxFirebaseChildEvent.EventType.MOVED));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onError(new RxFirebaseDataException(error));
                                }
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
