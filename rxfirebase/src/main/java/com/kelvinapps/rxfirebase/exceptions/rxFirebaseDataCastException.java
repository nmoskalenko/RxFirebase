package com.kelvinapps.rxfirebase.exceptions;

/**
 * Created by Nick Moskalenko on 19/05/2016.
 */
public class rxFirebaseDataCastException extends Exception {

    public rxFirebaseDataCastException() {
    }

    public rxFirebaseDataCastException(String detailMessage) {
        super(detailMessage);
    }

    public rxFirebaseDataCastException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public rxFirebaseDataCastException(Throwable throwable) {
        super(throwable);
    }
}
