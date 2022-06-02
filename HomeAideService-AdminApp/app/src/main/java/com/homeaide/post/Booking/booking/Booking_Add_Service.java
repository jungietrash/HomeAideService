package com.homeaide.post.Booking.booking;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.homeaide.post.HomeActivityForAdmin;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.homeaide.post.R;
public class Booking_Add_Service extends AppCompatActivity {

    private ImageView backButton, serviceImage;
    private EditText serviceName, serviceDescription;
    private Button saveButton;
    private TextView photoSelectTexView, startTime, endTime, serviceAddress, percentageFee, totalPrice,servicePrice;
    private Chip mondayChip, tuesdayChip, wednesdayChip, thursdayChip, fridayChip, saturdayChip, sundayChip;
    private Uri imageUri;
    private Bitmap bitmap;
    private Geocoder geocoder;
    private AutoCompleteTextView serviceCategory;

    private int PLACE_PICKER_REQUEST = 1;
    private int hour, minute;
    private String latLng, latString, longString, category;
    private String[] categoryList = {"Electrical","Handyman","Cleaning","Plumbing"};

    private ArrayAdapter<CharSequence> adapterCategoryItems;

    private boolean AvailMonday = false;
    private boolean AvailTuesday = false;
    private boolean AvailWednesday = false;
    private boolean AvailThursday = false;
    private boolean AvailFriday = false;
    private boolean AvailSaturday= false;
    private boolean AvailSunday = false;

    private FirebaseUser user;
    private DatabaseReference projectDatabase;
    private StorageReference projectStorage;
    private StorageTask addTask;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_add_services);

        serviceName = findViewById(R.id.serviceName);
        serviceDescription = findViewById(R.id.serviceDescription);
        backButton = findViewById(R.id.backButton);
        photoSelectTexView = findViewById(R.id.photoSelectTexView);
        serviceCategory = findViewById(R.id.serviceCategory);
        serviceImage = findViewById(R.id.serviceImage);
        servicePrice = findViewById(R.id.servicePrice);
        startTime = findViewById(R.id.startTime);
        serviceAddress = findViewById(R.id.serviceAddress);
        endTime = findViewById(R.id.endTime);
        percentageFee = findViewById(R.id.percentageFee);
        totalPrice = findViewById(R.id.totalPrice);
        saveButton = findViewById(R.id.saveButton);
        mondayChip = findViewById(R.id.mondayChip);
        tuesdayChip = findViewById(R.id.tuesdayChip);
        wednesdayChip = findViewById(R.id.wednesdayChip);
        thursdayChip = findViewById(R.id.thursdayChip);
        fridayChip = findViewById(R.id.fridayChip);
        saturdayChip = findViewById(R.id.saturdayChip);
        sundayChip = findViewById(R.id.sundayChip);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        projectStorage = FirebaseStorage.getInstance().getReference("Services");
        projectDatabase = FirebaseDatabase.getInstance().getReference("Services");

        setRef();
        initPlaces();
        onClick();
        spinnerCategory();



    }

    private void initPlaces() {
        Places.initialize(getApplicationContext(), getString(R.string.API_KEY));
        serviceAddress.setFocusable(false);
    }

    private void onClick() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Booking_Add_Service.this, HomeActivityForAdmin.class);
                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addTask != null && addTask.isInProgress()){
                    Toast.makeText(Booking_Add_Service.this, "In progress", Toast.LENGTH_SHORT).show();
                } else {
                    inputValidation();
                }
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        hour = i;
                        minute = i1;

                        boolean isPM = (hour >= 12);
                        startTime.setText(String.format("%02d:%02d %s", (hour == 12 || hour == 0) ? 12 : hour % 12, minute, isPM ? "PM" : "AM"));


                    }
                };

                int style = TimePickerDialog.THEME_HOLO_DARK;

                TimePickerDialog timePickerDialog = new TimePickerDialog(Booking_Add_Service.this, style, onTimeSetListener, hour, minute, false);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        hour = i;
                        minute = i1;

                        boolean isPM = (hour >= 12);
                        endTime.setText(String.format("%02d:%02d %s", (hour == 12 || hour == 0) ? 12 : hour % 12, minute, isPM ? "PM" : "AM"));

                    }
                };

                int style = TimePickerDialog.THEME_HOLO_DARK;

                TimePickerDialog timePickerDialog = new TimePickerDialog(Booking_Add_Service.this, style, onTimeSetListener, hour, minute, false);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });

        photoSelectTexView.setOnClickListener(new View.OnClickListener() {
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

        serviceAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<com.google.android.libraries.places.api.model.Place.Field> fieldList = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ADDRESS,
                        com.google.android.libraries.places.api.model.Place.Field.LAT_LNG, com.google.android.libraries.places.api.model.Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(Booking_Add_Service.this);

                startActivityForResult(intent, 100);

            }
        });



        serviceCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                category = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(Booking_Add_Service.this, category, Toast.LENGTH_SHORT).show();
            }
        });



        servicePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inputEditTextField= new EditText(Booking_Add_Service.this);
                inputEditTextField.setInputType(InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_VARIATION_NORMAL);

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(Booking_Add_Service.this);
                builderSingle.setTitle("Add Service Price:");
                builderSingle.setView(inputEditTextField);
                builderSingle.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String editTextInput = inputEditTextField.getText().toString();
                        double oneServicePrice = Double.parseDouble(editTextInput);
                        double onePercentageFee = oneServicePrice * .10;
                        double oneTotalPrice = onePercentageFee + oneServicePrice;

                        servicePrice.setText(String.valueOf(oneServicePrice));
                        percentageFee.setText( String.valueOf(onePercentageFee));
                        totalPrice.setText(String.valueOf(oneTotalPrice));
                    }
                });

                builderSingle.show();
            }
        });

    }

    private void inputValidation() {
        String inputServiceName = serviceName.getText().toString();
        String inputAddress = serviceAddress.getText().toString();
        String timeStart = startTime.getText().toString();
        String timeEnd = endTime.getText().toString();
        String price = servicePrice.getText().toString();

        if(hasImage(serviceImage)){
            Toast.makeText(this, "Photo required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(inputServiceName)){
            Toast.makeText(this, "Service name is required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(inputAddress)){
            Toast.makeText(this, "Address is required", Toast.LENGTH_SHORT).show();
        }
        else if(!mondayChip.isChecked() && !tuesdayChip.isChecked() && !wednesdayChip.isChecked() && !thursdayChip.isChecked()
                && !fridayChip.isChecked() && !saturdayChip.isChecked() && !sundayChip.isChecked()){
            Toast.makeText(this, "Availability required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(timeStart)){
            Toast.makeText(this, "Starting time is required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(timeEnd)){
            Toast.makeText(this, "End time is required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(price)){
            Toast.makeText(this, "Price is required", Toast.LENGTH_SHORT).show();
        }
        else{
            new AlertDialog.Builder(Booking_Add_Service.this)
                .setIcon(R.drawable.transparentlogo)
                .setMessage("Add this service?")
                .setCancelable(true)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        addService();

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

    private void addService() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Adding Project");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StorageReference fileReference = projectStorage.child(imageUri.getLastPathSegment());

        String addServiceName = serviceName.getText().toString();
        String addAddress = serviceAddress.getText().toString();
        String addPrice = totalPrice.getText().toString();
        String addPercentageFee = percentageFee.getText().toString();
        String addStartTime = startTime.getText().toString();
        String addEndTime = endTime.getText().toString();
        String addDescription = serviceDescription.getText().toString();
        String imageName = imageUri.getLastPathSegment();
        int ratings = 0;
        String ratingsText = String.valueOf(ratings);

        addTask = fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String downloadUrl = uri.toString();

                        if(mondayChip.isChecked()){
                            AvailMonday  = true;
                        }

                        if(tuesdayChip.isChecked()){
                            AvailTuesday  = true;
                        }

                        if(wednesdayChip.isChecked()){
                            AvailWednesday  = true;
                        }

                        if(thursdayChip.isChecked()){
                            AvailThursday  = true;
                        }

                        if(fridayChip.isChecked()){
                            AvailFriday  = true;
                        }

                        if(saturdayChip.isChecked()){
                            AvailSaturday  = true;
                        }

                        if(sundayChip.isChecked()){
                            AvailSunday  = true;
                        }

                        Services projects = new Services(userID, category, downloadUrl, imageName, addServiceName, latString, longString,
                                addAddress, addPrice, addPercentageFee, addStartTime, addEndTime, addDescription,
                                ratingsText, AvailMonday, AvailTuesday, AvailWednesday, AvailThursday,
                                AvailFriday, AvailSaturday, AvailSunday);

                        projectDatabase.push().setValue(projects).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(Booking_Add_Service.this, Booking_Projects.class);
                                    startActivity(intent);
                                    Toast.makeText(Booking_Add_Service.this, "Project Added", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(Booking_Add_Service.this, "Failed " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Booking_Add_Service.this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                            .into(serviceImage);

                }catch (Exception e){
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
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
                serviceAddress.setText(addressText);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else if(resultCode != RESULT_CANCELED) {
            if (requestCode == PLACE_PICKER_REQUEST) {

                List<Address> address = null;
                geocoder = new Geocoder(this, Locale.getDefault());

                Place place = PlacePicker.getPlace(data, this);

                try {
                    address = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String addressLine = String.valueOf(address.get(0).getAddressLine(0));
                String locality = String.valueOf(address.get(0).getLocality());
                String country = String.valueOf(address.get(0).getCountryName());

                String addressText =  addressLine + " " + locality + " " + country;

                serviceAddress.setText(addressText);
                latLng = place.getLatLng().toString();

            }
        }
    }

    private void PickImage() {
        CropImage.activity().start(this);
    }


    private void spinnerCategory() {
        adapterCategoryItems = new ArrayAdapter<CharSequence>(this, R.layout.list_property, categoryList);
        serviceCategory.setAdapter(adapterCategoryItems);
    }


    private boolean hasImage(ImageView iv){

        Drawable drawable = iv.getDrawable();
        BitmapDrawable bitmapDrawable = drawable instanceof BitmapDrawable ? (BitmapDrawable)drawable : null;

        return bitmapDrawable == null || bitmapDrawable.getBitmap() == null;
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
