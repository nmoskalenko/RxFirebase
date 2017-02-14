package com.kelvinapps.rxfirebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import io.reactivex.Emitter;

/**
 * Created by Nick Moskalenko on 24/05/2016.
 * Adapted to RxJava 2 by Remous-Aris Koutsiamanis on 13/02/2017.
 */
public class RxHandler<T> implements OnSuccessListener<T>, OnFailureListener, OnCompleteListener<T> {

    private final Emitter<? super T> subscriber;

    private RxHandler(Emitter<? super T> observer) {
        this.subscriber = observer;
    }

    public static <T> void assignOnTask(Emitter<? super T> observer, Task<T> task) {
        RxHandler handler = new RxHandler(observer);
        task.addOnSuccessListener(handler);
        task.addOnFailureListener(handler);
        try {
            task.addOnCompleteListener(handler);
        } catch (Throwable t) {
            // ignore
        }
    }

    @Override
    public void onSuccess(T res) {
        subscriber.onNext(res);
    }

    @Override
    public void onComplete(@NonNull Task<T> task) {
        subscriber.onComplete();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        subscriber.onError(e);
    }
}
