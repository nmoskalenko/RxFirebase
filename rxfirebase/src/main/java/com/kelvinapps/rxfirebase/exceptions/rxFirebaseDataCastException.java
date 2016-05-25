package com.kelvinapps.rxfirebase.exceptions;

import android.support.annotation.NonNull;

/**
 * Created by Nick Moskalenko on 19/05/2016.
 */
public class RxFirebaseDataCastException extends Exception {

    public RxFirebaseDataCastException() {
    }

    public RxFirebaseDataCastException(@NonNull String detailMessage) {
        super(detailMessage);
    }

    public RxFirebaseDataCastException(@NonNull String detailMessage, @NonNull Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RxFirebaseDataCastException(@NonNull Throwable throwable) {
        super(throwable);
    }
}
