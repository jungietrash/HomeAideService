package com.homeaide.post.Booking.booking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import com.homeaide.post.R;
public class Admin_Profile_Page extends AppCompatActivity {


    private StorageReference userStorage;
    private FirebaseUser user;
    private DatabaseReference userDatabase;
    private StorageTask addTask;
    private Task addTaskNoImage;
    private String userID, tempImageName, tempEmail;

    private ImageView saveButton, profilePhoto, btn_cancel, backBTN;
    private EditText firstName, lastName, contactNumber, emailAddress;
    private TextView uploadPicture,  changePass;
    private Button editButton;

    private ProgressBar progressBar;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile_page);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        userStorage = FirebaseStorage.getInstance().getReference("Users").child(userID);

        emailAddress = findViewById(R.id.emailAddress);
        backBTN = findViewById(R.id.backBTN);
        btn_cancel = findViewById(R.id.cancel_button);
        saveButton = findViewById(R.id.save_btn);
        uploadPicture = findViewById(R.id.uploadPicture);
        editButton = findViewById(R.id.updateBTN);


        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        contactNumber = findViewById(R.id.contactNumber);
        profilePhoto = findViewById(R.id.profileImage);
        progressBar = findViewById(R.id.progressBar);
        changePass = findViewById(R.id.changePass);

        onClick();
        editProfile();

    }

    private void ImagePicker() {
        CropImage.activity().start(this);
    }

    private void onClick() {

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Change_Password.class);
                startActivity(intent);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName.setEnabled(true);
                lastName.setEnabled(true);
                contactNumber.setEnabled(true);
                editButton.setVisibility(View.INVISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((addTask != null && addTask.isInProgress()) || (addTaskNoImage != null))
                {
                    Toast.makeText(Admin_Profile_Page.this, "In progress", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(Admin_Profile_Page.this)
                            .setTitle("Confirm Changes?")
                            .setCancelable(true)
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if(imageUri == null)
                                    {
                                        updateProjectNoImage();
                                    }
                                    else
                                    {
                                        updateProject();
                                    }
                                    Toast.makeText(Admin_Profile_Page.this, "Something went wrong", Toast.LENGTH_SHORT);
                                }
                            })
                            .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                }
                            })
                            .show();

                    firstName.setEnabled(false);
                    lastName.setEnabled(false);
                    contactNumber.setEnabled(false);
                    editButton.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.INVISIBLE);
                    uploadPicture.setVisibility(View.GONE);
                    btn_cancel.setVisibility(View.INVISIBLE);
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName.setEnabled(false);
                lastName.setEnabled(false);
                contactNumber.setEnabled(false);
                editButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.INVISIBLE);
                uploadPicture.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.INVISIBLE);
            }
        });

        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                boolean Imagepicker = true;
                if (Imagepicker == true){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else
                        ImagePicker();

                }else{
                    if(!checkStoragePermission()){
                        Permission();
                    }else
                        ImagePicker();
                }
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin_Profile_Page.this, Admin_Setting.class);
                startActivity(intent);
            }
        });
    }



    private void editProfile() {

        userDatabase.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userProfile = snapshot.getValue(Users.class);

                if(userProfile != null){
                    String profileFirstName = userProfile.firstName;
                    String profileLastName = userProfile.lastName;
                    String profileContact = userProfile.contactNum;
                    String profileEmail = userProfile.email;
                    String profileImage = userProfile.imageUrl;
                    tempImageName = userProfile.getImageName();
                    tempEmail = profileEmail;

                    firstName.setText(profileFirstName);
                    lastName.setText(profileLastName);
                    contactNumber.setText(profileContact);
                    emailAddress.setText(tempEmail);

                    if (!profileImage.isEmpty()) {
                        Picasso.get()
                                .load(profileImage)
                                .into(profilePhoto);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Admin_Profile_Page.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void updateProject() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Profile...");
        progressDialog.show();

        StorageReference fileReference = userStorage.child(imageUri.getLastPathSegment());

        String profileFirstName = firstName.getText().toString();
        String profileLastname = lastName.getText().toString();
        String profileContactNum = contactNumber.getText().toString();
        String profileEmailAddress = emailAddress.getText().toString();
        String profileImage = imageUri.getLastPathSegment();

        addTask = fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String imageURL = uri.toString();


                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("firstName", profileFirstName);
                        hashMap.put("lastName", profileLastname);
                        hashMap.put("contactNum", profileContactNum);
                        hashMap.put("username", profileEmailAddress);
                        hashMap.put("imageName", profileImage);
                        hashMap.put("imageUrl", imageURL);

                        userDatabase.child(userID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {

                                StorageReference imageRef = userStorage.child(tempImageName);
                                imageRef.delete();

                                progressDialog.dismiss();
                                Intent intent = new Intent(Admin_Profile_Page.this, Admin_Profile_Page.class);
                                startActivity(intent);
                                Toast.makeText(Admin_Profile_Page.this, "Updated", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Admin_Profile_Page.this, "Something is wrong" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void updateProjectNoImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Profile...");
        progressDialog.show();

        String firstName1 = firstName.getText().toString();
        String lastName1 = lastName.getText().toString();
        String contactNumber1 = contactNumber.getText().toString();
        String emailAddress1 = emailAddress.getText().toString();

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("firstName", firstName1);
        hashMap.put("lastName", lastName1);
        hashMap.put("contactNum", contactNumber1);
        hashMap.put("username", emailAddress1);

        addTaskNoImage = userDatabase.child(userID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                progressDialog.dismiss();
                Intent intent = new Intent(Admin_Profile_Page.this, Admin_Profile_Page.class);
                startActivity(intent);
                Toast.makeText(Admin_Profile_Page.this, "Profile is updated", Toast.LENGTH_SHORT).show();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Admin_Profile_Page.this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();

                try{
                    Picasso.get().load(imageUri)
                            .into(profilePhoto);

                }catch (Exception e){
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void Permission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private boolean checkStoragePermission() {
        boolean bool2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        return bool2;
    }

    private boolean checkCameraPermission() {
        boolean bool1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;
        boolean bool2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        return bool1 && bool2;
    }
}