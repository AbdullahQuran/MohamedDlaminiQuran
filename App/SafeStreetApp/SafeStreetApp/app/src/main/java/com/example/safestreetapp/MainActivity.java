package com.example.safestreetapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.StrictMode;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public SharedPreferences sharedpreferences;
    public EditText mTextUsername, mTextPassword;
    public AppCompatButton mButtonLogin;
    public TextView mTextViewRegister, mTextViewForgot;
    public boolean checked;
    public SharedPreferences.Editor editor;
    public Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        mTextUsername = findViewById(R.id.et_username);
        mTextPassword = findViewById(R.id.et_password);
        mButtonLogin =  findViewById(R.id.btn_login);
        mTextViewRegister = findViewById(R.id.tv_register);
        mTextViewForgot = findViewById(R.id.tv_forgot);



        //Session for user

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // creating session
        editor = sharedpreferences.edit();

        if(sharedpreferences.getBoolean("loggedin",false)){

            Intent takeToMenu = new Intent(MainActivity.this,Menu.class);
            startActivity(takeToMenu);

        }

        //ALLOW NETWORK IN MAIN THREAD
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        //GO TO REGISTER PAGE
        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterIntent = new Intent(MainActivity.this, Register.class);
                startActivity(RegisterIntent);
            }
        });

    }
    public void loginBtn(View v){


        String userlog  = mTextUsername.getText().toString();
        String passlog  = mTextPassword.getText().toString();

        //Start Asynctask for login
        LoginAsync logintask= new LoginAsync(this);
        logintask.execute(userlog,passlog);


    }

    // notification checkbox
    public void onCheckboxClicked(View view) {

        checked = ((CheckBox) view).isChecked();

    }

}


