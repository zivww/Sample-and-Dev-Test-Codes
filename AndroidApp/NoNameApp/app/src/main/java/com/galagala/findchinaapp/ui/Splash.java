package com.galagala.findchinaapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.galagala.findchinaapp.R;
import com.galagala.findchinaapp.dbhandler.DbUtils;

public class Splash extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_splash);
        getSupportActionBar().hide();
        DbUtils.attachDB(this);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Splash splash = Splash.this;
                //splash.startActivity(new Intent(splash, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                splash.startActivity(new Intent(splash, MainActivity.class));
                Splash.this.finish();
            }
        }, 2500);
    }
}
