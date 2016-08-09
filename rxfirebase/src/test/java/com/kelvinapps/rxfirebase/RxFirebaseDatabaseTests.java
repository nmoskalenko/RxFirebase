package com.kelvinapps.rxfirebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kelvinapps.rxfirebase.exceptions.RxFirebaseDataException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Func0;
import rx.functions.Func1;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Nick Moskalenko on 28/04/2016.
 */
public class RxFirebaseDatabaseTests {

    @Mock
    private DatabaseReference mockDatabase;

    @Mock
    private DataSnapshot mockFirebaseDataSnapshot;

    private TestData testData = new TestData();
    private List<TestData> testDataList = new ArrayList<>();
    private Map<String, TestData> testDataMap = new HashMap<>();

    private RxFirebaseChildEvent<TestData> testChildEventAdded;
    private RxFirebaseChildEvent<TestData> testChildEventChanged;
    private RxFirebaseChildEvent<TestData> testChildEventRemoved;
    private RxFirebaseChildEvent<TestData> testChildEventMoved;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testDataList.add(testData);
        testDataMap.put("key", testData);
        testChildEventAdded = new RxFirebaseChildEvent<>(testData, "root", RxFirebaseChildEvent.EventType.ADDED);
        testChildEventChanged = new RxFirebaseChildEvent<>(testData, "root", RxFirebaseChildEvent.EventType.CHANGED);
        testChildEventRemoved = new RxFirebaseChildEvent<>(testData, RxFirebaseChildEvent.EventType.REMOVED);
        testChildEventMoved = new RxFirebaseChildEvent<>(testData, "root", RxFirebaseChildEvent.EventType.MOVED);

        when(mockFirebaseDataSnapshot.getValue(TestData.class)).thenReturn(testData);
        when(mockFirebaseDataSnapshot.getKey()).thenReturn("key");
        when(mockFirebaseDataSnapshot.getChildren()).thenReturn(Arrays.asList(mockFirebaseDataSnapshot));
    }

    @Test
    public void testObserveSingleValue() throws InterruptedException {

        TestSubscriber<TestData> testSubscriber = new TestSubscriber<>();
        RxFirebaseDatabase.observeSingleValueEvent(mockDatabase, TestData.class)
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
        RxFirebaseDatabase.observeSingleValueEvent(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockDatabase).addListenerForSingleValueEvent(argument.capture());
        argument.getValue().onCancelled(DatabaseError.zzadi(DatabaseError.DISCONNECTED));

        testSubscriber.assertError(RxFirebaseDataException.class);
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testObserveValuesList_Failed() throws InterruptedException {

        TestSubscriber<List<TestData>> testSubscriber = new TestSubscriber<>();
        RxFirebaseDatabase.observeSingleValueEvent(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .toList()
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockDatabase).addListenerForSingleValueEvent(argument.capture());
        argument.getValue().onCancelled(DatabaseError.zzadi(DatabaseError.OPERATION_FAILED));

        testSubscriber.assertError(RxFirebaseDataException.class);
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testObserveValue() throws InterruptedException {

        TestSubscriber<TestData> testSubscriber = new TestSubscriber<>();
        RxFirebaseDatabase.observeValueEvent(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockDatabase).addValueEventListener(argument.capture());
        argument.getValue().onDataChange(mockFirebaseDataSnapshot);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(testData));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testObserveValues() throws InterruptedException {

        TestSubscriber<TestData> testSubscriber = new TestSubscriber<>();
        RxFirebaseDatabase.observeSingleValueEvent(mockDatabase, TestData.class)
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
    public void testObserveValuesList() throws InterruptedException {

        TestSubscriber<List<TestData>> testSubscriber = new TestSubscriber<>();
        RxFirebaseDatabase.observeSingleValueEvent(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .toList()
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
    public void testObserveValuesMap() throws InterruptedException {

        TestSubscriber<Map<String, TestData>> testSubscriber = new TestSubscriber<>();
        RxFirebaseDatabase.observeSingleValueEvent(mockDatabase)
                .subscribeOn(Schedulers.immediate())
                .toMap(new Func1<DataSnapshot, String>() {
                    @Override
                    public String call(DataSnapshot dataSnapshot) {
                        return dataSnapshot.getKey();
                    }
                }, new Func1<DataSnapshot, TestData>() {
                    @Override
                    public TestData call(DataSnapshot dataSnapshot) {
                        return dataSnapshot.getValue(TestData.class);
                    }
                }, new Func0<Map<String, TestData>>() {
                    @Override
                    public Map<String, TestData> call() {
                        return new LinkedHashMap<String, TestData>();
                    }
                })
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockDatabase).addListenerForSingleValueEvent(argument.capture());
        argument.getValue().onDataChange(mockFirebaseDataSnapshot);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(testDataMap));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testObserveChildrenEvents_Added() throws InterruptedException {

        TestSubscriber<RxFirebaseChildEvent<TestData>> testSubscriber = new TestSubscriber<>();
        RxFirebaseDatabase.observeChildEvent(mockDatabase, TestData.class)
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

        TestSubscriber<RxFirebaseChildEvent<TestData>> testSubscriber = new TestSubscriber<>();
        RxFirebaseDatabase.observeChildEvent(mockDatabase, TestData.class)
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

        TestSubscriber<RxFirebaseChildEvent<TestData>> testSubscriber = new TestSubscriber<>();
        RxFirebaseDatabase.observeChildEvent(mockDatabase, TestData.class)
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

        TestSubscriber<RxFirebaseChildEvent<TestData>> testSubscriber = new TestSubscriber<>();
        RxFirebaseDatabase.observeChildEvent(mockDatabase, TestData.class)
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

        TestSubscriber<RxFirebaseChildEvent<TestData>> testSubscriber = new TestSubscriber<>();
        RxFirebaseDatabase.observeChildEvent(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<ChildEventListener> argument = ArgumentCaptor.forClass(ChildEventListener.class);
        verify(mockDatabase).addChildEventListener(argument.capture());
        argument.getValue().onCancelled(DatabaseError.zzadi(DatabaseError.DISCONNECTED));

        testSubscriber.assertError(RxFirebaseDataException.class);
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    class TestData {
        int id;
        String str;
    }
}
