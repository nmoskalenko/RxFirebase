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
public class RxFirebaseUserTests {

    @Mock
    private FirebaseUser mockUser;

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

    @Mock
    private AuthCredential credential;

    @Mock
    private AuthResult authResult;

    @Captor
    private ArgumentCaptor<OnCompleteListener> testOnCompleteListener;

    @Captor
    private ArgumentCaptor<OnSuccessListener> testOnSuccessListener;

    @Captor
    private ArgumentCaptor<OnFailureListener> testOnFailureListener;

    private Void mockRes = null;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testOnCompleteListener = ArgumentCaptor.forClass(OnCompleteListener.class);
        testOnSuccessListener = ArgumentCaptor.forClass(OnSuccessListener.class);
        testOnFailureListener = ArgumentCaptor.forClass(OnFailureListener.class);

        when(mockTask.addOnCompleteListener(testOnCompleteListener.capture())).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(testOnFailureListener.capture())).thenReturn(mockTask);

        when(mockAuthResultTask.addOnCompleteListener(testOnCompleteListener.capture())).thenReturn(mockAuthResultTask);
        when(mockAuthResultTask.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(mockAuthResultTask);
        when(mockAuthResultTask.addOnFailureListener(testOnFailureListener.capture())).thenReturn(mockAuthResultTask);

        when(mockUser.updateEmail("newemail")).thenReturn(mockTask);
        when(mockUser.updatePassword("password")).thenReturn(mockTask);
        when(mockUser.updateProfile(userProfileChangeRequest)).thenReturn(mockTask);
        when(mockUser.delete()).thenReturn(mockTask);
        when(mockUser.reauthenticate(credential)).thenReturn(mockTask);
        when(mockUser.linkWithCredential(credential)).thenReturn(mockAuthResultTask);
    }

    @Test
    public void updateEmail()  {

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        RxFirebaseUser.updateEmail(mockUser, "newemail")
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockRes);
        testOnCompleteListener.getValue().onComplete(mockTask);

        verify(mockUser).updateEmail("newemail");

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockRes));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void updatePassword()  {

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        RxFirebaseUser.updatePassword(mockUser, "password")
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockRes);
        testOnCompleteListener.getValue().onComplete(mockTask);

        verify(mockUser).updatePassword("password");

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockRes));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void updateProfile()  {

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        RxFirebaseUser.updateProfile(mockUser, userProfileChangeRequest)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockRes);
        testOnCompleteListener.getValue().onComplete(mockTask);

        verify(mockUser).updateProfile(userProfileChangeRequest);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockRes));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void delete()  {

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        RxFirebaseUser.delete(mockUser)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockRes);
        testOnCompleteListener.getValue().onComplete(mockTask);

        verify(mockUser).delete();

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockRes));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void reauthenticate()  {

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        RxFirebaseUser.reauthenticate(mockUser, credential)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(mockRes);
        testOnCompleteListener.getValue().onComplete(mockTask);

        verify(mockUser).reauthenticate(credential);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockRes));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void linkWithCredential()  {

        TestSubscriber<AuthResult> testSubscriber = new TestSubscriber<>();
        RxFirebaseUser.linkWithCredential(mockUser, credential)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(authResult);
        testOnCompleteListener.getValue().onComplete(mockAuthResultTask);

        verify(mockUser).linkWithCredential(credential);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(authResult));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }
}
