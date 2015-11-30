package com.borqs.schedulepoweronoff;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmEntity;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmModel;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmPersistence;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmPersistenceImpl;
import com.borqs.schedulepoweronoff.utils.AlarmUtils;

public class AlarmInitReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmInitReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "AlarmInitReceiver" + action);
        context.startService(new Intent(context, TimeChangedService.class));
        AlarmPersistence persitence = AlarmPersistenceImpl.getInstance(context);
        List<AlarmModel> models = persitence.getAlarms();
        for (AlarmModel model : models) {
            AlarmEntity entity = model.getEntity();
            if (!entity.isEnable()) {
                continue;
            }
            if (model.isExpired()) {
                entity.setEnable(false);
                persitence.putAlarm(model);
                continue;
            }
            model.calcRTCTime();
            AlarmUtils.registerAlarmEvent(context, model);
        }
    }
}
