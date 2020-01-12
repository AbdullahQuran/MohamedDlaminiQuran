package com.example.safestreetapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.safestreetapp.R;

import java.net.HttpURLConnection;
import java.net.URL;

public class GetData extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getdata);


    }

    public void getExcel(View view) {

        String connstr = "http://10.0.2.2/SafeStreetMobile/exportexc.php";

        try {
            URL url = new URL(connstr);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoInput(true);
            http.setDoOutput(true);


        }catch (Exception e){
            e.printStackTrace();
        }

    }
}