[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0) [![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![Download](https://api.bintray.com/packages/kelvin/maven/rxfirebase/images/download.svg) ](https://bintray.com/kelvin/maven/rxfirebase/_latestVersion)
[![Build Status](https://img.shields.io/badge/Android%20Arsenal-rxFirebase-green.svg?style=true)](https://android-arsenal.com/details/1/3619)
[![Build Status](https://circleci.com/gh/nmoskalenko/rxFirebase/tree/master.svg?style=shield)](https://circleci.com/gh/nmoskalenko/rxFirebase/tree/master)
[![codecov](https://codecov.io/gh/nmoskalenko/rxFirebase/branch/master/graph/badge.svg)](https://codecov.io/gh/nmoskalenko/rxFirebase)

# rxFirebase

RxJava wrapper on Google's [Firebase for Android](https://www.firebase.com/docs/android/) library.


## Usage
Library provides set of static methods of classes:
- rxFirebaseAuth
- rxFirebaseUser
- rxFirebaseDatabase

##### Authentication:

Sign in anonymously and get token:
```java
rxFirebaseAuth.signInAnonymously(FirebaseAuth.getInstance())
            .flatMap(x -> rxFirebaseUser.getToken(FirebaseAuth.getInstance().getCurrentUser(), false))
            .subscribe(token -> {
                Log.i("rxFirebaseSample", "user token: " +  token.getToken());
            }, throwable -> {
                Toast.makeText(SampleActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
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
  compile 'com.kelvinapps:rxfirebase:0.0.7'
}
```

##### Maven:
```
<dependency>
  <groupId>com.kelvinapps</groupId>
  <artifactId>rxfirebase</artifactId>
  <version>0.0.7</version>
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
