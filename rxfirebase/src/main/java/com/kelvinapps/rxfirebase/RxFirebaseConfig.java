package com.kelvinapps.rxfirebase;

import android.support.annotation.NonNull;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Nick Moskalenko on 24/05/2016.
 */
public class RxFirebaseConfig {

    @NonNull
    public static Observable<Void> fetch(@NonNull final FirebaseRemoteConfig config) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(final Subscriber<? super Void> subscriber) {
                RxHandler.assignOnTask(subscriber, config.fetch());
            }
        });
    }

    @NonNull
    public static Observable<Void> fetch(@NonNull final FirebaseRemoteConfig config, final long cacheExpirationSeconds) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(final Subscriber<? super Void> subscriber) {
                RxHandler.assignOnTask(subscriber, config.fetch(cacheExpirationSeconds));
            }
        });
    }

}
