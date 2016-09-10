package kelvinapps.com.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kelvinapps.rxfirebase.DataSnapshotMapper;
import com.kelvinapps.rxfirebase.RxFirebaseAuth;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseStorage;
import com.kelvinapps.rxfirebase.RxFirebaseUser;

import java.io.File;
import java.io.IOException;

public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        final TextView postsTextView = (TextView) findViewById(R.id.txtPosts);
        final TextView userTextView = (TextView) findViewById(R.id.txtUsers);

        authenticate();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        getBlogPostsAsList(postsTextView, reference);
        getBlogPostsAsMap(postsTextView, reference);

        getUser(userTextView, reference);
        getNonExistedUser(userTextView, reference);
        getUserCustomMapper(userTextView, reference);

        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://project-1125675579821020265.appspot.com");
        downloadFile(storageRef);
        uploadFile(storageRef);
    }

    private void uploadFile(StorageReference storageRef) {
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

    private void downloadFile(StorageReference storageRef) {
        RxFirebaseStorage.getBytes(storageRef.child("README.md"), 1024 * 100)
                .subscribe(bytes -> {
                    Log.i("rxFirebaseSample", "downloaded: " + new String(bytes));
                }, throwable -> {
                    Log.e("rxFirebaseSample", throwable.toString());
                });
    }

    private void getUserCustomMapper(TextView userTextView, DatabaseReference reference) {
        // observe single user "nick"
        RxFirebaseDatabase.observeSingleValueEvent(reference.child("users").child("nick"), DataSnapshotMapper.of(User.class))
                .subscribe(user -> {
                    userTextView.setText(user.toString());
                }, throwable -> {
                    Toast.makeText(SampleActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                });
    }

    private void getUser(TextView userTextView, DatabaseReference reference) {
        // observe single user "nick"
        RxFirebaseDatabase.observeSingleValueEvent(reference.child("users").child("nick"), User.class)
                .subscribe(user -> {
                    userTextView.setText(user.toString());
                }, throwable -> {
                    Toast.makeText(SampleActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                });
    }

    private void getNonExistedUser(TextView userTextView, DatabaseReference reference) {
        // try to observe non-existed value - would return null
        RxFirebaseDatabase.observeSingleValueEvent(reference.child("users").child("unknown"), User.class)
                .subscribe(user -> {
                    if (user != null) {
                        userTextView.setText(user.toString());
                    } else {
                        userTextView.setText(userTextView.getText().toString() + "\nno such user");
                    }
                }, throwable -> {
                    Toast.makeText(SampleActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                });
    }

    private void getBlogPostsAsList(TextView postsTextView, DatabaseReference reference) {
        // observe posts as list of items
        RxFirebaseDatabase.observeSingleValueEvent(reference.child("posts"), DataSnapshotMapper.listOf(BlogPost.class))
                .subscribe(blogPost -> {
                    postsTextView.setText(postsTextView.getText().toString() + blogPost.toString() + "\n");
                }, throwable -> {
                    Toast.makeText(SampleActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                });
    }

    private void getBlogPostsAsMap(TextView postsTextView, DatabaseReference reference) {
        // observe posts as key-value map of items
        RxFirebaseDatabase.observeSingleValueEvent(reference.child("posts"), DataSnapshotMapper.mapOf(BlogPost.class))
                .subscribe(blogPostAsMapItem -> {
                    postsTextView.setText(postsTextView.getText().toString() + blogPostAsMapItem.toString() + "\n");
                }, throwable -> {
                    Toast.makeText(SampleActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                });
    }

    private void authenticate() {
        // authenticating and getting user token.
        RxFirebaseAuth.signInAnonymously(FirebaseAuth.getInstance())
                .flatMap(x -> RxFirebaseUser.getToken(FirebaseAuth.getInstance().getCurrentUser(), false))
                .subscribe(token -> {
                    Log.i("rxFirebaseSample", "user token: " + token.getToken());
                }, throwable -> {
                    Toast.makeText(SampleActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                });
    }
}
