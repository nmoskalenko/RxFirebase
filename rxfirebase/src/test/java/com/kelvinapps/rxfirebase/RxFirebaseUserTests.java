package com.kelvinapps.rxfirebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Nick Moskalenko on 24/05/2016.
 * Adapted to RxJava 2 by Remous-Aris Koutsiamanis on 13/02/2017.
 */
public class RxFirebaseUserTests {

    @Mock
    private FirebaseUser mockUser;

    private ArgumentCaptor<OnCompleteListener> testOnCompleteListener;
    private ArgumentCaptor<OnSuccessListener> testOnSuccessListener;
    private ArgumentCaptor<OnFailureListener> testOnFailureListener;

    @Mock
    private Task<Void> mockTask;

    @Mock
    private Task<GetTokenResult> mockTokenTask;

    @Mock
    private Task<AuthResult> mockAuthResultTask;


    @Mock
    private UserProfileChangeRequest userProfileChangeRequest;

    @Mock
    private GetTokenResult tokenResult;

    enum Irrelevant { INSTANCE; }
    private Object mockRes = Irrelevant.INSTANCE;

    @Mock
    AuthCredential credential;

    @Mock
    AuthResult authResult;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testOnCompleteListener = ArgumentCaptor.forClass(OnCompleteListener.class);
        testOnSuccessListener = ArgumentCaptor.forClass(OnSuccessListener.class);
        testOnFailureListener = ArgumentCaptor.forClass(OnFailureListener.class);

        when(mockTask.addOnCompleteListener(testOnCompleteListener.capture())).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(testOnFailureListener.capture())).thenReturn(mockTask);

        when(mockTokenTask.addOnCompleteListener(testOnCompleteListener.capture())).thenReturn(mockTokenTask);
        when(mockTokenTask.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(mockTokenTask);
        when(mockTokenTask.addOnFailureListener(testOnFailureListener.capture())).thenReturn(mockTokenTask);

        when(mockAuthResultTask.addOnCompleteListener(testOnCompleteListener.capture())).thenReturn(mockAuthResultTask);
        when(mockAuthResultTask.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(mockAuthResultTask);
        when(mockAuthResultTask.addOnFailureListener(testOnFailureListener.capture())).thenReturn(mockAuthResultTask);

        when(mockUser.updateEmail("newemail")).thenReturn(mockTask);
        when(mockUser.updatePassword("password")).thenReturn(mockTask);
        when(mockUser.updateProfile(userProfileChangeRequest)).thenReturn(mockTask);
        when(mockUser.getToken(true)).thenReturn(mockTokenTask);
        when(mockUser.delete()).thenReturn(mockTask);
        when(mockUser.reauthenticate(credential)).thenReturn(mockTask);
        when(mockUser.linkWithCredential(credential)).thenReturn(mockAuthResultTask);
    }

    @Test
    public void getToken() throws InterruptedException {

        TestObserver<GetTokenResult> testSubscriber = new TestObserver<>();
        RxFirebaseUser.getToken(mockUser, true)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(tokenResult);
        testOnCompleteListener.getValue().onComplete(mockTokenTask);

        verify(mockUser).getToken(true);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(tokenResult));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void updateEmail() throws InterruptedException {

        TestObserver<Object> testSubscriber = new TestObserver<>();
        RxFirebaseUser.updateEmail(mockUser, "newemail")
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockRes);
        testOnCompleteListener.getValue().onComplete(mockTask);

        verify(mockUser).updateEmail("newemail");

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(mockRes));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void updatePassword() throws InterruptedException {

        TestObserver<Object> testSubscriber = new TestObserver<>();
        RxFirebaseUser.updatePassword(mockUser, "password")
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockRes);
        testOnCompleteListener.getValue().onComplete(mockTask);

        verify(mockUser).updatePassword("password");

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(mockRes));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void updateProfile() throws InterruptedException {

        TestObserver<Object> testSubscriber = new TestObserver<>();
        RxFirebaseUser.updateProfile(mockUser, userProfileChangeRequest)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockRes);
        testOnCompleteListener.getValue().onComplete(mockTask);

        verify(mockUser).updateProfile(userProfileChangeRequest);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(mockRes));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void delete() throws InterruptedException {

        TestObserver<Object> testSubscriber = new TestObserver<>();
        RxFirebaseUser.delete(mockUser)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockRes);
        testOnCompleteListener.getValue().onComplete(mockTask);

        verify(mockUser).delete();

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(mockRes));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void reauthenticate() throws InterruptedException {

        TestObserver<Object> testSubscriber = new TestObserver<>();
        RxFirebaseUser.reauthenticate(mockUser, credential)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockRes);
        testOnCompleteListener.getValue().onComplete(mockTask);

        verify(mockUser).reauthenticate(credential);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(mockRes));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void linkWithCredential() throws InterruptedException {

        TestObserver<AuthResult> testSubscriber = new TestObserver<>();
        RxFirebaseUser.linkWithCredential(mockUser, credential)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(authResult);
        testOnCompleteListener.getValue().onComplete(mockAuthResultTask);

        verify(mockUser).linkWithCredential(credential);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(authResult));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }
}
