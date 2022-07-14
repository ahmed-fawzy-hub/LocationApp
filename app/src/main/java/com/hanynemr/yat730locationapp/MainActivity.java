package com.hanynemr.yat730locationapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {
    TextView locText;

    LocationManager manager;
    double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locText = findViewById(R.id.locText);
    }

    public void myLoc(View view) {

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] perm={Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this,perm,1);
        }else{
            manager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    manager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this, "feature not supported", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        locText.setText("lat:"+latitude+" long:"+longitude);

        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            String line = addressList.get(0).getAddressLine(0);
            locText.append("\n"+line);
        } catch (IOException e) {
            Toast.makeText(this, "location problem", Toast.LENGTH_SHORT).show();
        }

    }

    public void map(View view) {
        Intent in=new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+latitude+","+longitude));
        startActivity(in);
    }
}