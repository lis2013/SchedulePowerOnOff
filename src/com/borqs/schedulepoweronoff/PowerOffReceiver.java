package com.borqs.schedulepoweronoff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmEntity;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmModel;
import com.borqs.schedulepoweronoff.utils.AlarmUtils;
import com.borqs.schedulepoweronoff.utils.BaseConstants;

public class PowerOffReceiver extends BroadcastReceiver {
    private static final String TAG = "PowerOffReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "intent action " + String.valueOf(intent.getAction()));
        // close other dialog if existed
        Intent closeDialogIntent = new Intent(
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeDialogIntent);

        AlarmModel model = AlarmUtils.decodeAlarmData(intent);
        AlarmEntity entity = model.getEntity();
        AlarmUtils.switchToNextAlarm(context, model);

        // stale time check
        long now = System.currentTimeMillis();
        if (entity.getTime() < now - BaseConstants.STALE_OFFSET_TIME_SECONDS
                * BaseConstants.THOUSAND_MILLISECONDS) {
            Log.d(TAG,
                    "Stale alarm event(" + entity.toString()
                            + ") triggered, offset milliseconde: "
                            + (now - entity.getTime()) + ", ignored it. ");
            return;
        }

        // launch shutdown dialog
        Intent shutdownIntent = new Intent(context, ShutdownActivity.class);
        shutdownIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);
        context.startActivity(shutdownIntent);
    }
}
