package com.galagala.phone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DataTest  extends AppCompatActivity {
    private static String Tag = "Gala DataTest";
    Button apnCheckButton;
    Button connectivityCheckButton;
    Button throughputButton;
    Button pingButton;
    Button advancedTestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_test);
        Log.e(Tag, "onCreate!");
        apnCheckButton = (Button) findViewById(R.id.apnCheckButton);
        connectivityCheckButton = (Button) findViewById(R.id.connectivityCheckButton);
        pingButton = (Button) findViewById(R.id.pingButton);
        throughputButton  = (Button) findViewById(R.id.throughputButton);
        advancedTestButton = (Button) findViewById(R.id.advancedTestButton);

        apnCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(Tag, "Query Apn!");
                // query apn

            }
        });
        connectivityCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(Tag, "PingTest!");
                // logic goes here
                Intent intent = new Intent();
                intent.setClass(DataTest.this , ConnectivityCheckTest.class);
                startActivity(intent);
            }
        });
        pingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(Tag, "PingTest!");
                // logic goes here
                Intent intent = new Intent();
                intent.setClass(DataTest.this , PingTest.class);
                startActivity(intent);
            }
        });
    }
}
