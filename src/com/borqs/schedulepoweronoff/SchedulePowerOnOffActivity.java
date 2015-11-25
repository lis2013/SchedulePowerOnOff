package com.borqs.schedulepoweronoff;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmModel;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmPersistence;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmPersistenceImpl;
import com.borqs.schedulepoweronoff.ui.PowerOnOffAdapter;

public class SchedulePowerOnOffActivity extends Activity implements OnItemClickListener {
    private static final String TAG = "SchedulePowerOnOffActivity";
    static final boolean DEBUG = true;
    private ListView mList;
    private List<AlarmModel> mAlarmModel;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.power_on_off_activity_layout);
        mList = (ListView) findViewById(android.R.id.list);
        AlarmModelTest t = new AlarmModelTest();
        t.run();
        AlarmPersistence alarmPersistence = AlarmPersistenceImpl.getInstance(this);
        mAlarmModel = alarmPersistence.getAlarms();
        if (mList != null) {
            mList.setAdapter(new PowerOnOffAdapter(this, mAlarmModel));
            mList.setVerticalScrollBarEnabled(true);
            mList.setOnItemClickListener(this);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView parent, View v, int pos, long id) {
        Log.d(TAG, "onItemClick, id is " + id);
        AlarmModel alarmModel = mAlarmModel.get(pos);
        Intent intent = new Intent();
        intent.setClass(this, com.borqs.schedulepoweronoff.TimeSetActivity.class);
        intent.putExtra(AlarmModel.ENTITY, alarmModel.entityString());
        startActivity(intent);
    }
}
