package com.homeaide.post.Booking.booking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homeaide.post.HomeActivityForAdmin;
import com.homeaide.post.Model.LoginDetails;
import com.homeaide.post.R;
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
import com.homeaide.post.utilities.Constants;
import com.homeaide.post.utilities.PreferenceManager;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class sign_in extends AppCompatActivity {

    private EditText et_username, et_password;
    private TextView tv_forgotPassword, btn_signup;
    private Button btn_login;
    private FirebaseAuth fAuth;

    private DatabaseReference userDatabase;
    LoginDetails lg = new LoginDetails();
    private PreferenceManager preferenceManager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        progressBar = findViewById(R.id.progressBar);
        btn_login = findViewById(R.id.btn_login);
        fAuth = FirebaseAuth.getInstance();
        TextView email2 = findViewById(R.id.et_username);
        email2.setText(lg.getchatEmail(), TextView.BufferType.EDITABLE);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        preferenceManager = new PreferenceManager(getApplicationContext());
        btn_signup = findViewById(R.id.tv_signUp);
        tv_forgotPassword = findViewById(R.id.tv_forgotPassword);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentSignUp = new Intent(sign_in.this, register_page.class);
                startActivity(intentSignUp);

            }
        });


        tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fAuth.sendPasswordResetEmail(String.valueOf(et_username)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(sign_in.this, "Please check your email to reset your password.", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(sign_in.this,  "The email is no registered", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    et_username.setError("Email is Required");
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    et_username.setError("Incorrect Email Format");
                } else if (TextUtils.isEmpty(password)) {

                    et_password.setError("Password is Required");
                    return;
                } else if (password.length() < 8) {
                    et_password.setError("Password must be 8 or more characters");
                    return;
                } else {

                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    database.collection(Constants.KEY_COLLECTION_USERS)
                            .whereEqualTo(Constants.KEY_EMAIL, et_username.getText().toString())
                            .whereEqualTo(Constants.KEY_PASSWORD, et_password.getText().toString())
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult() != null &&
                                        task.getResult().getDocuments().size() > 0) {
                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                    preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                                    preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                                    preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));


                                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                if (user.isEmailVerified()) {
                                                    Toast.makeText(sign_in.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), HomeActivityForAdmin.class));
                                                } else {
                                                    user.sendEmailVerification();

                                                    Toast.makeText(sign_in.this, "Please check your email to verify your account.", Toast.LENGTH_SHORT).show();
                                                    new SweetAlertDialog(sign_in.this, SweetAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Account not verified.")
                                                            .setContentText("Please check your email " +
                                                                    "\nto verify your account.")
                                                            .show();
                                                }

                                            } else {
                                                Toast.makeText(sign_in.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                }
            }
        });
    }
}
