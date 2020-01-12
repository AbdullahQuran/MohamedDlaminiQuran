package com.example.safestreetapp;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.safestreetapp.MainActivity;
import com.example.safestreetapp.Menu;

import org.json.JSONArray;
import org.json.JSONObject;

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

public class LoginAsync extends AsyncTask<String, String, Bundle> {

    private WeakReference<MainActivity> mainActivityWeakReference;
    boolean stop = false;

    LoginAsync(MainActivity mainActivity){
        mainActivityWeakReference = new WeakReference<MainActivity>(mainActivity);
    }

    @Override
    protected void onPreExecute() {

        MainActivity mainActivity = mainActivityWeakReference.get();
        if(mainActivity == null || mainActivity.isFinishing()){
            return;
        }

        if (TextUtils.isEmpty(mainActivity.mTextUsername.getText().toString())){
           mainActivity.mTextUsername.setError("Please Enter Your Username!");
            stop = true;
        }
        else if (TextUtils.isEmpty(mainActivity.mTextPassword.getText().toString())){
            mainActivity.mTextPassword.setError("Please Enter Your Password!");
            stop = true;
        }
    }

    @Override
    protected Bundle doInBackground(String... strings) {

        String user = strings[0];
        String pass = strings[1];

        Bundle dbbundle = null;
        String resultuser = "";
        String resultpass = "";
        String connstr = "http://10.0.2.2/SafeStreetMobile/logins.php";
        String jsonline;
        String jsonresult;

        if(!stop){
            try {
                URL url = new URL(connstr);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8")
                        + "&&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");

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

                //data2= new String[ja.length()];

                for (int i = 0; i < ja.length(); i++) {

                    jo = ja.getJSONObject(i);
                    resultuser = jo.getString("Username");
                    resultpass = jo.getString("Password");

                }

                reader.close();
                ips.close();
                http.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }

        dbbundle = new Bundle();
        dbbundle.putString("user", resultuser);
        dbbundle.putString("pass", resultpass);
    }
        return dbbundle;
    }



    @Override
    protected void onPostExecute(Bundle bundle) {

        if(!stop){
            MainActivity mainActivity = mainActivityWeakReference.get();
            if(mainActivity == null || mainActivity.isFinishing()){
                return;
            }

            String resultuser = bundle.getString("user");
            String resultpass = bundle.getString("pass");

            if (resultuser.equals(mainActivity.mTextUsername.getText().toString()) && resultpass.equals(mainActivity.mTextPassword.getText().toString())) {

                //session variables

                if (mainActivity.checked) {

                    mainActivity.editor.putBoolean("notifications", true);
                } else {
                    mainActivity.editor.putBoolean("notifications", false);
                }

                mainActivity.editor.putString("usernameses", resultuser);
                mainActivity.editor.putBoolean("loggedin", true);
                mainActivity.editor.apply();

                //logging in
                Intent Loginintent = new Intent(mainActivity.context, Menu.class);
                mainActivity.startActivity(Loginintent);

            }
            else{

                Toast.makeText(mainActivity.context,"Wrong username and password combination!",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
