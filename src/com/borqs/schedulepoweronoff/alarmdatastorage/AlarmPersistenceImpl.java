package com.borqs.schedulepoweronoff.alarmdatastorage;

import java.util.ArrayList;
import java.util.List;

import com.borqs.schedulepoweronoff.utils.GSONUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class AlarmPersistenceImpl implements AlarmPersistence {
    private final static String TAG = AlarmPersistenceImpl.class
            .getSimpleName();
    private Editor mDataEditor;
    private SharedPreferences mPreferences;
    private static AlarmPersistenceImpl mInstance;

    public static synchronized AlarmPersistence getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AlarmPersistenceImpl(context);
        }
        return mInstance;
    }

    private AlarmPersistenceImpl(Context context) {
        mPreferences = context.getSharedPreferences("power_clock",
                Context.MODE_PRIVATE);
        mDataEditor = mPreferences.edit();
        // init value
        if (getAlarms().isEmpty()) {
            AlarmEntity onAlarm = new AlarmEntity();
            onAlarm.setEnable(false);
            onAlarm.setHour(8);
            onAlarm.setMinute(30);
            onAlarm.setWeekDays(127);// 1111111
            onAlarm.setType(AlarmEntity.POWERON_CLOCK);
            AlarmModel am = new AlarmModel(onAlarm);
            am.calcRTCTime();
            putAlarm(am);

            AlarmEntity offAlarm = new AlarmEntity();
            offAlarm.setEnable(false);
            offAlarm.setHour(23);
            offAlarm.setMinute(30);
            offAlarm.setWeekDays(31);// 11111
            offAlarm.setType(AlarmEntity.POWEROFF_CLOCK);
            am = new AlarmModel(offAlarm);
            am.calcRTCTime();
            putAlarm(am);
        }

    }

    @Override
    public synchronized boolean putAlarm(AlarmModel entity) {
        return putAlarm(entity.getEntity());
    }

    @Override
    public boolean putAlarm(AlarmEntity entity) {
        String value = entity.toString();
        Log.i(TAG, "putAlarm:" + value);
        return mDataEditor.putString(generateKey(entity.getType()), value)
                .commit();
    }

    @Override
    public synchronized AlarmModel getAlarm(int type) {
        String str = mPreferences.getString(generateKey(type), null);
        if (str == null)
            return null;
        Log.i(TAG, "getAlarm:" + str);
        AlarmEntity entity = GSONUtils.jsonToBean(str, AlarmEntity.class);
        return entity == null ? null : new AlarmModel(entity);
    }

    private String generateKey(int type) {
        return type + "";
    }

    @Override
    public synchronized List<AlarmModel> getAlarms() {
        // only two type
        List<AlarmModel> ret = new ArrayList<AlarmModel>();
        AlarmModel ae = getAlarm(AlarmEntity.POWERON_CLOCK);
        if (ae != null)
            ret.add(ae);

        ae = getAlarm(AlarmEntity.POWEROFF_CLOCK);
        if (ae != null)
            ret.add(ae);

        return ret;
    }

}
