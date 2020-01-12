package com.example.safestreetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.location.LocationManager.GPS_PROVIDER;

public class Report_Violation extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_LOCATION = 3;
    private static final int LOCATION_REQ = 5;
    private static final int PICK_IMAGE_RESULT = 15;
    private  static  final int TAKE_REQ = 1;
    private static  final int UPLOAD_REQ = 2;
    private static final int ERROR_DIALOGUE_REQUEST = 55;
    private static final int MAP_REQ= 77;

    Button mDate,mTime,mGetLocation,mTakePic;
    TextView mDateTV,mTimeTV;
    String BMdec = null;
    ImageButton  mUploadbtn;
    int filepathlen = 0;


    AutoCompleteTextView mAddress;
    String mStreet,mStreetno;
    Boolean frommap = false;
    LocationManager locationManager;
    private DrawerLayout drawer;
    File ViolationsFile = null;
    String currentPhotoPath = "";
    Uri imageUri = null;
    String SelectedViol = "";
    String userviol,cityviol,streetviol,streetnviol,typeviol,dateviol,timeviol,imageviol;
    CharSequence timeDB = "";
    Uri filepath;
    String BMenc;
    String City;
    Context context;
    String mAddressS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_violation);

        context = Report_Violation.this;

        mUploadbtn = findViewById(R.id.btn_upload);


        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.viol_drop);
        //create a list of items for the spinner.
        String[] items = new String[]{"Double Parking", "Non Parking Zone", "Bike Lane Parking","Handicap Parking","Badly Parked"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        //to do something with selected items
        dropdown.setOnItemSelectedListener(this);



      //set toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.ac_rep_viol);
        NavigationView navigationView = findViewById(R.id.sidebar_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_draw_open,R.string.navigation_draw_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Permissions for location

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION);

        //LOCATION BUTTON AND TEXTS

        mGetLocation = (Button) findViewById(R.id.btn_get_loc);
        mAddress = findViewById(R.id.input_search);

        mAddress.setAdapter(new PlaceAutocompleteAdapter(Report_Violation.this,android.R.layout.simple_list_item_1));

        mGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    //check if gps is enabled
                    if(!locationManager.isProviderEnabled(GPS_PROVIDER)){

                        TurnOnGPS();
                    }
                    else{

                        getLocation();
                    }
            }
        });


        // DATE AND TIME
        mDate = (Button) findViewById(R.id.btn_date_rep);
        mTime = (Button) findViewById(R.id.btn_time_rep);
        mDateTV = (TextView) findViewById(R.id.tv_date_rep);
        mTimeTV = (TextView) findViewById(R.id.tv_time_rep);

        initializedatetime();

        
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handledatebutton();
            }
        });

        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handletimebutton();
            }
        });



        mTakePic = (Button) findViewById(R.id.btn_take_image);
        mTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //GET PERMISSIONS FOR ACCESSING CAMERA AND EXTERNAL STORAGE
                    if(Build.VERSION.SDK_INT >= 23) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, TAKE_REQ);
                    }
                    else{
                        TakePhoto();
                    }

                }

       });

        mAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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


    }

    private void initializedatetime() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int Hour = calendar.get(Calendar.HOUR);
        int Minute = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        int month1 = month + 1;
        String dateString = year + "-" + month1 + "-" + day;
        mDateTV.setText(dateString);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.HOUR,Hour);
        calendar1.set(Calendar.MINUTE,Minute);

        if(is24HourFormat) {
            CharSequence timeCS = DateFormat.format("kk:mm", calendar1);
            timeDB = DateFormat.format("kk:mm", calendar1);
            mTimeTV.setText(timeCS);
        }

        if(!is24HourFormat) {
            CharSequence timeCS = DateFormat.format("hh:mm a", calendar1);
            timeDB = DateFormat.format("kk:mm", calendar1);
            mTimeTV.setText(timeCS);
        }
    }


    //Check if google services installed on device
    public boolean isServiceOK(){

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Report_Violation.this);

        if(available == ConnectionResult.SUCCESS){

            return true;

        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Report_Violation.this,available,ERROR_DIALOGUE_REQUEST);
            dialog.show();
        }

        return false;
    }


    //TURN ON GPS FUNC
    private void TurnOnGPS(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }



    private void getLocation(){

        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //check gps is enabled or not
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

            //enable gps
            Intent enableGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(enableGPS);

        }
        else if(!isServiceOK()){

            Toast.makeText(this, "Google Services needs to be Installed to use the map", Toast.LENGTH_SHORT).show();

        }
        else {

            //Check Permissions Again
            if (ActivityCompat.checkSelfPermission(Report_Violation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(Report_Violation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQ);
            }
            else {


                Intent mapIntent = new Intent(Report_Violation.this,MapActivity.class);
                startActivityForResult(mapIntent,MAP_REQ);

            }
        }


    }



    // DATE BUTTON FUNC.
    private void handledatebutton() {

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);


        DatePickerDialog datePickerViolation = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int month1 = month + 1;
                String dateString = year + "-" + month1 + "-" + dayOfMonth;
                mDateTV.setText(dateString);
            }
        },year,month,day);

        datePickerViolation.show();
    }

    // TIME BUTTON FUNC.
    private void handletimebutton() {

        Calendar calendar = Calendar.getInstance();


        int Hour = calendar.get(Calendar.HOUR);
        int Minute = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);


        TimePickerDialog TimePickerViolation = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR,hourOfDay);
                calendar1.set(Calendar.MINUTE,minute);

                if(is24HourFormat) {
                    CharSequence timeCS = DateFormat.format("kk:mm", calendar1);
                    timeDB = DateFormat.format("kk:mm", calendar1);
                    mTimeTV.setText(timeCS);
                }

                if(!is24HourFormat) {
                    CharSequence timeCS = DateFormat.format("hh:mm a", calendar1);
                    timeDB = DateFormat.format("kk:mm", calendar1);
                    mTimeTV.setText(timeCS);
                }
            }
        },Hour,Minute,is24HourFormat);

        TimePickerViolation.show();

    }

    //TAKE AND STORE PICTURE
    private void TakePhoto(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, TAKE_REQ);
        }
        else {
            Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takepic.resolveActivity(getPackageManager()) != null) {

                try {

                    ViolationsFile = createImageFile();
                   // Toast.makeText(Report_Violation.this,ViolationsFile.getAbsolutePath(),Toast.LENGTH_SHORT).show();


                    //cont if file was created successfully
                    if(ViolationsFile != null){
                        imageUri = FileProvider.getUriForFile(this,"com.example.safestreetapp.fileprovider",ViolationsFile);
                        takepic.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        startActivityForResult(takepic, TAKE_REQ);
                    }




                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }


        }

        @Override
        protected void onActivityResult(int requestcode, int resultcode, Intent data){
            super.onActivityResult(requestcode,resultcode,data);


            if(requestcode == TAKE_REQ && resultcode==RESULT_OK){

                Bitmap myBitmap = BitmapFactory.decodeFile(ViolationsFile.getAbsolutePath());
                String encFileName = ViolationsFile.getAbsolutePath().substring(ViolationsFile.getAbsolutePath().length() - 39);
                BMenc = BitMapToString(myBitmap);
                //Toast.makeText(Report_Violation.this, encFileName, Toast.LENGTH_SHORT).show();
                //mUploadbtn.setImageBitmap(myBitmap);
                FileOutputStream FoS = null;
                try {
                    FoS = openFileOutput(encFileName,MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    if(BMenc != null){
                    FoS.write(BMenc.getBytes());
                        //Toast.makeText(Report_Violation.this, "Saved to:"+getFilesDir(), Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(FoS != null){
                        try {
                            FoS.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            if(requestcode == PICK_IMAGE_RESULT && data != null && data.getData() != null) {

                        filepath = data.getData();
                        Bitmap photo = null;
                        try {
                            photo = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                            //BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), photo);
                            mUploadbtn.setImageBitmap(photo);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        filepathlen = filepath.getPath().length();
                        if(filepathlen < 39) {
                            Toast.makeText(this, filepath.getPath(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String encFileName = filepath.getPath().substring(filepath.getPath().length() - 39);


                            FileInputStream FiS = null;
                            try {
                                FiS = openFileInput(encFileName);
                                InputStreamReader isr = new InputStreamReader(FiS);
                                BufferedReader br = new BufferedReader(isr);
                                StringBuilder sb = new StringBuilder();
                                String text = null;
                                while ((text = br.readLine()) != null) {

                                    sb.append(text).append("\n");

                                }
                                BMenc = sb.toString();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            BMdec = BitMapToString(photo);
                        }
          }

            if (requestcode == MAP_REQ && resultcode == RESULT_OK && data != null) {

                mStreet = data.getStringExtra("Street");
                mStreetno = data.getStringExtra("StreetNo");
                City = data.getStringExtra("City");

                Toast.makeText(Report_Violation.this,City,Toast.LENGTH_LONG).show();

                mAddress.setText(data.getStringExtra("Address"));
                frommap = true;

            }
                    

            }



        @Override
        public void onRequestPermissionsResult(int requestcode, String[] permissions, int[] grantResult){

            if(requestcode == TAKE_REQ){

                if(grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED && grantResult[1] == PackageManager.PERMISSION_GRANTED){

                    TakePhoto();
                }
                else{
                    Toast.makeText(Report_Violation.this,"Wont work without camera permissions",Toast.LENGTH_LONG).show();
                }

            }
            if(requestcode == UPLOAD_REQ){

                if(grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED){

                    showFileChooser();
                }
                else{
                    Toast.makeText(Report_Violation.this,"Wont work without read permissions",Toast.LENGTH_LONG).show();
                }

            }


        }


        public File createImageFile() throws IOException{

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_"+timeStamp+"_";
            File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);



            File image = File.createTempFile(

                    imageFileName,
                    ".jpg",
                    storageDirectory


            );


            currentPhotoPath = image.getAbsolutePath();
            return image;

        }

    private void getLocate(){

        String searchString = mAddress.getText().toString();
        Geocoder geocoder = new Geocoder(Report_Violation.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list.size() > 0){

            Address address = list.get(0);
            String city = address.getLocality();
            String country = address.getCountryName();
            String area = address.getLocality();
            String postalcode = address.getPostalCode();
            String street = address.getThoroughfare();
            String streetNo = address.getSubThoroughfare();
            streetviol = street;
            streetnviol = streetNo;
            cityviol = city;
            mAddress.setText(address.getAddressLine(0));
            if (TextUtils.isEmpty(cityviol)){
                cityviol = country;
            }

        }
    }



    public void ReportBtn(View view) {

        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        userviol = sharedpreferences.getString("usernameses", "");
        if(frommap){
        streetviol = mStreet;
        streetnviol = mStreetno;
        if(TextUtils.isEmpty(streetviol)){
            streetviol = "NA";

        }
            if(TextUtils.isEmpty(streetnviol)){
                streetnviol = "NA";
            }
            cityviol = City;
        }
        else{
            getLocate();


            if(TextUtils.isEmpty(streetviol)){
                streetviol = "NA";
            }
            if(TextUtils.isEmpty(streetnviol)){
                streetnviol = "NA";
            }

        }

        typeviol = SelectedViol;
        dateviol = mDateTV.getText().toString();
        timeviol = timeDB.toString();
        mAddressS = mAddress.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure everything is filled out correctly?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();



    }

    public void UploadBtn(View view) {
//GET PERMISSIONS FOR ACCESSING CAMERA AND EXTERNAL STORAGE
        if(Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, UPLOAD_REQ);
        }
        else{
            showFileChooser();
        }

    }

    private void showFileChooser(){

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, UPLOAD_REQ);
        }
        else {

        Intent filechooseint = new Intent();
        filechooseint.setType("image/*");
        filechooseint.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(filechooseint,"Select Image"),PICK_IMAGE_RESULT);

        }

    }

    public void uploadImage() {

        UploadAsync uploadtask= new UploadAsync(this);
        uploadtask.execute(filepath);

    }


    // overide back button so that it doesnt switch if sidebar is open
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    // for listener of dropdown
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        String not ="";
        switch (menuItem.getItemId()){

            case R.id.nav_notEnable:

                editor = sharedpreferences.edit();
                editor.remove("notifications");
                editor.putBoolean("notifications", true);
                editor.apply();
                not = String.valueOf(sharedpreferences.getBoolean("notifications",false));
                Toast.makeText(getApplicationContext(),not,Toast.LENGTH_SHORT).show();


                break;

            case R.id.nav_notDisable:
                editor = sharedpreferences.edit();
                editor.remove("notifications");
                editor.putBoolean("notifications",false);
                editor.apply();
                not = String.valueOf(sharedpreferences.getBoolean("notifications",false));
                Toast.makeText(this,not,Toast.LENGTH_SHORT).show();

                break;

            case R.id.nav_logout:

                //reset session and log user out

                editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();
                Intent Logoutintent = new Intent(Report_Violation.this, MainActivity.class);
                startActivity(Logoutintent);

                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // for listener of dropdown
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        SelectedViol = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Convert Bitmap to String

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    ReportAsync reporttask= new ReportAsync(Report_Violation.this);
                    reporttask.execute(userviol,streetviol,streetnviol,cityviol,typeviol,dateviol,timeviol);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };
}
