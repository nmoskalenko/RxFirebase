package com.kelvinapps.rxfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import com.kelvinapps.rxfirebase.exceptions.RxFirebaseDataCastException;

import io.reactivex.Maybe;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;

/**
 * Created by renanferrari on 09/08/16.
 * Adapted to RxJava 2 by Remous-Aris Koutsiamanis on 13/02/2017.
 */

public abstract class DataSnapshotMapper<T, U> implements Function<T, U> {

    private DataSnapshotMapper() {
    }

    public static <U> DataSnapshotMapper<DataSnapshot, Maybe<U>> of(Class<U> clazz) {
        return new TypedDataSnapshotMapper<U>(clazz);
    }

    public static <U> DataSnapshotMapper<DataSnapshot, List<U>> listOf(Class<U> clazz) {
        return new TypedListDataSnapshotMapper<>(clazz);
    }

    public static <U> DataSnapshotMapper<DataSnapshot, LinkedHashMap<String, U>> mapOf(Class<U> clazz) {
        return new TypedMapDataSnapshotMapper<>(clazz);
    }

    public static <U> DataSnapshotMapper<DataSnapshot, U> of(GenericTypeIndicator<U> genericTypeIndicator) {
        return new GenericTypedDataSnapshotMapper<U>(genericTypeIndicator);
    }

    public static <U> DataSnapshotMapper<RxFirebaseChildEvent<DataSnapshot>, RxFirebaseChildEvent<U>> ofChildEvent(Class<U> clazz) {
        return new ChildEventDataSnapshotMapper<U>(clazz);
    }

    private static <U> U getDataSnapshotTypedValue(DataSnapshot dataSnapshot, Class<U> clazz) {
        U value = dataSnapshot.getValue(clazz);
        if (value == null) {
            throw Exceptions.propagate(new RxFirebaseDataCastException(
                    "unable to cast firebase data response to " + clazz.getSimpleName()));
        }
        return value;
    }

    private static class TypedDataSnapshotMapper<U> extends DataSnapshotMapper<DataSnapshot, Maybe<U>> {

        private final Class<U> clazz;

        public TypedDataSnapshotMapper(final Class<U> clazz) {
            this.clazz = clazz;
        }

        @Override
        public Maybe<U> apply(final DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                return Maybe.just(getDataSnapshotTypedValue(dataSnapshot, clazz));
            } else {
                return Maybe.empty();
            }
        }
    }

    private static class TypedListDataSnapshotMapper<U> extends DataSnapshotMapper<DataSnapshot, List<U>> {

        private final Class<U> clazz;

        public TypedListDataSnapshotMapper(final Class<U> clazz) {
            this.clazz = clazz;
        }

        @Override
        public List<U> apply(final DataSnapshot dataSnapshot) {
            List<U> items = new ArrayList<>();
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                items.add(getDataSnapshotTypedValue(childSnapshot, clazz));
            }
            return items;
        }
    }

    private static class TypedMapDataSnapshotMapper<U> extends DataSnapshotMapper<DataSnapshot, LinkedHashMap<String, U>> {

        private final Class<U> clazz;

        public TypedMapDataSnapshotMapper(final Class<U> clazz) {
            this.clazz = clazz;
        }

        @Override
        public LinkedHashMap<String, U> apply(final DataSnapshot dataSnapshot) {
            LinkedHashMap<String, U> items = new LinkedHashMap<>();
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                items.put(childSnapshot.getKey(), getDataSnapshotTypedValue(childSnapshot, clazz));
            }
            return items;
        }
    }

    private static class GenericTypedDataSnapshotMapper<U> extends DataSnapshotMapper<DataSnapshot, U> {

        private final GenericTypeIndicator<U> genericTypeIndicator;

        public GenericTypedDataSnapshotMapper(GenericTypeIndicator<U> genericTypeIndicator) {
            this.genericTypeIndicator = genericTypeIndicator;
        }

        @Override
        public U apply(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                U value = dataSnapshot.getValue(genericTypeIndicator);
                if (value == null) {
                    throw Exceptions.propagate(new RxFirebaseDataCastException(
                            "unable to cast firebase data response to generic type"));
                }
                return value;
            } else {
                return null;
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
        public RxFirebaseChildEvent<U> apply(final RxFirebaseChildEvent<DataSnapshot> rxFirebaseChildEvent) {
            DataSnapshot dataSnapshot = rxFirebaseChildEvent.getValue();
            if (dataSnapshot.exists()) {
                return new RxFirebaseChildEvent<U>(
                        dataSnapshot.getKey(),
                        getDataSnapshotTypedValue(dataSnapshot, clazz),
                        rxFirebaseChildEvent.getPreviousChildName(),
                        rxFirebaseChildEvent.getEventType());
            } else {
                throw Exceptions.propagate(new RuntimeException("child dataSnapshot doesn't exist"));
            }
        }
    }
}
