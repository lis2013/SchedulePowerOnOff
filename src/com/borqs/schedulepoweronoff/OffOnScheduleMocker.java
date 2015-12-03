package com.borqs.schedulepoweronoff;

import com.borqs.schedulepoweronoff.utils.AlarmUtils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class OffOnScheduleMocker {

    public static void mockAlarm(Context context, int systemAlarmType, String alarmJson, long rtc, String action) {
        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(action);
        i.putExtra(AlarmUtils.EXTRA_ALARM_DATA_NAME, alarmJson);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        mAlarmManager.set(systemAlarmType, rtc, pi);
    }
}
