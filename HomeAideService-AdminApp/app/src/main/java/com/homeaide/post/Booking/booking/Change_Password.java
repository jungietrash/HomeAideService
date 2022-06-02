package com.homeaide.post.Booking.booking;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.homeaide.post.R;

public class Change_Password extends AppCompatActivity {

    private EditText et_currentPassword, et_newPassword, et_confirmNewPassword;
    private ImageView tv_back;
    private Button btn_changePassword;
    private FirebaseUser user;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_change_password);

        user = FirebaseAuth.getInstance().getCurrentUser();
        fAuth = FirebaseAuth.getInstance();

        setRef();
        clickListeners();
    }



    private void clickListeners() {
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Admin_Profile_Page.class);
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
                    Toast.makeText(Change_Password.this, "Password is Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(newPassword))
                {
                    Toast.makeText(Change_Password.this, "New Password is Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(confirmNewPassword))
                {
                    Toast.makeText(Change_Password.this, "Please confirm password", Toast.LENGTH_SHORT).show();
                    return;
                }

                else if (currentPassword.length() < 8)
                {
                    Toast.makeText(Change_Password.this, "Password must be 8 or more characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (newPassword.length() < 8)
                {
                    Toast.makeText(Change_Password.this, "New Password must be 8 or more characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (confirmNewPassword.length() < 8)
                {
                    Toast.makeText(Change_Password.this, "Confirmation Password must be 8 or more characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                else if (isValidPassword(currentPassword))
                {
                    Toast.makeText(Change_Password.this, "Passwords should contain atleast one: uppercase letters: A-Z." +
                            " One lowercase letters: a-z. One number: 0-9. ", Toast.LENGTH_LONG).show();
                }
                else if (isValidPassword(newPassword))
                {
                    Toast.makeText(Change_Password.this, "New Passwords should contain atleast one: uppercase letters: A-Z." +
                            " One lowercase letters: a-z. One number: 0-9. ", Toast.LENGTH_LONG).show();
                }
                else if (isValidPassword(confirmNewPassword))
                {
                    Toast.makeText(Change_Password.this, "Confirmation Passwords should contain atleast one: uppercase letters: A-Z." +
                            " One lowercase letters: a-z. One number: 0-9. ", Toast.LENGTH_LONG).show();
                }
                else if (!newPassword.equals(confirmNewPassword))
                {
                    Toast.makeText(Change_Password.this, "Password did not match", Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(Change_Password.this, "Password is changed, please sign in", Toast.LENGTH_SHORT).show();
                                                    fAuth.signOut();
                                                    Intent intent = new Intent(Change_Password.this, sign_in.class);
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
                                        Toast.makeText(Change_Password.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
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
        btn_changePassword = findViewById(R.id.btn_changePassword);
    }

}