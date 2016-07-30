package com.kelvinapps.rxfirebase;

import com.google.firebase.database.DataSnapshot;
import com.kelvinapps.rxfirebase.exceptions.RxFirebaseDataCastException;

import rx.exceptions.Exceptions;
import rx.functions.Func1;

/**
 * Created by renanferrari on 29/07/16.
 */

public class RxFirebaseUtils {

    public static <T> Func1<DataSnapshot, T> mapDataSnapshotClass(final Class<T> clazz) {
        return new Func1<DataSnapshot, T>() {
            @Override
            public T call(final DataSnapshot dataSnapshot) {
                T value = dataSnapshot.getValue(clazz);
                if (value != null) {
                    return value;
                } else {
                    throw Exceptions.propagate(new RxFirebaseDataCastException(
                            "unable to cast firebase data response to " + clazz.getSimpleName()));
                }
            }
        };
    }

    public static <T> Func1<RxFirebaseChildEvent<DataSnapshot>, RxFirebaseChildEvent<T>> mapRxFirebaseChildEventClass(
            final Class<T> clazz) {
        return new Func1<RxFirebaseChildEvent<DataSnapshot>, RxFirebaseChildEvent<T>>() {
            @Override
            public RxFirebaseChildEvent<T> call(
                    final RxFirebaseChildEvent<DataSnapshot> rxFirebaseChildEvent) {
                DataSnapshot dataSnapshot = rxFirebaseChildEvent.getValue();
                T value = dataSnapshot.getValue(clazz);
                if (value != null) {
                    return new RxFirebaseChildEvent<T>(value, rxFirebaseChildEvent.getPreviousChildName(),
                            rxFirebaseChildEvent.getEventType());
                } else {
                    throw Exceptions.propagate(new RxFirebaseDataCastException(
                            "unable to cast firebase data response to " + clazz.getSimpleName()));
                }
            }
        };
    }
}
