package com.example.courseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.concurrent.Executor;


public class FingerPrintAuthentication extends AppCompatActivity {

    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print_authentication);
        Objects.requireNonNull(getSupportActionBar()).hide();


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                // If user is null send info to MainActivity to display a toast message
                if (mUser == null) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("key", 1);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(FingerPrintAuthentication.this, "Ready to use fingerprint to login!", Toast.LENGTH_LONG).show();
                }
                break;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(FingerPrintAuthentication.this, "The device doesn't support fingerprint sensor!", Toast.LENGTH_LONG).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(FingerPrintAuthentication.this, "The biometric sensor is currently unavailable!", Toast.LENGTH_LONG).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(FingerPrintAuthentication.this, "Your device doesn't have a fingerprint saved, check your settings!", Toast.LENGTH_LONG).show();
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new BiometricPrompt(FingerPrintAuthentication.this, executor, new BiometricPrompt.AuthenticationCallback() {


            @Override // This method is called when there is an error in the authentication
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                startActivity(new Intent(FingerPrintAuthentication.this, MainActivity.class));
            }

            @Override // This method is called when authentication is successful
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Login done successfully!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(FingerPrintAuthentication.this, Entry.class);
                startActivity(intent);
            }

            @Override // This method is called when authentication fails
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        // Set up fingerprint authentication pop up window
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login")
            .setDescription("Use your fingerprint to login")
            .setNegativeButtonText("Cancel")
            .build();

        biometricPrompt.authenticate(promptInfo);

    }
}