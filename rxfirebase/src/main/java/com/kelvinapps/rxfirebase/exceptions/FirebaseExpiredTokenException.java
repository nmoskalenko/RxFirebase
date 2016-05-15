package com.kelvinapps.rxfirebase.exceptions;

public class FirebaseExpiredTokenException extends Exception {

  public FirebaseExpiredTokenException() {
  }

  public FirebaseExpiredTokenException(String detailMessage) {
    super(detailMessage);
  }

  public FirebaseExpiredTokenException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

  public FirebaseExpiredTokenException(Throwable throwable) {
    super(throwable);
  }
}
