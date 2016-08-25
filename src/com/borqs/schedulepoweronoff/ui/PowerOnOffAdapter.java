package com.borqs.schedulepoweronoff.ui;

import java.util.List;

import com.borqs.schedulepoweronoff.utils.AlarmUtils;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.borqs.schedulepoweronoff.ChooseTypePersistence;
import com.borqs.schedulepoweronoff.R;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmModel;

public class PowerOnOffAdapter extends BaseAdapter {
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

	private void showTime(AlarmModel alarmModel, TextView timeFormat,
			TextView time) {
		if (DateFormat.is24HourFormat(mContext)) {
			timeFormat.setVisibility(View.GONE);

		} else {
			timeFormat.setVisibility(View.VISIBLE);
			if (alarmModel.isAm()) {
				timeFormat.setText(mContext
						.getString(R.string.time_format_morning));
			} else {
				timeFormat.setText(mContext
						.getString(R.string.time_format_afternoon));
			}
		}
		time.setText(alarmModel.getTime(mContext));

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final AlarmModel alarmModel = mAlarmModelList.get(position);
		ChooseTypePersistence  mPersistence = new ChooseTypePersistence(mContext, alarmModel);
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.power_on_off_time_list_item, parent, false);
			holder = new ViewHolder();
			holder.typeTitle = (TextView) convertView
					.findViewById(R.id.type_title);
			holder.timeFormat = (TextView) convertView
					.findViewById(R.id.time_format);
			holder.setTime = (TextView) convertView.findViewById(R.id.set_time);
			holder.weekDays = (TextView) convertView
					.findViewById(R.id.week_days);
			holder.sw = (Switch) convertView.findViewById(R.id.switch_button);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int resId = R.string.power_on_second_title;
		int color = Color.parseColor("#00BFFF");
		if (!alarmModel.isPowerOn()) {
			resId = R.string.power_off_second_title;
			color = Color.parseColor("#da1c37");
		}
		holder.typeTitle.setText(resId);
		holder.typeTitle.setTextColor(color);
		showTime(alarmModel, holder.timeFormat, holder.setTime);
		holder.weekDays.setText(mPersistence.getRepeatStr(mContext));
		holder.sw.setChecked(alarmModel.isEnabled());
		holder.sw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean checked = ((Switch) v).isChecked();
				alarmModel.enable(mContext, checked);
				if (checked) {
					AlarmUtils.toastAlarmPeriod(mContext, alarmModel);
				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		public TextView timeFormat;
		public TextView typeTitle;
		public TextView setTime;
		public TextView weekDays;
		public Switch sw;
	}
}
