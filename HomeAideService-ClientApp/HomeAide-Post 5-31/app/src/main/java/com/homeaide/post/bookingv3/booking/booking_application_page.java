package com.homeaide.post.bookingv3.booking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.homeaide.post.Model.LoginDetails;
import com.homeaide.post.R;
public class booking_application_page extends AppCompatActivity {

    private EditText et_addInfo;
    private TextInputEditText et_phoneNum;
    private TextView tv_address, tv_back, et_date, et_time;
    private Button btn_submit;
    private RadioGroup radioGroup;
    private RadioButton radio_apartment,radio_condo, radio_house;



    private TextView tv_projNameSummary;
    private Geocoder geocoder;
    LoginDetails lg = new LoginDetails();

    private AutoCompleteTextView auto_complete_txt,auto_aircon_type,auto_brand,auto_unit_type;
    private ArrayAdapter<CharSequence>adapterUnitType;
    private String[] unitType = {"HandyMan", "Plumbing", "Electrical", "Cleaning"};
    private String latLng, latString, longString, propertyType, acBrand, acType, acUnitType;
    private int hour, minute;
    private final SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_application_page);

        radio_apartment = findViewById(R.id.radio_apartment);
        radio_condo = findViewById(R.id.radio_condo);
        radio_house = findViewById(R.id.radio_house);
        radioGroup = findViewById(R.id.radioGroup);
        setRef();
        initPlaces();
        dropDownMenuTextView();

        clickListeners();
    }

    private void initPlaces() {

        //Initialize places
        Places.initialize(getApplicationContext(), getString(R.string.API_KEY));

        //Set edittext no focusable
        tv_address.setFocusable(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK){
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
                tv_address.setText(addressText);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
        }

    }

    private void clickListeners() {

        radio_condo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup.check(R.id.radio_condo);
                propertyType = "Condo";
            }
        });

        radio_house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup.check(R.id.radio_house);
                propertyType = "House";
            }
        });

        radio_apartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup.check(R.id.radio_apartment);
                propertyType = "Apartment";
            }
        });
        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // placePicker();


                //Initialize place field list
                List<Place.Field> fieldList = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ADDRESS,
                        com.google.android.libraries.places.api.model.Place.Field.LAT_LNG, com.google.android.libraries.places.api.model.Place.Field.NAME);

                //Create intent
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(booking_application_page.this);

                //Start Activity result
                startActivityForResult(intent, 100);



            }
        });

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        booking_application_page.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        month = month+1;
                        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE");
                        Date dateOfWeek = new Date(year, month, day-1);
                        String dayOfWeek = simpledateformat.format(dateOfWeek);

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("MMMM/dd/yyyy");
                        calendar.set(year, month, day);
                        String dateString = sdf.format(calendar.getTime());

                        et_date.setText(dateString  + "/" + dayOfWeek);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000+(1000*60*60*24*3));
                datePickerDialog.show();

            }
        });

        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        hour = i;
                        minute = i1;

                        boolean isPM = (hour >= 12);
                        et_time.setText(String.format("%02d:%02d %s", (hour == 12 || hour == 0) ? 12 : hour % 12, minute, isPM ? "PM" : "AM"));


                    }
                };

                int style = TimePickerDialog.THEME_HOLO_DARK;

                TimePickerDialog timePickerDialog = new TimePickerDialog(booking_application_page.this, style, onTimeSetListener, hour, minute, false);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });



        auto_unit_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 acUnitType = adapterView.getItemAtPosition(i).toString();
            }
        });



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(tv_address != null && propertyType != null && acUnitType != null && et_date != null &&  et_time != null && et_phoneNum != null && et_addInfo != null){

                    String projectIdFromIntent = getIntent().getStringExtra("projectIdFromIntent");
                    Intent intent = new Intent(booking_application_page.this, booking_summary_page.class);
                    Bundle extras = new Bundle();
                    extras.putString("latitude", latString);
                    extras.putString("longitude", longString);
                    extras.putString("projectIdFromIntent", projectIdFromIntent);
                    extras.putString("address", tv_address.getText().toString());
                    extras.putString("property type", propertyType);
                    extras.putString("unit type",acUnitType);
                    extras.putString("booking date", et_date.getText().toString());
                    extras.putString("booking time", et_time.getText().toString());
                    extras.putString("contact number", "+63" + et_phoneNum.getText().toString());
                    extras.putString("add info", et_addInfo.getText().toString());
                    intent.putExtras(extras);
                    startActivity(intent);

                }
                else  {

                    Toast.makeText(booking_application_page.this, "Please Input all fields", Toast.LENGTH_SHORT).show();

                }



            }
        });


    }

    private void setRef() {

        tv_address = findViewById(R.id.et_address);
        tv_back = findViewById(R.id.tv_back);

        et_phoneNum = findViewById(R.id.et_phoneNum);
        et_addInfo = findViewById(R.id.et_addInfo);
        et_date = findViewById(R.id.et_date);
        et_time = findViewById(R.id.et_time);


        auto_unit_type = findViewById(R.id.auto_unit_type);
        btn_submit = findViewById(R.id.btn_submit);

    }

    private void dropDownMenuTextView() {

        adapterUnitType = new ArrayAdapter<CharSequence>(this, R.layout.list_property, unitType);
        auto_unit_type.setAdapter(adapterUnitType);

    }
}