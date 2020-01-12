package com.example.safestreetapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class Register extends AppCompatActivity {

    EditText mTextUsername,mTextPassword,mTextConfirmPassword,mTextPhone,mTextEmail;
    Button mButtonRegister;
    TextView mTextViewLogin;
    ProgressDialog progressDialog;
    public Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = Register.this;


        mTextUsername = (EditText) findViewById(R.id.et_username_reg);
        mTextPassword = (EditText) findViewById(R.id.et_password_reg);
        mTextPhone = (EditText) findViewById(R.id.et_phone);
        mTextConfirmPassword = (EditText) findViewById(R.id.et_password_confirm_reg);
        mTextEmail = (EditText) findViewById(R.id.et_email_reg);
        mButtonRegister = (Button) findViewById(R.id.btn_register);
        mTextViewLogin = (TextView) findViewById(R.id.tv_bck_login);
        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginIntent = new Intent(Register.this, MainActivity.class);
                startActivity(LoginIntent);
            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userreg = mTextUsername.getText().toString();
                String phonereg = mTextPhone.getText().toString();
                String emailreg = mTextEmail.getText().toString();
                String passreg = mTextPassword.getText().toString();
                String passcreg = mTextConfirmPassword.getText().toString();

                Registration(userreg,passreg,passcreg,emailreg,phonereg);
            }
        });

    }

    public void Registration(String userreg,String passreg,String passcreg,String emailreg,String phonereg){


        //Start Asynctask for register
        RegisterAsync registertask= new RegisterAsync(this);
        registertask.execute(userreg,passreg,passcreg,emailreg,phonereg);

    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }



}
