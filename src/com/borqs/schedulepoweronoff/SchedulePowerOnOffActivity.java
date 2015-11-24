package com.borqs.schedulepoweronoff;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmEntity;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmModel;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmPersistence;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmPersistenceImpl;
import com.borqs.schedulepoweronoff.ui.PowerOnOffAdapter;

public class SchedulePowerOnOffActivity extends Activity implements OnItemClickListener {
    private static final String TAG = "SchedulePowerOnOffActivity";
    static final boolean DEBUG = true;
    private LayoutInflater mLayoutInflater;
    private ListView mList;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mLayoutInflater = LayoutInflater.from(this);
        View v = mLayoutInflater.inflate(R.layout.power_on_off_activity, null);
        AlarmModelTest t = new AlarmModelTest();
        t.run();
        setContentView(v);
        mList = (ListView) v.findViewById(android.R.id.list);
        AlarmPersistence alarmPersistence = AlarmPersistenceImpl.getInstance(this);
        List<AlarmModel> alarmModel = alarmPersistence.getAlarms();
        Log.d("lihongxia", "lihongxia--alarmModel.size==" + alarmModel.size());
        if (mList != null) {
        	mList.setAdapter(new PowerOnOffAdapter(this, alarmModel));
        	mList.setVerticalScrollBarEnabled(true);
        	mList.setOnItemClickListener(this);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView parent, View v, int pos, long id) {
        Log.d(TAG, "onItemClick, id is " + id);
        Intent intent = new Intent();
        intent.setClass(this, com.borqs.schedulepoweronoff.TimeSetActivity.class);
        final Bundle bundle = new Bundle();
        bundle.putInt(AlarmEntity.CLOCK_TYPE, (int) id);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
