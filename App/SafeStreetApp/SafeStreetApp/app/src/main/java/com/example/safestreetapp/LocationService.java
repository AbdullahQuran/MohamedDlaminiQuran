package com.example.safestreetapp;

import android.Manifest;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationService extends IntentService {

    public static final String ACTION_PROCESS_UPDATE="com.example.safestreetapp.UPDATE_LOCATION";

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    public LocationService() {
        super("location service");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent == null){
            return;
        }


        double latitude = intent.getDoubleExtra("LATITUDE",0.0);
        double longitude = intent.getDoubleExtra("LONGITUDE",0.0);
        ResultReceiver receiver = intent.getParcelableExtra("reciever");


        Geocoder geocoder = new Geocoder(this);

        if(latitude != 0.0 && longitude != 0.0) {

            List<Address> addresses = null;


            try {
                addresses = geocoder.getFromLocation(latitude,longitude, 1);
                String city = addresses.get(0).getLocality();
                String country = addresses.get(0).getCountryName();
                String area = addresses.get(0).getLocality();
                String postalcode = addresses.get(0).getPostalCode();
                String street = addresses.get(0).getThoroughfare();
                String streetNo = addresses.get(0).getSubThoroughfare();

                Bundle locationBundle = new Bundle();
                locationBundle.putString("city", city);
                locationBundle.putString("country", country);
                locationBundle.putString("area", area);
                locationBundle.putString("postalcode", postalcode);
                locationBundle.putString("street",street);
                locationBundle.putString("streetNo",streetNo);

                receiver.send(1234, locationBundle);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        }
}
