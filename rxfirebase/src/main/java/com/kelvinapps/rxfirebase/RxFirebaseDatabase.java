package com.kelvinapps.rxfirebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kelvinapps.rxfirebase.exceptions.RxFirebaseDataException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

/**
 * Created by Nick Moskalenko on 15/05/2016.
 */
public class RxFirebaseDatabase {

    @NonNull
    public static <T> Observable<RxFirebaseChildEvent<T>> observeChildEvent(
            @NonNull final Query query, @NonNull final Class<T> clazz) {
        return observeChildEvent(query).map(RxFirebaseUtils.mapRxFirebaseChildEventClass(clazz));
    }

    @NonNull
    public static Observable<RxFirebaseChildEvent<DataSnapshot>> observeChildEvent(
            @NonNull final Query query) {
        return Observable.create(new Observable.OnSubscribe<RxFirebaseChildEvent<DataSnapshot>>() {
            @Override
            public void call(final Subscriber<? super RxFirebaseChildEvent<DataSnapshot>> subscriber) {
                final ChildEventListener childEventListener = query.addChildEventListener(
                        new ChildEventListener() {

                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onNext(
                                            new RxFirebaseChildEvent<DataSnapshot>(dataSnapshot, previousChildName,
                                                    RxFirebaseChildEvent.EventType.ADDED));
                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onNext(
                                            new RxFirebaseChildEvent<DataSnapshot>(dataSnapshot, previousChildName,
                                                    RxFirebaseChildEvent.EventType.CHANGED));
                                }
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onNext(new RxFirebaseChildEvent<DataSnapshot>(dataSnapshot,
                                            RxFirebaseChildEvent.EventType.REMOVED));
                                }
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onNext(
                                            new RxFirebaseChildEvent<DataSnapshot>(dataSnapshot, previousChildName,
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
                        query.removeEventListener(childEventListener);
                    }
                }));
            }
        });
    }

    @NonNull
    public static <T> Observable<T> observeSingleValueEvent(@NonNull final Query query,
                                                            @NonNull final Class<T> clazz) {
        return observeSingleValueEvent(query).map(RxFirebaseUtils.mapDataSnapshotClass(clazz));
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
    public static <T> Observable<T> observeValueEvent(@NonNull final Query query,
                                                      @NonNull final Class<T> clazz) {
        return observeValueEvent(query).map(RxFirebaseUtils.mapDataSnapshotClass(clazz));
    }

    @NonNull
    public static Observable<DataSnapshot> observeValueEvent(final Query query) {
        return Observable.create(new Observable.OnSubscribe<DataSnapshot>() {
            @Override
            public void call(final Subscriber<? super DataSnapshot> subscriber) {
                final ValueEventListener valueEventListener = query.addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onNext(dataSnapshot);
                                }
                            }

                            @Override
                            public void onCancelled(final DatabaseError error) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onError(new RxFirebaseDataException(error));
                                }
                            }
                        });

                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        query.removeEventListener(valueEventListener);
                    }
                }));
            }
        });
    }

    @NonNull
    public static Observable<DataSnapshot> observeSingleValueChildrenEvent(
            @NonNull final Query query) {
        return observeSingleValueEvent(query).map(new Func1<DataSnapshot, Iterable<DataSnapshot>>() {
            @Override
            public Iterable<DataSnapshot> call(DataSnapshot dataSnapshot) {
                return dataSnapshot.getChildren();
            }
        }).flatMap(new Func1<Iterable<DataSnapshot>, Observable<DataSnapshot>>() {
            @Override
            public Observable<DataSnapshot> call(Iterable<DataSnapshot> dataSnapshots) {
                return Observable.from(dataSnapshots);
            }
        });
    }

    @NonNull
    public static <T> Observable<T> observeSingleValueChildrenEvent(@NonNull final Query query,
                                                                    @NonNull final Class<T> clazz) {
        return observeSingleValueChildrenEvent(query).map(RxFirebaseUtils.mapDataSnapshotClass(clazz));
    }

    @NonNull
    public static <T> Observable<List<T>> observeSingleValueChildrenEventAsList(
            @NonNull final Query query, @NonNull final Class<T> clazz) {
        return observeSingleValueChildrenEvent(query, clazz).toList();
    }

    @NonNull
    public static <T> Observable<Map<String, T>> observeSingleValueChildrenEventAsMap(
            @NonNull final Query query, @NonNull final Class<T> clazz) {
        return observeSingleValueChildrenEvent(query).toMap(
                new Func1<DataSnapshot, String>() {
                    @Override
                    public String call(DataSnapshot dataSnapshot) {
                        return dataSnapshot.getKey();
                    }
                },
                RxFirebaseUtils.mapDataSnapshotClass(clazz),
                new Func0<Map<String, T>>() {
                    @Override
                    public Map<String, T> call() {
                        return new LinkedHashMap<>();
                    }
                });
    }

    @Deprecated
    @NonNull
    public static <T> Observable<T> observeSingleValue(@NonNull final Query query,
                                                       @NonNull final Class<T> clazz) {
        return observeSingleValueEvent(query, clazz);
    }

    @Deprecated
    @NonNull
    public static <T> Observable<T> observeValue(@NonNull final Query query,
                                                 @NonNull final Class<T> clazz) {
        return observeValueEvent(query, clazz);
    }

    @Deprecated
    @NonNull
    public static <T> Observable<T> observeValues(@NonNull final Query query,
                                                  @NonNull final Class<T> clazz) {
        return observeSingleValueChildrenEvent(query, clazz);
    }

    @Deprecated
    @NonNull
    public static <T> Observable<List<T>> observeValuesList(@NonNull final Query query,
                                                            @NonNull final Class<T> clazz) {
        return observeSingleValueChildrenEventAsList(query, clazz);
    }

    @Deprecated
    @NonNull
    public static <T> Observable<LinkedHashMap<String, T>> observeValuesMap(
            @NonNull final Query query, @NonNull final Class<T> clazz) {
        return observeSingleValueChildrenEventAsMap(query, clazz).map(
                new Func1<Map<String, T>, LinkedHashMap<String, T>>() {
                    @Override
                    public LinkedHashMap<String, T> call(Map<String, T> stringTMap) {
                        return new LinkedHashMap<String, T>(stringTMap);
                    }
                });
    }

    @Deprecated
    @NonNull
    public static <T> Observable<RxFirebaseChildEvent<T>> observeChildrenEvents(
            @NonNull final Query query, @NonNull final Class<T> clazz) {
        return observeChildEvent(query, clazz);
    }
}
