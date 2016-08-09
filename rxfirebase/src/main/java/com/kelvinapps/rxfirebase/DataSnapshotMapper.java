package com.kelvinapps.rxfirebase;

import com.google.firebase.database.DataSnapshot;
import com.kelvinapps.rxfirebase.exceptions.RxFirebaseDataCastException;

import rx.exceptions.Exceptions;
import rx.functions.Func1;

/**
 * Created by renanferrari on 09/08/16.
 */

public abstract class DataSnapshotMapper<T, U> implements Func1<T, U> {

    private DataSnapshotMapper() {
    }

    public static <U> DataSnapshotMapper<DataSnapshot, U> of(Class<U> clazz) {
        return new TypedDataSnapshotMapper<U>(clazz);
    }

    public static <U> DataSnapshotMapper<RxFirebaseChildEvent<DataSnapshot>, RxFirebaseChildEvent<U>> ofChild(Class<U> clazz) {
        return new ChildEventDataSnapshotMapper<U>(clazz);
    }

    private static class TypedDataSnapshotMapper<U> extends DataSnapshotMapper<DataSnapshot, U> {

        private final Class<U> clazz;

        public TypedDataSnapshotMapper(final Class<U> clazz) {
            this.clazz = clazz;
        }

        @Override
        public U call(final DataSnapshot dataSnapshot) {
            U value = dataSnapshot.getValue(clazz);
            if (value != null) {
                return value;
            } else {
                throw Exceptions.propagate(new RxFirebaseDataCastException(
                        "unable to cast firebase data response to " + clazz.getSimpleName()));
            }
        }
    }

    private static class ChildEventDataSnapshotMapper<U> extends DataSnapshotMapper<RxFirebaseChildEvent<DataSnapshot>, RxFirebaseChildEvent<U>> {

        private final DataSnapshotMapper<DataSnapshot, U> dataSnapshotMapper;

        public ChildEventDataSnapshotMapper(final Class<U> clazz) {
            this.dataSnapshotMapper = DataSnapshotMapper.of(clazz);
        }

        @Override
        public RxFirebaseChildEvent<U> call(final RxFirebaseChildEvent<DataSnapshot> rxFirebaseChildEvent) {
            U value = dataSnapshotMapper.call(rxFirebaseChildEvent.getValue());
            return new RxFirebaseChildEvent<U>(value, rxFirebaseChildEvent.getPreviousChildName(),
                    rxFirebaseChildEvent.getEventType());
        }
    }
}
