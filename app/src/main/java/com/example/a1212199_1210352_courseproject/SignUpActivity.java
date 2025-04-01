package com.example.a1212199_1210352_courseproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Database db =new Database(SignUpActivity.this,"test4",null,1);

        // Initialize fields and buttons
        EditText emailField = findViewById(R.id.emailField);
        EditText fNameField = findViewById(R.id.fNameField);
        EditText lNameField = findViewById(R.id.lNameField);
        EditText passwordField1 = findViewById(R.id.passwordField1);
        EditText passwordField2 = findViewById(R.id.passwordField2);

        ConstraintLayout signupBtn = findViewById(R.id.signupBtn1);
        ConstraintLayout cancelBtn = findViewById(R.id.cancelBtn);

        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);

        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.WHITE); // Background color
        border.setCornerRadius(8);    // Corner radius
        border.setStroke(2, Color.RED); // Border width and color

        // Handle Sign-Up Button Click
        signupBtn.setOnClickListener(view -> {
            String email = emailField.getText().toString().trim();
            String fName = fNameField.getText().toString().trim();
            String lName = lNameField.getText().toString().trim();
            String password = passwordField1.getText().toString().trim();
            String password2 = passwordField2.getText().toString().trim();

            boolean isValid = true;

            // Validate Email
            if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailField.setError("Invalid email address");
                emailField.setBackground(border);
                isValid = false;
            } else {
                emailField.setBackgroundColor(Color.WHITE); // Reset to default
                emailField.setError(null);
            }

            // Validate First Name
            if (TextUtils.isEmpty(fName) || fName.length() < 5 || fName.length() > 20) {
                fNameField.setError("First name must be 5-20 characters");
                fNameField.setBackground(border);
                isValid = false;
            } else {
                fNameField.setBackgroundColor(Color.WHITE);
                fNameField.setError(null);
            }

            // Validate Last Name
            if (TextUtils.isEmpty(lName) || lName.length() < 5 || lName.length() > 20) {
                lNameField.setError("Last name must be 5-20 characters");
                lNameField.setBackground(border);
                isValid = false;
            } else {
                lNameField.setBackgroundColor(Color.WHITE);
                lNameField.setError(null);
            }

            // Validate Password
            if (TextUtils.isEmpty(password) ||
                    password.length() < 6 || password.length() > 12 ||
                    !password.matches(".*[0-9].*") ||
                    !password.matches(".*[a-z].*") ||
                    !password.matches(".*[A-Z].*")) {
                passwordField1.setError("Password must be 6-12 characters with at least 1 number, 1 uppercase, and 1 lowercase letter");
                passwordField1.setBackground(border);
                isValid = false;
            } else {
                passwordField1.setBackgroundColor(Color.WHITE);
                passwordField1.setError(null);
            }

            // Validate Confirm Password
            if (TextUtils.isEmpty(password2) || !password.equals(password2)) {
                passwordField2.setError("Passwords do not match");
                passwordField2.setBackground(border);
                isValid = false;
            } else {
                passwordField2.setBackgroundColor(Color.WHITE);
                passwordField2.setError(null);
            }

            // If all fields are valid
            if (isValid) {
                Toast.makeText(SignUpActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                // Redirect to Sign-In Activity
                newUser = new User(email, fName, lName, password);
                db.insertUser(newUser);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, "Please fix the errors highlighted in red", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Cancel Button Click
        cancelBtn.setOnClickListener(view -> {
            startActivity(intent);
            finish();
        });

    }
}