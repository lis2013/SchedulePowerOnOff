package com.borqs.schedulepoweronoff.ui;

import java.text.DateFormatSymbols;

import com.borqs.schedulepoweronoff.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PowerOnOffAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	//private String mTimeFormat;

    public PowerOnOffAdapter(Context context) {
    	super();
        this.mInflater = LayoutInflater.from(context);
        //String[] ampm = new DateFormatSymbols().getAmPmStrings();
        //mTimeFormat = ampm[0];
    }

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.power_on_off_time_list_item,null);
            holder = new ViewHolder();
            holder.typeTitle = (TextView) convertView.findViewById(R.id.type_title);
            holder.timeFormat = (TextView) convertView.findViewById(R.id.time_format);
            holder.setTime = (TextView) convertView.findViewById(R.id.set_time);
            //holder.digitalClock = (ShowClockTimeLayout) convertView.findViewById(R.id.digitalClock);
            holder.weekDays = (TextView) convertView.findViewById(R.id.week_days);
            holder.sw = (Switch)convertView.findViewById(R.id.switch_button);
            convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		if (holder.timeFormat != null) {
			//holder.timeFormat.setText(mAm);
		}
		//if (holder.poweronofficon != null) {
			
		//}
		//if (holder.digitalClock != null) {
			
		//}
		//if (holder.daysOfWeekView != null) {
			
		//}
		if (holder.sw != null) {
			//holder.sw .setChecked(alarm.mEnabled);
			holder.sw.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
                    //Alarms.enableAlarm(cont, alarm.mId, isChecked);
                    //if (isChecked) {
                        //SetAlarm.popAlarmSetToast(cont, alarm.mHour, alarm.mMinutes, alarm.mDaysOfWeek, alarm.mId);
                    //}
				}
             });
		}
		return convertView;
	}

	class ViewHolder{
        public TextView timeFormat;
        public TextView typeTitle;
        public TextView setTime;
        public TextView weekDays;
        public ShowClockTimeLayout clockTime;
        public Switch sw;
    }
}
