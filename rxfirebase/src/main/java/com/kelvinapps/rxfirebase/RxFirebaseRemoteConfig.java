package com.kelvinapps.rxfirebase;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigInfo;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;

import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;

public class RxFirebaseRemoteConfig implements RemoteConfigValues {

    private final FirebaseRemoteConfig remoteConfig;

    private RxFirebaseRemoteConfig(FirebaseRemoteConfig remoteConfig) {
        this.remoteConfig = remoteConfig;
    }

    public Observable<Void> fetch() {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                RxHandler.assignOnTask(subscriber, remoteConfig.fetch());
            }
        });
    }

    public Observable<Void> fetch(final long timeout) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                RxHandler.assignOnTask(subscriber, remoteConfig.fetch(timeout));
            }
        });
    }

    @Override
    public boolean getBoolean(String key) {
        return remoteConfig.getBoolean(key);
    }

    @Override
    public byte[] getByteArray(String key) {
        return remoteConfig.getByteArray(key);
    }

    @Override
    public double getDouble(String key) {
        return remoteConfig.getDouble(key);
    }

    @Override
    public long getLong(String key) {
        return remoteConfig.getLong(key);
    }

    @Override
    public String getString(String key) {
        return remoteConfig.getString(key);
    }

    @Override
    public FirebaseRemoteConfigValue getValue(String key) {
        return remoteConfig.getValue(key);
    }

    @Override
    public void setDefaults(int resourceId) {
        remoteConfig.setDefaults(resourceId);
    }

    @Override
    public void setDefaults(Map<String, Object> defaults) {
        remoteConfig.setDefaults(defaults);
    }

    @Override
    public Set<String> getKeysByPrefix(String prefix) {
        return remoteConfig.getKeysByPrefix(prefix);
    }

    public FirebaseRemoteConfigInfo getInfo() {
        return remoteConfig.getInfo();
    }

    public boolean activateFetched() {
        return remoteConfig.activateFetched();
    }

    public RemoteConfigValues withNamespace(String namespace) {
        return new ValuesWithNamespace(remoteConfig, namespace);
    }

    private static class ValuesWithNamespace implements RemoteConfigValues {
        private final FirebaseRemoteConfig remoteConfig;
        private final String namespace;

        private ValuesWithNamespace(FirebaseRemoteConfig remoteConfig, String namespace) {
            this.remoteConfig = remoteConfig;
            this.namespace = namespace;
        }

        @Override
        public boolean getBoolean(String key) {
            return remoteConfig.getBoolean(key, namespace);
        }

        @Override
        public byte[] getByteArray(String key) {
            return remoteConfig.getByteArray(key, namespace);
        }

        @Override
        public double getDouble(String key) {
            return remoteConfig.getDouble(key, namespace);
        }

        @Override
        public long getLong(String key) {
            return remoteConfig.getLong(key, namespace);
        }

        @Override
        public String getString(String key) {
            return remoteConfig.getString(key, namespace);
        }

        @Override
        public FirebaseRemoteConfigValue getValue(String key) {
            return remoteConfig.getValue(key, namespace);
        }

        @Override
        public void setDefaults(int resourceId) {
            remoteConfig.setDefaults(resourceId, namespace);
        }

        @Override
        public void setDefaults(Map<String, Object> defaults) {
            remoteConfig.setDefaults(defaults, namespace);
        }

        @Override
        public Set<String> getKeysByPrefix(String prefix) {
            return remoteConfig.getKeysByPrefix(prefix, namespace);
        }
    }

    public static class Builder {
        private final FirebaseRemoteConfig remoteConfig;

        public Builder(FirebaseRemoteConfig remoteConfig) {
            this.remoteConfig = remoteConfig;
        }

        public RxFirebaseRemoteConfig build() {
            return new RxFirebaseRemoteConfig(remoteConfig);
        }

        public Builder enableDebug(boolean enable) {
            return withSettings(new FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(enable)
                    .build());
        }

        public Builder withSettings(FirebaseRemoteConfigSettings settings) {
            remoteConfig.setConfigSettings(settings);
            return this;
        }
    }
}
