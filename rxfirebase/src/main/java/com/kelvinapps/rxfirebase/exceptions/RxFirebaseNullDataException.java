package com.kelvinapps.rxfirebase.exceptions;


import android.support.annotation.NonNull;

/**
 * Created by FranGSierra on 30/03/17.
 */
public class RxFirebaseNullDataException extends NullPointerException {

   public RxFirebaseNullDataException() {
   }

   public RxFirebaseNullDataException(@NonNull String detailMessage) {
      super(detailMessage);
   }

}
