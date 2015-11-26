package com.borqs.schedulepoweronoff;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.borqs.schedulepoweronoff.TimeChangeNotifier.TimeChangedListener;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmModel;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmPersistence;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmPersistenceImpl;
import com.borqs.schedulepoweronoff.ui.PowerOnOffAdapter;
import com.borqs.schedulepoweronoff.utils.AlarmUtils;

public class SchedulePowerOnOffActivity extends Activity implements
        OnItemClickListener {
    private static final String TAG = "SchedulePowerOnOffActivity";
    private ListView mList;
    private List<AlarmModel> mAlarmModel;
    private TimeChangeNotifier mNotifier;
    private TimeChangedListener mTimeChangedListener;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.power_on_off_activity_layout);
        mList = (ListView) findViewById(android.R.id.list);
        mNotifier = new TimeChangeNotifier();
        mNotifier.registerTimeChangedListener(this,
                mTimeChangedListener = new TimeChangedListener() {

                    @Override
                    public void onTimeChanged() {
                        ((PowerOnOffAdapter) mList.getAdapter())
                        .notifyDataSetChanged();
                    }

                    @Override
                    public void onTimeZoneChanged() {
                    }

                    @Override
                    public void onTimeFormatChanged() {
                        ((PowerOnOffAdapter) mList.getAdapter())
                                .notifyDataSetChanged();
                    }

                });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
        AlarmPersistence alarmPersistence = AlarmPersistenceImpl
                .getInstance(this);
        mAlarmModel = alarmPersistence.getAlarms();
        if (mList != null) {
            mList.setAdapter(new PowerOnOffAdapter(this, mAlarmModel));
            mList.setVerticalScrollBarEnabled(true);
            mList.setOnItemClickListener(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNotifier.unregisterTimeChangedListener(this, mTimeChangedListener);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
        Log.d(TAG, "onItemClick, id is " + id);
        AlarmModel alarmModel = mAlarmModel.get(pos);
        Intent intent = new Intent();
        intent.setClass(this,
                com.borqs.schedulepoweronoff.TimeSetActivity.class);
        intent.putExtra(AlarmUtils.EXTRA_ALARM_DATA_NAME,
                alarmModel.entityString());
        startActivity(intent);
    }
}
