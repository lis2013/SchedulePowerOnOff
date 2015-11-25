package com.borqs.schedulepoweronoff.ui;

import java.text.DateFormatSymbols;
import java.util.List;

import com.borqs.schedulepoweronoff.R;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class PowerOnOffAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private List<AlarmModel> mAlarmModelList;
	private Context mContext;

    public PowerOnOffAdapter(Context context, List<AlarmModel> alarmModel) {
    	super();
        this.mInflater = LayoutInflater.from(context);
        mAlarmModelList = alarmModel;
        mContext = context;
    }

	@Override
	public int getCount() {
		return mAlarmModelList.size();
	}

	@Override
	public Object getItem(int position) {
		return mAlarmModelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final AlarmModel alarmModel = mAlarmModelList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.power_on_off_time_list_item,parent, false);
            holder = new ViewHolder();
            holder.typeTitle = (TextView) convertView.findViewById(R.id.type_title);
            holder.timeFormat = (TextView) convertView.findViewById(R.id.time_format);
            holder.setTime = (TextView) convertView.findViewById(R.id.set_time);
            holder.weekDays = (TextView) convertView.findViewById(R.id.week_days);
            holder.sw = (Switch)convertView.findViewById(R.id.switch_button);
            convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		if (holder.typeTitle != null) {
		    int resId = R.string.power_on_second_title;
		    int color = Color.parseColor("#4cd964");
		    if (!alarmModel.isPowerOn()) {
	            resId = R.string.power_off_second_title;
	            color = Color.parseColor("#da1c37");
		    } 
            holder.typeTitle.setText(resId);
            holder.typeTitle.setTextColor(color);
		}
		if (holder.timeFormat != null) {
		    
		}
		if (holder.setTime != null) {
		    holder.setTime.setText(alarmModel.getTime());
		}
		if (holder.weekDays != null) {
		    holder.weekDays.setText(alarmModel.getRepeatedStr(mContext));
		}
		if (holder.sw != null) {
			holder.sw .setChecked(alarmModel.isEnabled());
			holder.sw.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					alarmModel.enable(mContext, isChecked);
                    if (isChecked) {
                    	Toast t = Toast.makeText(mContext, formartAlarmRtcTimePeriod(alarmModel), Toast.LENGTH_SHORT);
                    	t.show();
                    }
				}
             });
		}
		return convertView;
	}
	
	private String formartAlarmRtcTimePeriod(AlarmModel model){
		long period = model.getRTCTime() - System.currentTimeMillis();
		long hours  = period/(1000 * 60 * 60);
		long minutes = period/(1000 * 60) % 60;
		long day = hours / 24;
		hours = hours % 24;
		if(model.isPowerOn()){
			return mContext.getResources().getString(R.string.time_power_on, day, hours, minutes);
		}else{
			return mContext.getResources().getString(R.string.time_power_off, day, hours, minutes);
		}
	}
	
	class ViewHolder{
        public TextView timeFormat;
        public TextView typeTitle;
        public TextView setTime;
        public TextView weekDays;
        public Switch sw;
    }
}
