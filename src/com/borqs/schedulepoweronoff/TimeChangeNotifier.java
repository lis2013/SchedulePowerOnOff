package com.borqs.schedulepoweronoff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;

public class TimeChangeNotifier {
    private TimeChangedListener mTimeChangedListener;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (mTimeChangedListener != null) {
                if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                    mTimeChangedListener.onTimeZoneChanged();
                } else {
                    mTimeChangedListener.onTimeChanged();
                }

            }

        }
    };

    private ContentObserver mObserver = new ContentObserver(new Handler()) {

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (mTimeChangedListener != null) {
                mTimeChangedListener.onTimeFormatChanged();
            }

        }
    };

    public TimeChangeNotifier() {

    }

    public void registerTimeChangedListener(Context context,
            final TimeChangedListener listener) {
        if (mTimeChangedListener != null) {
            context.getContentResolver().unregisterContentObserver(mObserver);
            context.unregisterReceiver(mReceiver);
        }
        
        mTimeChangedListener = listener;
        context.getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI, true, mObserver);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        context.registerReceiver(mReceiver, filter, null, new Handler());

    }

    public void unregisterTimeChangedListener(Context context,
            TimeChangedListener listener) {
        if (mTimeChangedListener == listener) {
            context.getContentResolver().unregisterContentObserver(mObserver);
            context.unregisterReceiver(mReceiver);
        }
    }

    interface TimeChangedListener {
        void onTimeChanged();

        void onTimeZoneChanged();

        void onTimeFormatChanged();
    }

}
