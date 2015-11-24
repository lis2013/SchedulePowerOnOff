package com.borqs.schedulepoweronoff.alarmdatastorage;

public class AlarmModel {
	public final static String CLOCK_TYPE = "alarm_type";

	public final static int POWEROFF_CLOCK = 0;
	public final static int POWERON_CLOCK = 1;
	private AlarmEntity mEntity;
	private int mWeekDays;

	public AlarmModel(AlarmEntity e) {
		mEntity = e;
		mWeekDays = e.getWeekDays();
	}

	public void setAlarmEntity(AlarmEntity e) {
		mEntity = e;
		mWeekDays = mEntity.getWeekDays();
	}

	public AlarmEntity getEntity() {
		return mEntity;
	}

	public boolean isRepeated() {
		return mWeekDays > 0;
	}

	public boolean iSWeekDaySet(int weekDay) {
		return (mWeekDays & (1 << weekDay)) > 0;
	}

	public void setWeekDays(int weekDay, boolean clear) {
		if (clear) {
			weekDay &= ~(1 << weekDay);
		} else {
			weekDay |= (1 << weekDay);
		}
	}
}
