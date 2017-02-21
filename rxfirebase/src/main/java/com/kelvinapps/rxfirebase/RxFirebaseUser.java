package com.kelvinapps.rxfirebase;

import android.support.annotation.NonNull;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserProfileChangeRequest;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Nick Moskalenko on 24/05/2016.
 * Adapted to RxJava 2 by Remous-Aris Koutsiamanis on 13/02/2017.
 */
public class RxFirebaseUser {

    @NonNull
    public static Observable<GetTokenResult> getToken(@NonNull final FirebaseUser firebaseUser,
                                                      final boolean forceRefresh) {
        return Observable.create(new ObservableOnSubscribe<GetTokenResult>() {
            @Override
            public void subscribe(ObservableEmitter<GetTokenResult> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, firebaseUser.getToken(forceRefresh));
            }
        });
    }

    @NonNull
    public static Observable<Void> updateEmail(@NonNull final FirebaseUser firebaseUser,
                                               @NonNull final String email) {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(ObservableEmitter<Void> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, firebaseUser.updateEmail(email));
            }
        });
    }

    @NonNull
    public static Observable<Void> updatePassword(@NonNull final FirebaseUser firebaseUser,
                                                  @NonNull final String password) {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(ObservableEmitter<Void> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, firebaseUser.updatePassword(password));
            }
        });
    }

    @NonNull
    public static Observable<Void> updateProfile(@NonNull final FirebaseUser firebaseUser,
                                                 @NonNull final UserProfileChangeRequest request) {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(ObservableEmitter<Void> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, firebaseUser.updateProfile(request));
            }
        });
    }

    @NonNull
    public static Observable<Void> delete(@NonNull final FirebaseUser firebaseUser) {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(ObservableEmitter<Void> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, firebaseUser.delete());
            }
        });
    }

    @NonNull
    public static Observable<Void> reauthenticate(@NonNull final FirebaseUser firebaseUser,
                                                  @NonNull final AuthCredential credential) {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(ObservableEmitter<Void> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, firebaseUser.reauthenticate(credential));
            }
        });
    }

    @NonNull
    public static Observable<AuthResult> linkWithCredential(@NonNull final FirebaseUser firebaseUser,
                                                            @NonNull final AuthCredential credential) {
        return Observable.create(new ObservableOnSubscribe<AuthResult>() {
            @Override
            public void subscribe(ObservableEmitter<AuthResult> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, firebaseUser.linkWithCredential(credential));
            }
        });
    }


}
