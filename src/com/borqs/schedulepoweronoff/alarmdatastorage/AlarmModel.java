package com.borqs.schedulepoweronoff.alarmdatastorage;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import com.borqs.schedulepoweronoff.R;
import com.borqs.schedulepoweronoff.utils.AlarmUtils;
import com.borqs.schedulepoweronoff.utils.GSONUtils;

public class AlarmModel {
    private static final String TAG = AlarmModel.class.getSimpleName();
    private static final String CONCAT_REPEATED_SPLITOR = " ";
    private static final int WEEK_DAY_COUNT = 7;
    public static final String PREFERECNE_NAME = "set_type";
    private AlarmEntity mEntity;

    public AlarmModel(AlarmEntity e) {
        mEntity = e;
    }

    public void setAlarmEntity(AlarmEntity e) {
        mEntity = e;
    }

    public AlarmEntity getEntity() {
        return mEntity;
    }

    public void setTime(Context ctx, int hour, int minute, boolean enableNow) {
        this.mEntity.setHour(hour);
        this.mEntity.setMinute(minute);
        calcRTCTime();
        if (enableNow)
            enable(ctx, enableNow);
    }

    public boolean isRepeated() {
        return mEntity.getWeekDays() > 0;
    }

    public boolean isMondayToFriday(){
        return (mEntity.getWeekDays() ^ 31) == 0;
    }

    public boolean isEveryDay() {
        return (mEntity.getWeekDays() ^ 0x7F) == 0;
    }

    public boolean isPowerOn() {
        return mEntity.getType() == AlarmEntity.POWERON_CLOCK;
    }

    public boolean isPowerOff() {
        return mEntity.getType() == AlarmEntity.POWEROFF_CLOCK;
    }

    public String getTime(Context context) {
        String format = DateFormat.is24HourFormat(context) ? "kk:mm" : "h:mm";
        CharSequence time = DateFormat.format(format, new Date(mEntity.getTime()));
        return (String) time;
    }

    public long getRTCTime() {
        return mEntity.getTime();
    }

    public boolean isEnabled() {
        return mEntity.isEnable();
    }

    public String getWeekDayStr(Context context) {
        StringBuilder sb = new StringBuilder();
        String[] dayTStrings = context.getResources().getStringArray(R.array.week_day);
        for (int i = 0; i < dayTStrings.length; i++) {
            if (isWeekDaySet(i)) {
                sb.append(dayTStrings[i]);
                sb.append(CONCAT_REPEATED_SPLITOR);
            }
        }
        return sb.substring(0, sb.length() - CONCAT_REPEATED_SPLITOR.length());
    }

    /**
     *
     * @param weekDay
     *            start from 0
     * @return
     */
    private boolean isWeekDaySet(int weekDay) {
        return (mEntity.getWeekDays() & (1 << weekDay)) > 0;
    }

    public boolean[] getWeekDayStatus() {
        boolean[] status = new boolean[WEEK_DAY_COUNT];
        for (int i = 0; i < WEEK_DAY_COUNT; i++) {
            status[i] = isWeekDaySet(i);
        }
        return status;
    }

    public void setWeekDay(int weekDay, boolean set) {
        int weekDays = mEntity.getWeekDays();
        if (!set) {
            weekDays &= ~(1 << weekDay);
        } else {
            weekDays |= (1 << weekDay);
        }
        mEntity.setWeekDays(weekDays);
    }

    public void setWeekDays(boolean weekdays[]) {
        if (weekdays.length != WEEK_DAY_COUNT) {
            throw new IllegalArgumentException("weekdays length is llegal");
        }
        for (int i = 0; i < weekdays.length; i++) {
            setWeekDay(i, weekdays[i]);
        }
    }

    public boolean isExpired() {
        return !isRepeated() && isBeforeNowRTCTime();
    }

    private boolean isBeforeNowRTCTime() {
        if (mEntity.getTime() > 0) {
            return mEntity.getTime() <= System.currentTimeMillis();
        } else {
            // never calculate rtc
            return false;
        }

    }

    private boolean isBeforeNowHourMinutes() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute = calendar.get(Calendar.MINUTE);

        int hour = mEntity.getHour();
        int minutes = mEntity.getMinute();
        return (hour < nowHour || hour == nowHour && minutes <= nowMinute);
    }

    public static AlarmModel convertToObj(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            throw new IllegalArgumentException(jsonStr + " is empty.");
        }
        return new AlarmModel(GSONUtils.jsonToBean(jsonStr, AlarmEntity.class));
    }

    public void calcRTCTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, mEntity.getHour());
        calendar.set(Calendar.MINUTE, mEntity.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (!isRepeated() && isBeforeNowHourMinutes()) {
            // not repeat, only to next day
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            mEntity.setTime(calendar.getTimeInMillis());
            return;
        } else if (!isRepeated()) {
            mEntity.setTime(calendar.getTimeInMillis());
            return;
        }

        // if sunday is the first day
        Calendar now = Calendar.getInstance();
        boolean isFirtDayOfWeekSunday = (now.getFirstDayOfWeek() == Calendar.SUNDAY);
        int todayOfWeekDays = now.get(Calendar.DAY_OF_WEEK);
        if (isFirtDayOfWeekSunday) {
            // ensure monday is the 0 day,...,the sunday is the 6 day
            todayOfWeekDays = todayOfWeekDays - 2;
            if (todayOfWeekDays == -1) {
                todayOfWeekDays = 6;
            }
        }

        if (isWeekDaySet(todayOfWeekDays) && isBeforeNowHourMinutes()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        int i = 0;
        // which day is set
        for (; i < WEEK_DAY_COUNT; i++) {
            if (isWeekDaySet((todayOfWeekDays + i) % WEEK_DAY_COUNT)) {
                break;
            }
        }
        if (i != 0) {
            calendar.add(Calendar.DAY_OF_WEEK, i);
        }
        mEntity.setTime(calendar.getTimeInMillis());
        Log.i(TAG, "type:" + mEntity.getType() + " rtc time:" + mEntity.getTime());
    }

    /**
     * enable clock
     *
     * @param persistence
     * @param enable
     */
    public void enable(Context context, boolean enable) {
        mEntity.setEnable(enable);
        calcRTCTime();
        AlarmPersistenceImpl.getInstance(context).putAlarm(this);
        if (enable) {
            AlarmUtils.registerAlarmEvent(context, this);
        } else {
            AlarmUtils.unregisterAlarmEvent(context, this);
        }
    }

    public String entityString() {
        return mEntity.toString();
    }

    public boolean isAm() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(mEntity.getTime());
        return c.get(Calendar.AM_PM) == 0;
    }

    public String getAmPmStr(Context context) {
        if (DateFormat.is24HourFormat(context)) {
            return "";
        } else {
            return isAm() ? context.getString(R.string.time_format_morning) : context
                    .getString(R.string.time_format_afternoon);
        }
    }
}
