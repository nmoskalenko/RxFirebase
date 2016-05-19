package com.kelvinapps.rxfirebase.exceptions;

import android.support.annotation.NonNull;

/**
 * Created by Nick Moskalenko on 19/05/2016.
 */
public class rxFirebaseDataCastException extends Exception {

    public rxFirebaseDataCastException() {
    }

    public rxFirebaseDataCastException(@NonNull String detailMessage) {
        super(detailMessage);
    }

    public rxFirebaseDataCastException(@NonNull String detailMessage, @NonNull Throwable throwable) {
        super(detailMessage, throwable);
    }

    public rxFirebaseDataCastException(@NonNull Throwable throwable) {
        super(throwable);
    }
}
