package com.kelvinapps.rxfirebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Nick Moskalenko on 28/04/2016.
 */
public class rxFirebaseAuthTests {

    @Mock
    private FirebaseAuth mockDatabase;

    @Mock
    private Task<AuthResult> mockAuthTask;

    @Mock
    private AuthResult mockAuthResult;

    @Mock
    private DataSnapshot mockDatabaseDataSnapshot;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mockAuthTask.addOnCompleteListener(any(OnCompleteListener.class))).thenReturn(mockAuthTask);
        when(mockAuthTask.addOnSuccessListener(any(OnSuccessListener.class))).thenReturn(mockAuthTask);
        when(mockAuthTask.addOnFailureListener(any(OnFailureListener.class))).thenReturn(mockAuthTask);

        when(mockDatabase.signInAnonymously()).thenReturn(mockAuthTask);
    }

    @Test
    public void signInAnonymously() throws InterruptedException {

        TestSubscriber<AuthResult> testSubscriber = new TestSubscriber<>();
        rxFirebaseAuth.signInAnonymously(mockDatabase)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        verify(mockDatabase).signInAnonymously();

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockAuthResult));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

//    @Test
//    public void testAuthWithPassword() throws InterruptedException {
//
//        TestSubscriber<AuthData> testSubscriber = new TestSubscriber<>();
//        rxFirebase.authWithPassword(mockDatabase, "email", "password")
//                .subscribeOn(Schedulers.immediate())
//                .subscribe(testSubscriber);
//
//        ArgumentCaptor<Firebase.AuthResultHandler> argument = ArgumentCaptor.forClass(Firebase.AuthResultHandler.class);
//        verify(mockDatabase).authWithPassword(eq("email"), eq("password"), argument.capture());
//        argument.getValue().onAuthenticated(mockAuthData);
//
//        testSubscriber.assertNoErrors();
//        testSubscriber.assertValueCount(1);
//        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockAuthData));
//        testSubscriber.assertCompleted();
//        testSubscriber.unsubscribe();
//    }
//
//    @Test
//    public void testAuthWithPassword_AuthError() throws InterruptedException {
//
//        TestSubscriber<AuthData> testSubscriber = new TestSubscriber<>();
//        rxFirebase.authWithPassword(mockDatabase, "email", "password")
//                .subscribeOn(Schedulers.immediate())
//                .subscribe(testSubscriber);
//
//        ArgumentCaptor<Firebase.AuthResultHandler> argument = ArgumentCaptor.forClass(Firebase.AuthResultHandler.class);
//        verify(mockDatabase).authWithPassword(eq("email"), eq("password"), argument.capture());
//        argument.getValue().onAuthenticationError(new FirebaseError(FirebaseError.INVALID_PASSWORD, "invalid password"));
//
//        testSubscriber.assertError(rxFirebaseException.class);
//        testSubscriber.assertNotCompleted();
//        testSubscriber.unsubscribe();
//    }
//
//    @Test
//    public void testAuthWithOAuthToken() throws InterruptedException {
//
//        TestSubscriber<AuthData> testSubscriber = new TestSubscriber<>();
//        rxFirebase.authWithOAuthToken(mockDatabase, "provider", "token")
//                .subscribeOn(Schedulers.immediate())
//                .subscribe(testSubscriber);
//
//        ArgumentCaptor<Firebase.AuthResultHandler> argument = ArgumentCaptor.forClass(Firebase.AuthResultHandler.class);
//        verify(mockDatabase).authWithOAuthToken(eq("provider"), eq("token"), argument.capture());
//        argument.getValue().onAuthenticated(mockAuthData);
//
//        testSubscriber.assertNoErrors();
//        testSubscriber.assertValueCount(1);
//        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockAuthData));
//        testSubscriber.assertCompleted();
//        testSubscriber.unsubscribe();
//    }
//
//    @Test
//    public void testAuthWithOAuthTokenWithOptions() throws InterruptedException {
//
//        Map<String, String> options = new HashMap<>();
//        options.put("option1", "value1");
//        options.put("option2", "value2");
//
//        TestSubscriber<AuthData> testSubscriber = new TestSubscriber<>();
//        rxFirebase.authWithOAuthToken(mockDatabase, "provider", options)
//                .subscribeOn(Schedulers.immediate())
//                .subscribe(testSubscriber);
//
//        ArgumentCaptor<Firebase.AuthResultHandler> argument = ArgumentCaptor.forClass(Firebase.AuthResultHandler.class);
//        verify(mockDatabase).authWithOAuthToken(eq("provider"), eq(options), argument.capture());
//        argument.getValue().onAuthenticated(mockAuthData);
//
//        testSubscriber.assertNoErrors();
//        testSubscriber.assertValueCount(1);
//        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockAuthData));
//        testSubscriber.assertCompleted();
//        testSubscriber.unsubscribe();
//    }
//
//    @Test
//    public void testAuthWithCustomToken() throws InterruptedException {
//
//        TestSubscriber<AuthData> testSubscriber = new TestSubscriber<>();
//        rxFirebase.authWithCustomToken(mockDatabase, "token")
//                .subscribeOn(Schedulers.immediate())
//                .subscribe(testSubscriber);
//
//        ArgumentCaptor<Firebase.AuthResultHandler> argument = ArgumentCaptor.forClass(Firebase.AuthResultHandler.class);
//        verify(mockDatabase).authWithCustomToken(eq("token"), argument.capture());
//        argument.getValue().onAuthenticated(mockAuthData);
//
//        testSubscriber.assertNoErrors();
//        testSubscriber.assertValueCount(1);
//        testSubscriber.assertReceivedOnNext(Collections.singletonList(mockAuthData));
//        testSubscriber.assertCompleted();
//        testSubscriber.unsubscribe();
//    }

}
