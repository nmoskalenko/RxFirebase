package com.kelvinapps.rxfirebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigInfo;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RxFirebaseRemoteConfigTests {

    @Mock
    private FirebaseRemoteConfig remoteConfig;

    @Mock
    private FirebaseRemoteConfigSettings settings;

    @Mock
    private FirebaseRemoteConfigInfo info;

    @Mock
    private FirebaseRemoteConfigValue value;

    @Mock
    private Task<Void> task;

    @Captor
    private ArgumentCaptor<FirebaseRemoteConfigSettings> settingsCaptor;

    @Captor
    private ArgumentCaptor<OnCompleteListener> testOnCompleteListener;

    @Captor
    private ArgumentCaptor<OnSuccessListener> testOnSuccessListener;

    @Captor
    private ArgumentCaptor<OnFailureListener> testOnFailureListener;

    private RxFirebaseRemoteConfig testObject;

    @Before
    public void setUp() {
        setupTask(task);

        testObject = new RxFirebaseRemoteConfig.Builder(remoteConfig).build();
    }

    private <T> void setupTask(Task<T> task) {
        when(task.addOnCompleteListener(testOnCompleteListener.capture())).thenReturn(task);
        when(task.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(task);
        when(task.addOnFailureListener(testOnFailureListener.capture())).thenReturn(task);
    }

    @Test
    public void builderReturnsAnInstance() {
        assertNotNull(testObject);
    }

    @Test
    public void enableDebugModeViaSettings() {
        RxFirebaseRemoteConfig.Builder bob = new RxFirebaseRemoteConfig.Builder(remoteConfig);

        RxFirebaseRemoteConfig.Builder step2 = bob.withSettings(settings);
        assertSame(bob, step2);

        RxFirebaseRemoteConfig config = step2.build();
        assertNotNull(config);

        verify(remoteConfig).setConfigSettings(settings);
    }

    @Test
    public void enableDebugModeDirectly() {
        RxFirebaseRemoteConfig.Builder bob = new RxFirebaseRemoteConfig.Builder(remoteConfig);

        RxFirebaseRemoteConfig.Builder step2 = bob.enableDebug(true);
        assertSame(bob, step2);

        RxFirebaseRemoteConfig config = step2.build();
        assertNotNull(config);

        verify(remoteConfig).setConfigSettings(settingsCaptor.capture());
        assertTrue(settingsCaptor.getValue().isDeveloperModeEnabled());
    }

    @Test
    public void getSimpleBoolean() {
        String key = UUID.randomUUID().toString();
        when(remoteConfig.getBoolean(key)).thenReturn(true);

        boolean actual = testObject.getBoolean(key);

        verify(remoteConfig).getBoolean(key);
        assertTrue(actual);
    }

    @Test
    public void getSimpleByteArray() {
        String key = UUID.randomUUID().toString();
        byte[] expected = new byte[0];
        when(remoteConfig.getByteArray(key)).thenReturn(expected);

        byte[] actual = testObject.getByteArray(key);

        verify(remoteConfig).getByteArray(key);
        assertSame(expected, actual);
    }

    @Test
    public void getSimpleDouble() {
        String key = UUID.randomUUID().toString();
        double expected = new Random().nextDouble();
        when(remoteConfig.getDouble(key)).thenReturn(expected);

        double actual = testObject.getDouble(key);

        verify(remoteConfig).getDouble(key);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void getSimpleLong() {
        String key = UUID.randomUUID().toString();
        long expected = new Random().nextLong();
        when(remoteConfig.getLong(key)).thenReturn(expected);

        long actual = testObject.getLong(key);

        verify(remoteConfig).getLong(key);
        assertEquals(expected, actual);
    }

    @Test
    public void getSimpleString() {
        String key = UUID.randomUUID().toString();
        String expected = UUID.randomUUID().toString();
        when(remoteConfig.getString(key)).thenReturn(expected);

        String actual = testObject.getString(key);

        verify(remoteConfig).getString(key);
        assertEquals(expected, actual);
    }

    @Test
    public void getNamespaceBoolean() {
        String key = UUID.randomUUID().toString();
        String namespace = UUID.randomUUID().toString();
        when(remoteConfig.getBoolean(key, namespace)).thenReturn(true);

        boolean actual = testObject.withNamespace(namespace).getBoolean(key);

        verify(remoteConfig).getBoolean(key, namespace);
        assertTrue(actual);
    }

    @Test
    public void getNamespaceByteArray() {
        String key = UUID.randomUUID().toString();
        String namespace = UUID.randomUUID().toString();
        byte[] expected = new byte[0];
        when(remoteConfig.getByteArray(key, namespace)).thenReturn(expected);

        byte[] actual = testObject.withNamespace(namespace).getByteArray(key);

        verify(remoteConfig).getByteArray(key, namespace);
        assertSame(expected, actual);
    }

    @Test
    public void getNamespaceDouble() {
        String key = UUID.randomUUID().toString();
        String namespace = UUID.randomUUID().toString();
        double expected = new Random().nextDouble();
        when(remoteConfig.getDouble(key, namespace)).thenReturn(expected);

        double actual = testObject.withNamespace(namespace).getDouble(key);

        verify(remoteConfig).getDouble(key, namespace);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void getNamespaceLong() {
        String key = UUID.randomUUID().toString();
        String namespace = UUID.randomUUID().toString();
        long expected = new Random().nextLong();
        when(remoteConfig.getLong(key, namespace)).thenReturn(expected);

        long actual = testObject.withNamespace(namespace).getLong(key);

        verify(remoteConfig).getLong(key, namespace);
        assertEquals(expected, actual);
    }

    @Test
    public void getNamespaceString() {
        String key = UUID.randomUUID().toString();
        String namespace = UUID.randomUUID().toString();
        String expected = UUID.randomUUID().toString();
        when(remoteConfig.getString(key, namespace)).thenReturn(expected);

        String actual = testObject.withNamespace(namespace).getString(key);

        verify(remoteConfig).getString(key, namespace);
        assertEquals(expected, actual);
    }

    @Test
    public void setDefaultsFromResources() {
        int resource = new Random().nextInt();

        testObject.setDefaults(resource);

        verify(remoteConfig).setDefaults(resource);
    }

    @Test
    public void setNamespaceDefaultsFromResources() {
        int resource = new Random().nextInt();
        String namespace = UUID.randomUUID().toString();

        testObject.withNamespace(namespace).setDefaults(resource);

        verify(remoteConfig).setDefaults(resource, namespace);
    }

    @Test
    public void setDefaultsFromMap() {
        Map<String, Object> resource = new HashMap<>();

        testObject.setDefaults(resource);

        verify(remoteConfig).setDefaults(resource);
    }

    @Test
    public void setNamespaceDefaultsFromMap() {
        Map<String, Object> resource = new HashMap<>();
        String namespace = UUID.randomUUID().toString();

        testObject.withNamespace(namespace).setDefaults(resource);

        verify(remoteConfig).setDefaults(resource, namespace);
    }

    @Test
    public void activateFetched() {
        when(remoteConfig.activateFetched()).thenReturn(true);

        boolean result = testObject.activateFetched();

        verify(remoteConfig).activateFetched();
        assertTrue(result);
    }

    @Test
    public void getInfo() {
        when(remoteConfig.getInfo()).thenReturn(info);

        FirebaseRemoteConfigInfo actual = testObject.getInfo();

        verify(remoteConfig).getInfo();
        assertSame(info, actual);
    }

    @Test
    public void getKeysByPrefixWithoutNamespace() {
        String prefix = UUID.randomUUID().toString();
        Set<String> expected = new HashSet<>();
        when(remoteConfig.getKeysByPrefix(prefix)).thenReturn(expected);

        Set<String> actual = testObject.getKeysByPrefix(prefix);

        verify(remoteConfig).getKeysByPrefix(prefix);
        assertSame(expected, actual);
    }

    @Test
    public void getKeysByPrefixWithNamespace() {
        String prefix = UUID.randomUUID().toString();
        String namespace = UUID.randomUUID().toString();
        Set<String> expected = new HashSet<>();
        when(remoteConfig.getKeysByPrefix(prefix, namespace)).thenReturn(expected);

        Set<String> actual = testObject.withNamespace(namespace).getKeysByPrefix(prefix);

        verify(remoteConfig).getKeysByPrefix(prefix, namespace);
        assertSame(expected, actual);
    }

    @Test
    public void getValue() {
        String key = UUID.randomUUID().toString();
        when(remoteConfig.getValue(key)).thenReturn(value);

        FirebaseRemoteConfigValue actual = testObject.getValue(key);

        verify(remoteConfig).getValue(key);
        assertSame(value, actual);
    }

    @Test
    public void getValueWithNamespace() {
        String key = UUID.randomUUID().toString();
        String namespace = UUID.randomUUID().toString();
        when(remoteConfig.getValue(key, namespace)).thenReturn(value);

        FirebaseRemoteConfigValue actual = testObject.withNamespace(namespace).getValue(key);

        verify(remoteConfig).getValue(key, namespace);
        assertSame(value, actual);
    }

    @Test
    public void happyPathFetch() {
        when(remoteConfig.fetch()).thenReturn(task);

        Observable<Void> observable = testObject.fetch();
        assertNotNull(observable);

        TestSubscriber<Void> subscriber = new TestSubscriber<>();
        observable.subscribeOn(Schedulers.immediate()).subscribe(subscriber);

        verify(remoteConfig).fetch();
        verify(task).addOnCompleteListener(isA(OnCompleteListener.class));

        testOnSuccessListener.getValue().onSuccess(null);
        testOnCompleteListener.getValue().onComplete(task);

        subscriber.assertNoErrors();
        subscriber.assertCompleted();
        assertEquals(1, subscriber.getOnNextEvents().size());
    }

    @Test
    public void happyPathFetchWithTimeout() {
        long timeout = new Random().nextLong();
        when(remoteConfig.fetch(timeout)).thenReturn(task);

        Observable<Void> observable = testObject.fetch(timeout);
        assertNotNull(observable);

        TestSubscriber<Void> subscriber = new TestSubscriber<>();
        observable.subscribeOn(Schedulers.immediate()).subscribe(subscriber);

        verify(remoteConfig).fetch(timeout);
        verify(task).addOnCompleteListener(isA(OnCompleteListener.class));

        testOnSuccessListener.getValue().onSuccess(null);
        testOnCompleteListener.getValue().onComplete(task);

        subscriber.assertNoErrors();
        subscriber.assertCompleted();
        assertEquals(1, subscriber.getOnNextEvents().size());
    }
}
