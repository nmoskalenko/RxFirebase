package com.kelvinapps.rxfirebase.exceptions;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseError;

/**
 * Created by Nick Moskalenko on 16/05/2016.
 */
public class RxFirebaseDataException extends Exception {

    protected DatabaseError error;

    public RxFirebaseDataException(@NonNull DatabaseError error) {
        this.error = error;
    }

    public DatabaseError getError() {
        return error;
    }

    @Override
    public String toString() {
        return "RxFirebaseDataException{" +
                "error=" + error +
                '}';
    }
}
