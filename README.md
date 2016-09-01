[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0) [![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![Download](https://api.bintray.com/packages/kelvin/maven/rxfirebase/images/download.svg) ](https://bintray.com/kelvin/maven/rxfirebase/_latestVersion)
[![Build Status](https://img.shields.io/badge/Android%20Arsenal-rxFirebase-green.svg?style=true)](https://android-arsenal.com/details/1/3619)
[![Build Status](https://circleci.com/gh/nmoskalenko/RxFirebase/tree/master.svg?style=shield)](https://circleci.com/gh/nmoskalenko/RxFirebase/tree/master)
[![codecov](https://codecov.io/gh/nmoskalenko/rxFirebase/branch/master/graph/badge.svg)](https://codecov.io/gh/nmoskalenko/rxFirebase)

# RxFirebase

RxJava wrapper on Google's [Firebase for Android](https://www.firebase.com/docs/android/) library.


## Usage
Library provides set of static methods of classes:
- RxFirebaseAuth
- RxFirebaseUser
- RxFirebaseDatabase
- RxFirebaseStorage

### Authentication:

Sign in anonymously and get token:
```java
RxFirebaseAuth.signInAnonymously(FirebaseAuth.getInstance())
                .flatMap(x -> RxFirebaseUser.getToken(FirebaseAuth.getInstance().getCurrentUser(), false))
                .subscribe(token -> {
                    Log.i("RxFirebaseSample", "user token: " + token.getToken());
                }, throwable -> {
                    Log.e("RxFirebaseSample", throwable.toString());
                });
```

### Database:
Many thanks to @renanferrari for making it much more flexible.

You can observe the value providing the Class of expected data like:
```java
RxFirebaseDatabase.observeSingleValue(reference.child("users").child("nick"), User.class)
            .subscribe(user -> {
                userTextView.setText(user.toString());
            }, throwable -> {
                Log.e("RxFirebaseSample", throwable.toString());
            });
```

or providing the your own mapper between DataSnapshot and your data type:

```java
RxFirebaseDatabase.observeValueEvent(reference.child("posts"), DataSnapshotMapper.of(new GenericTypeIndicator<List<BlogPost>>() {}))
                .flatMap(Observable::from)
                .subscribe(blogPost -> {
                    postsTextView.setText(postsTextView.getText().toString() + blogPost.toString() + "\n");
                }, throwable -> {
                    Toast.makeText(SampleActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                });
```

### Storage

Download file from Firebase storage

```java
StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://project-1125675579821020265.appspot.com");
RxFirebaseStorage.getFile(storageRef.child("README.md"), targetFile)
        .subscribe(snapshot -> {
            Log.i("RxFirebaseSample", "transferred: " + snapshot.getBytesTransferred() + " bytes");
        }, throwable -> {
            Log.e("RxFirebaseSample", throwable.toString());
        });
```

or download file as bytes array

```java
RxFirebaseStorage.getBytes(storageRef.child("README.md"), 1024 * 100)
                .subscribe(bytes -> {
                    Log.i("RxFirebaseSample", "downloaded: " + new String(bytes));
                }, throwable -> {
                    Log.e("RxFirebaseSample", throwable.toString());
                });
```


## Download

##### Gradle:
```groovy
dependencies {
  compile 'com.google.firebase:firebase-auth:9.4.0'
  compile 'com.google.firebase:firebase-database:9.4.0'
  compile 'com.google.firebase:firebase-storage:9.4.0'
  compile 'com.kelvinapps:rxfirebase:0.0.12'
}
```

##### Maven:
```
<dependency>
  <groupId>com.kelvinapps</groupId>
  <artifactId>rxfirebase</artifactId>
  <version>0.0.12</version>
  <type>pom</type>
</dependency>
```


## License
    Copyright 2016 Nickolay Moskalenko

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
