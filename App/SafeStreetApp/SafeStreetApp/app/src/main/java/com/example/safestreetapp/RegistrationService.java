package com.example.safestreetapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.widget.Toast;

import androidx.annotation.Nullable;

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

public class RegistrationService extends IntentService {

    public RegistrationService() {
        super("location service");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent == null){
            return;
        }


        String jsonline;
        String connstr = "http://10.0.2.2/SafeStreetMobile/register.php";
        String userreg = intent.getStringExtra("USER");
        String passreg = intent.getStringExtra("PASS");
        String passcreg = intent.getStringExtra("PASSC");
        String emailreg = intent.getStringExtra("EMAIL");
        String phonereg = intent.getStringExtra("PHONE");
        ResultReceiver receiver = intent.getParcelableExtra("reciever");



        try {
            URL url = new URL(connstr);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoInput(true);
            http.setDoOutput(true);

            OutputStream ops = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
            String data = URLEncoder.encode("userreg", "UTF-8") + "=" + URLEncoder.encode(userreg, "UTF-8")
                    + "&&" + URLEncoder.encode("passreg", "UTF-8") + "=" + URLEncoder.encode(passreg, "UTF-8")
                    + "&&" + URLEncoder.encode("passcreg", "UTF-8") + "=" + URLEncoder.encode(passcreg, "UTF-8")
                    + "&&" + URLEncoder.encode("emailreg", "UTF-8") + "=" + URLEncoder.encode(emailreg, "UTF-8")
                    + "&&" + URLEncoder.encode("phonereg", "UTF-8") + "=" + URLEncoder.encode(phonereg, "UTF-8");

            writer.write(data);
            writer.flush();
            writer.close();
            ops.close();

            InputStream ips = http.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));


            while ((jsonline = reader.readLine()) != null) {

                String result="";
                result += jsonline;
                Bundle registrationBundle = new Bundle();
                registrationBundle.putString("result", result);
                receiver.send(1234, registrationBundle);

            }




            reader.close();
            ips.close();
            http.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
