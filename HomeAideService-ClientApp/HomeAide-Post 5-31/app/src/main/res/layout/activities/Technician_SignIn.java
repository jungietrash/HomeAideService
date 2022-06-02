package com.example.testing01.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testing01.Login_as;
import com.example.testing01.OnBoardingActivity;
import com.example.testing01.R;
import com.example.testing01.Registration;
import com.example.testing01.User_Login;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Technician_SignIn extends AppCompatActivity {

    EditText EEmail, EPassword;
    Button ELoginBtn;
    ImageView backBtn;
    FirebaseAuth FAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_sign_in);


        EEmail = findViewById(R.id.emailaddress);
        FAuth = FirebaseAuth.getInstance();
        ELoginBtn = findViewById(R.id.Loginbtn);

        ELoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = EEmail.getText().toString().trim();
                String password = EPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    EEmail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    EPassword.setError("Password is Required");
                    return;
                }
                if (password.length() < 6) {
                    EPassword.setError("Password must be greater than 6 Characters");
                }

                FAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Technician_SignIn.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Technician_LandingPage.class));
                        } else {
                            Toast.makeText(Technician_SignIn.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        backBtn = findViewById(R.id.backBtn);

        ImageView backBtn = (ImageView) findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login_as.class));
            }
        });


    }
}