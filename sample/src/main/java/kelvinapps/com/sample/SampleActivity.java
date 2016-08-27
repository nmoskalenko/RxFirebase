package kelvinapps.com.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kelvinapps.rxfirebase.DataSnapshotMapper;
import com.kelvinapps.rxfirebase.RxFirebaseAuth;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseStorage;
import com.kelvinapps.rxfirebase.RxFirebaseUser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import rx.Observable;

public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        final TextView postsTextView = (TextView) findViewById(R.id.txtPosts);
        final TextView userTextView = (TextView) findViewById(R.id.txtUsers);

        // authenticating and getting user token.
        RxFirebaseAuth.signInAnonymously(FirebaseAuth.getInstance())
                .flatMap(x -> RxFirebaseUser.getToken(FirebaseAuth.getInstance().getCurrentUser(), false))
                .subscribe(token -> {
                    Log.i("rxFirebaseSample", "user token: " + token.getToken());
                }, throwable -> {
                    Toast.makeText(SampleActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                });


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        // observe posts list under "posts" child.
        RxFirebaseDatabase.observeValueEvent(reference.child("posts"), DataSnapshotMapper.of(new GenericTypeIndicator<List<BlogPost>>() {
        }))
                .flatMap(Observable::from)
                .subscribe(blogPost -> {
                    postsTextView.setText(postsTextView.getText().toString() + blogPost.toString() + "\n");
                }, throwable -> {
                    Toast.makeText(SampleActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                });

        // observe single user "nick"
        RxFirebaseDatabase.observeSingleValueEvent(reference.child("users").child("nick"), User.class)
                .subscribe(user -> {
                    userTextView.setText(user.toString());
                }, throwable -> {
                    Toast.makeText(SampleActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                });

        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://project-1125675579821020265.appspot.com");
        RxFirebaseStorage.getBytes(storageRef.child("README.md"), 1024 * 100)
                .subscribe(bytes -> {
                    Log.i("rxFirebaseSample", "downloaded: " + new String(bytes));
                }, throwable -> {
                    Log.e("rxFirebaseSample", throwable.toString());
                });


        File targetFile = null;
        try {
            targetFile = File.createTempFile("tmp", "rx");
        } catch (IOException e) {
            e.printStackTrace();
        }
        RxFirebaseStorage.getFile(storageRef.child("README.md"), targetFile)
                .subscribe(snapshot -> {
                    Log.i("rxFirebaseSample", "transferred: " + snapshot.getBytesTransferred() + " bytes");
                }, throwable -> {
                    Log.e("rxFirebaseSample", throwable.toString());
                });


    }
}
