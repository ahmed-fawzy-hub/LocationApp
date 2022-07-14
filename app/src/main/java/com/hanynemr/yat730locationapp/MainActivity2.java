package com.hanynemr.yat730locationapp;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    EditText fromText, toText;
    TextView resultText;
    LocationManager manager;
    PendingIntent pe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        fromText = findViewById(R.id.fromText);
        toText = findViewById(R.id.toText);
        resultText = findViewById(R.id.resultText);
    }

    public void calc(View view) {
        Location from = getLocation(fromText.getText().toString() + " egypt");
        Location to = getLocation(toText.getText().toString() + " egypt");
        if (from != null && to != null) {
            float distance = from.distanceTo(to);
            float kilos = distance / 1000;
            resultText.setText(kilos + "");
        } else {
            Toast.makeText(this, "location error", Toast.LENGTH_SHORT).show();
        }


    }

    private Location getLocation(String address) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocationName(address, 1);
            if (addressList != null && !addressList.isEmpty()) {
                double latitude = addressList.get(0).getLatitude();
                double longitude = addressList.get(0).getLongitude();

                Location loc = new Location("");
                loc.setLatitude(latitude);
                loc.setLongitude(longitude);
                return loc;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void remind(View view) {

        Intent in = new Intent(this, MainActivity3.class);
        pe = PendingIntent.getActivity(this, 0, in, 0);

        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] perm={Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this,perm,1);
        }else{
            manager.addProximityAlert(30.4, 30.5, 50, -1, pe);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    manager.addProximityAlert(30.4, 30.5, 50, -1, pe);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this, "feature not supported", Toast.LENGTH_SHORT).show();
            }
        }

    }
}