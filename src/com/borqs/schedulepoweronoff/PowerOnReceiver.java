package com.borqs.schedulepoweronoff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmModel;
import com.borqs.schedulepoweronoff.utils.AlarmUtils;
public class PowerOnReceiver extends BroadcastReceiver {
    private static final String TAG = "PowerOnReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "intent action " + String.valueOf(intent.getAction()));
        AlarmModel model = AlarmUtils.decodeAlarmData(intent);
        AlarmUtils.switchToNextAlarm(context, model);
    }
}
