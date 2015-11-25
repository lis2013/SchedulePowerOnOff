package com.borqs.schedulepoweronoff.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.Toast;

import com.borqs.schedulepoweronoff.OffOnScheduleMocker;
import com.borqs.schedulepoweronoff.R;
import com.borqs.schedulepoweronoff.ShutdownActivity;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmModel;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmPersistenceImpl;

public class AlarmUtils {
    private static final String TAG = "AlarmUtils";
    public static final String EXTRA_ALARM_DATA_NAME = "alarm_data";

    private static WakeLock screenLock = null;
    private static WakeLock cpuLock = null;

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
            systemAlarmType = AlarmManager.RTC_WAKEUP; // TODO: use
        } else {
            Log.e(TAG, "Error Alarm type");
            return;
        }
        String alarmJson = model.entityString();
		register(context, systemAlarmType, alarmJson, rtcTime, action);
        // test code, need to repalce product code after integrate module.
		//OffOnScheduleMocker.mockAlarm(context, systemAlarmType,  alarmJson, rtcTime, action);
        Log.d(TAG, "Register alarm event, alarm event(" + alarmJson + ")");
    }

    private static void register(Context context, int systemAlarmType,
            String alarmJson, long rtc, String action) {
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(action);
        i.putExtra(EXTRA_ALARM_DATA_NAME, alarmJson);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i,
                PendingIntent.FLAG_CANCEL_CURRENT);
        manager.set(systemAlarmType, rtc, pi);
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
        Toast t = Toast.makeText(ctx, formartAlarmRtcTimePeriod(ctx, model),
                Toast.LENGTH_SHORT);
        t.show();
    }

    private static String formartAlarmRtcTimePeriod(Context ctx,
            AlarmModel model) {
        long period = model.getRTCTime() - System.currentTimeMillis();
        long hours = period / (1000 * 60 * 60);
        long minutes = period / (1000 * 60) % 60;
        long day = hours / 24;
        hours = hours % 24;
        if (model.isPowerOn()) {
            return ctx.getResources().getString(R.string.time_power_on, day,
                    hours, minutes);
        } else {
            return ctx.getResources().getString(R.string.time_power_off, day,
                    hours, minutes);
        }
    }

    public static void acquireWakeLock(Context context) {
        PowerManager pm = (PowerManager) context
                .getSystemService(Context.POWER_SERVICE);
        screenLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
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
