package com.borqs.schedulepoweronoff.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.Toast;

import com.borqs.schedulepoweronoff.R;
import com.borqs.schedulepoweronoff.ShutdownActivity;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmModel;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmPersistenceImpl;

public class AlarmUtils {
    private static final String TAG = "AlarmUtils";
    public static final String EXTRA_ALARM_DATA_NAME = "alarm_data";

    private static WakeLock screenLock = null;
    private static WakeLock cpuLock = null;

    private static PendingIntent powerOnPendingIntentCache = null;
    private static PendingIntent powerOffPendingIntentCache = null;

    public static void registerAlarmEvent(Context context, AlarmModel model) {
        if (!model.isEnabled()) {
            Log.e(TAG, "alarm is not enable, register alarm event faiure");
            return;
        }
        long rtcTime = model.getRTCTime();
        // invalid alarm time
        long now = System.currentTimeMillis();
        if (rtcTime <= now) {
            Log.e(TAG, "alarm time is expired");
            return;
        }

        String action = null;
        int systemAlarmType = AlarmManager.RTC_WAKEUP;
        if (model.isPowerOff()) {
            action = BaseConstants.ACTION_POWER_OFF;
            systemAlarmType = AlarmManager.RTC_WAKEUP;
        } else if (model.isPowerOn()) {
            action = BaseConstants.ACTION_POWER_ON;
            systemAlarmType = AlarmManager.RTC_POWEROFF_WAKEUP;
        } else {
            Log.e(TAG, "Error Alarm type");
            return;
        }
        String alarmJson = model.entityString();
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(action);
        i.putExtra(EXTRA_ALARM_DATA_NAME, alarmJson);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i,
                PendingIntent.FLAG_CANCEL_CURRENT);
        if (model.isPowerOff()) {
            if (powerOffPendingIntentCache != null) {
                manager.cancel(powerOffPendingIntentCache);
            }
            powerOffPendingIntentCache = pi;
        } else if (model.isPowerOn()) {
            if (powerOnPendingIntentCache != null) {
                manager.cancel(powerOnPendingIntentCache);
            }
            powerOnPendingIntentCache = pi;
        }
        manager.set(systemAlarmType, rtcTime, pi);
        // test code, need to repalce product code after integrate module.
        //OffOnScheduleMocker.mockAlarm(context, systemAlarmType,  alarmJson, rtcTime, action);
        Log.d(TAG, "Register alarm event, alarm event(" + alarmJson + ")");
    }

    public static void unregisterAlarmEvent(Context context, AlarmModel model) {
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        if (model.isPowerOff() && powerOffPendingIntentCache != null) {
            am.cancel(powerOffPendingIntentCache);
            powerOffPendingIntentCache = null;
        } else if (model.isPowerOn() && powerOnPendingIntentCache != null) {
            am.cancel(powerOnPendingIntentCache);
            powerOnPendingIntentCache = null;
        }
        Log.d(TAG,
                "Unregister alarm event, is power on alarm event: "
                        + model.isPowerOn());
    }

    /**
     * fetch data from Intent extra data, then decode to AlarmModel object
     */
    public static AlarmModel decodeAlarmData(Intent i) {
        String jsonStr = i.getStringExtra(EXTRA_ALARM_DATA_NAME);
        return AlarmModel.convertToObj(jsonStr);
    }

    public static void switchToNextAlarm(Context ctx, AlarmModel model) {
        if (model.isRepeated()) {
            // update the alarm event, switch to next trigger time, register it
            // to AlarmManager
            model.calcRTCTime();
            AlarmUtils.registerAlarmEvent(ctx, model);
        } else {
            // update the alarm event data, make the event be disabled and save
            // it
            model.getEntity().setEnable(false);
            AlarmPersistenceImpl.getInstance(ctx).putAlarm(model);

        }
    }

    public static void toastAlarmPeriod(Context ctx, AlarmModel model) {
        Toast t = Toast.makeText(ctx, formartAlarmRtcTimePeriod(ctx, model), Toast.LENGTH_SHORT);
        t.show();
    }

    private static String formartAlarmRtcTimePeriod(Context ctx, AlarmModel model) {
        long period = model.getRTCTime() - System.currentTimeMillis();
        long hours = period / (1000 * 60 * 60);
        long minutes = period / (1000 * 60) % 60;
        long day = hours / 24;
        hours = hours % 24;
        StringBuffer sb = new StringBuffer();
        if((day | hours | minutes) == 0){
            sb.append(ctx.getString(R.string.minutes, 0));
        }
        if (day != 0) {
            sb.append(ctx.getString(R.string.days, day));
        }
        if (hours != 0) {
            sb.append(ctx.getString(R.string.hours, hours));
        }
        if (minutes != 0) {
            sb.append(ctx.getString(R.string.minutes, minutes));
        }
        String str = model.isPowerOn() ? ctx.getString(R.string.time_power_on,
                sb.toString()) : ctx.getString(R.string.time_power_off,
                sb.toString());

        return str;
    }

    @SuppressWarnings("deprecation")
    public static void acquireWakeLock(Context context) {
        PowerManager pm = (PowerManager) context
                .getSystemService(Context.POWER_SERVICE);
        screenLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE,
                ShutdownActivity.class.getSimpleName());
        screenLock.setReferenceCounted(false);
        screenLock.acquire();

        cpuLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                ShutdownActivity.class.getSimpleName());
        cpuLock.setReferenceCounted(false);
        cpuLock.acquire();
    }

    public static void releaseWakeLock() {
        if (screenLock != null) {
            screenLock.release();
            screenLock = null;
        }
        if (cpuLock != null) {
            cpuLock.release();
            cpuLock = null;
        }
    }
}
