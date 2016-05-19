package com.kelvinapps.rxfirebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Nick Moskalenko on 28/04/2016.
 */
public class rxFirebaseAuthTests {

    @Mock
    private FirebaseAuth mockAuth;

    @Mock
    private Task<AuthResult> mockAuthTask;

    @Mock
    private AuthResult mockAuthResult;

    @Mock
    private DataSnapshot mockDatabaseDataSnapshot;

    @Mock
    private AuthCredential mockCredentials;

    @Mock
    private FirebaseUser mockUser;


    private ArgumentCaptor<OnCompleteListener> testOnCompleteListener;
    private ArgumentCaptor<OnSuccessListener> testOnSuccessListener;
    private ArgumentCaptor<OnFailureListener> testOnFailureListener;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testOnCompleteListener = ArgumentCaptor.forClass(OnCompleteListener.class);
        testOnSuccessListener = ArgumentCaptor.forClass(OnSuccessListener.class);
        testOnFailureListener = ArgumentCaptor.forClass(OnFailureListener.class);

        when(mockAuthTask.addOnCompleteListener(testOnCompleteListener.capture())).thenReturn(mockAuthTask);
        when(mockAuthTask.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(mockAuthTask);
        when(mockAuthTask.addOnFailureListener(testOnFailureListener.capture())).thenReturn(mockAuthTask);

        when(mockAuth.signInAnonymously()).thenReturn(mockAuthTask);
        when(mockAuth.signInWithEmailAndPassword("email", "password")).thenReturn(mockAuthTask);
        when(mockAuth.signInWithCredential(mockCredentials)).thenReturn(mockAuthTask);
        when(mockAuth.signInWithCustomToken("token")).thenReturn(mockAuthTask);

        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
    }

    @Test
    public void signInAnonymously() throws InterruptedException {

        TestSubscriber<AuthResult> testSubscriber = new TestSubscriber<>();
        rxFirebaseAuth.signInAnonymously(mockAuth)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockAuthResult);
        testOnCompleteListener.getValue().onComplete(mockAuthTask);

        verify(mockAuth).signInAnonymously();

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockAuthResult));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void signInAnonymously_Failed() throws InterruptedException {

        TestSubscriber<AuthResult> testSubscriber = new TestSubscriber<>();
        rxFirebaseAuth.signInAnonymously(mockAuth)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        Exception e = new Exception("something bad happened");
        testOnFailureListener.getValue().onFailure(e);

        verify(mockAuth).signInAnonymously();

        testSubscriber.assertError(e);
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void signInWithEmailAndPassword() throws InterruptedException {

        TestSubscriber<AuthResult> testSubscriber = new TestSubscriber<>();
        rxFirebaseAuth.signInWithEmailAndPassword(mockAuth, "email", "password")
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockAuthResult);
        testOnCompleteListener.getValue().onComplete(mockAuthTask);

        verify(mockAuth).signInWithEmailAndPassword(eq("email"), eq("password"));

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockAuthResult));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void signInWithEmailAndPassword_AuthError() throws InterruptedException {

        TestSubscriber<AuthResult> testSubscriber = new TestSubscriber<>();
        rxFirebaseAuth.signInWithEmailAndPassword(mockAuth, "email", "password")
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        Exception e = new Exception("something bad happened");
        testOnFailureListener.getValue().onFailure(e);

        verify(mockAuth).signInWithEmailAndPassword(eq("email"), eq("password"));

        testSubscriber.assertError(e);
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void signInWithCredential() throws InterruptedException {

        TestSubscriber<AuthResult> testSubscriber = new TestSubscriber<>();
        rxFirebaseAuth.signInWithCredential(mockAuth, mockCredentials)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockAuthResult);
        testOnCompleteListener.getValue().onComplete(mockAuthTask);

        verify(mockAuth).signInWithCredential(mockCredentials);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockAuthResult));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void signInWithCustomToken() throws InterruptedException {

        TestSubscriber<AuthResult> testSubscriber = new TestSubscriber<>();
        rxFirebaseAuth.signInWithCustomToken(mockAuth, "token")
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockAuthResult);
        testOnCompleteListener.getValue().onComplete(mockAuthTask);

        verify(mockAuth).signInWithCustomToken(eq("token"));

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockAuthResult));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void testObserveAuthState() throws InterruptedException {

        TestSubscriber<FirebaseUser> testSubscriber = new TestSubscriber<>();
        rxFirebaseAuth.observeAuthState(mockAuth)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        ArgumentCaptor<FirebaseAuth.AuthStateListener> argument = ArgumentCaptor.forClass(FirebaseAuth.AuthStateListener.class);
        verify(mockAuth).addAuthStateListener(argument.capture());
        argument.getValue().onAuthStateChanged(mockAuth);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockUser));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }
}
