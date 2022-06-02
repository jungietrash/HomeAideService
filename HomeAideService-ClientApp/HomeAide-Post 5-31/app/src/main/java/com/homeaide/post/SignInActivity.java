package com.homeaide.post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.analytics.FirebaseAnalytics;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homeaide.post.Model.LoginDetails;
import com.homeaide.post.bookingv2.User;
import com.homeaide.post.chat.ChatSignUpActivity;
import com.homeaide.post.utilities.PreferenceManager;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private EditText signInEmail , signInPass;
    private Button signInBtn;
    PreferenceManager preferenceManager;
    private TextView sign_up_text, userforgotPassword;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;

    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        LoginDetails lg = new LoginDetails();
        mAuth = FirebaseAuth.getInstance();
        userforgotPassword = findViewById(R.id.forgotPassword);
        signInEmail = findViewById(R.id.sign_in_email);
        signInPass = findViewById(R.id.sing_in_pass);
        signInBtn = findViewById(R.id.sign_in_btn);
        sign_up_text = findViewById(R.id.sign_up_text);

        firestore = FirebaseFirestore.getInstance();
        sign_up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this , ChatSignUpActivity.class));
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signInEmail.getText().toString();
                String pass = signInPass.getText().toString();
                lg.setEmail(email);
                lg.setchatEmail(email);

                String PASSWORD_REGEX = "((?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})";


                if (TextUtils.isEmpty(email)) {
                    signInEmail.setError("Email is Required");
                    return;
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    signInEmail.setError("Incorrect Email Format");
                }
                else if (TextUtils.isEmpty(pass)) {

                    signInPass.setError("Password is Required");
                    return;
                }
                else {


                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                              /*  if (user.isEmailVerified()) {
                                    Toast.makeText(SignInActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), OnBoardingActivity.class));
                                } else {
                                    user.sendEmailVerification();
                                    Toast.makeText(SignInActivity.this, "Please check your email to verify your account.", Toast.LENGTH_SHORT).show();
                                }
                            } else {

                               */
                                startActivity(new Intent(getApplicationContext(), OnBoardingActivity.class));
                            }
                        }
                    });


                }
            }
        });

                userforgotPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextInputEditText resetMail = new TextInputEditText(view.getContext());
                        resetMail.setPadding(24, 8, 8, 8);


                        AlertDialog.Builder pwResetDialog = new AlertDialog.Builder(view.getContext());
                        pwResetDialog.setTitle("Reset Password?");
                        pwResetDialog.setMessage("Please enter your email to reset password.");
                        pwResetDialog.setView(resetMail);

                        pwResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String email = resetMail.getText().toString();

                                if (TextUtils.isEmpty(email)) {
                                    Toast.makeText(SignInActivity.this, "Email is Required", Toast.LENGTH_SHORT).show();

                                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                    Toast.makeText(SignInActivity.this, "Invalid format", Toast.LENGTH_SHORT).show();
                                } else {

                                    mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            Toast.makeText(SignInActivity.this, "Please check your email to reset your password.", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignInActivity.this, "The email is no registered", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }


                            }
                        });

                        pwResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        pwResetDialog.create().show();
                    }
                });


            }


            private static final String dbTAG = "Database";

            public void currentUser() {
                // Check if user is signed in (non-null) and update UI accordingly.
                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null) {
                    //get userType
                        databaseUsers.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            User appUser = dataSnapshot.getValue(User.class);
                            Log.d(dbTAG, "User type is: " + appUser.getuserType());

                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(SignInActivity.this, OnBoardingActivity.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(dbTAG, "Failed to read value.", error.toException());
                        }
                    });

                }

            }

    }
