package com.homeaide.post.booking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.homeaide.post.R;
public class RegisterActivity extends AppCompatActivity {

    EditText name, email, password;
    private FirebaseAuth auth;
    Button signupX;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signupX = findViewById(R.id.signupbtn);
        login = findViewById(R.id.login);

        if (auth.getCurrentUser() !=null){
            startActivity(new Intent(RegisterActivity.this, BookingMainActivity.class));
            finish();
        }

        signupX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = name.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(RegisterActivity.this, "Enter Name! ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(RegisterActivity.this, "Email Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(userPassword)) {
                    Toast.makeText(RegisterActivity.this, "Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, BookingMainActivity.class));
                        }else{
                            Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, BookingLoginActivity.class));

            }
        });
    }
}