package com.kelvinapps.rxfirebase;

import android.support.annotation.NonNull;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.kelvinapps.rxfirebase.exceptions.FirebaseAuthProviderDisabledException;
import com.kelvinapps.rxfirebase.exceptions.FirebaseExpiredTokenException;
import com.kelvinapps.rxfirebase.exceptions.FirebaseGeneralException;
import com.kelvinapps.rxfirebase.exceptions.FirebaseInvalidTokenException;
import com.kelvinapps.rxfirebase.exceptions.FirebaseNetworkErrorException;
import com.kelvinapps.rxfirebase.exceptions.FirebasePermissionDeniedException;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Nick Moskalenko on 15/05/2016.
 */
public class rxFirebase<T> {

    public Observable<T> observeSingleValue(@NonNull final Query query, @NonNull final Class<T> clazz) {

        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        subscriber.onNext(dataSnapshot.getValue(clazz));
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        onError(subscriber, firebaseError);
                    }
                });

            }
        });
    }

    public Observable<List<T>> observeValuesList(final Query query, final Class<T> clazz) {
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
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        onError(subscriber, firebaseError);
                    }
                });
            }

        });
    }

    private void onError(@NonNull Subscriber subscriber, @NonNull FirebaseError firebaseError) {
        switch (firebaseError.getCode()) {
            case FirebaseError.INVALID_TOKEN:
                subscriber.onError(new FirebaseInvalidTokenException(firebaseError.getMessage()));
                break;
            case FirebaseError.AUTHENTICATION_PROVIDER_DISABLED:
                subscriber.onError(new FirebaseAuthProviderDisabledException(firebaseError.getMessage()));
                break;
            case FirebaseError.EXPIRED_TOKEN:
                subscriber.onError(new FirebaseExpiredTokenException(firebaseError.getMessage()));
                break;
            case FirebaseError.NETWORK_ERROR:
                subscriber.onError(new FirebaseNetworkErrorException(firebaseError.getMessage()));
                break;
            case FirebaseError.PERMISSION_DENIED:
                subscriber.onError(new FirebasePermissionDeniedException(firebaseError.getMessage()));
                break;
            default:
                subscriber.onError(new FirebaseGeneralException(firebaseError.getMessage()));
        }
    }
}
