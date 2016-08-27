package com.kelvinapps.rxfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
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

    public static <U> DataSnapshotMapper<DataSnapshot, U> of(GenericTypeIndicator<U> genericTypeIndicator) {
        return new GenericTypedDataSnapshotMapper<U>(genericTypeIndicator);
    }

    public static <U> DataSnapshotMapper<RxFirebaseChildEvent<DataSnapshot>, RxFirebaseChildEvent<U>> ofChildEvent(Class<U> clazz) {
        return new ChildEventDataSnapshotMapper<U>(clazz);
    }

    private static class TypedDataSnapshotMapper<U> extends DataSnapshotMapper<DataSnapshot, U> {

        private final Class<U> clazz;

        public TypedDataSnapshotMapper(final Class<U> clazz) {
            this.clazz = clazz;
        }

        @Override
        public U call(final DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                return dataSnapshot.getValue(clazz);
            } else {
                throw Exceptions.propagate(new RxFirebaseDataCastException(
                        "unable to cast firebase data response to " + clazz.getSimpleName()));
            }
        }
    }

    private static class GenericTypedDataSnapshotMapper<U> extends DataSnapshotMapper<DataSnapshot, U> {

        private final GenericTypeIndicator<U> genericTypeIndicator;

        public GenericTypedDataSnapshotMapper(GenericTypeIndicator<U> genericTypeIndicator) {
            this.genericTypeIndicator = genericTypeIndicator;
        }

        @Override
        public U call(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                return dataSnapshot.getValue(genericTypeIndicator);
            } else {
                throw Exceptions.propagate(new RxFirebaseDataCastException(
                        "unable to cast firebase data response to generic type"));
            }
        }
    }

    private static class ChildEventDataSnapshotMapper<U>
            extends DataSnapshotMapper<RxFirebaseChildEvent<DataSnapshot>, RxFirebaseChildEvent<U>> {

        private final Class<U> clazz;

        public ChildEventDataSnapshotMapper(final Class<U> clazz) {
            this.clazz = clazz;
        }

        @Override
        public RxFirebaseChildEvent<U> call(final RxFirebaseChildEvent<DataSnapshot> rxFirebaseChildEvent) {
            DataSnapshot dataSnapshot = rxFirebaseChildEvent.getValue();
            if (dataSnapshot.exists()) {
                return new RxFirebaseChildEvent<U>(dataSnapshot.getValue(clazz),
                        rxFirebaseChildEvent.getPreviousChildName(),
                        rxFirebaseChildEvent.getEventType());
            } else {
                throw Exceptions.propagate(new RxFirebaseDataCastException(
                        "unable to cast firebase data response to " + clazz.getSimpleName()));
            }
        }
    }
}
