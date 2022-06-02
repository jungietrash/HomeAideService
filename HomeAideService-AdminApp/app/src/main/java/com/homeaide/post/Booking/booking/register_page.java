package com.homeaide.post.Booking.booking;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.google.firebase.firestore.FirebaseFirestore;
import com.homeaide.post.Model.LoginDetails;
import com.homeaide.post.R;
import com.homeaide.post.utilities.Constants;
import com.homeaide.post.utilities.PreferenceManager;
import com.makeramen.roundedimageview.RoundedImageView;

public class register_page extends AppCompatActivity {

    private EditText et_firstName, et_lastName, et_contactNumber, et_username, et_password_signup,
            et_confirmPassword;
    private TextView tv_signIn, textAddImage;
    private Button btn_signUp;
    private FirebaseAuth fAuth;
    private DatabaseReference userDatabase;
    private PreferenceManager preferenceManager;
    LoginDetails lg = new LoginDetails();
    protected FirebaseDatabase database;
    private FrameLayout layoutImage;
    private String encodedImage;
    private RoundedImageView imageProfile;
    private ProgressBar progressBar;
    EditText email, password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        tv_signIn = findViewById(R.id.tv_signIn);
        et_firstName = findViewById(R.id.et_firstName);
        et_lastName = findViewById(R.id.et_lastName);
        et_contactNumber = findViewById(R.id.et_contactNumber);
        et_username = findViewById(R.id.et_username);
        et_password_signup = findViewById(R.id.et_password_signup);
        et_confirmPassword = findViewById(R.id.et_confirmPassword);
        btn_signUp = findViewById(R.id.btn_signUp);


        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        preferenceManager = new PreferenceManager(getApplicationContext());
        database = FirebaseDatabase.getInstance();
        userDatabase = database.getReference("Users");

        userDatabase = FirebaseDatabase.getInstance().getReference(Users.class.getSimpleName());

        textAddImage = findViewById(R.id.textAddImage);
        layoutImage = findViewById(R.id.layoutImage);
        imageProfile = findViewById(R.id.imageProfile);
        email = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password_signup);
        ClickListener();


    }

    private void ClickListener() {
        tv_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register_page.this, sign_in.class);
                startActivity(intent);
            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidSignUpDetails()) {
                    signUp();
                    signUpUser();
                }

            }
        });

        layoutImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }


    private void signUp(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String,Object> user = new HashMap<>();
        user.put(Constants.KEY_FIRST_NAME, et_firstName.getText().toString());
        user.put(Constants.KEY_LAST_NAME, et_lastName.getText().toString());
        user.put(Constants.KEY_EMAIL, et_username.getText().toString());
        user.put(Constants.KEY_PASSWORD, et_password_signup.getText().toString());
        user.put(Constants.KEY_IMAGE, encodedImage);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                    preferenceManager.putString(Constants.KEY_USER_ID,documentReference.getId());
                    user.put(Constants.KEY_FIRST_NAME, et_firstName.getText().toString());
                    user.put(Constants.KEY_LAST_NAME, et_lastName.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE,encodedImage);
                }).addOnFailureListener(exception -> {
                    loading(false);
                    showToast(exception.getMessage());
                });

    }

    private void signUpUser() {

        String firstName = et_firstName.getText().toString();
        String lastName = et_lastName.getText().toString();
        String username = et_username.getText().toString();
        String password = et_password_signup.getText().toString();
        String confirmPass = et_confirmPassword.getText().toString();
        String contactNum = "0" + et_contactNumber.getText().toString();
        String imageName = "";
        String imageUrl = "";
        String ratings = "0";


        if (TextUtils.isEmpty(firstName))
        {
            et_firstName.setError("This field is required");
        }
        else if (TextUtils.isEmpty(lastName))
        {
            et_lastName.setError("This field is required");
        }
        else if (TextUtils.isEmpty(contactNum))
        {
            et_contactNumber.setError("This field is required");
        }
        else if (TextUtils.isEmpty(username) )
        {
            et_username.setError("This field is required");
        }
        else if ( !Patterns.EMAIL_ADDRESS.matcher(username).matches())
        {
            et_username.setError("Incorrect Email Format");
        }
        else if (contactNum.length() < 11)
        {
            et_contactNumber.setError("Contact number must be 10 digit");
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
        }
        else if (password.length() < 8)
        {
            Toast.makeText(this, "Password must be 8 or more characters", Toast.LENGTH_LONG).show();

        }
        else if (!isValidPassword(password))
        {
            new SweetAlertDialog(register_page.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Error!.")
                    .setContentText("Please choose a stronger password. Try a mix of letters, numbers, and symbols.")
                    .show();

            Toast.makeText(this, "Passwords should contain atleast one: uppercase letters: A-Z. One Special Characters. One number: 0-9. ", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(confirmPass))
        {
            Toast.makeText(this, "Please confirm the password", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmPass))
        {
            Toast.makeText(this, "Password did not match", Toast.LENGTH_SHORT).show();
        }
        else
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Creating account");
            progressDialog.show();

            fAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    fAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String userID = user.getUid().toString();

                                Users users = new Users(userID, firstName, lastName, contactNum, username, password, imageName, imageUrl, ratings);

                                userDatabase.child(user.getUid())
                                        .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    fAuth.signOut();
                                                    startActivity(new Intent(getApplicationContext(), sign_in.class));
                                                    Toast.makeText(register_page.this, "User Created", Toast.LENGTH_LONG).show();

                                                } else {
                                                    Toast.makeText(register_page.this, "Creation Failed " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                            } else
                            {
                                Toast.makeText(register_page.this, "Creation Failed " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            });
        }
    }




    private String encodedImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth,previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),result -> {
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            imageProfile.setImageBitmap(bitmap);
                            textAddImage.setVisibility(View.GONE);
                            encodedImage = encodedImage(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private Boolean isValidSignUpDetails(){
        if (encodedImage == null){
            showToast("Select Profile Pic");
            return false;
        }else if (et_firstName.getText().toString().trim().isEmpty()){
            showToast("Enter First name");
            return false;
        }else if (et_lastName.getText().toString().trim().isEmpty()){
            showToast("Enter Last Name");
            return false;
        }else if (et_username.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(et_username.getText().toString()).matches()){
            showToast("Enter Valid image");
            return false;
        }else if (et_password_signup.getText().toString().trim().isEmpty()) {
            showToast("Enter Name");
            return false;
        }else if (et_confirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter Name");
            return false;
        }else if (!et_password_signup.getText().toString().equals(et_confirmPassword.getText().toString())){
            showToast("Password & confirm password must match");
            return false;
        }else {
            return true;
        }
    }

    private void loading(Boolean isLoading){
        if (isLoading){
            btn_signUp.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            btn_signUp.setVisibility(View.VISIBLE);


        }
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private static boolean isValidPassword(String password) {

        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=?!#$%&()*+,./])"
                + "(?=\\S+$).{8,15}$";


        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);

        return m.matches();
    }

}