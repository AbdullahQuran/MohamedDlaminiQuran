package com.example.safestreetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Statistics extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {


    AnyChartView mPieChartView;
    int[] moccurences = {0,0,0,0,0};
    String[] mviolations = {"Double Parking","Non Parking Zone","Bike Lane Parking","Handicap Parking","Badly Parked" };
    Button mDateS,mTimeS,mDateE,mTimeE;
    TextView mDateTVS,mTimeTVS,mDateTVE,mTimeTVE;
    String dateviolS,dateviolE,timeviolS,timeviolE;
    AutoCompleteTextView mAddress;
    EditText mStreet,mStreetNo,mCity;
    String SelectedViol ="";
    String cityviol,streetviol,streetnviol,typeviol,dateviol,timeviol;
    String[] dbresults;
    CharSequence timeDBE = "";
    CharSequence timeDBS = "";
    boolean checkedTime,checkedDate;
    List<DataEntry> occurences = new ArrayList<>();
    Pie pie;
    private DrawerLayout drawer;
    Context context;
    Boolean init = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);


        context = Statistics.this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAddress = findViewById(R.id.input_search);

        mAddress.setAdapter(new PlaceAutocompleteAdapter(Statistics.this,android.R.layout.simple_list_item_1));

        drawer = findViewById(R.id.ac_rep_viol);
        NavigationView navigationView = findViewById(R.id.sidebar_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_draw_open,R.string.navigation_draw_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();





        mPieChartView = findViewById(R.id.piechart_viol);
        pie = AnyChart.pie();

        // DATE AND TIME
        mDateS = (Button) findViewById(R.id.btn_dates_rep);
        mTimeS = (Button) findViewById(R.id.btn_timestart_rep);
        mDateTVS = (TextView) findViewById(R.id.tv_dates_rep);
        mTimeTVS = (TextView) findViewById(R.id.tv_timestart_rep);
        mDateE = (Button) findViewById(R.id.btn_datee_rep);
        mTimeE = (Button) findViewById(R.id.btn_timeend_rep);
        mDateTVE = (TextView) findViewById(R.id.tv_datee_rep);
        mTimeTVE = (TextView) findViewById(R.id.tv_timeend_rep);

        if(!checkedTime){
            mTimeS.setEnabled(false);
            mTimeTVS.setEnabled(false);
            mTimeE.setEnabled(false);
            mTimeTVE.setEnabled(false);
        }
        if(!checkedDate){
            mDateS.setEnabled(false);
            mDateTVS.setEnabled(false);
            mDateE.setEnabled(false);
            mDateTVE.setEnabled(false);
        }

        initializedatetime();

        mDateS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handledatebutton(1);
            }
        });

        mTimeS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handletimebutton(1);
            }
        });
        mDateE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handledatebutton(2);
            }
        });

        mTimeE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handletimebutton(2);
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

        //pie chart initialization
        StatsAsync statstask= new StatsAsync(this);
        statstask.execute("init","init","init","init","init","init","init","init",false,false);

    }

    private void getLocate(){

        String searchString = mAddress.getText().toString();
        Geocoder geocoder = new Geocoder(Statistics.this);
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

    public void addDataSet(){

        for (int i = 0; i < mviolations.length; i++){
            occurences.add(new ValueDataEntry(mviolations[i],moccurences[i]));
        }

        pie.data(occurences);
        mPieChartView.setChart(pie);



        final int delayMillis = 300;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                List<DataEntry> occurences = new ArrayList<>();
                occurences.add(new ValueDataEntry(mviolations[0],moccurences[0]));
                occurences.add(new ValueDataEntry(mviolations[1],moccurences[1]));
                occurences.add(new ValueDataEntry(mviolations[2],moccurences[2]));
                occurences.add(new ValueDataEntry(mviolations[3],moccurences[3]));
                occurences.add(new ValueDataEntry(mviolations[4],moccurences[4]));
                pie.data(occurences);

                handler.postDelayed(this,delayMillis);

            }
        };
        handler.postDelayed(runnable,delayMillis);



    }

    // DATE BUTTON FUNC.
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

            mDateTVS.setText(dateString);
            mDateTVE.setText(dateString);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.HOUR,Hour);
        calendar1.set(Calendar.MINUTE,Minute);


            if (is24HourFormat) {
                CharSequence timeCS = DateFormat.format("kk:mm", calendar1);
                timeDBS = DateFormat.format("kk:mm", calendar1);
                timeDBE = DateFormat.format("kk:mm", calendar1);
                mTimeTVS.setText(timeCS);
                mTimeTVE.setText(timeCS);
            }
            if (!is24HourFormat) {
                CharSequence timeCS = DateFormat.format("hh:mm a", calendar1);
                timeDBS = DateFormat.format("kk:mm", calendar1) + ":00";
                timeDBE = DateFormat.format("kk:mm", calendar1) + ":00";
                mTimeTVS.setText(timeCS);
                mTimeTVE.setText(timeCS);
            }



    }

    private void handledatebutton(int caller) {

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);


        DatePickerDialog datePickerViolation = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int month1 = month + 1;
                String dateString = year + "-" + month1 + "-" + dayOfMonth;

                if(caller == 1){

                    mDateTVS.setText(dateString);
                }
                if(caller == 2){

                    mDateTVE.setText(dateString);
                }


            }
        },year,month,day);

        datePickerViolation.show();
    }

    // TIME BUTTON FUNC.
    private void handletimebutton(int caller) {

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

                    if(caller == 1){

                        mTimeTVS.setText(timeCS);
                        timeDBS = DateFormat.format("kk:mm", calendar1);
                    }
                    if(caller == 2){

                        mTimeTVE.setText(timeCS);
                        timeDBE = DateFormat.format("kk:mm", calendar1);
                    }

                }

                if(!is24HourFormat) {
                    CharSequence timeCS = DateFormat.format("hh:mm a", calendar1);
                    if(caller == 1){

                        mTimeTVS.setText(timeCS);
                        timeDBS = DateFormat.format("kk:mm", calendar1);
                    }
                    if(caller == 2){

                        mTimeTVE.setText(timeCS);
                        timeDBE = DateFormat.format("kk:mm", calendar1);
                    }
                }
            }
        },Hour,Minute,is24HourFormat);

        TimePickerViolation.show();

    }

    //for selected dropdown items
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        SelectedViol = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




    public void UpdateStats(View view) {

            getLocate();


            if(TextUtils.isEmpty(streetviol)){
                streetviol = "NA";
            }
            if(TextUtils.isEmpty(streetnviol)){
                streetnviol = "NA";
            }


        typeviol = SelectedViol;

        dateviolS = mDateTVS.getText().toString();
        dateviolE = mDateTVE.getText().toString();
        timeviolS = timeDBS.toString()+":00";
        timeviolE = timeDBE.toString()+":00";

        //Start Asynctask for stats
        StatsAsync statstask= new StatsAsync(this);
        statstask.execute(streetviol,streetnviol,cityviol,typeviol,dateviolS,dateviolE,timeviolS,timeviolE,checkedDate,checkedTime);
    }

    public void onCheckboxClickedTime(View view) {

        checkedTime = ((CheckBox) view).isChecked();
        if(!checkedTime){
            mTimeS.setEnabled(false);
            mTimeTVS.setEnabled(false);
            mTimeE.setEnabled(false);
            mTimeTVE.setEnabled(false);
        }
        else{
            mTimeS.setEnabled(true);
            mTimeTVS.setEnabled(true);
            mTimeE.setEnabled(true);
            mTimeTVE.setEnabled(true);

        }
    }

    public void onCheckboxClickedDate(View view) {
        checkedDate = ((CheckBox) view).isChecked();
        if(!checkedDate){
            mDateS.setEnabled(false);
            mDateTVS.setEnabled(false);
            mDateE.setEnabled(false);
            mDateTVE.setEnabled(false);
        }
        else{
            mDateS.setEnabled(true);
            mDateTVS.setEnabled(true);
            mDateE.setEnabled(true);
            mDateTVE.setEnabled(true);

        }

    }

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
                Intent Logoutintent = new Intent(Statistics.this, MainActivity.class);
                startActivity(Logoutintent);

                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
