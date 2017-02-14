package com.kelvinapps.rxfirebase;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.google.firebase.storage.UploadTask;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import java.io.File;
import java.io.InputStream;

import io.reactivex.Observable;

/**
 * Created by Nick Moskalenko on 24/05/2016.
 * Adapted to RxJava 2 by Remous-Aris Koutsiamanis on 13/02/2017.
 */
public class RxFirebaseStorage {

    @NonNull
    public static Observable<byte[]> getBytes(@NonNull final StorageReference storageRef,
                                              final long maxDownloadSizeBytes) {
        return Observable.create(new ObservableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(ObservableEmitter<byte[]> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, storageRef.getBytes(maxDownloadSizeBytes));
            }
        });

    }

    @NonNull
    public static Observable<Uri> getDownloadUrl(@NonNull final StorageReference storageRef) {
        return Observable.create(new ObservableOnSubscribe<Uri>() {
            @Override
            public void subscribe(ObservableEmitter<Uri> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, storageRef.getDownloadUrl());
            }
        });
    }

    @NonNull
    public static Observable<FileDownloadTask.TaskSnapshot> getFile(@NonNull final StorageReference storageRef,
                                                                    @NonNull final File destinationFile) {
        return Observable.create(new ObservableOnSubscribe<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void subscribe(ObservableEmitter<FileDownloadTask.TaskSnapshot> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, storageRef.getFile(destinationFile));
            }
        });
    }

    @NonNull
    public static Observable<FileDownloadTask.TaskSnapshot> getFile(@NonNull final StorageReference storageRef,
                                                                    @NonNull final Uri destinationUri) {
        return Observable.create(new ObservableOnSubscribe<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void subscribe(ObservableEmitter<FileDownloadTask.TaskSnapshot> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, storageRef.getFile(destinationUri));
            }
        });
    }

    @NonNull
    public static Observable<StorageMetadata> getMetadata(@NonNull final StorageReference storageRef) {
        return Observable.create(new ObservableOnSubscribe<StorageMetadata>() {
            @Override
            public void subscribe(ObservableEmitter<StorageMetadata> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, storageRef.getMetadata());
            }
        });
    }

    @NonNull
    public static Observable<StreamDownloadTask.TaskSnapshot> getStream(@NonNull final StorageReference storageRef) {
        return Observable.create(new ObservableOnSubscribe<StreamDownloadTask.TaskSnapshot>() {
            @Override
            public void subscribe(ObservableEmitter<StreamDownloadTask.TaskSnapshot> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, storageRef.getStream());
            }
        });
    }

    @NonNull
    public static Observable<StreamDownloadTask.TaskSnapshot> getStream(@NonNull final StorageReference storageRef,
                                                                        @NonNull final StreamDownloadTask.StreamProcessor processor) {
        return Observable.create(new ObservableOnSubscribe<StreamDownloadTask.TaskSnapshot>() {
            @Override
            public void subscribe(ObservableEmitter<StreamDownloadTask.TaskSnapshot> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, storageRef.getStream(processor));
            }
        });
    }


    @NonNull
    public static Observable<UploadTask.TaskSnapshot> putBytes(@NonNull final StorageReference storageRef,
                                                               @NonNull final byte[] bytes) {
        return Observable.create(new ObservableOnSubscribe<UploadTask.TaskSnapshot>() {
            @Override
            public void subscribe(ObservableEmitter<UploadTask.TaskSnapshot> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, storageRef.putBytes(bytes));
            }
        });
    }

    @NonNull
    public static Observable<UploadTask.TaskSnapshot> putBytes(@NonNull final StorageReference storageRef,
                                                               @NonNull final byte[] bytes,
                                                               @NonNull final StorageMetadata metadata) {
        return Observable.create(new ObservableOnSubscribe<UploadTask.TaskSnapshot>() {
            @Override
            public void subscribe(ObservableEmitter<UploadTask.TaskSnapshot> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, storageRef.putBytes(bytes, metadata));
            }
        });
    }

    @NonNull
    public static Observable<UploadTask.TaskSnapshot> putFile(@NonNull final StorageReference storageRef,
                                                              @NonNull final Uri uri) {
        return Observable.create(new ObservableOnSubscribe<UploadTask.TaskSnapshot>() {
            @Override
            public void subscribe(ObservableEmitter<UploadTask.TaskSnapshot> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, storageRef.putFile(uri));
            }
        });
    }

    @NonNull
    public static Observable<UploadTask.TaskSnapshot> putFile(@NonNull final StorageReference storageRef,
                                                              @NonNull final Uri uri,
                                                              @NonNull final StorageMetadata metadata) {
        return Observable.create(new ObservableOnSubscribe<UploadTask.TaskSnapshot>() {
            @Override
            public void subscribe(ObservableEmitter<UploadTask.TaskSnapshot> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, storageRef.putFile(uri, metadata));
            }
        });
    }

    @NonNull
    public static Observable<UploadTask.TaskSnapshot> putFile(@NonNull final StorageReference storageRef,
                                                              @NonNull final Uri uri,
                                                              @NonNull final StorageMetadata metadata,
                                                              @NonNull final Uri existingUploadUri) {
        return Observable.create(new ObservableOnSubscribe<UploadTask.TaskSnapshot>() {
            @Override
            public void subscribe(ObservableEmitter<UploadTask.TaskSnapshot> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, storageRef.putFile(uri, metadata, existingUploadUri));
            }
        });
    }

    @NonNull
    public static Observable<UploadTask.TaskSnapshot> putStream(@NonNull final StorageReference storageRef,
                                                                @NonNull final InputStream stream,
                                                                @NonNull final StorageMetadata metadata) {
        return Observable.create(new ObservableOnSubscribe<UploadTask.TaskSnapshot>() {
            @Override
            public void subscribe(ObservableEmitter<UploadTask.TaskSnapshot> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, storageRef.putStream(stream, metadata));
            }
        });
    }

    @NonNull
    public static Observable<UploadTask.TaskSnapshot> putStream(@NonNull final StorageReference storageRef,
                                                                @NonNull final InputStream stream) {
        return Observable.create(new ObservableOnSubscribe<UploadTask.TaskSnapshot>() {
            @Override
            public void subscribe(ObservableEmitter<UploadTask.TaskSnapshot> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, storageRef.putStream(stream));
            }
        });
    }

    @NonNull
    public static Observable<StorageMetadata> updateMetadata(@NonNull final StorageReference storageRef,
                                                             @NonNull final StorageMetadata metadata) {
        return Observable.create(new ObservableOnSubscribe<StorageMetadata>() {
            @Override
            public void subscribe(ObservableEmitter<StorageMetadata> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, storageRef.updateMetadata(metadata));
            }
        });
    }

    @NonNull
    public static Observable<Void> delete(@NonNull final StorageReference storageRef) {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(ObservableEmitter<Void> subscriber) throws Exception {
                RxHandler.assignOnTask(subscriber, storageRef.delete());
            }
        });
    }


}
