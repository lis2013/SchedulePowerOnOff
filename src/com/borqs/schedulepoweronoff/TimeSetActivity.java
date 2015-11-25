package com.borqs.schedulepoweronoff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmEntity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TimePicker;

public class TimeSetActivity extends Activity implements TimePickerDialog.OnTimeSetListener, OnClickListener{
    private ListView mList;
    private int mTimeType;
    private String mSetTime;
    private String mRepeat;
    private ImageButton mResetButton;
    private ImageButton mFinishButton;
    private View mPopLayout;
    private PopupWindow mPopWindow;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_set_layout);
        mTimeType = getIntent().getIntExtra(AlarmEntity.CLOCK_TYPE, 0);
        mSetTime = getIntent().getStringExtra(AlarmEntity.SET_TIME);
        mRepeat = getIntent().getStringExtra(AlarmEntity.REPEAT_INFO);
        mResetButton = (ImageButton)findViewById(R.id.reset);
        mFinishButton = (ImageButton)findViewById(R.id.finish);
        mResetButton.setOnClickListener(this);
        mFinishButton.setOnClickListener(this);
        if (mTimeType == AlarmEntity.POWERON_CLOCK) {
            setTitle(R.string.set_power_on);
        } else {
            setTitle(R.string.set_power_off);
        }
        mList = (ListView) findViewById(android.R.id.list);
        SimpleAdapter simpleAdapter = getListAdapter();
        mList.setAdapter(simpleAdapter);
    }

    private SimpleAdapter getListAdapter() {
        String[] title = { this.getResources().getString(R.string.time_text), this.getResources().getString(R.string.repeat) };  
        String[] info = {mSetTime, mRepeat};  
        int[] imageids = { R.drawable.show_icon_time, R.drawable.show_icon_repeat }; 
        List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();  
        for (int i = 0; i < title.length; i++) {  
            Map<String, Object> listem = new HashMap<String, Object>();  
            listem.put("title", title[i]);
            listem.put("info", info[i]); 
            listem.put("imageid", imageids[i]);
            listems.add(listem);  
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listems,  
                R.layout.time_set_list_item, new String[] { "title", "info", "imageid" },  
                new int[] {R.id.title_text,R.id.info_text,R.id.show_button}) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final int pos = position;
                        final View view=super.getView(position, convertView, parent);
                        ImageButton show_button = (ImageButton)view.findViewById(R.id.show_button);
                        show_button.setOnClickListener(new OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                switch (pos){
                                    case 0:
                                        new TimePickerDialog(TimeSetActivity.this, TimeSetActivity.this, 20, 00, DateFormat.is24HourFormat(TimeSetActivity.this)).show();
                                        break;
                                    case 1:
                                        showRepeatPopupWindow();
                                        break;
                                }
                            }
                            
                        });
                        return view;
                    }
            
            
        };
        return simpleAdapter;
    }

    private void showRepeatPopupWindow() {
       if (mPopLayout == null ) {
           mPopLayout = getLayoutInflater().inflate(R.layout.set_repeat, null);
       }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
        case R.id.reset:
            finish();
            break;
        case R.id.finish:
            // 1.set next power on/off time
            //2. popup toast
            break;          
        }
        
    }

}
