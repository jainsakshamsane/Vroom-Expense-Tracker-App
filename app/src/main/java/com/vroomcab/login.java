// login.java
package com.vroomcab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.vroomcab.DBhandler.DBHandler;
import com.vroomcab.utils.BiometricUtils;

import java.util.concurrent.Executor;

public class login extends AppCompatActivity {

    EditText email3;
    EditText password3;
    String email1;
    String password1;
    Button login1;
    Button fingerprintButton;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        email3 = findViewById(R.id.email3);
        password3 = findViewById(R.id.password3);
        login1 = findViewById(R.id.login1);
        fingerprintButton = findViewById(R.id.fingerprintbutton); // Assuming you have a button with ID fingerprintButton
        dbHandler = new DBHandler(login.this);

        // Check if biometric authentication is available and registered
        if (BiometricUtils.isBiometricAvailable(this)) {
            showBiometricPrompt();
        }

        login1.setOnClickListener(view -> {
            email1 = email3.getText().toString();
            password1 = password3.getText().toString();
            if (email1.isEmpty() && password1.isEmpty()) {
                Toast.makeText(login.this, "Please Enter Values", Toast.LENGTH_LONG).show();
            } else {
                // Perform regular login
                performRegularLogin();
            }
        });

        fingerprintButton.setOnClickListener(view -> {
            // Check if biometric authentication is available and registered
            if (BiometricUtils.isBiometricAvailable(this)) {
                showBiometricPrompt();
            } else {
                Toast.makeText(login.this, "Biometric authentication is not available on this device", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showBiometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Biometric authentication succeeded, perform login
                startAddYourExpenseActivity();
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // Handle authentication errors (optional)
                // Toast.makeText(login.this, "Biometric authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Use your biometric credential to log in")
                .setNegativeButtonText("Use password")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void performRegularLogin() {
        String data = dbHandler.login(email1, password1);
        if (data.isEmpty()) {
            Toast.makeText(login.this, "Username not found. Please sign up", Toast.LENGTH_LONG).show();
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
            SharedPreferences.Editor myedit = sharedPreferences.edit();
            myedit.putString("userid", data);
            myedit.apply();
            Toast.makeText(login.this, "Successfully login", Toast.LENGTH_LONG).show();
            startAddYourExpenseActivity();
        }
    }

    private void startAddYourExpenseActivity() {
        Intent intent = new Intent(login.this, addyourexpense.class);
        startActivity(intent);
        Toast.makeText(login.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
        finish(); // Optional: finish the current activity if needed
    }
}
