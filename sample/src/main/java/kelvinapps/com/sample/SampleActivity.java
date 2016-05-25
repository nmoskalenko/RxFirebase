package kelvinapps.com.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseAuth;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseUser;

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
        RxFirebaseDatabase.observeValuesList(reference.child("posts"), BlogPost.class)
                .subscribe(blogPosts -> {
                    postsTextView.setText(blogPosts.toString());
                }, throwable -> {
                    Toast.makeText(SampleActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                });

        // observe single user "nick"
        RxFirebaseDatabase.observeSingleValue(reference.child("users").child("nick"), User.class)
                .subscribe(user -> {
                    userTextView.setText(user.toString());
                }, throwable -> {
                    Toast.makeText(SampleActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                });
    }
}
