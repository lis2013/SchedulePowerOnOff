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
        if (model.isRepeated()) {
            // update the alarm event, switch to next trigger time, register it to AlarmManager
        } else {
            // update the alarm event data, make the event be disabled
        }
    }
}
