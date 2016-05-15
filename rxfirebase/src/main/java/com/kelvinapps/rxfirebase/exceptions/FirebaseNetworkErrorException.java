package com.kelvinapps.rxfirebase.exceptions;

public class FirebaseNetworkErrorException extends Exception {

  public FirebaseNetworkErrorException() {
  }

  public FirebaseNetworkErrorException(String detailMessage) {
    super(detailMessage);
  }

  public FirebaseNetworkErrorException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

  public FirebaseNetworkErrorException(Throwable throwable) {
    super(throwable);
  }
}
