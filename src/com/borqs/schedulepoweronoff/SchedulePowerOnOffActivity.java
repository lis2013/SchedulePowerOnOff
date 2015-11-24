package com.borqs.schedulepoweronoff;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmEntity;
import com.borqs.schedulepoweronoff.ui.PowerOnOffAdapter;

public class SchedulePowerOnOffActivity extends PreferenceActivity implements OnItemClickListener {
    private static final String TAG = "SchedulePowerOnOffActivity";
    static final boolean DEBUG = true;
    private LayoutInflater mLayoutInflater;
    private ListView mList;
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mLayoutInflater = LayoutInflater.from(this);
        View v = mLayoutInflater.inflate(R.layout.power_on_off_activity, null);
        setContentView(v);
        mList = (ListView) v.findViewById(android.R.id.list);
        if (mList != null) {
        	mList.setAdapter(new PowerOnOffAdapter(this));
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
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
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
