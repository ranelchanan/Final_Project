package mta.com.final_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userDB;

    private Button searchAnimalButton;
    private Button foundAnimalButton;
    private Button communityButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        checkIfUserLoggedIn();
        foundAnimalHandler();
    }

    private void foundAnimalHandler() {
        foundAnimalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FoundDogActivity.class));
            }
        });
    }

    private void initViews(){
        mAuth = FirebaseAuth.getInstance();
        userDB = FirebaseDatabase.getInstance().getReference().child("users");

        searchAnimalButton = findViewById(R.id.searchAnimalButton_home);
        foundAnimalButton = findViewById(R.id.foundAnimalButton_home);
        communityButton = findViewById(R.id.communityButton_home);
    }


    private void checkIfUserLoggedIn(){

        if(mAuth.getCurrentUser() == null){
            goToLoginPage();
        }
    }

    private void goToLoginPage(){

        finish();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        boolean res = false;

        switch(item.getItemId()){
            case R.id.logoutMenu:
                res = onClickLogoutMenu();
                break;
        }

        if (res != false){
            return res;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private boolean onClickLogoutMenu(){
        boolean res = false;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Log Out");
        builder.setMessage("Are you sure you want to log out?");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        return true;
    }
}
