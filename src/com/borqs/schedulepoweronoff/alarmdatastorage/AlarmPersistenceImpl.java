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
	public synchronized boolean putAlarm(AlarmEntity entry) {
		String value = entry.toString();
		Log.i(TAG, "putAlarm:" + value);
		return mDataEditor.putString(generateKey(entry.getType()), value)
				.commit();
	}

	@Override
	public synchronized AlarmEntity getAlarm(int type) {
		String str = mPreferences.getString(generateKey(type), null);
		if (str == null)
			return null;
		AlarmEntity entity = GSONUtils.jsonToBean(str, AlarmEntity.class);
		return entity;
	}

	private String generateKey(int type) {
		return type + "";
	}

	@Override
	public synchronized List<AlarmEntity> getAlarms() {
		// only two type
		List<AlarmEntity> ret = new ArrayList<AlarmEntity>();
		AlarmEntity ae = getAlarm(AlarmEntity.POWEROFF_CLOCK);
		if (ae != null)
			ret.add(ae);

		ae = getAlarm(AlarmEntity.POWERON_CLOCK);
		if (ae != null)
			ret.add(ae);

		return ret;
	}

}
