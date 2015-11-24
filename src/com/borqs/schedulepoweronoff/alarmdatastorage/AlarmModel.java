package com.borqs.schedulepoweronoff.alarmdatastorage;

import java.util.Calendar;

import android.content.Context;

import com.borqs.schedulepoweronoff.R;
import com.borqs.schedulepoweronoff.utils.GSONUtils;

public class AlarmModel {

	private static final String CONCAT_REPEATED_SPLITOR = " ";
	private static final String NO_REPEATED_SHOWER = "";
	private AlarmEntity mEntity;

	public AlarmModel(AlarmEntity e) {
		mEntity = e;
	}

	public void setAlarmEntity(AlarmEntity e) {
		mEntity = e;
	}

	public AlarmEntity getEntity() {
		return mEntity;
	}

	public boolean isRepeated() {
		return mEntity.getWeekDays() > 0;
	}

	public boolean isEveryDay() {
		return (mEntity.getWeekDays() ^ 0x7F) == 0;
	}

	public String getRepeatedStr(Context context) {
		if (!isRepeated())
			return NO_REPEATED_SHOWER;

		if (isEveryDay()) {
			return context.getText(R.string.every_day).toString();
		}
		StringBuilder sb = new StringBuilder();
		String[] dayTStrings = context.getResources().getStringArray(
				R.array.week_day);
		for (int i = 0; i < dayTStrings.length; i++) {
			if (iSWeekDaySet(i)) {
				sb.append(dayTStrings[i]);
				sb.append(CONCAT_REPEATED_SPLITOR);
			}
		}
		return sb.substring(0, sb.length() - CONCAT_REPEATED_SPLITOR.length());
	}
	
	
	/**
	 * 
	 * @param weekDay
	 *            start from 0
	 * @return
	 */
	public boolean iSWeekDaySet(int weekDay) {

		return (mEntity.getWeekDays() & (1 << weekDay)) > 0;
	}

	public void setWeekDays(int weekDay, boolean clear) {
		int weekDays = mEntity.getWeekDays();
		if (clear) {
			weekDays &= ~(1 << weekDay);
		} else {
			weekDays |= (1 << weekDay);
		}
		mEntity.setWeekDays(weekDays);
	}

	public boolean isExpired() {
		return !isRepeated() && isBeforeNowTime();
	}

	private boolean isBeforeNowTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());

		int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
		int nowMinute = calendar.get(Calendar.MINUTE);

		int hour = mEntity.getHour();
		int minutes = mEntity.getMinute();
		return (hour < nowHour || hour == nowHour && minutes <= nowMinute);
	}
	
	public static AlarmModel convertToObj(String jsonStr){
		return new AlarmModel(GSONUtils.jsonToBean(jsonStr, AlarmEntity.class));
	}
	
	public void calcRTCTime() {
		if (!isExpired()) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			if (isBeforeNowTime()) {
				calendar.add(Calendar.DAY_OF_YEAR, 1);
			}
			calendar.set(Calendar.HOUR_OF_DAY, mEntity.getHour());
			calendar.set(Calendar.MINUTE, mEntity.getMinute());
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			//TODO
			int today = calendar.get(Calendar.DAY_OF_WEEK);
		}
	}
}
