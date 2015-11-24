package com.borqs.schedulepoweronoff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmInitReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmInitReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "AlarmInitReceiver" + action);
    }
}
