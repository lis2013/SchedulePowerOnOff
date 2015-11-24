package com.borqs.schedulepoweronoff.alarmdatastorage;

public class AlarmModel {
	
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
			mWeekDays &= ~(1 << weekDay);
		} else {
			mWeekDays |= (1 << weekDay);
		}
	}
}
