package kelvinapps.com.sample;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Nick Moskalenko on 17/05/2016.
 */
public class SampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);

    }
}
