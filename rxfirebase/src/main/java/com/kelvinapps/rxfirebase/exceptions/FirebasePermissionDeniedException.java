package com.kelvinapps.rxfirebase.exceptions;

public class FirebasePermissionDeniedException extends Exception {

  public FirebasePermissionDeniedException() {
  }

  public FirebasePermissionDeniedException(String detailMessage) {
    super(detailMessage);
  }

  public FirebasePermissionDeniedException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

  public FirebasePermissionDeniedException(Throwable throwable) {
    super(throwable);
  }
}
