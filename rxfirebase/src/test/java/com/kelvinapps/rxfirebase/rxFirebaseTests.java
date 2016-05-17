package com.kelvinapps.rxfirebase;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Nick Moskalenko on 28/04/2016.
 */
public class rxFirebaseTests {

    class TestData {
        int id;
        String str;
    }

    @Mock
    private Firebase mockFirebase;

    @Mock
    private AuthData mockAuthData;

    @Mock
    private DataSnapshot mockFirebaseDataSnapshot;


    TestData testData = new TestData();
    List<TestData> testDataList = new ArrayList<>();

    rxFirebaseChildEvent<TestData> testChildEventAdded;
    rxFirebaseChildEvent<TestData> testChildEventChanged;
    rxFirebaseChildEvent<TestData> testChildEventRemoved;
    rxFirebaseChildEvent<TestData> testChildEventMoved;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testDataList.add(testData);
        testChildEventAdded = new rxFirebaseChildEvent<>(testData, "root", rxFirebaseChildEvent.EventType.ADDED);
        testChildEventChanged = new rxFirebaseChildEvent<>(testData, "root", rxFirebaseChildEvent.EventType.CHANGED);
        testChildEventRemoved = new rxFirebaseChildEvent<>(testData, rxFirebaseChildEvent.EventType.REMOVED);
        testChildEventMoved = new rxFirebaseChildEvent<>(testData, "root", rxFirebaseChildEvent.EventType.MOVED);

        when(mockFirebaseDataSnapshot.getValue(TestData.class)).thenReturn(testData);
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
    public void testAuthWithPassword_AuthError() throws InterruptedException {

        TestSubscriber<AuthData> testSubscriber = new TestSubscriber<>();
        rxFirebase.authWithPassword(mockFirebase, "email", "password")
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<Firebase.AuthResultHandler> argument = ArgumentCaptor.forClass(Firebase.AuthResultHandler.class);
        verify(mockFirebase).authWithPassword(eq("email"), eq("password"), argument.capture());
        argument.getValue().onAuthenticationError(new FirebaseError(FirebaseError.INVALID_PASSWORD, "invalid password"));

        testSubscriber.assertError(rxFirebaseException.class);
        testSubscriber.assertNotCompleted();
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
    public void testAuthWithOAuthTokenWithOptions() throws InterruptedException {

        Map<String, String> options = new HashMap<>();
        options.put("option1", "value1");
        options.put("option2", "value2");

        TestSubscriber<AuthData> testSubscriber = new TestSubscriber<>();
        rxFirebase.authWithOAuthToken(mockFirebase, "provider", options)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<Firebase.AuthResultHandler> argument = ArgumentCaptor.forClass(Firebase.AuthResultHandler.class);
        verify(mockFirebase).authWithOAuthToken(eq("provider"), eq(options), argument.capture());
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

        TestSubscriber<TestData> testSubscriber = new TestSubscriber<>();
        rxFirebase.observeSingleValue(mockFirebase, TestData.class)
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
    public void testObserveSingleValue_Disconnected() throws InterruptedException {

        TestSubscriber<TestData> testSubscriber = new TestSubscriber<>();
        rxFirebase.observeSingleValue(mockFirebase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockFirebase).addListenerForSingleValueEvent(argument.capture());
        argument.getValue().onCancelled(new FirebaseError(FirebaseError.DISCONNECTED, "disconnected"));

        testSubscriber.assertError(rxFirebaseException.class);
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testObserveValuesList_Failed() throws InterruptedException {

        TestSubscriber<List<TestData>> testSubscriber = new TestSubscriber<>();
        rxFirebase.observeValuesList(mockFirebase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockFirebase).addListenerForSingleValueEvent(argument.capture());
        argument.getValue().onCancelled(new FirebaseError(FirebaseError.OPERATION_FAILED, "operation failed"));

        testSubscriber.assertError(rxFirebaseException.class);
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testObserveValuesList() throws InterruptedException {

        TestSubscriber<List<TestData>> testSubscriber = new TestSubscriber<>();
        rxFirebase.observeValuesList(mockFirebase, TestData.class)
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


    @Test
    public void testObserveChildrenEvents_Added() throws InterruptedException {

        TestSubscriber<rxFirebaseChildEvent<TestData>> testSubscriber = new TestSubscriber<>();
        rxFirebase.observeChildrenEvents(mockFirebase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ChildEventListener> argument = ArgumentCaptor.forClass(ChildEventListener.class);
        verify(mockFirebase).addChildEventListener(argument.capture());
        argument.getValue().onChildAdded(mockFirebaseDataSnapshot, "root");

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(testChildEventAdded));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testObserveChildrenEvents_Changed() throws InterruptedException {

        TestSubscriber<rxFirebaseChildEvent<TestData>> testSubscriber = new TestSubscriber<>();
        rxFirebase.observeChildrenEvents(mockFirebase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ChildEventListener> argument = ArgumentCaptor.forClass(ChildEventListener.class);
        verify(mockFirebase).addChildEventListener(argument.capture());
        argument.getValue().onChildChanged(mockFirebaseDataSnapshot, "root");

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(testChildEventChanged));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testObserveChildrenEvents_Removed() throws InterruptedException {

        TestSubscriber<rxFirebaseChildEvent<TestData>> testSubscriber = new TestSubscriber<>();
        rxFirebase.observeChildrenEvents(mockFirebase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ChildEventListener> argument = ArgumentCaptor.forClass(ChildEventListener.class);
        verify(mockFirebase).addChildEventListener(argument.capture());
        argument.getValue().onChildRemoved(mockFirebaseDataSnapshot);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(testChildEventRemoved));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testObserveChildrenEvents_Moved() throws InterruptedException {

        TestSubscriber<rxFirebaseChildEvent<TestData>> testSubscriber = new TestSubscriber<>();
        rxFirebase.observeChildrenEvents(mockFirebase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ChildEventListener> argument = ArgumentCaptor.forClass(ChildEventListener.class);
        verify(mockFirebase).addChildEventListener(argument.capture());
        argument.getValue().onChildMoved(mockFirebaseDataSnapshot, "root");

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(testChildEventMoved));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testObserveChildrenEvents_Cancelled() throws InterruptedException {

        TestSubscriber<rxFirebaseChildEvent<TestData>> testSubscriber = new TestSubscriber<>();
        rxFirebase.observeChildrenEvents(mockFirebase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ChildEventListener> argument = ArgumentCaptor.forClass(ChildEventListener.class);
        verify(mockFirebase).addChildEventListener(argument.capture());
        argument.getValue().onCancelled(new FirebaseError(FirebaseError.OPERATION_FAILED, "operation failed"));

        testSubscriber.assertError(rxFirebaseException.class);
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }


}
