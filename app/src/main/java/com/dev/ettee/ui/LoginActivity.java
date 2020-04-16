package com.dev.ettee.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dev.ettee.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.login_btn_login);
        login.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onBackPressed()
    {
        Intent welcomeAct = new Intent(LoginActivity.this, WelcomeActivity.class);
        startActivity(welcomeAct);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.login_btn_login:
                //TODO check field and request
                Intent homeAct = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(homeAct);
                finish();
                break;
        }
    }
}
