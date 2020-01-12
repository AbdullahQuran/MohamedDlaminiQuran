package com.example.safestreetapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.safestreetapp.Statistics;

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

public class StatsAsync extends AsyncTask<Object, String, Bundle> {

    private WeakReference<Statistics> statisticsWeakReference;
    private boolean stop = false;

    StatsAsync(Statistics statsActivity){
        statisticsWeakReference = new WeakReference<Statistics>(statsActivity);
    }

    @Override
    protected void onPreExecute() {

        Statistics statsActivity = statisticsWeakReference.get();
        if(statsActivity == null || statsActivity.isFinishing()){
            return;
        }

        if(statsActivity.init){

        //make sure date,time,location are correctly input
        if (TextUtils.isEmpty(statsActivity.mAddress.getText())){
            statsActivity.mAddress.setError("Please atleast enter a city!");
            stop = true;
        }
        int chkdate = statsActivity.dateviolS.compareTo(statsActivity.dateviolE);
        int chktime = statsActivity.timeviolS.compareTo(statsActivity.timeviolE);



        if(statsActivity.streetviol.isEmpty() && !statsActivity.streetnviol.isEmpty() && !stop){
            Toast.makeText(statsActivity.context,"Cant search by just street number",Toast.LENGTH_SHORT).show();
            stop = true;
        }
        else if(chkdate > 0 && !stop){
            Toast.makeText(statsActivity.context,"Your start date cant be after your end date! ",Toast.LENGTH_SHORT).show();
            stop = true;
        }
        else if(chktime > 0 && !stop){
            Toast.makeText(statsActivity.context,"Your start time cant be after your end time!",Toast.LENGTH_SHORT).show();
            stop = true;
        }
        }

    }


    @Override
    protected Bundle doInBackground(Object... objects) {


        String connstr = "http://10.0.2.2/SafeStreetMobile/get_stats.php";
        Bundle dbbundle = null;
        String streetviol = (String) objects[0];
        String streetnviol = (String) objects[1];
        String cityviol = (String) objects[2];
        String typeviol = (String) objects[3];
        String dateviolS = (String) objects[4];
        String dateviolE = (String) objects[5];
        String timeviolS = (String) objects[6];
        String timeviolE = (String) objects[7];
        Boolean checkedDate = (Boolean) objects[8];
        Boolean checkedTime = (Boolean) objects[9];
        int[] moccurences = {0,0,0,0,0};


        if(!stop){

            if (!checkedDate) {
                dateviolS = "0000-00-00";
                dateviolE = "9999-12-31";
            }
            if (!checkedTime) {
                timeviolS = "00:00:00";
                timeviolE = "23:59:59";
            }

            try {
                URL url = new URL(connstr);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream ops = http.getOutputStream();
                if (Build.VERSION.SDK_INT >= 19) {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));


                    String data = URLEncoder.encode("streetviol", "UTF-8") + "=" + URLEncoder.encode(streetviol, "UTF-8")
                            + "&&" + URLEncoder.encode("streetnviol", "UTF-8") + "=" + URLEncoder.encode(streetnviol, "UTF-8")
                            + "&&" + URLEncoder.encode("cityviol", "UTF-8") + "=" + URLEncoder.encode(cityviol, "UTF-8")
                            + "&&" + URLEncoder.encode("typeviol", "UTF-8") + "=" + URLEncoder.encode(typeviol, "UTF-8")
                            + "&&" + URLEncoder.encode("dateviolS", "UTF-8") + "=" + URLEncoder.encode(dateviolS, "UTF-8")
                            + "&&" + URLEncoder.encode("dateviolE", "UTF-8") + "=" + URLEncoder.encode(dateviolE, "UTF-8")
                            + "&&" + URLEncoder.encode("timeviolS", "UTF-8") + "=" + URLEncoder.encode(timeviolS, "UTF-8")
                            + "&&" + URLEncoder.encode("timeviolE", "UTF-8") + "=" + URLEncoder.encode(timeviolE, "UTF-8");

                    writer.write(data);
                    writer.flush();
                    writer.close();
                    ops.close();
                }

                InputStream ips = http.getInputStream();
                if (Build.VERSION.SDK_INT >= 19) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));

                    String line = "";
                    for (int i = 0; i < 5; i++) {

                        //parse php data
                        line += reader.readLine();
                        String n = line.replace("n", "");
                        String u = n.replace("u", "");
                        String l = u.replace("l", "");
                        String trim = l.trim();


                        trim = Character.toString(line.charAt(i));
                        if (!trim.equals("")) {

                            moccurences[i] = Integer.parseInt(trim);
                        }


                    }


                    reader.close();
                    ips.close();
                    http.disconnect();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            dbbundle = new Bundle();
            dbbundle.putInt("Double Parking", moccurences[0]);
            dbbundle.putInt("Non Parking Zone", moccurences[1]);
            dbbundle.putInt("Bike Lane Parking", moccurences[2]);
            dbbundle.putInt("Handicap Parking", moccurences[3]);
            dbbundle.putInt("Badly Parked", moccurences[4]);
        }

        return dbbundle;
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        Statistics statsActivity = statisticsWeakReference.get();

        if(!stop){

            if(statsActivity == null || statsActivity.isFinishing()){
                return;
            }

            statsActivity.addDataSet();
            statsActivity.moccurences[0] = bundle.getInt("Double Parking");
            statsActivity.moccurences[1] = bundle.getInt("Non Parking Zone");
            statsActivity.moccurences[2] = bundle.getInt("Bike Lane Parking");
            statsActivity.moccurences[3] = bundle.getInt("Handicap Parking");
            statsActivity.moccurences[4] = bundle.getInt("Badly Parked");





        }
        statsActivity.init = true;
    }
}
