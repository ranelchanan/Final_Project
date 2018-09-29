package mta.com.final_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class CardViewItemActivity extends AppCompatActivity {

    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view_item);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        TextView descriptionTextView = findViewById(R.id.description_cardViewItem);
        String userId = getIntent().getExtras().get("userId").toString();
        String descriptionStr = getIntent().getExtras().get("description").toString();
        String message;

        if (Objects.equals(userId, currentUser.getUid())) {
            message = "This is your post!";
        } else {
            message = "This is NOT you post!";
        }

        descriptionTextView.setText(message);
    }
}
