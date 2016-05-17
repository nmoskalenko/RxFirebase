[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0) [![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![Download](https://api.bintray.com/packages/kelvin/maven/rxfirebase/images/download.svg) ](https://bintray.com/kelvin/maven/rxfirebase/_latestVersion)

# RxFirebase

RxJava wrapper on Google's [Firebase for Android](https://www.firebase.com/docs/android/) library.


## Usage
Library provides set of static methods of rxFirebase class.

##### Authentication:

According to Firebase API there are 4 different authentication methods:
- authAnonymously
- authWithPassword
- authWithOAuthToken
- authWithCustomToken


```java
    Firebase firebase = new Firebase("https://docs-examples.firebaseio.com/android/saving-data/fireblog");

    rxFirebase.authAnonymously(firebase)
                .subscribe(new Action1<AuthData>() {
                    @Override
                    public void call(AuthData authData) {
                        // process with authData
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // process auth error
                    }
                });
```

##### Query single value:
You need to provide Firebase Query object and Class you are expecting in response and rxFirebase will do the rest.

```java
    rxFirebase.observeSingleValue(firebase.child("users").child("gracehop"), User.class)
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        // process User value
                    }
                });
```

##### Query values list:
To query the list of values you need to provide Firebase Query object and Class of value.

```java
    rxFirebase.observeValuesList(firebase.child("posts"), BlogPost.class)
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
  compile 'com.kelvinapps:rxfirebase:0.0.3'
}
```

##### Maven:
```
<dependency>
  <groupId>com.kelvinapps</groupId>
  <artifactId>rxfirebase</artifactId>
  <version>0.0.3</version>
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
