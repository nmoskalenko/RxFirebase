[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0) [![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![Download](https://api.bintray.com/packages/kelvin/maven/rxfirebase/images/download.svg) ](https://bintray.com/kelvin/maven/rxfirebase/_latestVersion)
[![Build Status](https://circleci.com/gh/nmoskalenko/rxFirebase/tree/master.svg?style=shield)](https://circleci.com/gh/nmoskalenko/rxFirebase/tree/master)

# RxFirebase

RxJava wrapper on Google's [Firebase for Android](https://www.firebase.com/docs/android/) library.


## Usage
Library provides set of static methods of classes:
- rxFirebaseAuth
- rxFirebaseDatabase

##### Authentication:

According to Firebase API there are 4 different authentication methods:
- signInAnonymously
- signInWithEmailAndPassword
- signInWithCredential
- signInWithCustomToken


```java
    rxFirebaseAuth.signInAnonymously(FirebaseAuth.getInstance())
                .subscribe(new Action1<AuthResult>() {
                    @Override
                    public void call(AuthResult authData) {
                        // process with authData
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // process auth error
                    }
                });
```

##### Database:
You can query single value like:
```java
    rxFirebaseDatabase.observeSingleValue(firebase.child("users").child("nick"), User.class)
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        // process User value
                    }
                });
```

or the list of values:

```java
    rxFirebaseDatabase.observeValuesList(firebase.child("posts"), BlogPost.class)
                .subscribe(new Action1<List<BlogPost>>() {
                    @Override
                    public void call(List<BlogPost> blogPosts) {
                        // process blogPosts collection
                    }
                });
```


## Download

##### Gradle:
```groovy
dependencies {
  compile 'com.google.firebase:firebase-auth:9.0.0'
  compile 'com.google.firebase:firebase-database:9.0.0'
  compile 'com.kelvinapps:rxfirebase:0.0.5'
}
```

##### Maven:
```
<dependency>
  <groupId>com.kelvinapps</groupId>
  <artifactId>rxfirebase</artifactId>
  <version>0.0.5</version>
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
