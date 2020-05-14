package com.galagala.phone;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class Alibudas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alibudas);


        Button fileioButton = (Button) findViewById(R.id.fileioButton);

        fileioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // logic goes here
                Intent intent = new Intent();
                intent.setClass(Alibudas.this , FileIO.class);
                startActivity(intent);
            }
        });

    }
}
