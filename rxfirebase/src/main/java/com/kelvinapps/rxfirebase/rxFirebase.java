package com.kelvinapps.rxfirebase;

import android.support.annotation.NonNull;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Created by Nick Moskalenko on 15/05/2016.
 */
public class rxFirebase {

    public static Observable<AuthData> authAnonymously(@NonNull final Firebase firebase) {
        return Observable.create(new Observable.OnSubscribe<AuthData>() {
            @Override
            public void call(final Subscriber<? super AuthData> subscriber) {
                firebase.authAnonymously(new ObservableAuthResultHandler(subscriber));
            }
        });
    }

    public static Observable<AuthData> authWithPassword(@NonNull final Firebase firebase,
                                                        @NonNull final String email,
                                                        @NonNull final String password) {
        return Observable.create(new Observable.OnSubscribe<AuthData>() {
            @Override
            public void call(final Subscriber<? super AuthData> subscriber) {
                firebase.authWithPassword(email, password, new ObservableAuthResultHandler(subscriber));
            }
        });
    }

    public static Observable<AuthData> authWithOAuthToken(@NonNull final Firebase firebase,
                                                          @NonNull final String provider,
                                                          @NonNull final String token) {
        return Observable.create(new Observable.OnSubscribe<AuthData>() {
            @Override
            public void call(final Subscriber<? super AuthData> subscriber) {
                firebase.authWithOAuthToken(provider, token, new ObservableAuthResultHandler(subscriber));
            }
        });
    }

    public static Observable<AuthData> authWithOAuthToken(@NonNull final Firebase firebase,
                                                          @NonNull final String provider,
                                                          @NonNull final Map<String, String> options) {
        return Observable.create(new Observable.OnSubscribe<AuthData>() {
            @Override
            public void call(final Subscriber<? super AuthData> subscriber) {
                firebase.authWithOAuthToken(provider, options, new ObservableAuthResultHandler(subscriber));
            }
        });
    }

    public static Observable<AuthData> authWithCustomToken(@NonNull final Firebase firebase,
                                                           @NonNull final String token) {
        return Observable.create(new Observable.OnSubscribe<AuthData>() {
            @Override
            public void call(final Subscriber<? super AuthData> subscriber) {
                firebase.authWithCustomToken(token, new ObservableAuthResultHandler(subscriber));
            }
        });
    }

    public static Observable<AuthData> observeAuthState(@NonNull final Firebase firebase) {

        return Observable.create(new Observable.OnSubscribe<AuthData>() {
            @Override
            public void call(final Subscriber<? super AuthData> subscriber) {
                final Firebase.AuthStateListener listener = firebase.addAuthStateListener(new Firebase.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(AuthData authData) {
                        subscriber.onNext(authData);
                    }
                });

                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        firebase.removeAuthStateListener(listener);
                    }
                }));
            }
        });
    }

    public static <T> Observable<T> observeSingleValue(@NonNull final Query query, @NonNull final Class<T> clazz) {

        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        subscriber.onNext(dataSnapshot.getValue(clazz));
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        onError(subscriber, firebaseError);
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
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            items.add(postSnapshot.getValue(clazz));
                        }
                        subscriber.onNext(items);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        onError(subscriber, firebaseError);
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
                            public void onCancelled(FirebaseError error) {
                                onError(subscriber, error);
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


    private static void onError(@NonNull Subscriber subscriber, @NonNull FirebaseError firebaseError) {
        subscriber.onError(new rxFirebaseException(firebaseError));
    }


    private static class ObservableAuthResultHandler implements Firebase.AuthResultHandler {

        private final Observer<? super AuthData> observer;

        private ObservableAuthResultHandler(Observer<? super AuthData> observer) {
            this.observer = observer;
        }

        @Override
        public void onAuthenticated(AuthData authData) {
            observer.onNext(authData);
            observer.onCompleted();
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            observer.onError(new rxFirebaseException(firebaseError));
        }
    }
}
