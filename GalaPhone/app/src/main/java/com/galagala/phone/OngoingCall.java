package com.galagala.phone;

import android.telecom.Call;
import android.util.Log;

import androidx.annotation.Nullable;

import io.reactivex.subjects.BehaviorSubject;
//RxJava
//import io.reactivex.subjects.BehaviorSubject;

public class OngoingCall {
    private static Call call;
    private static String Tag = "Gala OngoingCall";
    public static BehaviorSubject<Integer> state = BehaviorSubject.create();
    private Object callback = new Call.Callback() {
        @Override
        public void onStateChanged(Call call, int newState) {
            super.onStateChanged(call, newState);
            //RxJava
            state.onNext(newState);
        }
    };

    public final void setCall(@Nullable Call value) {
        Log.e(Tag, "setCall!");
        if (call != null) {
            call.unregisterCallback((Call.Callback)callback);
        }

        if (value != null) {
            value.registerCallback((Call.Callback)callback);
            //RxJava
            state.onNext(value.getState());
        }

        call = value;
    }

    public void hangup() {
        assert call != null:"Gala: call is NULL";
        call.disconnect();
    }
}
