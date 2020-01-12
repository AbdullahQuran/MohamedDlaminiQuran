package com.example.safestreetapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.safestreetapp.Report_Violation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ReportAsync extends AsyncTask<String,String, String>
{

    private WeakReference<Report_Violation> reportViolationWeakReference;
    private boolean stop = false;

    ReportAsync(Report_Violation reportViolationActivity){
        reportViolationWeakReference = new WeakReference<Report_Violation>(reportViolationActivity);
    }


    @Override
    protected void onPreExecute() {

        Report_Violation reportViolationActivity = reportViolationWeakReference.get();
        if(reportViolationActivity == null || reportViolationActivity.isFinishing()){
            return;
        }

        if (TextUtils.isEmpty(reportViolationActivity.mAddressS)){
            reportViolationActivity.mAddress.setError("Please Enter a Location!");
            stop = true;
        }
        else if (TextUtils.isEmpty(reportViolationActivity.cityviol)){
            reportViolationActivity.mAddress.setError("Please Pick a Location with a city atleast");
            stop = true;
        }
        else if (TextUtils.isEmpty(reportViolationActivity.dateviol)){
            reportViolationActivity.mDate.setError("Please Pick a Date for Your Report!");
            stop = true;
        }
        else if (TextUtils.isEmpty(reportViolationActivity.timeviol)){
            reportViolationActivity.mTime.setError("Please Pick a Time for your Report!");
            stop = true;
        }else if(reportViolationActivity.mUploadbtn.getDrawable() == null){
            Toast.makeText(reportViolationActivity.context, "Please Upload an Image of the Violation", Toast.LENGTH_SHORT).show();
            stop = true;
        }
        else if(reportViolationActivity.filepathlen < 39){
            Toast.makeText(reportViolationActivity.context, "For Security reason image must be uploaded from the Safestreet internal storage directory only- Android SDK storage/Android/data/com.example.safestreetapp/files/Pictures (Make sure your internal storage is visible)", Toast.LENGTH_LONG).show();
            stop = true;
        }

    }



    @Override
    protected String doInBackground(String... objects) {
        String result = "";
        String jsonline;
        String connstr = "http://10.0.2.2/SafeStreetMobile/rep_viol.php";


        String userviol = objects[0];
        String streetviol = objects[1];
        String streetnviol =  objects[2];
        String cityviol =  objects[3];
        String typeviol =  objects[4];
        String dateviol =  objects[5];
        String timeviol =  objects[6];


        if(!stop) {

            try {
                URL url = new URL(connstr);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream ops = http.getOutputStream();
                if (Build.VERSION.SDK_INT >= 19) {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));

                    String data = URLEncoder.encode("userviol", "UTF-8") + "=" + URLEncoder.encode(userviol, "UTF-8")
                            + "&&" + URLEncoder.encode("streetviol", "UTF-8") + "=" + URLEncoder.encode(streetviol, "UTF-8")
                            + "&&" + URLEncoder.encode("streetnviol", "UTF-8") + "=" + URLEncoder.encode(streetnviol, "UTF-8")
                            + "&&" + URLEncoder.encode("cityviol", "UTF-8") + "=" + URLEncoder.encode(cityviol, "UTF-8")
                            + "&&" + URLEncoder.encode("typeviol", "UTF-8") + "=" + URLEncoder.encode(typeviol, "UTF-8")
                            + "&&" + URLEncoder.encode("dateviol", "UTF-8") + "=" + URLEncoder.encode(dateviol, "UTF-8")
                            + "&&" + URLEncoder.encode("timeviol", "UTF-8") + "=" + URLEncoder.encode(timeviol, "UTF-8");

                    writer.write(data);
                    writer.flush();
                    writer.close();
                    ops.close();
                }
                InputStream ips = http.getInputStream();
                if (Build.VERSION.SDK_INT >= 19) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));

                    while ((jsonline = reader.readLine()) != null) {

                        result += jsonline;

                    }


                    reader.close();
                    ips.close();
                    http.disconnect();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String string) {

        if(!stop) {

            Report_Violation reportViolationActivity = reportViolationWeakReference.get();
            if(reportViolationActivity == null || reportViolationActivity.isFinishing()){
                return;
            }

            reportViolationActivity.uploadImage();
            Toast.makeText(reportViolationActivity.context, string, Toast.LENGTH_SHORT).show();



        }

    }
}
