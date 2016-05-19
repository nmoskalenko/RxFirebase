package com.kelvinapps.rxfirebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Created by Nick Moskalenko on 15/05/2016.
 */
public class rxFirebaseAuth {

    public static Observable<AuthResult> signInAnonymously(@NonNull final FirebaseAuth firebaseAuth) {
        return Observable.create(new Observable.OnSubscribe<AuthResult>() {
            @Override
            public void call(final Subscriber<? super AuthResult> subscriber) {
                ObservableAuthResultHandler handler = new ObservableAuthResultHandler(subscriber);
                firebaseAuth.signInAnonymously()
                        .addOnSuccessListener(handler)
                        .addOnFailureListener(handler)
                        .addOnCompleteListener(handler);
            }
        });
    }

    public static Observable<AuthResult> signInWithEmailAndPassword(@NonNull final FirebaseAuth firebaseAuth,
                                                                    @NonNull final String email,
                                                                    @NonNull final String password) {
        return Observable.create(new Observable.OnSubscribe<AuthResult>() {
            @Override
            public void call(final Subscriber<? super AuthResult> subscriber) {
                ObservableAuthResultHandler handler = new ObservableAuthResultHandler(subscriber);
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(handler)
                        .addOnFailureListener(handler)
                        .addOnCompleteListener(handler);
            }
        });
    }

    public static Observable<AuthResult> signInWithCredential(@NonNull final FirebaseAuth firebaseAuth,
                                                              @NonNull final AuthCredential credential) {
        return Observable.create(new Observable.OnSubscribe<AuthResult>() {
            @Override
            public void call(final Subscriber<? super AuthResult> subscriber) {
                ObservableAuthResultHandler handler = new ObservableAuthResultHandler(subscriber);
                firebaseAuth.signInWithCredential(credential)
                        .addOnSuccessListener(handler)
                        .addOnFailureListener(handler)
                        .addOnCompleteListener(handler);
            }
        });
    }

    public static Observable<AuthResult> signInWithCustomToken(@NonNull final FirebaseAuth firebaseAuth,
                                                               @NonNull final String token) {
        return Observable.create(new Observable.OnSubscribe<AuthResult>() {
            @Override
            public void call(final Subscriber<? super AuthResult> subscriber) {
                ObservableAuthResultHandler handler = new ObservableAuthResultHandler(subscriber);
                firebaseAuth.signInWithCustomToken(token)
                        .addOnSuccessListener(handler)
                        .addOnFailureListener(handler)
                        .addOnCompleteListener(handler);
            }
        });
    }

    public static Observable<FirebaseUser> observeAuthState(@NonNull final FirebaseAuth firebaseAuth) {

        return Observable.create(new Observable.OnSubscribe<FirebaseUser>() {
            @Override
            public void call(final Subscriber<? super FirebaseUser> subscriber) {
                final FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        subscriber.onNext(firebaseAuth.getCurrentUser());
                    }
                };
                firebaseAuth.addAuthStateListener(authStateListener);

                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        firebaseAuth.removeAuthStateListener(authStateListener);
                    }
                }));
            }
        });
    }

    private static class ObservableAuthResultHandler
            implements OnSuccessListener<AuthResult>, OnFailureListener, OnCompleteListener<AuthResult> {

        private final Observer<? super AuthResult> observer;

        private ObservableAuthResultHandler(Observer<? super AuthResult> observer) {
            this.observer = observer;
        }

        @Override
        public void onSuccess(AuthResult authResult) {
            observer.onNext(authResult);
        }

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            observer.onCompleted();
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            observer.onError(e);
        }
    }
}
