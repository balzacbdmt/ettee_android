package com.dev.ettee.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.dev.ettee.Category;
import com.dev.ettee.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class LoadingActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent next = null;
                if (checkConnectedUser()) {
                    next = new Intent(LoadingActivity.this, HomeActivity.class);
                } else {
                    next = new Intent(LoadingActivity.this, WelcomeActivity.class);
                }
                startActivity(next);
                finish();
            }
        },SPLASH_TIME_OUT);
    }

    private boolean checkConnectedUser() {
        String userMail = null;
        String userPassword = null;
        try {
            File file = new File("data/data/"+getPackageName()+"/user.json");
            if (file.exists()) {
                JSONObject jsonUser = new JSONObject(readJsonUser());
                userMail = jsonUser.getString("email");
                userPassword = jsonUser.getString("password");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        if (userMail != null && userPassword != null) {
            return true;
        }
        return false;
    }

    public String readJsonUser(){
        String content = null;
        try {
            InputStream is = this.openFileInput("user.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            content = new String(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return content;
    }

}
