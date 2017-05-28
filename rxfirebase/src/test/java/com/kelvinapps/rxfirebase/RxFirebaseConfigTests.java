package com.kelvinapps.rxfirebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Nick Moskalenko on 24/05/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class RxFirebaseConfigTests {

    @Mock
    private FirebaseRemoteConfig mockConfig;

    @Mock
    private Task<Void> mockTask;

    @Captor
    private ArgumentCaptor<OnCompleteListener> testOnCompleteListener;

    @Captor
    private ArgumentCaptor<OnSuccessListener> testOnSuccessListener;

    private Void mockRes = null;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testOnCompleteListener = ArgumentCaptor.forClass(OnCompleteListener.class);
        testOnSuccessListener = ArgumentCaptor.forClass(OnSuccessListener.class);

        when(mockTask.addOnCompleteListener(testOnCompleteListener.capture())).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(mockTask);

        when(mockConfig.fetch()).thenReturn(mockTask);
        when(mockConfig.fetch(1)).thenReturn(mockTask);
    }

    @Test
    public void fetch() throws InterruptedException {

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        RxFirebaseConfig.fetch(mockConfig)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockRes);
        testOnCompleteListener.getValue().onComplete(mockTask);

        verify(mockConfig).fetch();

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockRes));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void fetchWithExpiration() throws InterruptedException {

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        RxFirebaseConfig.fetch(mockConfig, 1)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockRes);
        testOnCompleteListener.getValue().onComplete(mockTask);

        verify(mockConfig).fetch(1);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockRes));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }
}
