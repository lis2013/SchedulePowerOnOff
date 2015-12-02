package com.borqs.schedulepoweronoff.alarmdatastorage;

import java.util.List;

public interface AlarmPersistence {


	boolean putAlarm(AlarmModel entry);

	boolean putAlarm(AlarmEntity entry);

	AlarmModel getAlarm(int type);

	List<AlarmModel> getAlarms();
}
