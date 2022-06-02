package com.homeaide.post.Maps;

import android.os.Bundle;
import android.widget.TextView;
import com.homeaide.post.R;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayLatlong extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_display_latlong);
        Latitude lt = new Latitude();

        TextView textLat = (TextView) findViewById(R.id.userLatitude);
        TextView textLon = (TextView) findViewById(R.id.userLongitude);
        TextView haversine = (TextView) findViewById(R.id.haversine);

        textLat.setText(String.valueOf(lt.getuserLatitude()));
        textLon.setText(String.valueOf(lt.getuserLongitude()));

        String result = String.valueOf(haversine(lt.getuserLatitude(), lt.getuserLongitude(), 10.383279348267282, 123.92252474852688));

        haversine.setText(result);
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


}
