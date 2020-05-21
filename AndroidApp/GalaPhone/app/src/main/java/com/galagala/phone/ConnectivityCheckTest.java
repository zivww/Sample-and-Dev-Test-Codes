package com.galagala.phone;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ConnectivityCheckTest extends AppCompatActivity {
    private static String Tag = "ConnectivityCheckTest";
    boolean isWiFiConn = false;
    boolean isCellularConn = false;
    boolean isDataEnabled = false;

    @BindView(R.id.dataTestResult)
    TextView dataTestResult;

    @BindView(R.id.enableDataButton)
    Button enableDataButton;

    @BindView(R.id.checkDataEnabledButton)
    Button checkDataEnabledButton;

    //private static Call call;
    //private OngoingCall ongoingCall;

    //public static int REQUEST_PERMISSION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connectivity_check_test);

        ButterKnife.bind(this);
        checkExistNetworks();
    }

    private void appendResultsText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dataTestResult.append(text + "\n");
            }
        });
    }

    @OnClick(R.id.enableDataButton)
    public void setDataEnabled() {
        Log.e(Tag, "enableData: origial: " + isDataEnabled);
        //    <uses-permission android:name="android.permission.MODIFY_PHONE_STATE" />
        //telephonyManager.setDataEnabled(!isDataEnabled);

        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

            Method setMobileDataEnabledMethod = telephonyManager.getClass().getDeclaredMethod("setDataEnabled", boolean.class);

            if (null != setMobileDataEnabledMethod) {
                setMobileDataEnabledMethod.invoke(telephonyManager, isDataEnabled);
            }
            Log.e(Tag, "isDataEnabled: " + isDataEnabled);
        } catch (Exception ex) {
            Log.e(Tag, "Error setting mobile data state", ex);
        }
    }

    @OnClick(R.id.checkDataEnabledButton)
    public void checkExistNetworks() {
        Log.e(Tag, "checkExistNetworks++");
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        //reset
        isWiFiConn = false;
        isCellularConn = false;

        for(Network nw:connectivityManager.getAllNetworks()){
            NetworkInfo nwInfo = connectivityManager.getNetworkInfo(nw);
            if(nwInfo.getType() == ConnectivityManager.TYPE_WIFI){
                isWiFiConn |= nwInfo.isConnected();
            }
            if(nwInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                isCellularConn |= nwInfo.isConnected();
            }
        }
        Log.e(Tag, "isWiFiConn: " + isWiFiConn + " , isCellularConn: " + isCellularConn);
        appendResultsText("isWiFiConn: " + isWiFiConn + " , isCellularConn: " + isCellularConn);
    }
}
