package com.borqs.schedulepoweronoff.alarmdatastorage;

import java.util.Calendar;

import android.content.Context;
import android.text.TextUtils;

import com.borqs.schedulepoweronoff.R;
import com.borqs.schedulepoweronoff.utils.GSONUtils;

public class AlarmModel {

	private static final String CONCAT_REPEATED_SPLITOR = " ";
	private static final String NO_REPEATED_SHOWER = "";
	private static final int WEEK_DAY_COUNT = 7;

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

	public boolean isPowerOn() {
		return mEntity.getType() == AlarmEntity.POWERON_CLOCK;
	}

	public String getTime() {
		return mEntity.getHour() + ":" + mEntity.getMinute() + "";
	}

	public boolean isEnabled() {
		return mEntity.isEnable();
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
			if (isWeekDaySet(i)) {
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
	private boolean isWeekDaySet(int weekDay) {

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
		return !isRepeated() && isBeforeNow();
	}

	private boolean isBeforeNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
		int nowMinute = calendar.get(Calendar.MINUTE);

		int hour = mEntity.getHour();
		int minutes = mEntity.getMinute();
		return (hour < nowHour || hour == nowHour && minutes <= nowMinute);
	}

	public static AlarmModel convertToObj(String jsonStr) {
		if (TextUtils.isEmpty(jsonStr)) {
			return null;
		}
		return new AlarmModel(GSONUtils.jsonToBean(jsonStr, AlarmEntity.class));
	}

	public void calcRTCTime() {
		if (!isExpired()) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.set(Calendar.HOUR_OF_DAY, mEntity.getHour());
			calendar.set(Calendar.MINUTE, mEntity.getMinute());
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);

			if (!isRepeated()) {
				// not repeat, only to next day
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				mEntity.setTime(calendar.getTimeInMillis());
				return;
			}

			// if sunday is the first day
			Calendar now = Calendar.getInstance();
			boolean isFirtDayOfWeekSunday = (now.getFirstDayOfWeek() == Calendar.SUNDAY);
			int todayOfWeekDays = now.get(Calendar.DAY_OF_WEEK);
			if (isFirtDayOfWeekSunday) {
				// ensure monday is the 0 day,...,the sunday is the 6 day
				todayOfWeekDays = todayOfWeekDays - 2;
				if (todayOfWeekDays == -1) {
					todayOfWeekDays = 6;
				}
			}
			
			if(isWeekDaySet(todayOfWeekDays) && isBeforeNow()){
				calendar.add(Calendar.DAY_OF_YEAR, 1);
			}
			
			int i = 0;
			// which day is set
			for (; i < WEEK_DAY_COUNT; i++) {
				if (isWeekDaySet((todayOfWeekDays + i) % WEEK_DAY_COUNT)) {
					break;
				}
			}
			if (i != 0) {
				calendar.add(Calendar.DAY_OF_WEEK, i);
			}
			mEntity.setTime(calendar.getTimeInMillis());
		}
	}
}
