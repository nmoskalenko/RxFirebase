package com.kelvinapps.rxfirebase;

import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.google.firebase.storage.UploadTask;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;

import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Nick Moskalenko on 25/05/2016.
 * Adapted to RxJava 2 by Remous-Aris Koutsiamanis on 13/02/2017.
 */
public class RxFirebaseStorageTests {

    @Mock
    private StorageReference mockStorageRef;

    private ArgumentCaptor<OnCompleteListener> testOnCompleteListener;
    private ArgumentCaptor<OnSuccessListener> testOnSuccessListener;
    private ArgumentCaptor<OnFailureListener> testOnFailureListener;

    @Mock
    private Task<Void> mockVoidTask;

    @Mock
    private Task<byte[]> mockBytesTask;

    @Mock
    private Task<Uri> mockUriTask;

    @Mock
    private FileDownloadTask mockFileDownloadTask;

    @Mock
    private StreamDownloadTask mockStreamDownloadTask;

    @Mock
    private Task<StorageMetadata> mockMetadataTask;

    @Mock
    private UploadTask mockUploadTask;


    @Mock
    private Uri uri;

    @Mock
    private File file;

    @Mock
    private StorageMetadata metadata;

    @Mock
    private FileDownloadTask.TaskSnapshot fileSnapshot;

    @Mock
    private StreamDownloadTask.TaskSnapshot streamSnapshot;

    @Mock
    private UploadTask.TaskSnapshot uploadSnapshot;


    private byte[] bytes = new byte[1];

    @Mock
    private StreamDownloadTask.StreamProcessor processor;

    @Mock
    private InputStream stream;

    enum Irrelevant { INSTANCE; }
    private Object voidData = Irrelevant.INSTANCE;




    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testOnCompleteListener = ArgumentCaptor.forClass(OnCompleteListener.class);
        testOnSuccessListener = ArgumentCaptor.forClass(OnSuccessListener.class);
        testOnFailureListener = ArgumentCaptor.forClass(OnFailureListener.class);

        setupTask(mockBytesTask);
        setupTask(mockVoidTask);
        setupTask(mockUriTask);
        setupTask(mockFileDownloadTask);
        setupTask(mockStreamDownloadTask);
        setupTask(mockMetadataTask);
        setupTask(mockUploadTask);

        when(mockStorageRef.getBytes(20)).thenReturn(mockBytesTask);
        when(mockStorageRef.getDownloadUrl()).thenReturn(mockUriTask);
        when(mockStorageRef.getFile(file)).thenReturn(mockFileDownloadTask);
        when(mockStorageRef.getFile(uri)).thenReturn(mockFileDownloadTask);
        when(mockStorageRef.getStream()).thenReturn(mockStreamDownloadTask);
        when(mockStorageRef.getStream(processor)).thenReturn(mockStreamDownloadTask);
        when(mockStorageRef.getMetadata()).thenReturn(mockMetadataTask);
        when(mockStorageRef.putBytes(bytes)).thenReturn(mockUploadTask);
        when(mockStorageRef.putBytes(bytes, metadata)).thenReturn(mockUploadTask);
        when(mockStorageRef.putFile(uri)).thenReturn(mockUploadTask);
        when(mockStorageRef.putFile(uri, metadata)).thenReturn(mockUploadTask);
        when(mockStorageRef.putFile(uri, metadata, uri)).thenReturn(mockUploadTask);
        when(mockStorageRef.putStream(stream)).thenReturn(mockUploadTask);
        when(mockStorageRef.putStream(stream, metadata)).thenReturn(mockUploadTask);
        when(mockStorageRef.updateMetadata(metadata)).thenReturn(mockMetadataTask);
        when(mockStorageRef.delete()).thenReturn(mockVoidTask);
    }


    private <T> void setupTask(Task<T> task) {
        when(task.addOnCompleteListener(testOnCompleteListener.capture())).thenReturn(task);
        when(task.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(task);
        when(task.addOnFailureListener(testOnFailureListener.capture())).thenReturn(task);
    }

    @Test
    public void getBytes() throws InterruptedException {

        TestObserver<byte[]> testSubscriber = new TestObserver<>();
        RxFirebaseStorage.getBytes(mockStorageRef, 20)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(bytes);
        testOnCompleteListener.getValue().onComplete(mockBytesTask);

        verify(mockStorageRef).getBytes(20);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(bytes));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void getDownloadUrl() throws InterruptedException {

        TestObserver<Uri> testSubscriber = new TestObserver<>();
        RxFirebaseStorage.getDownloadUrl(mockStorageRef)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uri);
        testOnCompleteListener.getValue().onComplete(mockUriTask);

        verify(mockStorageRef).getDownloadUrl();

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(uri));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }

    @Test
    public void getFile() throws InterruptedException {

        TestObserver<FileDownloadTask.TaskSnapshot> testSubscriber = new TestObserver<>();
        RxFirebaseStorage.getFile(mockStorageRef, file)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(fileSnapshot);

        verify(mockStorageRef).getFile(file);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(fileSnapshot));
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void getFileUri() throws InterruptedException {

        TestObserver<FileDownloadTask.TaskSnapshot> testSubscriber = new TestObserver<>();
        RxFirebaseStorage.getFile(mockStorageRef, uri)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(fileSnapshot);

        verify(mockStorageRef).getFile(uri);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(fileSnapshot));
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }


    @Test
    public void getMetadata() throws InterruptedException {

        TestObserver<StorageMetadata> testSubscriber = new TestObserver<>();
        RxFirebaseStorage.getMetadata(mockStorageRef)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(metadata);
        testOnCompleteListener.getValue().onComplete(mockMetadataTask);

        verify(mockStorageRef).getMetadata();

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(metadata));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }


    @Test
    public void getStream() throws InterruptedException {

        TestObserver<StreamDownloadTask.TaskSnapshot> testSubscriber = new TestObserver<>();
        RxFirebaseStorage.getStream(mockStorageRef)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(streamSnapshot);

        verify(mockStorageRef).getStream();

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(streamSnapshot));
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void getStreamProcessor() throws InterruptedException {

        TestObserver<StreamDownloadTask.TaskSnapshot> testSubscriber = new TestObserver<>();
        RxFirebaseStorage.getStream(mockStorageRef, processor)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(streamSnapshot);

        verify(mockStorageRef).getStream(processor);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(streamSnapshot));
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void putBytes() throws InterruptedException {

        TestObserver<UploadTask.TaskSnapshot> testSubscriber = new TestObserver<>();
        RxFirebaseStorage.putBytes(mockStorageRef, bytes)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);

        verify(mockStorageRef).putBytes(bytes);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void putBytesMetadata() throws InterruptedException {

        TestObserver<UploadTask.TaskSnapshot> testSubscriber = new TestObserver<>();
        RxFirebaseStorage.putBytes(mockStorageRef, bytes, metadata)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);

        verify(mockStorageRef).putBytes(bytes, metadata);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void putFile() throws InterruptedException {

        TestObserver<UploadTask.TaskSnapshot> testSubscriber = new TestObserver<>();
        RxFirebaseStorage.putFile(mockStorageRef, uri)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);

        verify(mockStorageRef).putFile(uri);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void putFileMetadata() throws InterruptedException {

        TestObserver<UploadTask.TaskSnapshot> testSubscriber = new TestObserver<>();
        RxFirebaseStorage.putFile(mockStorageRef, uri, metadata)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);

        verify(mockStorageRef).putFile(uri, metadata);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void putFileMetadataAndUri() throws InterruptedException {

        TestObserver<UploadTask.TaskSnapshot> testSubscriber = new TestObserver<>();
        RxFirebaseStorage.putFile(mockStorageRef, uri, metadata, uri)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);

        verify(mockStorageRef).putFile(uri, metadata, uri);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void putStream() throws InterruptedException {

        TestObserver<UploadTask.TaskSnapshot> testSubscriber = new TestObserver<>();
        RxFirebaseStorage.putStream(mockStorageRef, stream)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);

        verify(mockStorageRef).putStream(stream);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void putStreamMetadata() throws InterruptedException {

        TestObserver<UploadTask.TaskSnapshot> testSubscriber = new TestObserver<>();
        RxFirebaseStorage.putStream(mockStorageRef, stream, metadata)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);

        verify(mockStorageRef).putStream(stream, metadata);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void updateMetadata() throws InterruptedException {

        TestObserver<StorageMetadata> testSubscriber = new TestObserver<>();
        RxFirebaseStorage.updateMetadata(mockStorageRef, metadata)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(metadata);

        verify(mockStorageRef).updateMetadata(metadata);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(metadata));
        testSubscriber.assertNotComplete();
        testSubscriber.dispose();
    }

    @Test
    public void delete() throws InterruptedException {

        TestObserver<Object> testSubscriber = new TestObserver<>();
        RxFirebaseStorage.delete(mockStorageRef)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(voidData);
        testOnCompleteListener.getValue().onComplete(mockVoidTask);

        verify(mockStorageRef).delete();

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueSequence(Collections.singletonList(voidData));
        testSubscriber.assertComplete();
        testSubscriber.dispose();
    }


}
