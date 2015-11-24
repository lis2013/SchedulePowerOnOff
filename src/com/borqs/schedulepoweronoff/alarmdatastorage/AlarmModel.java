package com.borqs.schedulepoweronoff.alarmdatastorage;

import android.content.Context;

import com.borqs.schedulepoweronoff.R;

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
}
