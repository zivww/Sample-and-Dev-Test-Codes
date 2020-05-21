package com.galagala.phone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileIO extends AppCompatActivity {
    private TextView resultText;
    private Button readButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fileio);

        resultText = (TextView) findViewById(R.id.resultText);
        readButton = (Button) findViewById(R.id.readButton);

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // logic goes here
                if(!resultText.getText().toString().equals("")) {
                    //String message = resultText.getText().toString();
                    try {
                        if(readFromFile() != null) {
                            resultText.setText(readFromFile());
                        }
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // nothing
                }
            }
        });

    }

    private String readFromFile() throws IOException {
        Log.e("Zanna", "++ readFromFile ++");
        String testPath = Environment.getExternalStorageDirectory() + File.separator + "test" + File.separator + "todolist.txt";
        String result = "";
        try ( FileInputStream inputStream = new FileInputStream (testPath)){//openFileInput("todolist.txt")) {
            if (inputStream != null) {
                Log.e("Zanna", "readFromFile inputStream!= null");
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String tempString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((tempString = bufferedReader.readLine()) != null) {
                    Log.e("Zanna", "readFromFile bufferedReader" + tempString);
                    stringBuilder.append(tempString);
                }

                inputStream.close();
                result = stringBuilder.toString();
                Log.e("Zanna", "readFromFile result: " + result);
            }
        }

        return result;
    }
}
