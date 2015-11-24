package com.borqs.schedulepoweronoff.alarmdatastorage;

import java.util.ArrayList;
import java.util.List;

import com.borqs.schedulepoweronoff.utils.GSONUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class AlarmPersistenceImpl implements AlarmPersistence {
	private final static String TAG = AlarmPersistenceImpl.class
			.getSimpleName();
	private Editor mDataEditor;
	private SharedPreferences mPreferences;
	private static AlarmPersistenceImpl mInstance;

	public static synchronized AlarmPersistence getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new AlarmPersistenceImpl(context);
		}
		return mInstance;
	}

	private AlarmPersistenceImpl(Context context) {
		mPreferences = context.getSharedPreferences("power_clock",
				Context.MODE_PRIVATE);
		mDataEditor = mPreferences.edit();
	}

	@Override
	public synchronized boolean putAlarm(AlarmModel entry) {
		String value = entry.toString();
		Log.i(TAG, "putAlarm:" + value);
		return mDataEditor.putString(generateKey(entry.getEntity().getType()), value)
				.commit();
	}

	@Override
	public synchronized AlarmModel getAlarm(int type) {
		String str = mPreferences.getString(generateKey(type), null);
		if (str == null)
			return null;
		AlarmEntity entity = GSONUtils.jsonToBean(str, AlarmEntity.class);
		return new AlarmModel(entity);
	}

	private String generateKey(int type) {
		return type + "";
	}

	@Override
	public synchronized List<AlarmModel> getAlarms() {
		// only two type
		List<AlarmModel> ret = new ArrayList<AlarmModel>();
		AlarmModel ae = getAlarm(AlarmModel.POWEROFF_CLOCK);
		if (ae != null)
			ret.add(ae);

		ae = getAlarm(AlarmModel.POWERON_CLOCK);
		if (ae != null)
			ret.add(ae);

		return ret;
	}

}
