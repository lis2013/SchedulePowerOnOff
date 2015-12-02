package com.borqs.schedulepoweronoff;

import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmModel;

import android.content.Context;
import android.content.SharedPreferences;

public class ChooseTypePersistence {

    public static final int CHOOSE_MONDAY_TO_FRIDAY = 0;
    public static final int CHOOSE_EVERY_DAY = 1;
    public static final int CHOOSE_CUSTOM = 2;

    private SharedPreferences mPreference;
    private int mRepeateType;
    private AlarmModel mModel;

    public ChooseTypePersistence(Context ctx, AlarmModel model) {
        mModel = model;
        mPreference = ctx.getSharedPreferences(AlarmModel.PREFERECNE_NAME,
                Context.MODE_PRIVATE);
        mRepeateType = mPreference.getInt(AlarmModel.CHOOSE_TYPE_KEY, -1);
        if (mRepeateType == -1) {
            if (model.isMondayToFriday()) {
                mRepeateType = CHOOSE_MONDAY_TO_FRIDAY;
            } else if (model.isEveryDay()) {
                mRepeateType = CHOOSE_EVERY_DAY;
            } else {
                mRepeateType = CHOOSE_CUSTOM;
            }
        }
    }

    public int getType(){
        return mRepeateType;
    }

    public void save(){
        mPreference.edit().putInt(AlarmModel.CHOOSE_TYPE_KEY, mRepeateType)
        .commit();
    }

    public void setChooseType(int type){
        mRepeateType = type;
    }

    public String getRepeatStr(Context context) {
        String[] result = new String[] { context.getString(R.string.never),
                context.getResources().getStringArray(R.array.repeat_type)[0],
                context.getText(R.string.every_day).toString(), };
        if (!mModel.isRepeated())
            return result[0];

        if (mModel.isMondayToFriday() && mRepeateType == CHOOSE_MONDAY_TO_FRIDAY) {
            return result[1];
        }
        if (mModel.isEveryDay() && mRepeateType == CHOOSE_EVERY_DAY) {
            return result[2];
        }
        return mModel.getWeekDayStr(context);
    }

}
