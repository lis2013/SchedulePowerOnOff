package com.borqs.schedulepoweronoff.alarmdatastorage;

import java.util.List;

public interface AlarmPersistence {


	boolean putAlarm(AlarmEntity entry);

	AlarmEntity getAlarm(int type);

	List<AlarmEntity> getAlarms();
	
}
