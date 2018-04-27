package mta.com.final_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressDialog loginProgressDialog;
    private TextView signUpTextView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    private void initViews() {

        emailEditText = findViewById(R.id.emailEditText_login);
        passwordEditText = findViewById(R.id.passwordEditText_login);
        loginButton = findViewById(R.id.loginButton_login);
        signUpTextView = findViewById(R.id.signUpTextView_login);
        loginProgressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        loginUser();
        goToSignUpActivityHandler();
    }

    private void goToSignUpActivityHandler() {
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    private void loginUser(){

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String emailStr = emailEditText.getText().toString().trim();
                final String passwordStr = passwordEditText.getText().toString().trim();

                if (emailStr.equals("") || !Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
                    emailEditText.setError("please enter valid email address");
                    emailEditText.requestFocus();
                } else if (passwordStr.length() < 6) {
                    passwordEditText.setError("password minimum contain 6 character");
                    passwordEditText.requestFocus();
                } else if (passwordStr.equals("")) {
                    passwordEditText.setError("please enter password");
                    passwordEditText.requestFocus();
                } else {

                    loginProgressDialog.setMessage("Please wait...");
                    loginProgressDialog.show();
                    createAccount(emailStr, passwordStr);

                }

            }

        });
    }

    private void createAccount(String email, String password){


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        loginProgressDialog.dismiss();

                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}
