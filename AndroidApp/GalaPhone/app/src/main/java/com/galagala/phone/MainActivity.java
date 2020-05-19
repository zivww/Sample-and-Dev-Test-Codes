package com.galagala.phone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button callButton;
    Button dataButton;
    Button imsButton;
    Button gpsButton;
    Button nvButton;
    Button alibudaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callButton = (Button) findViewById(R.id.callButton);
        dataButton = (Button) findViewById(R.id.dataButton);
        imsButton = (Button) findViewById(R.id.imsButton);
        gpsButton  = (Button) findViewById(R.id.gpsButton);
        nvButton = (Button) findViewById(R.id.nvButton);
        alibudaButton = (Button) findViewById(R.id.alibudaButton);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // logic goes here
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , CallTest.class);
                startActivity(intent);
            }
        });
        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // logic goes here
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , DataTest.class);
                startActivity(intent);
            }
        });
        alibudaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // logic goes here
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , Alibudas.class);
                startActivity(intent);
            }
        });
    }
}