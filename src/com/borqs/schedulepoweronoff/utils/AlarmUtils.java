package com.borqs.schedulepoweronoff.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmEntity;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmModel;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmPersistence;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmPersistenceImpl;

public class AlarmUtils {
    private static final String TAG = "AlarmUtils";
    private static final String EXTRA_ALARM_DATA_NAME = "alarm_data";

    public static void registerAlarmEvent(Context context, AlarmModel model) {
        AlarmEntity entity = model.getEntity();
        long rtcTime = entity.getTime();
        int type = entity.getType();

        // invalid alarm time
        long now = System.currentTimeMillis();
        if (rtcTime <= now) {
            return;
        }

        String action = null;
        int systemAlarmType = AlarmManager.RTC_WAKEUP;
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        if (type == AlarmEntity.POWEROFF_CLOCK) {
            action = BaseConstants.ACTION_POWER_OFF;
            systemAlarmType = AlarmManager.RTC_WAKEUP;
        } else if (type == AlarmEntity.POWERON_CLOCK) {
            action = BaseConstants.ACTION_POWER_ON;
            systemAlarmType = AlarmManager.RTC_WAKEUP; // TODO: use
                                                        // RTC_POWEROFF_WAKEUP
        } else {
            return;
        }
        Intent i = new Intent(action);
        String alarmJson = entity.toString();
        i.putExtra(EXTRA_ALARM_DATA_NAME, alarmJson);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i,
                PendingIntent.FLAG_CANCEL_CURRENT);
        manager.set(systemAlarmType, rtcTime, pi);
        Log.d(TAG, "Register alarm event, alarm event(" + alarmJson + ")");
    }

    /**
     * fetch data from Intent extra data, then decode to AlarmModel object
     */
    public static AlarmModel decodeAlarmData(Intent i) {
        String jsonStr = i.getStringExtra(EXTRA_ALARM_DATA_NAME);
        return AlarmModel.convertToObj(jsonStr);
    }

    public static void switchToNextAlarm(Context ctx, AlarmModel model) {
        AlarmEntity entity = model.getEntity();
        if (model.isRepeated()) {
            // update the alarm event, switch to next trigger time, register it
            // to AlarmManager
            model.calcRTCTime();
            AlarmUtils.registerAlarmEvent(ctx, model);
        } else {
            // update the alarm event data, make the event be disabled and save
            // it
            entity.setEnable(false);
            AlarmPersistence store = AlarmPersistenceImpl.getInstance(ctx);
            store.putAlarm(entity);
        }
    }
}
