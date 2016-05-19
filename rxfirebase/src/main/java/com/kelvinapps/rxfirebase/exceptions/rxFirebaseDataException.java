package com.kelvinapps.rxfirebase.exceptions;

import com.google.firebase.database.DatabaseError;

/**
 * Created by Nick Moskalenko on 16/05/2016.
 */
public class rxFirebaseDataException extends Exception {

    protected DatabaseError error;

    public rxFirebaseDataException(DatabaseError error) {
        this.error = error;
    }

    public DatabaseError getError() {
        return error;
    }

    @Override
    public String toString() {
        return "rxFirebaseDataException{" +
                "error=" + error +
                '}';
    }
}
