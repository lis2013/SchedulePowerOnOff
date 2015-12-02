package com.borqs.schedulepoweronoff;

import android.util.Log;

import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmEntity;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmModel;

public class AlarmModelTest {

	public void run() {
		AlarmModel am = null;
		AlarmEntity ae = null;
		long ret = 0;
		//test expired
		ae = new AlarmEntity();
		ae.setHour(12);
		ae.setMinute(7);
		ae.setWeekDays(0);
		ae.setEnable(true);
		am = new AlarmModel(ae);
		am.getEntity().setTime(1448424420000L);
		//am.calcRTCTime();
		boolean exipred = am.isExpired();
		if(exipred == true){
			Log.e("test", "isExpired success");
		}else{
			Log.e("test", "isExpired failure");
		}
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
