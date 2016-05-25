package com.kelvinapps.rxfirebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import rx.Subscriber;

/**
 * Created by Nick Moskalenko on 24/05/2016.
 */
public class RxHandler<T> implements OnSuccessListener<T>, OnFailureListener, OnCompleteListener<T> {

    private final Subscriber<? super T> subscriber;

    private RxHandler(Subscriber<? super T> observer) {
        this.subscriber = observer;
    }

    public static <T> void assignOnTask(Subscriber<? super T> observer, Task<T> task) {
        RxHandler handler = new RxHandler(observer);
        task.addOnSuccessListener(handler)
                .addOnFailureListener(handler)
                .addOnCompleteListener(handler);
    }

    public static <T> void assignOnTaskNotCompleted(Subscriber<? super T> observer, Task<T> task) {
        RxHandler handler = new RxHandler(observer);
        task.addOnSuccessListener(handler)
                .addOnFailureListener(handler);
    }

    @Override
    public void onSuccess(T res) {
        if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(res);
        }
    }

    @Override
    public void onComplete(@NonNull Task<T> task) {
        if (!subscriber.isUnsubscribed()) {
            subscriber.onCompleted();
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        if (!subscriber.isUnsubscribed()) {
            subscriber.onError(e);
        }
    }
}
