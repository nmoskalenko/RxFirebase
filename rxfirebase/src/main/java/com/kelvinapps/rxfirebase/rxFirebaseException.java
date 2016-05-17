package com.kelvinapps.rxfirebase;

import com.firebase.client.FirebaseError;

/**
 * Created by Nick Moskalenko on 16/05/2016.
 */
public class rxFirebaseException extends Exception {

    protected FirebaseError error;

    public rxFirebaseException(FirebaseError error) {
        this.error = error;
    }

    public FirebaseError getError() {
        return error;
    }

    @Override
    public String toString() {
        return "rxFirebaseException{" +
                "error=" + error +
                '}';
    }
}
