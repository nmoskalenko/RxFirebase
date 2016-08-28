package com.kelvinapps.rxfirebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kelvinapps.rxfirebase.exceptions.RxFirebaseDataException;

import rx.AsyncEmitter;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Nick Moskalenko on 15/05/2016.
 */
public class RxFirebaseDatabase {

    @NonNull
    public static Observable<DataSnapshot> observeValueEvent(final Query query) {
        return Observable.fromAsync(new Action1<AsyncEmitter<DataSnapshot>>() {
            @Override
            public void call(final AsyncEmitter<DataSnapshot> dataSnapshotAsyncEmitter) {
                final ValueEventListener valueEventListener = query.addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshotAsyncEmitter.onNext(dataSnapshot);
                            }

                            @Override
                            public void onCancelled(final DatabaseError error) {
                                dataSnapshotAsyncEmitter.onError(new RxFirebaseDataException(error));
                            }
                        });

                dataSnapshotAsyncEmitter.setCancellation(new AsyncEmitter.Cancellable() {
                    @Override
                    public void cancel() {
                        query.removeEventListener(valueEventListener);
                    }
                });
            }
        }, AsyncEmitter.BackpressureMode.BUFFER);
    }

    @NonNull
    public static Observable<DataSnapshot> observeSingleValueEvent(@NonNull final Query query) {
        return Observable.create(new Observable.OnSubscribe<DataSnapshot>() {
            @Override
            public void call(final Subscriber<? super DataSnapshot> subscriber) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(dataSnapshot);
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
    public static Observable<RxFirebaseChildEvent<DataSnapshot>> observeChildEvent(
            @NonNull final Query query) {
        return Observable.fromAsync(new Action1<AsyncEmitter<RxFirebaseChildEvent<DataSnapshot>>>() {
            @Override
            public void call(final AsyncEmitter<RxFirebaseChildEvent<DataSnapshot>> subscriber) {
                final ChildEventListener childEventListener = query.addChildEventListener(
                        new ChildEventListener() {

                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                                subscriber.onNext(
                                        new RxFirebaseChildEvent<>(dataSnapshot, previousChildName,
                                                RxFirebaseChildEvent.EventType.ADDED));
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                                subscriber.onNext(
                                        new RxFirebaseChildEvent<>(dataSnapshot, previousChildName,
                                                RxFirebaseChildEvent.EventType.CHANGED));
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                subscriber.onNext(new RxFirebaseChildEvent<>(dataSnapshot,
                                        RxFirebaseChildEvent.EventType.REMOVED));
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                                subscriber.onNext(
                                        new RxFirebaseChildEvent<>(dataSnapshot, previousChildName,
                                                RxFirebaseChildEvent.EventType.MOVED));
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                subscriber.onError(new RxFirebaseDataException(error));
                            }
                        });

                subscriber.setCancellation(new AsyncEmitter.Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        query.removeEventListener(childEventListener);
                    }
                });
            }
        }, AsyncEmitter.BackpressureMode.BUFFER);
    }

    @NonNull
    public static <T> Observable<T> observeValueEvent(@NonNull final Query query,
                                                      @NonNull final Class<T> clazz) {
        return observeValueEvent(query, DataSnapshotMapper.of(clazz));
    }

    @NonNull
    public static <T> Observable<T> observeSingleValueEvent(@NonNull final Query query,
                                                            @NonNull final Class<T> clazz) {
        return observeSingleValueEvent(query, DataSnapshotMapper.of(clazz));
    }

    @NonNull
    public static <T> Observable<RxFirebaseChildEvent<T>> observeChildEvent(
            @NonNull final Query query, @NonNull final Class<T> clazz) {
        return observeChildEvent(query, DataSnapshotMapper.ofChildEvent(clazz));
    }

    @NonNull
    public static <T> Observable<T> observeValueEvent(@NonNull final Query query,
                                                      @NonNull final Func1<? super DataSnapshot, ? extends T> mapper) {
        return observeValueEvent(query).map(mapper);
    }

    @NonNull
    public static <T> Observable<T> observeSingleValueEvent(@NonNull final Query query, @NonNull
    final Func1<? super DataSnapshot, ? extends T> mapper) {
        return observeSingleValueEvent(query).map(mapper);
    }

    @NonNull
    public static <T> Observable<RxFirebaseChildEvent<T>> observeChildEvent(
            @NonNull final Query query, @NonNull final Func1<? super RxFirebaseChildEvent<DataSnapshot>, ? extends RxFirebaseChildEvent<T>> mapper) {
        return observeChildEvent(query).map(mapper);
    }
}
