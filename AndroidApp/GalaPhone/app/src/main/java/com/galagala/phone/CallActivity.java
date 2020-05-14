package com.galagala.phone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import kotlin.collections.CollectionsKt;
import timber.log.Timber;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CallActivity extends AppCompatActivity {

    @BindView(R.id.hangup)
    Button hangup;
    @BindView(R.id.callInfo)
    TextView callInfo;


    private CompositeDisposable disposables;
    private String number;
    private OngoingCall ongoingCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_activity);
        ButterKnife.bind(this);
        Log.e("CallActivity", "Zanna onCreate!");
        ongoingCall = new OngoingCall();
        disposables = new CompositeDisposable();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        number = Objects.requireNonNull(getIntent().getData()).getSchemeSpecificPart();
    }


    @OnClick(R.id.hangup)
    public void onHangupClicked() {
        Log.e("CallActivity", "Zanna onHangupClicked!");
        ongoingCall.hangup();
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.e("CallActivity", "Zanna onStart!");
        assert updateUi(-1) != null;
        disposables.add(
                OngoingCall.state
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                updateUi(integer);
                            }
                        }));

        disposables.add(
                OngoingCall.state
                        .filter(new Predicate<Integer>() {
                            @Override
                            public boolean test(Integer integer) throws Exception {
                                return integer == Call.STATE_DISCONNECTED;
                            }
                        })
                        .delay(1, TimeUnit.SECONDS)
                        .firstElement()
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                finish();
                            }
                        }));

    }


    private static String asString(int data) {
        String value;
        switch (data) {
            case 0:
                value = "NEW";
                break;
            case 1:
                value = "DIALING";
                break;
            case 2:
                value = "RINGING";
                break;
            case 3:
                value = "HOLDING";
                break;
            case 4:
                value = "ACTIVE";
                break;
            case 7:
                value = "DISCONNECTED";
                break;
            case 8:
                value = "SELECT_PHONE_ACCOUNT";
                break;
            case 9:
                value = "CONNECTING";
                break;
            case 10:
                value = "DISCONNECTING";
                break;
            default:
                Timber.w("Unknown state " + data);
                value = "UNKNOWN";
                break;
        }
        return value;
    }

    @SuppressLint("SetTextI18n")
    private Consumer<? super Integer> updateUi(Integer state) {

        callInfo.setText(asString(state) + "\n" + number);


        if (CollectionsKt.listOf(new Integer[]{
                Call.STATE_DIALING,
                Call.STATE_RINGING,
                Call.STATE_ACTIVE}).contains(state)) {
            hangup.setVisibility(View.VISIBLE);
        } else
            hangup.setVisibility(View.GONE);

        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("CallActivity", "Zanna onStop!");
        disposables.clear();
    }

    public static void start(Context context, Call call) {
        Log.e("CallActivity", "Zanna start!");
        Intent intent = new Intent(context, CallActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(call.getDetails().getHandle());
        context.startActivity(intent);
    }
}
