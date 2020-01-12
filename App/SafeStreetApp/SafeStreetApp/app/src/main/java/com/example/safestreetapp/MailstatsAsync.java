package com.example.safestreetapp;


import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MailstatsAsync extends AsyncTask<String,String,String> {


    private WeakReference<Menu> menuWeakReference;


    MailstatsAsync(Menu menuActivity){
        menuWeakReference = new WeakReference<Menu>(menuActivity);
    }


    @Override
    protected void onPreExecute() {

    }



    @Override
    protected String doInBackground(String... strings) {

        String username = strings[0];
        String result ="";
        //UPDATE EXCEL SHEET

        String connstrex = "http://10.0.2.2/SafeStreetMobile/exportexc.php";
        try {
            URL url = new URL(connstrex);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDoInput(true);

            InputStream ips = http.getInputStream();

            ips.close();
            http.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            result = "There was a problem sending the data";


        }

        //MAIL DATA AS ATTACHMENT
        String connstr = "http://10.0.2.2/SafeStreetMobile/mailer.php";

        try {
            URL url = new URL(connstr);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
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

            result = "The data has been sent to Your registered email address!";


            ips.close();
            http.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            result = "There was a problem sending the data";

        }

        return result;

    }

    @Override
    protected void onPostExecute(String s) {

        Menu menuActivity = menuWeakReference.get();
        if(menuActivity == null || menuActivity.isFinishing()){
            return;
        }

        else{
            Toast.makeText(menuActivity.context, s, Toast.LENGTH_SHORT).show();
        }

    }
}
