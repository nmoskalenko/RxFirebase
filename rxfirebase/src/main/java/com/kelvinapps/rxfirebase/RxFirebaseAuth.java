package com.kelvinapps.rxfirebase;

import android.support.annotation.NonNull;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;

/**
 * Created by Nick Moskalenko on 15/05/2016.
 * Adapted to RxJava 2 by Remous-Aris Koutsiamanis on 13/02/2017.
 */
public class RxFirebaseAuth {

    @NonNull
    public static Observable<AuthResult> signInAnonymously(@NonNull final FirebaseAuth firebaseAuth) {
        return Observable.create(new ObservableOnSubscribe<AuthResult>() {
            @Override
            public void subscribe(ObservableEmitter<AuthResult> subscriber) throws Exception {
                  RxHandler.assignOnTask(subscriber, firebaseAuth.signInAnonymously());
              }
        });
    }

    @NonNull
    public static Observable<AuthResult> signInWithEmailAndPassword(@NonNull final FirebaseAuth firebaseAuth,
                                                                    @NonNull final String email,
                                                                    @NonNull final String password) {
        return Observable.create(new ObservableOnSubscribe<AuthResult>() {
            @Override
            public void subscribe(ObservableEmitter<AuthResult> subscriber) throws Exception {
                  RxHandler.assignOnTask(subscriber, firebaseAuth.signInWithEmailAndPassword(email, password));
              }
        });
    }

    @NonNull
    public static Observable<AuthResult> signInWithCredential(@NonNull final FirebaseAuth firebaseAuth,
                                                              @NonNull final AuthCredential credential) {
        return Observable.create(new ObservableOnSubscribe<AuthResult>() {
            @Override
            public void subscribe(ObservableEmitter<AuthResult> subscriber) throws Exception {
                  RxHandler.assignOnTask(subscriber, firebaseAuth.signInWithCredential(credential));
              }
        });
    }

    @NonNull
    public static Observable<AuthResult> signInWithCustomToken(@NonNull final FirebaseAuth firebaseAuth,
                                                               @NonNull final String token) {
        return Observable.create(new ObservableOnSubscribe<AuthResult>() {
            @Override
            public void subscribe(ObservableEmitter<AuthResult> subscriber) throws Exception {
                  RxHandler.assignOnTask(subscriber, firebaseAuth.signInWithCustomToken(token));
              }
        });
    }

    @NonNull
    public static Observable<AuthResult> createUserWithEmailAndPassword(@NonNull final FirebaseAuth firebaseAuth,
                                                                        @NonNull final String email,
                                                                        @NonNull final String password) {
        return Observable.create(new ObservableOnSubscribe<AuthResult>() {
            @Override
            public void subscribe(ObservableEmitter<AuthResult> subscriber) throws Exception {
                  RxHandler.assignOnTask(subscriber, firebaseAuth.createUserWithEmailAndPassword(email, password));
              }
        });
    }

    @NonNull
    public static Observable<ProviderQueryResult> fetchProvidersForEmail(@NonNull final FirebaseAuth firebaseAuth,
                                                                         @NonNull final String email) {
        return Observable.create(new ObservableOnSubscribe<ProviderQueryResult>() {
            @Override
            public void subscribe(ObservableEmitter<ProviderQueryResult> subscriber) throws Exception {
                  RxHandler.assignOnTask(subscriber, firebaseAuth.fetchProvidersForEmail(email));
              }
        });
    }

    @NonNull
    public static Observable<Void> sendPasswordResetEmail(@NonNull final FirebaseAuth firebaseAuth,
                                                                @NonNull final String email) {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(ObservableEmitter<Void> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, firebaseAuth.sendPasswordResetEmail(email));
            }
        });
    }

    @NonNull
    public static Observable<FirebaseUser> observeAuthState(@NonNull final FirebaseAuth firebaseAuth) {
         return Observable.create(new ObservableOnSubscribe<FirebaseUser>() {
            @Override
            public void subscribe(final ObservableEmitter<FirebaseUser> subscriber) throws Exception {
                final FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth updatedFirebaseAuth) {
                        if (!subscriber.isDisposed()) {
                            subscriber.onNext(updatedFirebaseAuth.getCurrentUser());
                        }
                    }
                };

                firebaseAuth.addAuthStateListener(authStateListener);

                subscriber.setCancellable(new Cancellable()
                {
                    @Override public void cancel() throws Exception
                    {
                        firebaseAuth.removeAuthStateListener(authStateListener);
                    }
                });
            }
        });
    }
}
