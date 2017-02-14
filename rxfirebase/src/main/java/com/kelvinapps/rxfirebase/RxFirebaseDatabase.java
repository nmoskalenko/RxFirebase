package com.kelvinapps.rxfirebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kelvinapps.rxfirebase.exceptions.RxFirebaseDataException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Function;

/**
 * Created by Nick Moskalenko on 15/05/2016.
 * Adapted to RxJava 2 by Remous-Aris Koutsiamanis on 13/02/2017.
 */
public class RxFirebaseDatabase {

    @NonNull
    public static Observable<DataSnapshot> observeValueEvent(final Query query) {
        return Observable.create(new ObservableOnSubscribe<DataSnapshot>() {
            @Override
            public void subscribe(final ObservableEmitter<DataSnapshot> subscriber) throws Exception {
                final ValueEventListener valueEventListener = query.addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!subscriber.isDisposed()) {
                                subscriber.onNext(dataSnapshot);
                            }
                        }

                        @Override
                        public void onCancelled(final DatabaseError error) {
                            if (!subscriber.isDisposed()) {
                                subscriber.onError(new RxFirebaseDataException(error));
                            }
                        }
                    });

              subscriber.setCancellable(new Cancellable()
              {
                  @Override public void cancel() throws Exception
                  {
                    query.removeEventListener(valueEventListener);
                  }
              });
            }
        });
    }

    @NonNull
    public static Observable<DataSnapshot> observeSingleValueEvent(@NonNull final Query query) {
        return Observable.create(new ObservableOnSubscribe<DataSnapshot>() {
            @Override
            public void subscribe(final ObservableEmitter<DataSnapshot> subscriber) throws Exception {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!subscriber.isDisposed()) {
                            subscriber.onNext(dataSnapshot);
                            subscriber.onComplete();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        if (!subscriber.isDisposed()) {
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
        return Observable.create(new ObservableOnSubscribe<RxFirebaseChildEvent<DataSnapshot>>() {
            @Override
            public void subscribe(final ObservableEmitter<RxFirebaseChildEvent<DataSnapshot>> subscriber) throws Exception {
                final ChildEventListener childEventListener = query.addChildEventListener(
                    new ChildEventListener() {

                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                            if (!subscriber.isDisposed()) {
                                subscriber.onNext(
                                        new RxFirebaseChildEvent<DataSnapshot>(dataSnapshot.getKey(), dataSnapshot, previousChildName,
                                                RxFirebaseChildEvent.EventType.ADDED));
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                            if (!subscriber.isDisposed()) {
                                subscriber.onNext(
                                        new RxFirebaseChildEvent<DataSnapshot>(dataSnapshot.getKey(), dataSnapshot, previousChildName,
                                                RxFirebaseChildEvent.EventType.CHANGED));
                            }
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            if (!subscriber.isDisposed()) {
                                subscriber.onNext(new RxFirebaseChildEvent<DataSnapshot>(dataSnapshot.getKey(), dataSnapshot,
                                        RxFirebaseChildEvent.EventType.REMOVED));
                            }
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                            if (!subscriber.isDisposed()) {
                                subscriber.onNext(
                                        new RxFirebaseChildEvent<DataSnapshot>(dataSnapshot.getKey(), dataSnapshot, previousChildName,
                                                RxFirebaseChildEvent.EventType.MOVED));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            if (!subscriber.isDisposed()) {
                                subscriber.onError(new RxFirebaseDataException(error));
                            }
                        }
                    });
              subscriber.setCancellable(new Cancellable()
              {
                  @Override public void cancel() throws Exception
                  {
                    query.removeEventListener(childEventListener);
                  }
              });
            }
        });
    }

    @NonNull
    public static <T> Observable<Maybe<T>> observeValueEvent(@NonNull final Query query,
                                                           @NonNull final Class<T> clazz) {
        return observeValueEvent(query, DataSnapshotMapper.of(clazz));
    }

    @NonNull
    public static <T> Observable<Maybe<T>> observeSingleValueEvent(@NonNull final Query query,
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
                                                      @NonNull final Function<? super DataSnapshot, ? extends T> mapper) {
        return observeValueEvent(query).map(mapper);
    }

    @NonNull
    public static <T> Observable<T> observeSingleValueEvent(@NonNull final Query query,
                                                            @NonNull final Function<? super DataSnapshot, ? extends T> mapper) {
        return observeSingleValueEvent(query).map(mapper);
    }

    @NonNull
    public static <T> Observable<RxFirebaseChildEvent<T>> observeChildEvent(
            @NonNull final Query query, @NonNull final Function<? super RxFirebaseChildEvent<DataSnapshot>, ? extends RxFirebaseChildEvent<T>> mapper) {
        return observeChildEvent(query).map(mapper);
    }
}
