package kelvinapps.com.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.kelvinapps.rxfirebase.rxFirebase;

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

        // create Firebase reference
        Firebase firebase = new Firebase("https://docs-examples.firebaseio.com/android/saving-data/fireblog");

        // observe posts list under "posts" child.
        rxFirebase.observeValuesList(firebase.child("posts"), BlogPost.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<BlogPost>>() {
                    @Override
                    public void call(List<BlogPost> blogPosts) {
                        postsTextView.setText(blogPosts.toString());
                    }
                });

        // observe posts list under "posts" child.
        rxFirebase.observeSingleValue(firebase.child("users").child("gracehop"), User.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        userTextView.setText(user.toString());
                    }
                });
    }
}
