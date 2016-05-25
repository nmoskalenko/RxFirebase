package com.kelvinapps.rxfirebase;

import android.support.annotation.NonNull;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserProfileChangeRequest;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Nick Moskalenko on 24/05/2016.
 */
public class RxFirebaseUser {

    @NonNull
    public static Observable<GetTokenResult> getToken(@NonNull final FirebaseUser firebaseUser,
                                                      final boolean forceRefresh) {
        return Observable.create(new Observable.OnSubscribe<GetTokenResult>() {
            @Override
            public void call(final Subscriber<? super GetTokenResult> subscriber) {
                RxHandler.assignOnTask(subscriber, firebaseUser.getToken(forceRefresh));
            }
        });
    }

    @NonNull
    public static Observable<Void> updateEmail(@NonNull final FirebaseUser firebaseUser,
                                               @NonNull final String email) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(final Subscriber<? super Void> subscriber) {
                RxHandler.assignOnTask(subscriber, firebaseUser.updateEmail(email));
            }
        });
    }

    @NonNull
    public static Observable<Void> updatePassword(@NonNull final FirebaseUser firebaseUser,
                                                  @NonNull final String password) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(final Subscriber<? super Void> subscriber) {
                RxHandler.assignOnTask(subscriber, firebaseUser.updatePassword(password));
            }
        });
    }

    @NonNull
    public static Observable<Void> updateProfile(@NonNull final FirebaseUser firebaseUser,
                                                 @NonNull final UserProfileChangeRequest request) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(final Subscriber<? super Void> subscriber) {
                RxHandler.assignOnTask(subscriber, firebaseUser.updateProfile(request));
            }
        });
    }

    @NonNull
    public static Observable<Void> delete(@NonNull final FirebaseUser firebaseUser) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(final Subscriber<? super Void> subscriber) {
                RxHandler.assignOnTask(subscriber, firebaseUser.delete());
            }
        });
    }

    @NonNull
    public static Observable<Void> reauthenticate(@NonNull final FirebaseUser firebaseUser,
                                                  @NonNull final AuthCredential credential) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(final Subscriber<? super Void> subscriber) {
                RxHandler.assignOnTask(subscriber, firebaseUser.reauthenticate(credential));
            }
        });
    }

    @NonNull
    public static Observable<AuthResult> linkWithCredential(@NonNull final FirebaseUser firebaseUser,
                                                            @NonNull final AuthCredential credential) {
        return Observable.create(new Observable.OnSubscribe<AuthResult>() {
            @Override
            public void call(final Subscriber<? super AuthResult> subscriber) {
                RxHandler.assignOnTask(subscriber, firebaseUser.linkWithCredential(credential));
            }
        });
    }


}
