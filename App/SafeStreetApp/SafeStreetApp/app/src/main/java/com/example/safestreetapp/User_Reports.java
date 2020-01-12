package com.example.safestreetapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;

import android.content.SharedPreferences;

import android.graphics.Color;

import android.os.Bundle;

import android.view.Gravity;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class User_Reports extends AppCompatActivity {

    TableLayout tableLayout;
    TableRow newrow,newrow2;
    TextView Violation,Street,City,Date,Time,PlaceHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__reports);

        tableLayout = findViewById(R.id.user_reports_table);

        createTable();


    }


    public void createTable() {


        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("usernameses", "");

        String connstr = "http://10.0.2.2/SafeStreetMobile/user_reports.php";
        String jsonline;
        String jsonresult;

            try {
                URL url = new URL(connstr);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                InputStream ips = http.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                StringBuilder sb = new StringBuilder();
                while ((jsonline = reader.readLine()) != null) {

                    sb.append(jsonline + "\n");

                }
                jsonresult = sb.toString();

                // Parsing Json Data
                JSONArray ja = new JSONArray(jsonresult);
                JSONObject jo = null;

                for (int i = 0; i < ja.length(); i++) {

                    int color = Color.parseColor("#2196F3");

                    if(i%2 == 1) {
                        color = Color.parseColor("#1FE227" );
                    }

                    jo = ja.getJSONObject(i);
                    newrow = new TableRow(this);
                    Violation = new TextView(this);
                    Street = new TextView(this);
                    Date = new TextView(this);
                    City = new TextView(this);
                    Time = new TextView(this);
                    PlaceHolder = new TextView(this);
                    Violation.setText(jo.getString("VType"));
                    Violation.setGravity(Gravity.CENTER);
                    Violation.setBackgroundColor(color);
                    String address = jo.getString("VStreetName")+" "+jo.getString("VStreetNo");
                    Street.setText(address);
                    Street.setGravity(Gravity.CENTER);
                    Street.setBackgroundColor(color);
                    Date.setText(jo.getString("VDate"));
                    Date.setGravity(Gravity.CENTER);
                    Date.setBackgroundColor(color);
                    City.setText(jo.getString("VCity"));
                    City.setGravity(Gravity.CENTER);
                    City.setBackgroundColor(color);
                    Time.setText(jo.getString("VTime"));
                    Time.setGravity(Gravity.CENTER);
                    Time.setBackgroundColor(color);
                    PlaceHolder.setText(" ");
                    PlaceHolder.setGravity(Gravity.CENTER);
                    PlaceHolder.setBackgroundColor(color);

                    newrow.addView(Violation);
                    newrow.addView(Street);
                    newrow.addView(Date);
                    newrow2 = new TableRow(this);
                    newrow2.addView(PlaceHolder);
                    newrow2.addView(City);
                    newrow2.addView(Time);


                    tableLayout.addView(newrow);
                    tableLayout.addView(newrow2);

                }

                reader.close();
                ips.close();
                http.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }







    }
}
