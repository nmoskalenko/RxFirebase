package com.kelvinapps.rxfirebase;

import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;

import java.util.Map;
import java.util.Set;

public interface RemoteConfigValues {
    boolean getBoolean(String key);

    byte[] getByteArray(String key);

    double getDouble(String key);

    long getLong(String key);

    String getString(String key);

    FirebaseRemoteConfigValue getValue(String key);

    void setDefaults(int resourceId);

    void setDefaults(Map<String, Object> defaults);

    Set<String> getKeysByPrefix(String prefix);
}
