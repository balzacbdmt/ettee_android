package com.dev.ettee.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dev.ettee.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    NavController nc;
    File file;
    String fileNameUser = "user.json";
    FileOutputStream outputStream;

    Button register;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        createJsonUser();

        register = (Button) findViewById(R.id.welcome_btn_register);
        login = (Button) findViewById(R.id.welcome_btn_login);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.welcome_btn_register:
                Intent registerAct = new Intent(WelcomeActivity.this, RegisterActivity.class);
                startActivity(registerAct);
                finish();
                break;
            case R.id.welcome_btn_login:
                Intent loginAct = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(loginAct);
                finish();
                break;
        }
    }

    public void createJsonUser(){
        file = new File("data/data/"+getPackageName()+"/"+fileNameUser);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setContentJsonUser();
        }
    }

    public void setContentJsonUser(){
        InputStream is = getResources().openRawResource(R.raw.user);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String contentJson = writer.toString();
        try {
            outputStream = openFileOutput(fileNameUser, Context.MODE_PRIVATE);
            outputStream.write(contentJson.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
