package com.tec77.bsatahalk.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.login.Login;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.database.SharedPref;


public class SplashActivity extends AppCompatActivity {

    Button startBtn;
    SharedPref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pref = SharedPref.getInstance(this);

        if (pref.loggeedIn()) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }

        startBtn = findViewById(R.id.SplashActivity_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        });
    }
}
