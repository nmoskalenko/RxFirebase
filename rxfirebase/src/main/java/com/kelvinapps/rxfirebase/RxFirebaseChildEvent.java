package com.kelvinapps.rxfirebase;

import android.support.annotation.NonNull;

/**
 * Created by Nick Moskalenko on 17/05/2016.
 */
public class RxFirebaseChildEvent<T> {

    public enum EventType {
        ADDED,
        CHANGED,
        REMOVED,
        MOVED
    }

    private EventType eventType;
    private T value;
    private String previousChildName;


    public RxFirebaseChildEvent(@NonNull T data,
                                @NonNull String previousChildName,
                                @NonNull EventType eventType) {
        this.value = data;
        this.previousChildName = previousChildName;
        this.eventType = eventType;
    }

    public RxFirebaseChildEvent(@NonNull T data, @NonNull EventType eventType) {
        this.value = data;
        this.eventType = eventType;
    }

    @NonNull
    public T getValue() {
        return value;
    }

    public void setValue(@NonNull T value) {
        this.value = value;
    }

    @NonNull
    public String getPreviousChildName() {
        return previousChildName;
    }

    public void setPreviousChildName(@NonNull String previousChildName) {
        this.previousChildName = previousChildName;
    }

    @NonNull
    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(@NonNull EventType eventType) {
        this.eventType = eventType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RxFirebaseChildEvent<?> that = (RxFirebaseChildEvent<?>) o;

        if (eventType != that.eventType) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return previousChildName != null ? previousChildName.equals(that.previousChildName) : that.previousChildName == null;

    }

    @Override
    public int hashCode() {
        int result = eventType != null ? eventType.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (previousChildName != null ? previousChildName.hashCode() : 0);
        return result;
    }
}
