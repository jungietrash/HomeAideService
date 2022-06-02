package com.homeaide.post.Booking.booking;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import com.homeaide.post.R;
public class Booking_Edit_Service extends AppCompatActivity {

    private ImageView backBTN, projectImage;
    private EditText editServiceName,editPrice, editDescription;
    private Button editDelete, editUpdate;
    private TextView editUploadPhoto, editStartTime, editEndTime, editAddress, editPercentageFee, editTotalPrice;
    private Chip editChipMonday, editChipTuesday, editChipWednesday, editChipThursday, editChipFriday, editChipSaturday, editChipSunday;
    private String imageUriText, serviceIdIntent, latLng, latString, longString,  tempImageName, category;
    private Uri imageUri, tempUri;
    private Geocoder geocoder;

    private boolean AvailMonday = false;
    private boolean AvailTuesday = false;
    private boolean AvailWednesday = false;
    private boolean AvailThursday = false;
    private boolean AvailFriday = false;
    private boolean AvailSaturday = false;
    private boolean AvailSunday = false;
    private int PLACE_PICKER_REQUEST = 1;
    private int hour, minute;

    private FirebaseUser user;
    private FirebaseStorage mStorage;
    private StorageReference projectStorage;
    private DatabaseReference projectDatabase;
    private StorageTask addTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_edit_service);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        projectStorage = FirebaseStorage.getInstance().getReference("Services");
        projectDatabase = FirebaseDatabase.getInstance().getReference("Services");

        backBTN = findViewById(R.id.backBTN);
        projectImage = findViewById(R.id.iv_projPhotoSummary);
        editDescription = findViewById(R.id.editDescription);
        editServiceName = findViewById(R.id.editServiceName);
        editPrice = findViewById(R.id.editPrice);
        editStartTime = findViewById(R.id.editStartTime);
        editAddress = findViewById(R.id.editAddress);
        editUploadPhoto = findViewById(R.id.editUploadPhoto);
        editEndTime = findViewById(R.id.editEndTime);
        editPercentageFee = findViewById(R.id.editPercentageFee);
        editTotalPrice = findViewById(R.id.editTotalPrice);
        editUpdate = findViewById(R.id.editUpdate);
        editDelete = findViewById(R.id.editDelete);
        editChipMonday = findViewById(R.id.editChipMonday);
        editChipTuesday = findViewById(R.id.editChipTuesday);
        editChipWednesday = findViewById(R.id.editChipWednesday);
        editChipThursday = findViewById(R.id.editChipThursday);
        editChipFriday = findViewById(R.id.editChipFriday);
        editChipSaturday = findViewById(R.id.editChipSaturday);
        editChipSunday = findViewById(R.id.editChipSunday);

        DataValue();
        initPlaces();
        onClick();
    }

    private void initPlaces() {

        Places.initialize(getApplicationContext(), getString(R.string.API_KEY));
        editAddress.setFocusable(false);
    }

    private void onClick() {

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Booking_Edit_Service.this, Booking_Projects.class);
                startActivity(intent);
            }
        });

        editDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteService();
            }
        });

        editUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(addTask != null && addTask.isInProgress()){
                    Toast.makeText(Booking_Edit_Service.this, "In progress", Toast.LENGTH_SHORT).show();
                } else {
                    inputValidation();
                }

            }
        });

        editStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int style = TimePickerDialog.THEME_DEVICE_DEFAULT_DARK;
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        hour = i;
                        minute = i1;

                        boolean isPM = (hour >= 12);
                        editStartTime.setText(String.format("%02d:%02d %s", (hour == 12 || hour == 0) ? 12 : hour % 12, minute, isPM ? "PM" : "AM"));


                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(Booking_Edit_Service.this, style, onTimeSetListener, hour, minute, false);
                timePickerDialog.setTitle("Start Time");
                timePickerDialog.show();
            }
        });

        editEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        hour = i;
                        minute = i1;

                        boolean isPM = (hour >= 12);
                        editEndTime.setText(String.format("%02d:%02d %s", (hour == 12 || hour == 0) ? 12 : hour % 12, minute, isPM ? "PM" : "AM"));

                    }
                };

                int style = TimePickerDialog.THEME_DEVICE_DEFAULT_DARK;

                TimePickerDialog timePickerDialog = new TimePickerDialog(Booking_Edit_Service.this, style, onTimeSetListener, hour, minute, false);
                timePickerDialog.setTitle("End Time");
                timePickerDialog.show();
            }
        });

        editUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                boolean pick = true;
                if (pick == true){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else
                        PickImage();

                }else{
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else
                        PickImage();
                }
            }
        });

        editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG, Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(Booking_Edit_Service.this);
                startActivityForResult(intent, 100);

            }
        });


        editPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inputEditTextField= new EditText(Booking_Edit_Service.this);
                inputEditTextField.setInputType(InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_VARIATION_NORMAL);

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(Booking_Edit_Service.this);
                builderSingle.setIcon(R.drawable.transparentlogo);
                builderSingle.setTitle("Enter Price:");
                builderSingle.setView(inputEditTextField);
                builderSingle.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String editTextInput = inputEditTextField.getText().toString();
                        double servicePrice = Double.parseDouble(editTextInput);
                        double percentageFee = servicePrice * .10;
                        double totalPrice = percentageFee + servicePrice;

                        editPrice.setText(String.valueOf(servicePrice));
                        editPercentageFee.setText( String.valueOf(percentageFee));
                        editTotalPrice.setText(String.valueOf(totalPrice));
                    }
                });

                builderSingle.show();
            }
        });

    }

    private void deleteService() {
        new AlertDialog.Builder(Booking_Edit_Service.this)
                .setIcon(R.drawable.transparentlogo)
                .setTitle("Delete Service?")
                .setCancelable(true)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        serviceIdIntent = getIntent().getStringExtra("Project ID");
                        projectDatabase.child(serviceIdIntent).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Services projectData = snapshot.getValue(Services.class);

                                String imageName = projectData.getImageName();
                                StorageReference imageRef = projectStorage.child(imageName);

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    imageRef.delete();
                                    dataSnapshot.getRef().removeValue();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                })
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                    }
                })
                .show();
    }

    private void inputValidation() {
        String serviceName = editServiceName.getText().toString();
        String serviceAddress = editAddress.getText().toString();
        String serviceTimeStart = editStartTime.getText().toString();
        String serviceEndTime = editEndTime.getText().toString();
        String servicePrice = editPrice.getText().toString();

        if (TextUtils.isEmpty(serviceName)){
            Toast.makeText(this, "Service Name is required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(serviceAddress)){
            Toast.makeText(this, "Address is required", Toast.LENGTH_SHORT).show();
        }
        else if(!editChipMonday.isChecked() && !editChipTuesday.isChecked() && !editChipWednesday.isChecked() && !editChipThursday.isChecked()
                && !editChipFriday.isChecked() && !editChipSaturday.isChecked() && !editChipSunday.isChecked()){
            Toast.makeText(this, "Availability is required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(serviceTimeStart)){
            Toast.makeText(this, "Starting time is required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(serviceEndTime)){
            Toast.makeText(this, "End time is required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(servicePrice)){
            Toast.makeText(this, "Price is required", Toast.LENGTH_SHORT).show();
        }
        else{
            new AlertDialog.Builder(Booking_Edit_Service.this)
                    .setIcon(R.drawable.transparentlogo)
                    .setTitle("Updating project")
                    .setMessage("Please make sure all information entered are correct")
                    .setCancelable(true)
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(imageUri == null)
                            {
                                updateProjectNoImage();
                            }
                            else{
                                updateProject();
                            }

                        }
                    })
                    .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                        }
                    })
                    .show();
        }

    }

    private void setRef() {



    }

    private void updateProject() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Please Wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StorageReference fileReference = projectStorage.child(imageUri.getLastPathSegment());

        String Name = editServiceName.getText().toString();
        String Address = editAddress.getText().toString();
        String price = editPrice.getText().toString();
        String StartTime = editStartTime.getText().toString();
        String EndTime = editEndTime.getText().toString();
        String Description = editDescription.getText().toString();
        String imageName = imageUri.getLastPathSegment();

        addTask = fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String imageURL = uri.toString();

                                chipIfTrue();

                                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                                hashMap.put("category", category);
                                hashMap.put("imageUrl", imageURL);
                                hashMap.put("imageName", imageName);
                                hashMap.put("projName", Name);
                                hashMap.put("longitude", longString);
                                hashMap.put("latitude", latString);
                                hashMap.put("projAddress", Address);
                                hashMap.put("price", price);
                                hashMap.put("startTime", StartTime);
                                hashMap.put("endTime", EndTime);
                                hashMap.put("projInstruction", Description);
                                hashMap.put("availableMon", AvailMonday);
                                hashMap.put("availableTue", AvailTuesday);
                                hashMap.put("availableWed", AvailWednesday);
                                hashMap.put("availableThu", AvailThursday);
                                hashMap.put("availableFri", AvailFriday);
                                hashMap.put("availableSat", AvailSaturday);
                                hashMap.put("availableSun", AvailSunday);

                                serviceIdIntent = getIntent().getStringExtra("Project ID");
                                projectDatabase.child(serviceIdIntent).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {

                                        StorageReference imageRef = projectStorage.child(tempImageName);
                                        imageRef.delete();


                                        progressDialog.dismiss();

                                    }
                                });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Booking_Edit_Service.this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void updateProjectNoImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating list...");
        progressDialog.show();

        String projName = editServiceName.getText().toString();
        String projAddress = editAddress.getText().toString();
        String price = editPrice.getText().toString();
        String serviceStartTime = editStartTime.getText().toString();
        String sp_projEndTime = editEndTime.getText().toString();
        String projInstruction = editDescription.getText().toString();

        chipIfTrue();

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("category", category);
        hashMap.put("projName", projName);
        hashMap.put("longitude", longString);
        hashMap.put("latitude", latString);
        hashMap.put("projAddress", projAddress);
        hashMap.put("price", price);
        hashMap.put("startTime", serviceStartTime);
        hashMap.put("endTime", sp_projEndTime);
        hashMap.put("projInstruction", projInstruction);
        hashMap.put("availableMon", AvailMonday);
        hashMap.put("availableTue", AvailTuesday);
        hashMap.put("availableWed", AvailWednesday);
        hashMap.put("availableThu", AvailThursday);
        hashMap.put("availableFri", AvailFriday);
        hashMap.put("availableSat", AvailSaturday);
        hashMap.put("availableSun", AvailSunday);

        serviceIdIntent = getIntent().getStringExtra("Project ID");
        projectDatabase.child(serviceIdIntent).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                progressDialog.dismiss();

                Toast.makeText(Booking_Edit_Service.this, "Project is updated", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();

                try{
                    Picasso.get().load(imageUri)
                            .placeholder(R.drawable.transparentlogo)
                            .error(R.drawable.transparentlogo)
                            .into(projectImage);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }

        else if(requestCode == 100 && resultCode == RESULT_OK){
            com.google.android.libraries.places.api.model.Place place = Autocomplete.getPlaceFromIntent(data);

            List<Address> address = null;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                address = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);

                latString = String.valueOf(address.get(0).getLatitude());
                longString = String.valueOf(address.get(0).getLongitude());
                String latLngText = latString + "," + longString;
                String addressText =  place.getAddress().toString();

                latLng = latLngText;
                editAddress.setText(addressText);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
        }

        else if(requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {


                List<Address> address = null;
                geocoder = new Geocoder(this, Locale.getDefault());

                assert data != null;
                com.google.android.gms.location.places.Place place = PlacePicker.getPlace(data, this);

                try {
                    address = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String addressLine = String.valueOf(address.get(0).getAddressLine(0));
                String locality = String.valueOf(address.get(0).getLocality());
                String country = String.valueOf(address.get(0).getCountryName());

                String addressText =  addressLine + " " + locality + " " + country;

                editAddress.setText(addressText);
                latLng = place.getLatLng().toString();


            }
        }

    }

    private void DataValue() {

        serviceIdIntent = getIntent().getStringExtra("Project ID");
        projectDatabase.child(serviceIdIntent).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Services projectData = snapshot.getValue(Services.class);

                if(projectData != null){
                    try{
                        tempImageName = projectData.getImageName();

                        category = projectData.getCategory();
                        imageUriText = projectData.getImageUrl();
                        String serviceName = projectData.getProjName();
                        String serviceAddress = projectData.getProjAddress();
                        String servicePPrice = projectData.getPrice();
                        String serviceLatitude = projectData.getLatitude();
                        String serviceLongtitude = projectData.getLongitude();
                        String servicePercentageFee = projectData.getPercentageFee();
                        String serviceStartTime = projectData.getStartTime();
                        String serviceEndTime = projectData.getEndTime();
                        String serviceDescription = projectData.getProjInstruction();
                        boolean AvailMonday = projectData.isAvailableMon();
                        boolean AvailTuesday = projectData.isAvailableTue();
                        boolean AvailWednesday = projectData.isAvailableWed();
                        boolean AvailThursday = projectData.isAvailableThu();
                        boolean AvailFriday = projectData.isAvailableFri();
                        boolean AvailSaturday = projectData.isAvailableSat();
                        boolean AvailSunday = projectData.isAvailableSun();

                        tempUri = Uri.parse(imageUriText);

                        Picasso.get().load(tempUri)
                                .into(projectImage);

                        editServiceName.setText(serviceName);
                        editAddress.setText(serviceAddress);
                        editStartTime.setText(serviceStartTime);
                        editEndTime.setText(serviceEndTime);
                        editDescription.setText(serviceDescription);
                        latString = serviceLatitude;
                        longString = serviceLongtitude;

                        try{
                            double percentageFee = Double.parseDouble(servicePercentageFee);
                            double totalPrice = Double.parseDouble(servicePPrice);
                            double servicePrice = totalPrice - percentageFee;

                            editPrice.setText(String.valueOf(servicePrice));
                            editPercentageFee.setText(servicePercentageFee);
                            editTotalPrice.setText(servicePPrice);

                        } catch(NumberFormatException ex){ // handle your exception
                        }



                        if(AvailMonday == true){
                            editChipMonday.setChecked(true);
                        }
                        if(AvailTuesday == true){
                            editChipTuesday.setChecked(true);
                        }
                        if(AvailWednesday == true){
                            editChipWednesday.setChecked(true);
                        }
                        if(AvailThursday == true){
                            editChipThursday.setChecked(true);
                        }
                        if(AvailFriday == true){
                            editChipFriday.setChecked(true);
                        }
                        if(AvailSaturday == true){
                            editChipSaturday.setChecked(true);
                        }
                        if(AvailSunday == true){
                            editChipSunday.setChecked(true);
                        }


                    }catch (Exception e){
                        e.printStackTrace();

                    }
                }
                else
                {
                    Toast.makeText(Booking_Edit_Service.this, "Empty", Toast.LENGTH_SHORT).show();
                    System.out.println("Empty");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Booking_Edit_Service.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void PickImage() {
        CropImage.activity().start(this);
    }

    private void chipIfTrue() {

        if(!editChipMonday.isChecked() && !editChipTuesday.isChecked() && !editChipWednesday.isChecked() && !editChipThursday.isChecked()
                && !editChipFriday.isChecked() && !editChipSaturday.isChecked() && !editChipSunday.isChecked()){
            Toast.makeText(this, "Please choose a day you are available", Toast.LENGTH_SHORT).show();
        }

        if(editChipMonday.isChecked()){
            AvailMonday = true;
        }

        if(editChipTuesday.isChecked()){
            AvailTuesday = true;
        }

        if(editChipWednesday.isChecked()){
            AvailWednesday = true;
        }

        if(editChipThursday.isChecked()){
            AvailThursday = true;
        }

        if(editChipFriday.isChecked()){
            AvailFriday = true;
        }

        if(editChipSaturday.isChecked()){
            AvailSaturday = true;
        }

        if(editChipSunday.isChecked()){
            AvailSunday = true;
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private boolean checkStoragePermission() {
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        return res2;
    }

    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }

}