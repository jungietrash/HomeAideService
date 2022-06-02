package com.homeaide.post.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.homeaide.post.MainActivity;
import com.homeaide.post.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.homeaide.post.Model.LoginDetails;
import com.homeaide.post.utilities.PreferenceManager;
public class BookingLoginActivity extends AppCompatActivity {


    EditText email, password;
    private FirebaseAuth auth;
    Button loginX;
    TextView userEmail;


    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference reference = db.getReference().child("Register-test");
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginDetails lg = new LoginDetails();

        auth = FirebaseAuth.getInstance();
        password = findViewById(R.id.password);
        loginX = findViewById(R.id.loginbtn);

        userEmail = findViewById(R.id.email);
        userEmail.setText(lg.getEmail(), TextView.BufferType.EDITABLE);

        loginX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String userPassword = password.getText().toString();

                auth.signInWithEmailAndPassword(lg.getEmail(),userPassword).addOnCompleteListener(BookingLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(BookingLoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(BookingLoginActivity.this, MainActivity.class));
                        }
                        else{
                            Toast.makeText(BookingLoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });


            }
        });



    }
}