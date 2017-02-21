package com.kelvinapps.rxfirebase;

import android.support.annotation.NonNull;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kelvinapps.rxfirebase.exceptions.RxFirebaseDataException;

import io.reactivex.Maybe;
import io.reactivex.functions.Predicate;
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

import java.util.concurrent.Callable;
import io.reactivex.functions.Function;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Nick Moskalenko on 28/04/2016.
 * Adapted to RxJava 2 by Remous-Aris Koutsiamanis on 13/02/2017.
 */
public class RxFirebaseDatabaseTests {

    @Mock
    private DatabaseReference mockDatabase;

    @Mock
    private DataSnapshot mockFirebaseDataSnapshot;

    private Maybe<TestData> testData = Maybe.just(new TestData());
    private List<Maybe<TestData>> testDataList = new ArrayList<>();
    private Map<String, Maybe<TestData>> testDataMap = new HashMap<>();

    private RxFirebaseChildEvent<TestData> testChildEventAdded;
    private RxFirebaseChildEvent<TestData> testChildEventChanged;
    private RxFirebaseChildEvent<TestData> testChildEventRemoved;
    private RxFirebaseChildEvent<TestData> testChildEventMoved;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testDataList.add(testData);
        testDataMap.put("key", testData);

        testChildEventAdded = new RxFirebaseChildEvent<>("key", testData.blockingGet(), "root", RxFirebaseChildEvent.EventType.ADDED);
        testChildEventChanged = new RxFirebaseChildEvent<>("key", testData.blockingGet(), "root", RxFirebaseChildEvent.EventType.CHANGED);
        testChildEventRemoved = new RxFirebaseChildEvent<>("key", testData.blockingGet(), RxFirebaseChildEvent.EventType.REMOVED);
        testChildEventMoved = new RxFirebaseChildEvent<>("key", testData.blockingGet(), "root", RxFirebaseChildEvent.EventType.MOVED);

        when(mockFirebaseDataSnapshot.exists()).thenReturn(true);
        when(mockFirebaseDataSnapshot.getValue(TestData.class)).thenReturn(testData.blockingGet());
        when(mockFirebaseDataSnapshot.getValue(Maybe.class)).thenReturn(testData);
        when(mockFirebaseDataSnapshot.getKey()).thenReturn("key");
        when(mockFirebaseDataSnapshot.getChildren()).thenReturn(Arrays.asList(mockFirebaseDataSnapshot));
    }

    @Test
    public void testObserveSingleValue() throws InterruptedException {

        TestObserver<Maybe<TestData>> testSubscriber = new TestObserver<>();
        RxFirebaseDatabase.observeSingleValueEvent(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockDatabase).addListenerForSingleValueEvent(argument.capture());
        argument.getValue().onDataChange(mockFirebaseDataSnapshot);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(getMaybeEqualityPredicate(testData));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void testObserveSingleNoData() throws InterruptedException {

        DataSnapshot mockFirebaseDataSnapshotNoData = mock(DataSnapshot.class);
        when(mockFirebaseDataSnapshotNoData.exists()).thenReturn(false);

        TestObserver<Maybe<TestData>> testSubscriber = new TestObserver<>();
        RxFirebaseDatabase.observeSingleValueEvent(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockDatabase).addListenerForSingleValueEvent(argument.capture());
        argument.getValue().onDataChange(mockFirebaseDataSnapshotNoData);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void testObserveSingleWrongType() throws InterruptedException {

        TestObserver<Maybe<WrongType>> testSubscriber = new TestObserver<>();
        RxFirebaseDatabase.observeSingleValueEvent(mockDatabase, WrongType.class)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockDatabase).addListenerForSingleValueEvent(argument.capture());
        argument.getValue().onDataChange(mockFirebaseDataSnapshot);

        testSubscriber.assertError(RuntimeException.class);
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void testObserveSingleValue_Disconnected() throws InterruptedException {

        TestObserver<Maybe<TestData>> testSubscriber = new TestObserver<>();
        RxFirebaseDatabase.observeSingleValueEvent(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockDatabase).addListenerForSingleValueEvent(argument.capture());
        argument.getValue().onCancelled(DatabaseError.zzpI(DatabaseError.DISCONNECTED));

        testSubscriber.assertError(RxFirebaseDataException.class);
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void testObserveSingleValueEvent_Failed() throws InterruptedException {

        TestObserver<List<Maybe<TestData>>> testSubscriber = new TestObserver<>();
        RxFirebaseDatabase.observeSingleValueEvent(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.trampoline())
                .toList()
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockDatabase).addListenerForSingleValueEvent(argument.capture());
        argument.getValue().onCancelled(DatabaseError.zzpI(DatabaseError.OPERATION_FAILED));

        testSubscriber.assertError(RxFirebaseDataException.class);
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void testObserveValueEvent() throws InterruptedException {

        TestObserver<Maybe<TestData>> testSubscriber = new TestObserver<>();
        RxFirebaseDatabase.observeValueEvent(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockDatabase).addValueEventListener(argument.capture());
        argument.getValue().onDataChange(mockFirebaseDataSnapshot);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(getMaybeEqualityPredicate(testData));
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }


    @Test
    public void testSingleValueEvent() throws InterruptedException {

        TestObserver<Maybe<TestData>> testSubscriber = new TestObserver<>();
        RxFirebaseDatabase.observeSingleValueEvent(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockDatabase).addListenerForSingleValueEvent(argument.capture());
        argument.getValue().onDataChange(mockFirebaseDataSnapshot);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(getMaybeEqualityPredicate(testData));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void testObserveValueEventList() throws InterruptedException {

        TestObserver<List<Maybe<TestData>>> testSubscriber = new TestObserver<>();
        RxFirebaseDatabase.observeSingleValueEvent(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.trampoline())
                .toList()
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockDatabase).addListenerForSingleValueEvent(argument.capture());
        argument.getValue().onDataChange(mockFirebaseDataSnapshot);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(getListMaybeEqualityPredicate(testDataList));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void testObserveValuesMap() throws InterruptedException {

        TestObserver<Map<String, Maybe<TestData>>> testSubscriber = new TestObserver<>();
        RxFirebaseDatabase.observeSingleValueEvent(mockDatabase)
                .subscribeOn(Schedulers.trampoline())
                .toMap(new Function<DataSnapshot, String>() {
                    @Override
                    public String apply(DataSnapshot dataSnapshot) {
                        return dataSnapshot.getKey();
                    }
                }, new Function<DataSnapshot, Maybe<TestData>>() {
                    @Override
                    public Maybe<TestData> apply(DataSnapshot dataSnapshot) {
                        return dataSnapshot.getValue(Maybe.class);
                    }
                }, new Callable<Map<String, Maybe<TestData>>>() {
                    @Override
                    public Map<String, Maybe<TestData>> call() {
                        return new LinkedHashMap<String, Maybe<TestData>>();
                    }
                })
                .subscribe(testSubscriber);

        ArgumentCaptor<ValueEventListener> argument = ArgumentCaptor.forClass(ValueEventListener.class);
        verify(mockDatabase).addListenerForSingleValueEvent(argument.capture());
        argument.getValue().onDataChange(mockFirebaseDataSnapshot);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(getMapMaybeEqualityPredicate(testDataMap));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void testObserveChildEvent_Added() throws InterruptedException {

        TestObserver<RxFirebaseChildEvent<TestData>> testSubscriber = new TestObserver<>();
        RxFirebaseDatabase.observeChildEvent(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        ArgumentCaptor<ChildEventListener> argument = ArgumentCaptor.forClass(ChildEventListener.class);
        verify(mockDatabase).addChildEventListener(argument.capture());
        argument.getValue().onChildAdded(mockFirebaseDataSnapshot, "root");

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(testChildEventAdded));
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void testObserveChildEvent_Changed() throws InterruptedException {

        TestObserver<RxFirebaseChildEvent<TestData>> testSubscriber = new TestObserver<>();
        RxFirebaseDatabase.observeChildEvent(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        ArgumentCaptor<ChildEventListener> argument = ArgumentCaptor.forClass(ChildEventListener.class);
        verify(mockDatabase).addChildEventListener(argument.capture());
        argument.getValue().onChildChanged(mockFirebaseDataSnapshot, "root");

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(testChildEventChanged));
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void testObserveChildEvent_Removed() throws InterruptedException {

        TestObserver<RxFirebaseChildEvent<TestData>> testSubscriber = new TestObserver<>();
        RxFirebaseDatabase.observeChildEvent(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        ArgumentCaptor<ChildEventListener> argument = ArgumentCaptor.forClass(ChildEventListener.class);
        verify(mockDatabase).addChildEventListener(argument.capture());
        argument.getValue().onChildRemoved(mockFirebaseDataSnapshot);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(testChildEventRemoved));
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void testObserveChildEvent_Moved() throws InterruptedException {

        TestObserver<RxFirebaseChildEvent<TestData>> testSubscriber = new TestObserver<>();
        RxFirebaseDatabase.observeChildEvent(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        ArgumentCaptor<ChildEventListener> argument = ArgumentCaptor.forClass(ChildEventListener.class);
        verify(mockDatabase).addChildEventListener(argument.capture());
        argument.getValue().onChildMoved(mockFirebaseDataSnapshot, "root");

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(testChildEventMoved));
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void testObserveChildEvent_Cancelled() throws InterruptedException {

        TestObserver<RxFirebaseChildEvent<TestData>> testSubscriber = new TestObserver<>();
        RxFirebaseDatabase.observeChildEvent(mockDatabase, TestData.class)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        ArgumentCaptor<ChildEventListener> argument = ArgumentCaptor.forClass(ChildEventListener.class);
        verify(mockDatabase).addChildEventListener(argument.capture());
        argument.getValue().onCancelled(DatabaseError.zzpI(DatabaseError.DISCONNECTED));

        testSubscriber.assertError(RxFirebaseDataException.class);
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    class TestData {
        int id;
        String str;
    }

    class WrongType {
        String somethingWrong;
        long more;
    }

    @NonNull private <T> Predicate<Maybe<T>> getMaybeEqualityPredicate(final Maybe<T> correctMaybeValue)
    {
        return new Predicate<Maybe<T>>()
        {
            @Override public boolean test(Maybe<T> testMaybeValue) throws Exception
            {
                if(testMaybeValue.isEmpty().blockingGet() == correctMaybeValue.isEmpty().blockingGet()){
                    if(testMaybeValue.isEmpty().blockingGet())
                        return true;
                    else
                        return correctMaybeValue.blockingGet().equals(testMaybeValue.blockingGet());
                } else
                    return false;
            }
        };
    }

    @NonNull private <T> Predicate<List<Maybe<T>>> getListMaybeEqualityPredicate(final List<Maybe<T>> correctMaybeValues)
    {
        return new Predicate<List<Maybe<T>>>()
        {
            @Override public boolean test(List<Maybe<T>> maybes) throws Exception
            {
                if(correctMaybeValues.size() == maybes.size()){
                    for (int i = 0; i < correctMaybeValues.size(); i++)
                    {
                        if(! getMaybeEqualityPredicate(correctMaybeValues.get(i)).test(maybes.get(i)))
                            return false;
                    }
                    return true;
                }
                return false;
            }
        };
    }

    @NonNull private <T> Predicate<Map<String, Maybe<T>>> getMapMaybeEqualityPredicate(final Map<String, Maybe<T>> correctMaybeValues)
    {
        return new Predicate<Map<String, Maybe<T>>>()
        {
            @Override public boolean test(Map<String, Maybe<T>> maybes) throws Exception
            {
                if(correctMaybeValues.size() == maybes.size() && correctMaybeValues.keySet().equals(maybes.keySet())){
                    for (String key: correctMaybeValues.keySet())
                    {
                        if(! getMaybeEqualityPredicate(correctMaybeValues.get(key)).test(maybes.get(key)))
                            return false;
                    }
                    return true;
                }
                return false;
            }
        };
    }
}
