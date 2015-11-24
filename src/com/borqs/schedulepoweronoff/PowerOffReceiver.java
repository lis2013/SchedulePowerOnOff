package com.borqs.schedulepoweronoff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;

public class PowerOffReceiver extends BroadcastReceiver {
    private static final String TAG = "PowerOffReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "intent action " + String.valueOf(intent.getAction()));
    }
}
