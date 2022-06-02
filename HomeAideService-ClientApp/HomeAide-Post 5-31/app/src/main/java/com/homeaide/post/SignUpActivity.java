package com.homeaide.post;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homeaide.post.Model.LoginDetails;
import com.homeaide.post.chat.ChatMainActivity;
import com.homeaide.post.utilities.Constants;

import java.util.HashMap;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.homeaide.post.databinding.ActivityChatSignUpBinding;
import com.homeaide.post.utilities.Constants;
import com.homeaide.post.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homeaide.post.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailSignUp, passSignUp, phoneSignUp, nameSignUp;
    private Button signUpBtn;
    private TextView signINText;
    private FirebaseAuth auth;
    private FrameLayout layoutImage;
    private ActivityChatSignUpBinding binding;
    private String encodedImage;
    private PreferenceManager preferenceManager;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference reference = db.getReference().child("Register-test");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        LoginDetails lg = new LoginDetails();
        auth = FirebaseAuth.getInstance();

        emailSignUp = findViewById(R.id.sign_up_email);
        passSignUp = findViewById(R.id.sign_up_password);
        layoutImage = findViewById(R.id.layoutImage);
        nameSignUp = findViewById(R.id.sign_up_name);
        phoneSignUp = findViewById(R.id.sign_up_phone);

        signUpBtn = findViewById(R.id.sign_up_btn);
        signINText = findViewById(R.id.sign_in_text);

        signINText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = emailSignUp.getText().toString();
                String txt_password = passSignUp.getText().toString();
                String txt_name = nameSignUp.getText().toString();
                String txt_phone = phoneSignUp.getText().toString();
                lg.setEmail(txt_email);
                lg.setPassword(txt_password);
                if (TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_phone)) {
                    Toast.makeText(SignUpActivity.this, "Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userId = firebaseUser.getUid();
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("email", txt_email);
                            hashMap.put("name", txt_name);
                            hashMap.put("phone", txt_phone);
                            hashMap.put("password", txt_password);


                            reference.child(userId).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SignUpActivity.this, "Client Details Added", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUpActivity.this, "Error! Client Details Not Added" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(SignUpActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });

            }
        });

    }
}