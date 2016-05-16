package com.kelvinapps.firebase;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.kelvinapps.rxfirebase.rxFirebase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Nick Moskalenko on 28/04/2016.
 */
public class rxFirebaseTests {

    @Mock
    private Firebase mockFirebase;

    @Mock
    private AuthData mockAuthData;

    @Mock
    private DataSnapshot mockFirebaseDataSnapshot;


    TestClass testData = new TestClass();
    List<TestClass> testDataList = new ArrayList<>();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testDataList.add(testData);
        when(mockFirebaseDataSnapshot.getValue(TestClass.class)).thenReturn(testData);
        when(mockFirebaseDataSnapshot.getChildren()).thenReturn(Arrays.asList(mockFirebaseDataSnapshot));
    }

    @Test
    public void testAuthAnonymously() throws InterruptedException {

        TestSubscriber<AuthData> testSubscriber = new TestSubscriber<>();
        rxFirebase.authAnonymously(mockFirebase)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<Firebase.AuthResultHandler> argument = ArgumentCaptor.forClass(Firebase.AuthResultHandler.class);
        verify(mockFirebase).authAnonymously(argument.capture());
        argument.getValue().onAuthenticated(mockAuthData);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockAuthData));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testAuthWithPassword() throws InterruptedException {

        TestSubscriber<AuthData> testSubscriber = new TestSubscriber<>();
        rxFirebase.authWithPassword(mockFirebase, "email", "password")
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<Firebase.AuthResultHandler> argument = ArgumentCaptor.forClass(Firebase.AuthResultHandler.class);
        verify(mockFirebase).authWithPassword(eq("email"), eq("password"), argument.capture());
        argument.getValue().onAuthenticated(mockAuthData);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockAuthData));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testAuthWithOAuthToken() throws InterruptedException {

        TestSubscriber<AuthData> testSubscriber = new TestSubscriber<>();
        rxFirebase.authWithOAuthToken(mockFirebase, "provider", "token")
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<Firebase.AuthResultHandler> argument = ArgumentCaptor.forClass(Firebase.AuthResultHandler.class);
        verify(mockFirebase).authWithOAuthToken(eq("provider"), eq("token"), argument.capture());
        argument.getValue().onAuthenticated(mockAuthData);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockAuthData));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testAuthWithCustomToken() throws InterruptedException {

        TestSubscriber<AuthData> testSubscriber = new TestSubscriber<>();
        rxFirebase.authWithCustomToken(mockFirebase, "token")
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<Firebase.AuthResultHandler> argument = ArgumentCaptor.forClass(Firebase.AuthResultHandler.class);
        verify(mockFirebase).authWithCustomToken(eq("token"), argument.capture());
        argument.getValue().onAuthenticated(mockAuthData);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockAuthData));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testObserveAuthState() throws InterruptedException {

        TestSubscriber<AuthData> testSubscriber = new TestSubscriber<>();
        rxFirebase.observeAuthState(mockFirebase)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<Firebase.AuthStateListener> argument = ArgumentCaptor.forClass(Firebase.AuthStateListener.class);
        verify(mockFirebase).addAuthStateListener(argument.capture());
        argument.getValue().onAuthStateChanged(mockAuthData);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockAuthData));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testObserveSingleValue() throws InterruptedException {

        TestSubscriber<TestClass> testSubscriber = new TestSubscriber<>();
        rxFirebase.observeSingleValue(mockFirebase, TestClass.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockFirebase).addListenerForSingleValueEvent(argument.capture());
        argument.getValue().onDataChange(mockFirebaseDataSnapshot);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(testData));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testObserveValuesList() throws InterruptedException {

        TestSubscriber<List<TestClass>> testSubscriber = new TestSubscriber<>();
        rxFirebase.observeValuesList(mockFirebase, TestClass.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockFirebase).addListenerForSingleValueEvent(argument.capture());
        argument.getValue().onDataChange(mockFirebaseDataSnapshot);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(testDataList));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

}
