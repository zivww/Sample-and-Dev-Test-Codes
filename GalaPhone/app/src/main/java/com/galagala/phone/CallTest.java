package com.galagala.phone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.telecom.Call;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import kotlin.collections.ArraysKt;

import static android.Manifest.permission.CALL_PHONE;
import static android.telecom.TelecomManager.ACTION_CHANGE_DEFAULT_DIALER;
import static android.telecom.TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME;

public class CallTest extends AppCompatActivity {
    private static String Tag = "Gala CallTest";

    @BindView(R.id.phoneNumber)
    EditText phoneNumber;
    @BindView(R.id.startDialButton)
    Button startDialButton;
    @BindView(R.id.dropDialButton)
    Button dropDialButton;
    @BindView(R.id.repeaTimes)
    EditText repeaTimes;

    //TelecomManager mTelecomManager = null;
    //private static Context mContext;

    private static Call call;
    private OngoingCall ongoingCall;

    public static int REQUEST_PERMISSION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_test);

        offerReplacingDefaultDialer();
        ButterKnife.bind(this);
        if (getIntent() != null && getIntent().getData() != null) {
            phoneNumber.setText(getIntent().getData().getSchemeSpecificPart());
        }

        ongoingCall = new OngoingCall();
    }

    private void launchDialer(String number){
        String numberToDial = "tel:"+number;
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(numberToDial)));
    }


    private void dropDialer( ) {
        assert call != null;
        //https://github.com/Abror96/CustomPhoneDialer/tree/master/app/src/main/java/customphonedialer/abror96/customphonedialer
        ongoingCall.hangup();
        Log.e(Tag, "dropDialer disconnect!");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("DialerActivity", "Zanna onStart!");

        // Set up buttons and attach click listeners
        //phoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            //@Override
            //public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            //    makeCall();
            //    return true;
            //}
        //});

        startDialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // logic goes here
                if(!phoneNumber.getText().toString().equals("")) {
                    String message = phoneNumber.getText().toString();
                    makeCall();
                } else {
                    // nothing
                }

            }
        });

        repeaTimes.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.e("DialerActivity", "Zanna TODO: repeaTimes!");
                return true;
            }
        });
    }

    private void makeCall() {
        Log.e("DialerActivity", "Zanna makeCall!");
        if (ContextCompat.checkSelfPermission(this, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Uri uri = Uri.parse("tel:"+phoneNumber.getText().toString().trim());
            startActivity(new Intent(Intent.ACTION_CALL, uri));
        }
    }

    private void offerReplacingDefaultDialer() {
        TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);

        if (!getPackageName().equals(telecomManager.getDefaultDialerPackage())) {
            Intent intent = new Intent(ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION && ArraysKt.contains(grantResults, PackageManager.PERMISSION_GRANTED)) {
            makeCall();
        }
    }
}
