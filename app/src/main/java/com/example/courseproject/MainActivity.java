package com.example.courseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register, forgottenPassword;
    private Button login;
    private EditText editTextEmail, editTextPassword;
    private ImageView fingerPrintImage;
    private TextInputEditText txt;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    Context context = null;
    PasswordChecker pc = PasswordChecker.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        Objects.requireNonNull(getSupportActionBar()).hide();

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);
        forgottenPassword = (TextView) findViewById(R.id.forgotPassword);
        forgottenPassword.setOnClickListener(this);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        fingerPrintImage = (ImageView) findViewById(R.id.fingerPrint);
        fingerPrintImage.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();

        int i = getIntentInfo();
        if (i == 1) {
            Toast.makeText(MainActivity.this, "FingerPrint Login Failed! User needs to be logged in to use it!", Toast.LENGTH_LONG + 10).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // If register is pressed go to new activity and create a new user
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;

            // If Login is successful start the app
            case R.id.login:
                loginToApp();
                break;

            // If forgot password is pressed go to new activity that allows user to reset password
            case R.id.forgotPassword:
                startActivity(new Intent(this, ResetPassword.class));
                break;

            // If fingerprint is pressed let the user log in with fingerprint
            case R.id.fingerPrint:
                startActivity(new Intent(this, FingerPrintAuthentication.class));
                break;

        }
    }

    // Check password and email validity
    private void loginToApp() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is invalid, please try again!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 12) {
            editTextPassword.setError("Minimum length for password should be 12 characters");
            editTextPassword.requestFocus();
            return;
        }

        boolean uppercase = pc.checkPasswordContainsUpperCaseCharacter(password);
        if (!uppercase) {
            editTextPassword.setError("Password needs to have at least one uppercase letter");
            editTextPassword.requestFocus();
            return;
        }

        boolean lowercase = pc.checkPasswordContainsLowerCaseCharacter(password);
        if (!lowercase) {
            editTextPassword.setError("Password needs to have at least one lowercase letter");
            editTextPassword.requestFocus();
            return;
        }

        boolean specialChar = pc.checkPasswordContainsSpecialCharacter(password);
        if (!specialChar) {
            editTextPassword.setError("Password needs to have at least one special character");
            editTextPassword.requestFocus();
            return;
        }

        boolean number = pc.checkPasswordContainsNumber(password);
        if (!number) {
            editTextPassword.setError("Password needs to have at least one number");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        Intent intent = new Intent(MainActivity.this, Entry.class);
                        intent.putExtra("mainActivity", 1);
                        startActivity(intent);

                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your email to verify your accout!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Failed to login! Please try again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    /* If User is null and trying to login with fingerprint
    Send a toast message to give further information
    Toast message done in onCreate method */
    private int getIntentInfo() {
        Intent mIntent = getIntent();
        int i = mIntent.getIntExtra("key", 0);
        return i;
    }

}