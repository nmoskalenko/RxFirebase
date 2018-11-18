package com.kelvinapps.rxfirebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableReference;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Nick Moskalenko on 24/05/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class RxFirebaseFunctionsTests {

    @Mock
    private FirebaseFunctions mockFunctions;

    @Mock
    private Task<HttpsCallableResult> mockTask;

    @Mock
    private HttpsCallableReference httpsCallableReference;

    @Mock
    private HttpsCallableResult mockRes;


    @Captor
    private ArgumentCaptor<OnCompleteListener> testOnCompleteListener;

    @Captor
    private ArgumentCaptor<OnSuccessListener> testOnSuccessListener;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testOnCompleteListener = ArgumentCaptor.forClass(OnCompleteListener.class);
        testOnSuccessListener = ArgumentCaptor.forClass(OnSuccessListener.class);

        when(mockTask.addOnCompleteListener(testOnCompleteListener.capture())).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(mockTask);

        when(httpsCallableReference.call()).thenReturn(mockTask);
        when(httpsCallableReference.call(any())).thenReturn(mockTask);
        when(mockFunctions.getHttpsCallable(anyString())).thenReturn(httpsCallableReference);
    }

    @Test
    public void getHttpsCallable() {

        TestSubscriber<HttpsCallableResult> testSubscriber = new TestSubscriber<>();
        RxFirebaseFunctions.getHttpsCallable(mockFunctions, "test")
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockRes);
        testOnCompleteListener.getValue().onComplete(mockTask);

        verify(mockFunctions).getHttpsCallable("test");

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockRes));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void getHttpsCallableWithParam() {

        TestSubscriber<HttpsCallableResult> testSubscriber = new TestSubscriber<>();
        RxFirebaseFunctions.getHttpsCallable(mockFunctions, "test", "param")
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockRes);
        testOnCompleteListener.getValue().onComplete(mockTask);

        verify(mockFunctions).getHttpsCallable("test");

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockRes));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }
}
