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

import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Nick Moskalenko on 25/05/2016.
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


    private byte[] bytes;

    @Mock
    private StreamDownloadTask.StreamProcessor processor;

    @Mock
    private InputStream stream;

    private Void voidData = null;




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

        TestSubscriber<byte[]> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.getBytes(mockStorageRef, 20)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(bytes);
        testOnCompleteListener.getValue().onComplete(mockBytesTask);

        verify(mockStorageRef).getBytes(20);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(bytes));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void getDownloadUrl() throws InterruptedException {

        TestSubscriber<Uri> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.getDownloadUrl(mockStorageRef)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uri);
        testOnCompleteListener.getValue().onComplete(mockUriTask);

        verify(mockStorageRef).getDownloadUrl();

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(uri));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void getFile() throws InterruptedException {

        TestSubscriber<FileDownloadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.getFile(mockStorageRef, file)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(fileSnapshot);
        testOnCompleteListener.getValue().onComplete(mockFileDownloadTask);

        verify(mockStorageRef).getFile(file);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(fileSnapshot));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void getFileUri() throws InterruptedException {

        TestSubscriber<FileDownloadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.getFile(mockStorageRef, uri)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(fileSnapshot);
        testOnCompleteListener.getValue().onComplete(mockFileDownloadTask);

        verify(mockStorageRef).getFile(uri);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(fileSnapshot));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }


    @Test
    public void getMetadata() throws InterruptedException {

        TestSubscriber<StorageMetadata> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.getMetadata(mockStorageRef)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(metadata);
        testOnCompleteListener.getValue().onComplete(mockMetadataTask);

        verify(mockStorageRef).getMetadata();

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(metadata));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }


    @Test
    public void getStream() throws InterruptedException {

        TestSubscriber<StreamDownloadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.getStream(mockStorageRef)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(streamSnapshot);
        testOnCompleteListener.getValue().onComplete(mockStreamDownloadTask);

        verify(mockStorageRef).getStream();

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(streamSnapshot));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void getStreamProcessor() throws InterruptedException {

        TestSubscriber<StreamDownloadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.getStream(mockStorageRef, processor)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(streamSnapshot);
        testOnCompleteListener.getValue().onComplete(mockStreamDownloadTask);

        verify(mockStorageRef).getStream(processor);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(streamSnapshot));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void putBytes() throws InterruptedException {

        TestSubscriber<UploadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.putBytes(mockStorageRef, bytes)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);
        testOnCompleteListener.getValue().onComplete(mockUploadTask);

        verify(mockStorageRef).putBytes(bytes);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void putBytesMetadata() throws InterruptedException {

        TestSubscriber<UploadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.putBytes(mockStorageRef, bytes, metadata)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);
        testOnCompleteListener.getValue().onComplete(mockUploadTask);

        verify(mockStorageRef).putBytes(bytes, metadata);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void putFile() throws InterruptedException {

        TestSubscriber<UploadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.putFile(mockStorageRef, uri)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);
        testOnCompleteListener.getValue().onComplete(mockUploadTask);

        verify(mockStorageRef).putFile(uri);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void putFileMetadata() throws InterruptedException {

        TestSubscriber<UploadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.putFile(mockStorageRef, uri, metadata)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);
        testOnCompleteListener.getValue().onComplete(mockUploadTask);

        verify(mockStorageRef).putFile(uri, metadata);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void putFileMetadataAndUri() throws InterruptedException {

        TestSubscriber<UploadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.putFile(mockStorageRef, uri, metadata, uri)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);
        testOnCompleteListener.getValue().onComplete(mockUploadTask);

        verify(mockStorageRef).putFile(uri, metadata, uri);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void putStream() throws InterruptedException {

        TestSubscriber<UploadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.putStream(mockStorageRef, stream)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);
        testOnCompleteListener.getValue().onComplete(mockUploadTask);

        verify(mockStorageRef).putStream(stream);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void putStreamMetadata() throws InterruptedException {

        TestSubscriber<UploadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.putStream(mockStorageRef, stream, metadata)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);
        testOnCompleteListener.getValue().onComplete(mockUploadTask);

        verify(mockStorageRef).putStream(stream, metadata);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void updateMetadata() throws InterruptedException {

        TestSubscriber<StorageMetadata> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.updateMetadata(mockStorageRef, metadata)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(metadata);
        testOnCompleteListener.getValue().onComplete(mockMetadataTask);

        verify(mockStorageRef).updateMetadata(metadata);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(metadata));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void delete() throws InterruptedException {

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.delete(mockStorageRef)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(voidData);
        testOnCompleteListener.getValue().onComplete(mockVoidTask);

        verify(mockStorageRef).delete();

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(voidData));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }


}
