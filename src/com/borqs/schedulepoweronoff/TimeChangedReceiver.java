package com.borqs.schedulepoweronoff;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmModel;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmPersistenceImpl;

public class TimeChangedReceiver extends BroadcastReceiver {
    private static final String TAG = TimeChangedReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "time change broadcast received:" + intent.getAction());
        List<AlarmModel> models = AlarmPersistenceImpl.getInstance(context).getAlarms();
        for (AlarmModel m : models) {
            m.enable(context, m.getEntity().isEnable());
        }
    }

}
