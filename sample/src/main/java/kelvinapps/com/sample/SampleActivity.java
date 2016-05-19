package kelvinapps.com.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.rxFirebaseAuth;
import com.kelvinapps.rxfirebase.rxFirebaseDatabase;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        final TextView postsTextView = (TextView) findViewById(R.id.txtPosts);
        final TextView userTextView = (TextView) findViewById(R.id.txtUsers);

        // try to authenticate.
        // will fire exception because example Firebase storage doesn't support authentication
        rxFirebaseAuth.signInAnonymously(FirebaseAuth.getInstance())
                .subscribe(new Action1<AuthResult>() {
                    @Override
                    public void call(AuthResult authResult) {
                        Log.i("rxFirebaseSample", authResult.getUser().toString());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(SampleActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        // observe posts list under "posts" child.
        rxFirebaseDatabase.observeValuesList(reference.child("posts"), BlogPost.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<BlogPost>>() {
                    @Override
                    public void call(List<BlogPost> blogPosts) {
                        postsTextView.setText(blogPosts.toString());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(SampleActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        // observe single user "nick"
        rxFirebaseDatabase.observeSingleValue(reference.child("users").child("nick"), User.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        userTextView.setText(user.toString());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(SampleActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
