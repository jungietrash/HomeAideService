package com.homeaide.post.chat;

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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.homeaide.post.Model.LoginDetails;
import com.homeaide.post.OnBoardingActivity;
import com.homeaide.post.SignInActivity;
import com.homeaide.post.databinding.ActivityChatSignUpBinding;
import com.homeaide.post.utilities.Constants;
import com.homeaide.post.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homeaide.post.R;

import com.google.firebase.database.DatabaseReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class ChatSignUpActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private static final String dbTAG = "Database";

    private FirebaseAuth auth;
    private ActivityChatSignUpBinding binding;
    private String encodedImage;
    private PreferenceManager preferenceManager;

    protected FirebaseDatabase database;
    protected DatabaseReference databaseUsers;
    LoginDetails lg = new LoginDetails();
    EditText email, password;
    Button signUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        binding = ActivityChatSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListener();

        database = FirebaseDatabase.getInstance();
        databaseUsers = database.getReference("Users");

        auth= FirebaseAuth.getInstance();

        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);



    }

    private void setListener(){
        binding.textSignIn.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), SignInActivity.class)));


        binding.buttonSignUp.setOnClickListener(view -> {
            if (isValidSignUpDetails()){
                signUp();
            }
        });
        binding.layoutImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void signUp(){

        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        lg.setEmail(binding.inputEmail.getText().toString());

        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        lg.setchatEmail(userEmail);
        lg.setchatPassword(userPassword);

        lg.setEmail(userEmail);

        HashMap<String,Object> user = new HashMap<>();
        user.put(Constants.KEY_FIRST_NAME, binding.inputFirstName.getText().toString());
        user.put(Constants.KEY_LAST_NAME, binding.inputLastName.getText().toString());
        user.put(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
        user.put(Constants.KEY_IMAGE, encodedImage);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                    preferenceManager.putString(Constants.KEY_USER_ID,documentReference.getId());
                    preferenceManager.putString(Constants.KEY_FIRST_NAME,binding.inputFirstName.getText().toString());
                    preferenceManager.putString(Constants.KEY_LAST_NAME,binding.inputLastName.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE,encodedImage);
                    Intent intent = new Intent(getApplicationContext(), OnBoardingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }).addOnFailureListener(exception -> {
                    loading(false);
                    showToast(exception.getMessage());

                });

        auth.createUserWithEmailAndPassword(lg.getchatEmail(),lg.getchatPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userId = firebaseUser.getUid();

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userId);
                    hashMap.put("email", binding.inputEmail.getText().toString());
                    hashMap.put("firstName", binding.inputFirstName.getText().toString());
                    hashMap.put("lastName", binding.inputLastName.getText().toString());
                    hashMap.put("phone", binding.inputPhoneNum.getText().toString());


                    Toast.makeText(ChatSignUpActivity.this, "Account creation complete.",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ChatSignUpActivity.this , SignInActivity.class));

                } else {
                    Toast.makeText(getApplicationContext(), "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });




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
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.textAddImage.setVisibility(View.GONE);
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
        }else if (binding.inputFirstName.getText().toString().trim().isEmpty()){
            showToast("Enter First Name");
            return false;
        }else if (binding.inputLastName.getText().toString().trim().isEmpty()){
            showToast("Enter Last Name");
            return false;
        }else if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
            showToast("Enter Valid Email");
            return false;
        }else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter Name");
            return false;
        }else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter Name");
            return false;
        }else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())){
            showToast("Password & confirm password must match");
            return false;
        }else {
            return true;
        }
    }

    private void loading(Boolean isLoading){
        if (isLoading){
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);


        }
    }



    public boolean fieldIsEmpty(EditText et) {
        String value = et.getText().toString();
        if (TextUtils.isEmpty(value)) {
            et.setError("Required.");
            return false;
        } else {
            et.setError(null);
            return true;
        }
    }
}