package com.kelvinapps.rxfirebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Nick Moskalenko on 18/11/2018.
 */
public class RxFirebaseFunctions {

    @NonNull
    public static Observable<HttpsCallableResult> getHttpsCallable(@NonNull final FirebaseFunctions functions,
                                                                   @NonNull final String name,
                                                                   @Nullable final Object data) {

        return Observable.unsafeCreate(new Observable.OnSubscribe<HttpsCallableResult>() {
            @Override
            public void call(Subscriber<? super HttpsCallableResult> subscriber) {
                RxTask.assignOnTask(subscriber, functions.getHttpsCallable(name).call(data));
            }
        });
    }

    @NonNull
    public static Observable<HttpsCallableResult> getHttpsCallable(@NonNull final FirebaseFunctions functions,
                                                                   @NonNull final String name) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<HttpsCallableResult>() {
            @Override
            public void call(Subscriber<? super HttpsCallableResult> subscriber) {
                RxTask.assignOnTask(subscriber, functions.getHttpsCallable(name).call());
            }
        });
    }
}
