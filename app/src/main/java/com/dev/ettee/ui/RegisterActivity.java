package com.dev.ettee.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.dev.ettee.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void onBackPressed()
    {
        Intent welcomeAct = new Intent(RegisterActivity.this, WelcomeActivity.class);
        startActivity(welcomeAct);
        finish();
    }
}
