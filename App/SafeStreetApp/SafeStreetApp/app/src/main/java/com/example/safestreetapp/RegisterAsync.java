package com.example.safestreetapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

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

public class RegisterAsync extends AsyncTask<String,String,String> {

    private WeakReference<Register> registerWeakReference;
    private boolean stop = false;

    RegisterAsync(Register registerActivity){
        registerWeakReference = new WeakReference<Register>(registerActivity);
    }

    @Override
    protected void onPreExecute() {

        Register registerActivity = registerWeakReference.get();
        if(registerActivity == null || registerActivity.isFinishing()){
            return;
        }

        if (TextUtils.isEmpty(registerActivity.mTextUsername.getText())){
            registerActivity.mTextUsername.setError("Please Enter a Username!");
            stop = true;
        }
        else if (TextUtils.isEmpty(registerActivity.mTextPhone.getText())){
            registerActivity.mTextPhone.setError("Please Enter Your Phone Number!");
            stop = true;
        }
        else if (TextUtils.isEmpty(registerActivity.mTextEmail.getText())){
            registerActivity.mTextEmail.setError("Please Enter your Email Address!");
            stop = true;
        }
        else if(!registerActivity.isValidEmail(registerActivity.mTextEmail.getText())){
            registerActivity.mTextEmail.setError("Please Enter a valid email address");
            stop = true;
        }
        else if (TextUtils.isEmpty(registerActivity.mTextPassword.getText())){
            registerActivity.mTextPassword.setError("Please Enter a Password!");
            stop = true;
        }
        else if (TextUtils.isEmpty(registerActivity.mTextConfirmPassword.getText())){
            registerActivity.mTextConfirmPassword.setError("Please Enter a Confirmation Password!");
            stop = true;
        }


    }


    @Override
    protected String doInBackground(String... strings) {

        String userreg = strings[0];
        String passreg = strings[1];
        String passcreg = strings[2];
        String emailreg = strings[3];
        String phonereg = strings[4];
        String result = "";




        if(!stop){


            String jsonline;
            String connstr = "http://10.0.2.2/SafeStreetMobile/register.php";


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


                result="";
                while ((jsonline = reader.readLine()) != null) {


                    result += jsonline;
                    Bundle registrationBundle = new Bundle();
                    registrationBundle.putString("result", result);

                }






                reader.close();
                ips.close();
                http.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }



        }
        return result;
    }


    @Override
    protected void onPostExecute(String s) {

        Register registerActivity = registerWeakReference.get();
        if(registerActivity == null || registerActivity.isFinishing()){
            return;
        }

        else if(!stop) {

            Toast.makeText(registerActivity.context, s, Toast.LENGTH_SHORT).show();
        }
    }
}
