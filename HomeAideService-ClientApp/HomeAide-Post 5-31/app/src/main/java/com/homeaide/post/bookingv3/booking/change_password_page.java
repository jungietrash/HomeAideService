package com.homeaide.post.bookingv3.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.homeaide.post.R;
import com.homeaide.post.User_Electrical;

public class change_password_page extends AppCompatActivity {

    private EditText et_currentPassword, et_newPassword, et_confirmNewPassword;
    private TextView tv_forgotPassword, tv_back;
    private Button btn_changePassword;
    private FirebaseUser user;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_page);

        user = FirebaseAuth.getInstance().getCurrentUser();
        fAuth = FirebaseAuth.getInstance();

        setRef();
        clickListeners();
    }



    private void clickListeners() {
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), edit_profile_page.class);
                startActivity(intent);
            }
        });

        btn_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = getIntent().getStringExtra("User Email");
                String currentPassword = et_currentPassword.getText().toString().trim();
                String newPassword = et_newPassword.getText().toString().trim();
                String confirmNewPassword = et_confirmNewPassword.getText().toString().trim();

                if (TextUtils.isEmpty(currentPassword))
                {
                    Toast.makeText(change_password_page.this, "Password is Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(newPassword))
                {
                    Toast.makeText(change_password_page.this, "New Password is Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(confirmNewPassword))
                {
                    Toast.makeText(change_password_page.this, "Please confirm password", Toast.LENGTH_SHORT).show();
                    return;
                }

                else if (currentPassword.length() < 8)
                {
                    Toast.makeText(change_password_page.this, "Password must be 8 or more characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (newPassword.length() < 8)
                {
                    Toast.makeText(change_password_page.this, "New Password must be 8 or more characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (confirmNewPassword.length() < 8)
                {
                    Toast.makeText(change_password_page.this, "Confirmation Password must be 8 or more characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                else if (isValidPassword(currentPassword))
                {
                    Toast.makeText(change_password_page.this, "Passwords should contain atleast one: uppercase letters: A-Z." +
                            " One lowercase letters: a-z. One number: 0-9. ", Toast.LENGTH_LONG).show();
                }
                else if (isValidPassword(newPassword))
                {
                    Toast.makeText(change_password_page.this, "New Passwords should contain atleast one: uppercase letters: A-Z." +
                            " One lowercase letters: a-z. One number: 0-9. ", Toast.LENGTH_LONG).show();
                }
                else if (isValidPassword(confirmNewPassword))
                {
                    Toast.makeText(change_password_page.this, "Confirmation Passwords should contain atleast one: uppercase letters: A-Z." +
                            " One lowercase letters: a-z. One number: 0-9. ", Toast.LENGTH_LONG).show();
                }
                else if (!newPassword.equals(confirmNewPassword))
                {
                    Toast.makeText(change_password_page.this, "Password did not match", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email, currentPassword);

                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(change_password_page.this, "Password is changed, please sign in", Toast.LENGTH_SHORT).show();
                                                    fAuth.signOut();
                                                    Intent intent = new Intent(change_password_page.this, login_page.class);
                                                    startActivity(intent);
                                                }
                                                else
                                                {
                                                }
                                            }
                                        });
                                    }
                                    else
                                    {
                                        et_currentPassword.setError("Incorrect Password");
                                        Toast.makeText(change_password_page.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=?!])"
                + "(?=\\S+$).{8,15}$";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);

        return !m.matches();
    }

    private void setRef() {
        et_currentPassword = findViewById(R.id.et_currentPassword);
        et_newPassword = findViewById(R.id.et_newPassword);
        et_confirmNewPassword = findViewById(R.id.et_confirmNewPassword);
        tv_back = findViewById(R.id.tv_back);
        tv_forgotPassword = findViewById(R.id.tv_forgotPassword);
        btn_changePassword = findViewById(R.id.btn_changePassword);
    }

}