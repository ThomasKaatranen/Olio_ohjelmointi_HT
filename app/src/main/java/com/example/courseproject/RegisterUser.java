package com.example.courseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

//Used this as an example https://www.youtube.com/watch?v=Z-RE1QuUWPg&t=3s

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView banner, registerUser;
    private EditText editTextName, editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    PasswordChecker pc = PasswordChecker.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextName = (EditText) findViewById(R.id.name);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.registerUser:
                registerUser();
                break;
        }

    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String name = editTextName.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError("Name is required!");
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        //Checking for a valid email address has @ or .com for example
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide a valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        //Checking if password is valid to be a password
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

        //Add new user to database if registering was done successfully
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(name, email, null);
                    FirebaseDatabase.getInstance("https://courseproject-71f99-default-rtdb.firebaseio.com/").getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterUser.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(RegisterUser.this, MainActivity.class));
                            } else {
                                Toast.makeText(RegisterUser.this, "Failed to register! Try Again!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });

                } else {
                    Toast.makeText(RegisterUser.this, "Failed to register the user", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

}