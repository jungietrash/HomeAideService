package com.homeaide.post.Maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import com.homeaide.post.R;

public class MapsFragment extends Fragment {

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @SuppressLint("MissingPermission")
        @Override
        public void onMapReady(GoogleMap googleMap) {

//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            getDataFromFirebase(googleMap);
            googleMap.setPadding(20,20,20,200);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMap.setMyLocationEnabled(true);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View view=  inflater.inflate(R.layout.fragment_maps, container, false);
    return view; }

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
    Latitude lt = new Latitude();
    ArrayList<Double> result = new ArrayList<Double>();
    List<com.homeaide.post.Maps.Places> placesList = new ArrayList<>();
    void getDataFromFirebase(GoogleMap googleMap){
        placesList.clear();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                placesList.add(snapshot.getValue(com.homeaide.post.Maps.Places.class));
                LatLng address;
                Log.e("placesList",placesList.size()+"");
                for(int i=0;i<placesList.size();i++){
                    try {
                        Log.e("placesList",placesList.get(i).getAddress());
                              ;


                        result.add((double) haversine(lt.getuserLatitude(),lt.getuserLongitude(),placesList.get(i).getLatitude(), placesList.get(i).getLongitude()));
                        address=new LatLng(placesList.get(i).getLatitude(),placesList.get(i).getLongitude());
                        byte[] imageAsByte = Base64.decode(placesList.get(i).getImage().getBytes(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsByte, 0, imageAsByte.length);
                        googleMap.addMarker(new MarkerOptions().position(address)
                                .title(placesList.get(i).getAddress()+", " + String.valueOf(String.format("%.02f",result.get(i*2)))+" KM"))
                                .setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));


                       if (i==0)
                           googleMap.moveCamera(CameraUpdateFactory.newLatLng(address));
                    }catch (Exception e){}
                }
                //remove duplicates
                for(int i=0;i<result.size();i++){
                    for(int j=i+1;j<result.size();j++){
                        if(result.get(i).equals(result.get(j))){
                            result.remove(j);
                            j--;
                        }
                    }

                }

            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }

    public static double haversine(double lat1, double lon1,
                                   double lat2, double lon2) {
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;

    }

    LatLng getLatLongFromAddress(Context context,String strAddress){
        Geocoder geocoder=new Geocoder(context);
        List<Address> address;
        LatLng latLng=null;
        try {
            address=geocoder.getFromLocationName(strAddress,2);
            if (address==null){
                return null;
            }

            Address loc=address.get(0);
            latLng=new LatLng( loc.getLatitude(), loc.getLongitude());
        }catch (Exception e){}
        return latLng;
    }
}