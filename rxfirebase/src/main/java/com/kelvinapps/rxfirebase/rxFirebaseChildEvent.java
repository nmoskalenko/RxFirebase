package com.kelvinapps.rxfirebase;

/**
 * Created by Nick Moskalenko on 17/05/2016.
 */
public class rxFirebaseChildEvent<T> {

    public enum EventType {
        ADDED,
        CHANGED,
        REMOVED,
        MOVED
    }

    private EventType eventType;
    private T value;
    private String previousChildName;


    public rxFirebaseChildEvent(T data, String previousChildName,
                                EventType eventType) {
        this.value = data;
        this.previousChildName = previousChildName;
        this.eventType = eventType;
    }

    public rxFirebaseChildEvent(T data, EventType eventType) {
        this.value = data;
        this.eventType = eventType;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getPreviousChildName() {
        return previousChildName;
    }

    public void setPreviousChildName(String previousChildName) {
        this.previousChildName = previousChildName;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        rxFirebaseChildEvent<?> that = (rxFirebaseChildEvent<?>) o;

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
