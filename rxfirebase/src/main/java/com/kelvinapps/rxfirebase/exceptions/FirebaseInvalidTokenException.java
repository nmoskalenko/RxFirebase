package com.kelvinapps.rxfirebase.exceptions;

public class FirebaseInvalidTokenException extends Exception {

  public FirebaseInvalidTokenException() {
  }

  public FirebaseInvalidTokenException(String detailMessage) {
    super(detailMessage);
  }

  public FirebaseInvalidTokenException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

  public FirebaseInvalidTokenException(Throwable throwable) {
    super(throwable);
  }
}
