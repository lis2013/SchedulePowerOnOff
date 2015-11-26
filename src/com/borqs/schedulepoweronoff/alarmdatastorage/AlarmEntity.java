package com.borqs.schedulepoweronoff.alarmdatastorage;


import com.borqs.schedulepoweronoff.utils.GSONUtils;

public class AlarmEntity {
    public final static String CLOCK_TYPE = "alarm_type";
	public final static int POWEROFF_CLOCK = 0;
	public final static int POWERON_CLOCK = 1;
	private int hour;
	private int minute;
	private int type; // 0: off clock 1: on clock
	private boolean enable;
	private int weekDays; // the low seven bits， every bit signify the day of
							// one week, from right to left（monday--> sunday）, 0
							// signify not repeat
	private long time;

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		if (type != POWEROFF_CLOCK && type != POWERON_CLOCK)
			throw new IllegalArgumentException("type error");
		this.type = type;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@Override
	public String toString() {
		return GSONUtils.objectToJson(this);
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getWeekDays() {
		return weekDays;
	}

	public void setWeekDays(int weekDays) {
		this.weekDays = weekDays;
	}
}
