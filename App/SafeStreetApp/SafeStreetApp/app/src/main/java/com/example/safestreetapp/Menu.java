package com.example.safestreetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    Button mReportViolation, mStatistics,mMyReport,mRequestData;
    private DrawerLayout drawer;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        context = Menu.this;

        mReportViolation = (Button) findViewById(R.id.btn_report_viol);
        mStatistics = (Button) findViewById(R.id.btn_statistics);
        mMyReport = (Button) findViewById(R.id.btn_my_report);
        mRequestData = (Button) findViewById(R.id.btn_request_data);


        //set toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.ac_rep_viol);
        NavigationView navigationView = findViewById(R.id.sidebar_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_draw_open,R.string.navigation_draw_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mReportViolation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ReportIntent = new Intent(Menu.this, Report_Violation.class);
                startActivity(ReportIntent);

            }
        });

        mStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent StatisticsIntent = new Intent(Menu.this, Statistics.class);
                startActivity(StatisticsIntent);

            }
        });

        mMyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myReportIntent = new Intent(Menu.this, User_Reports.class);
                startActivity(myReportIntent);

            }
        });


    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
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
                Intent Logoutintent = new Intent(Menu.this, MainActivity.class);
                startActivity(Logoutintent);

                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void mail(View view) {
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("usernameses", "");

        MailstatsAsync mailtask= new MailstatsAsync(this);
        mailtask.execute(username);
    }
}
