package com.kelvinapps.rxfirebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kelvinapps.rxfirebase.exceptions.rxFirebaseDataException;

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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Nick Moskalenko on 28/04/2016.
 */
public class rxFirebaseDatabaseTests {

    TestData testData = new TestData();
    List<TestData> testDataList = new ArrayList<>();
    rxFirebaseChildEvent<TestData> testChildEventAdded;
    rxFirebaseChildEvent<TestData> testChildEventChanged;
    rxFirebaseChildEvent<TestData> testChildEventRemoved;
    rxFirebaseChildEvent<TestData> testChildEventMoved;
    @Mock
    private DatabaseReference mockDatabase;
    @Mock
    private DataSnapshot mockFirebaseDataSnapshot;

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
    public void testObserveSingleValue() throws InterruptedException {

        TestSubscriber<TestData> testSubscriber = new TestSubscriber<>();
        rxFirebaseDatabase.observeSingleValue(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockDatabase).addListenerForSingleValueEvent(argument.capture());
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
        rxFirebaseDatabase.observeSingleValue(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockDatabase).addListenerForSingleValueEvent(argument.capture());
        argument.getValue().onCancelled(DatabaseError.zznw(DatabaseError.DISCONNECTED));

        testSubscriber.assertError(rxFirebaseDataException.class);
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testObserveValuesList_Failed() throws InterruptedException {

        TestSubscriber<List<TestData>> testSubscriber = new TestSubscriber<>();
        rxFirebaseDatabase.observeValuesList(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockDatabase).addListenerForSingleValueEvent(argument.capture());
        argument.getValue().onCancelled(DatabaseError.zznw(DatabaseError.OPERATION_FAILED));

        testSubscriber.assertError(rxFirebaseDataException.class);
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testObserveValuesList() throws InterruptedException {

        TestSubscriber<List<TestData>> testSubscriber = new TestSubscriber<>();
        rxFirebaseDatabase.observeValuesList(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockDatabase).addListenerForSingleValueEvent(argument.capture());
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
        rxFirebaseDatabase.observeChildrenEvents(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ChildEventListener> argument = ArgumentCaptor.forClass(ChildEventListener.class);
        verify(mockDatabase).addChildEventListener(argument.capture());
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
        rxFirebaseDatabase.observeChildrenEvents(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ChildEventListener> argument = ArgumentCaptor.forClass(ChildEventListener.class);
        verify(mockDatabase).addChildEventListener(argument.capture());
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
        rxFirebaseDatabase.observeChildrenEvents(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ChildEventListener> argument = ArgumentCaptor.forClass(ChildEventListener.class);
        verify(mockDatabase).addChildEventListener(argument.capture());
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
        rxFirebaseDatabase.observeChildrenEvents(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ChildEventListener> argument = ArgumentCaptor.forClass(ChildEventListener.class);
        verify(mockDatabase).addChildEventListener(argument.capture());
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
        rxFirebaseDatabase.observeChildrenEvents(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ChildEventListener> argument = ArgumentCaptor.forClass(ChildEventListener.class);
        verify(mockDatabase).addChildEventListener(argument.capture());
        argument.getValue().onCancelled(DatabaseError.zznw(DatabaseError.DISCONNECTED));

        testSubscriber.assertError(rxFirebaseDataException.class);
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    class TestData {
        int id;
        String str;
    }


}
