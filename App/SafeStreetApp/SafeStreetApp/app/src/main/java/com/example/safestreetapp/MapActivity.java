package com.example.safestreetapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    private static final float DEFAULT_ZOOM = 15f;

    //widgets
    private AutoCompleteTextView mSearchText;
    private ImageView mGps;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mSearchText =  findViewById(R.id.input_search);
        mGps = findViewById(R.id.ic_gps);

        mSearchText.setAdapter(new PlaceAutocompleteAdapter(MapActivity.this,android.R.layout.simple_list_item_1));

        initMap();
        init();
    }

    private void getLocate(){

        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list.size() > 0){

            Address address = list.get(0);
            Intent output = new Intent();
            String city = address.getLocality();
            String country = address.getCountryName();
            String area = address.getLocality();
            String postalcode = address.getPostalCode();
            String street = address.getThoroughfare();
            String streetNo = address.getSubThoroughfare();
            output.putExtra("City",city);
            output.putExtra("Country",country);
            output.putExtra("Area",area);
            output.putExtra("Postalcode",postalcode);
            output.putExtra("Address",address.getAddressLine(0));
            output.putExtra("StreetNo",streetNo);
            output.putExtra("Street",street);

            setResult(RESULT_OK, output);
            finish();
            //moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
        }
    }

    private void init(){


        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event.getAction() == KeyEvent.ACTION_DOWN
                    || event.getAction() == KeyEvent.KEYCODE_ENTER){

                    getLocate();

                }
                return false;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });
        hideSoftKeyboard();

    }

    private void getDeviceLocation(){

        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Task location = mfusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Location currentLocation = (Location) task.getResult();
                    moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFAULT_ZOOM,"My Location");
                }
                else{
                    Toast.makeText(MapActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void moveCamera(LatLng latlng, float zoom, String title){

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));

        MarkerOptions options = new MarkerOptions().position(latlng).title(title);
        mMap.addMarker(options);

        hideSoftKeyboard();

    }

    private void initMap(){

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        getDeviceLocation();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
