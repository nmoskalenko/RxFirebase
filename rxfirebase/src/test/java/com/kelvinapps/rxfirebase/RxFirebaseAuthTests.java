package com.kelvinapps.rxfirebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Nick Moskalenko on 28/04/2016.
 * Adapted to RxJava 2 by Remous-Aris Koutsiamanis on 13/02/2017.
 */
public class RxFirebaseAuthTests {

    @Mock
    private FirebaseAuth mockAuth;

    @Mock
    private Task<AuthResult> mockAuthTask;

    @Mock
    private Task<ProviderQueryResult> mockProviderQueryResultTask;

    @Mock
    private Task<Void> mockVoidTask;

    @Mock
    private AuthResult mockAuthResult;

    @Mock
    private ProviderQueryResult mockProviderQueryResult;

    @Mock
    private DataSnapshot mockDatabaseDataSnapshot;

    @Mock
    private AuthCredential mockCredentials;

    @Mock
    private FirebaseUser mockUser;

    enum Irrelevant { INSTANCE; }
    private Irrelevant mockRes = Irrelevant.INSTANCE;

    private ArgumentCaptor<OnCompleteListener> testOnCompleteListener;
    private ArgumentCaptor<OnSuccessListener> testOnSuccessListener;
    private ArgumentCaptor<OnFailureListener> testOnFailureListener;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testOnCompleteListener = ArgumentCaptor.forClass(OnCompleteListener.class);
        testOnSuccessListener = ArgumentCaptor.forClass(OnSuccessListener.class);
        testOnFailureListener = ArgumentCaptor.forClass(OnFailureListener.class);

        setupTask(mockAuthTask);
        setupTask(mockProviderQueryResultTask);
        setupTask(mockVoidTask);

        when(mockAuth.signInAnonymously()).thenReturn(mockAuthTask);
        when(mockAuth.signInWithEmailAndPassword("email", "password")).thenReturn(mockAuthTask);
        when(mockAuth.signInWithCredential(mockCredentials)).thenReturn(mockAuthTask);
        when(mockAuth.signInWithCustomToken("token")).thenReturn(mockAuthTask);
        when(mockAuth.createUserWithEmailAndPassword("email", "password")).thenReturn(mockAuthTask);
        when(mockAuth.fetchProvidersForEmail("email")).thenReturn(mockProviderQueryResultTask);
        when(mockAuth.sendPasswordResetEmail("email")).thenReturn(mockVoidTask);

        when(mockAuth.getCurrentUser()).thenReturn(mockUser);

    }

    private <T> void setupTask(Task<T> task) {
        when(task.addOnCompleteListener(testOnCompleteListener.capture())).thenReturn(task);
        when(task.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(task);
        when(task.addOnFailureListener(testOnFailureListener.capture())).thenReturn(task);
    }

    @Test
    public void signInAnonymously() throws InterruptedException {

        TestObserver<AuthResult> testSubscriber = new TestObserver<>();
        RxFirebaseAuth.signInAnonymously(mockAuth)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockAuthResult);
        testOnCompleteListener.getValue().onComplete(mockAuthTask);

        verify(mockAuth).signInAnonymously();

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(mockAuthResult));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void signInAnonymously_Failed() throws InterruptedException {

        TestObserver<AuthResult> testSubscriber = new TestObserver<>();
        RxFirebaseAuth.signInAnonymously(mockAuth)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        Exception e = new Exception("something bad happened");
        testOnFailureListener.getValue().onFailure(e);

        verify(mockAuth).signInAnonymously();

        testSubscriber.assertError(e);
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void signInWithEmailAndPassword() throws InterruptedException {

        TestObserver<AuthResult> testSubscriber = new TestObserver<>();
        RxFirebaseAuth.signInWithEmailAndPassword(mockAuth, "email", "password")
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockAuthResult);
        testOnCompleteListener.getValue().onComplete(mockAuthTask);

        verify(mockAuth).signInWithEmailAndPassword(eq("email"), eq("password"));

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(mockAuthResult));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void signInWithEmailAndPassword_AuthError() throws InterruptedException {

        TestObserver<AuthResult> testSubscriber = new TestObserver<>();
        RxFirebaseAuth.signInWithEmailAndPassword(mockAuth, "email", "password")
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        Exception e = new Exception("something bad happened");
        testOnFailureListener.getValue().onFailure(e);

        verify(mockAuth).signInWithEmailAndPassword(eq("email"), eq("password"));

        testSubscriber.assertError(e);
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void signInWithCredential() throws InterruptedException {

        TestObserver<AuthResult> testSubscriber = new TestObserver<>();
        RxFirebaseAuth.signInWithCredential(mockAuth, mockCredentials)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockAuthResult);
        testOnCompleteListener.getValue().onComplete(mockAuthTask);

        verify(mockAuth).signInWithCredential(mockCredentials);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(mockAuthResult));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void signInWithCustomToken() throws InterruptedException {

        TestObserver<AuthResult> testSubscriber = new TestObserver<>();
        RxFirebaseAuth.signInWithCustomToken(mockAuth, "token")
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockAuthResult);
        testOnCompleteListener.getValue().onComplete(mockAuthTask);

        verify(mockAuth).signInWithCustomToken(eq("token"));

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(mockAuthResult));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void createUserWithEmailAndPassword() throws InterruptedException {

        TestObserver<AuthResult> testSubscriber = new TestObserver<>();
        RxFirebaseAuth.createUserWithEmailAndPassword(mockAuth, "email", "password")
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockAuthResult);
        testOnCompleteListener.getValue().onComplete(mockAuthTask);

        verify(mockAuth).createUserWithEmailAndPassword(eq("email"), eq("password"));

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(mockAuthResult));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void fetchProvidersForEmail() throws InterruptedException {

        TestObserver<ProviderQueryResult> testSubscriber = new TestObserver<>();
        RxFirebaseAuth.fetchProvidersForEmail(mockAuth, "email")
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockProviderQueryResult);
        testOnCompleteListener.getValue().onComplete(mockProviderQueryResultTask);

        verify(mockAuth).fetchProvidersForEmail(eq("email"));

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(mockProviderQueryResult));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void sendPasswordResetEmail() throws InterruptedException {

        TestObserver<Object> testSubscriber = new TestObserver<>();
        RxFirebaseAuth.sendPasswordResetEmail(mockAuth, "email")
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockRes);
        testOnCompleteListener.getValue().onComplete(mockVoidTask);

        verify(mockAuth).sendPasswordResetEmail(eq("email"));

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(mockRes));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void testObserveAuthState() throws InterruptedException {

        TestObserver<FirebaseUser> testSubscriber = new TestObserver<>();
        RxFirebaseAuth.observeAuthState(mockAuth)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        ArgumentCaptor<FirebaseAuth.AuthStateListener> argument = ArgumentCaptor.forClass(FirebaseAuth.AuthStateListener.class);
        verify(mockAuth).addAuthStateListener(argument.capture());
        argument.getValue().onAuthStateChanged(mockAuth);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(mockUser));
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }
}
