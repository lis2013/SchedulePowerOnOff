package com.borqs.schedulepoweronoff;

import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmEntity;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmModel;

public class AlarmModelTest {

	public void run() {
		AlarmModel am = null;
		AlarmEntity ae = null;
		long ret = 0;
		ae = new AlarmEntity();
		ae.setHour(3);
		ae.setMinute(30);
		ae.setWeekDays(127);
		am = new AlarmModel(ae);
		am.calcRTCTime();
		ret = am.getEntity().getTime();
		
		ae = new AlarmEntity();
		ae.setHour(3);
		ae.setMinute(30);
		ae.setWeekDays(64);
		am = new AlarmModel(ae);
		am.calcRTCTime();
		ret = am.getEntity().getTime();
		
		ae = new AlarmEntity();
		ae.setHour(3);
		ae.setMinute(30);
		ae.setWeekDays(70);
		am = new AlarmModel(ae);
		am.calcRTCTime();
		ret = am.getEntity().getTime();

		ae = new AlarmEntity();
		ae.setHour(23);
		ae.setMinute(30);
		ae.setWeekDays(0);
		am = new AlarmModel(ae);
		am.calcRTCTime();
		ret = am.getEntity().getTime();

		ae = new AlarmEntity();
		ae.setHour(23);
		ae.setMinute(30);
		ae.setWeekDays(60);
		am = new AlarmModel(ae);
		am.calcRTCTime();
		ret = am.getEntity().getTime();
	}
}
