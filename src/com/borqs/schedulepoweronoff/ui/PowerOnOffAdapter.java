package com.borqs.schedulepoweronoff.ui;

import java.text.DateFormatSymbols;

import com.borqs.schedulepoweronoff.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PowerOnOffAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private String mAm;
	private String mPm;

    public PowerOnOffAdapter(Context context) {
    	super();
        this.mInflater = LayoutInflater.from(context);
        String[] ampm = new DateFormatSymbols().getAmPmStrings();
        mAm = ampm[0];
        mPm = ampm[1];
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
            //holder.poweronofficon = (ImageView) convertView.findViewById(R.id.power_on_off);
            //holder.am = (TextView) convertView.findViewById(R.id.am);
            //holder.pm = (TextView) convertView.findViewById(R.id.pm);
            //holder.digitalClock = (ShowClockTimeLayout) convertView.findViewById(R.id.digitalClock);
            //holder.daysOfWeekView = (TextView) holder.digitalClock.findViewById(R.id.daysOfWeek);
            holder.sw = (Switch)convertView.findViewById(R.id.alarmButton); 
            convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		if (holder.am != null) {
			holder.am.setText(mAm);
		}
		if (holder.pm != null) {
			holder.pm.setText(mPm);
		}
		//if (holder.poweronofficon != null) {
			
		//}
		//if (holder.digitalClock != null) {
			
		//}
		//if (holder.daysOfWeekView != null) {
			
		//}
		if (holder.sw != null) {
			//holder.sw .setChecked(alarm.mEnabled);
			holder.sw .setOnCheckedChangeListener(new OnCheckedChangeListener(){
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
		public ImageView poweronofficon;
        public TextView am;
        public TextView pm;
        public TextView daysOfWeekView;
        public ShowClockTimeLayout digitalClock;
        public Switch sw;
    }
}
