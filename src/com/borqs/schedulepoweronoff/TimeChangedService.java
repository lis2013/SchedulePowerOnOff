package com.borqs.schedulepoweronoff;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class TimeChangedService extends Service {
    private static final String TAG = TimeChangedService.class.getSimpleName();
    private TimeChangedReceiver mTimeChangedReceiver;
    private IntentFilter mFilter;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFilter = new IntentFilter();
        mFilter.addAction(Intent.ACTION_TIME_TICK);
        mFilter.addAction(Intent.ACTION_TIME_CHANGED);
        mFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        // only by exlicitly
        // registering for it with Context.registerReceiver().
        mTimeChangedReceiver = new TimeChangedReceiver();
        registerReceiver(mTimeChangedReceiver, mFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "TimeChangedService start");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mTimeChangedReceiver);
    }

}
