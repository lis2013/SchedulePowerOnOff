package com.borqs.schedulepoweronoff.alarmdatastorage;

import com.borqs.schedulepoweronoff.utils.BaseConstants;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmUtils {
    private static final String EXTRA_ALARM_DATA_NAME = "alarm_data";

    public static void registerAlarmEvent(Context context, AlarmModel model) {
        AlarmEntity entity = model.getEntity();
        long rtcTime = entity.getTime();
        int type = entity.getType();

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
    }

    // fetch data from Intent extra data, then decode to AlarmModel object
    public static AlarmModel decodeAlarmData(Intent i) {
        String jsonStr = i.getStringExtra(EXTRA_ALARM_DATA_NAME);
        return AlarmModel.convertToObj(jsonStr);
    }
}
