package mta.com.final_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userDB;
    private BottomNavigationView bottomNavigationView;
    private static boolean isOnResume;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        checkIfUserLoggedIn();
        bottomMenuHandler();
    }


    private void initViews(){
        mAuth = FirebaseAuth.getInstance();
        userDB = FirebaseDatabase.getInstance().getReference().child("users");
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


    public void bottomMenuHandler() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.homeItem_bottomNavigationMenu:
                                selectedFragment = new FoundAndLostTabsContainer();
                                break;
                            case R.id.postItem_bottomNavigationMenu:
                                selectedFragment = new FormFragment();
                                break;
                            case R.id.profileItem_bottomNavigationMenu:
                                selectedFragment = new ProfileFragment();
                                break;
                        }

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new FoundAndLostTabsContainer());//new AnimalsListFragment());
        transaction.commit();
    }

    public static void setIsOnResume() {
        isOnResume = true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isOnResume) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new ProfileFragment());//new AnimalsListFragment());
            transaction.commit();
            bottomNavigationView.setSelectedItemId(R.id.profileItem_bottomNavigationMenu);
        }
    }
}
