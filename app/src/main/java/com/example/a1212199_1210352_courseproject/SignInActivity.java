package com.example.a1212199_1210352_courseproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    public static User user;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Database db =new Database(SignInActivity.this,"test4",null,1);

        sharedPrefManager = SharedPrefManager.getInstance(this);

        ConstraintLayout loginBtn = (ConstraintLayout) findViewById(R.id.signupBtn1);
        ConstraintLayout signupBtn = (ConstraintLayout) findViewById(R.id.cancelBtn);
        EditText emailField = (EditText) findViewById(R.id.emailField);
        EditText passwordField = (EditText) findViewById(R.id.passwordField);
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        Intent intent1 = new Intent(SignInActivity.this, MainActivity.class);

        CheckBox rememberMe = (CheckBox) findViewById(R.id.rememberMe);

        emailField.setText(sharedPrefManager.readString("email", ""));
        passwordField.setText(sharedPrefManager.readString("password", ""));

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                boolean valid = false;
                Cursor allUsers = db.getAllUsers();
                while(allUsers.moveToNext()){
                        if(allUsers.getString(0).equals(email) && allUsers.getString(3).equals(password)){
                            Toast.makeText(SignInActivity.this, "SingIn success", Toast.LENGTH_SHORT).show();
                            if(rememberMe.isChecked()){
                                sharedPrefManager.writeString("email",email);
                                sharedPrefManager.writeString("password",password);
                            }
                            valid = true;
                            user = new User(allUsers.getString(0), allUsers.getString(1), allUsers.getString(2), allUsers.getString(3));
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                startActivity(intent1);
                                finish();
                            }, 500); // 500ms delay
                        }
                }
                if(!valid){
                    Toast.makeText(SignInActivity.this, "Invalid Information", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInActivity.this.startActivity(intent);
                finish();
            }
        });
    }
}