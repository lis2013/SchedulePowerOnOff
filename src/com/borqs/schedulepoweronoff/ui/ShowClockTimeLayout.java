package com.borqs.schedulepoweronoff.ui;

import java.util.Calendar;

import com.borqs.schedulepoweronoff.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShowClockTimeLayout extends LinearLayout {
	private Context mContext;
    public ShowClockTimeLayout(Context context) {
    	this(context, null);
	}
    public ShowClockTimeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

    }
	/*private static final String TAG = "DigitalClock";
    private static final String M12 = "h:mm";

    private Calendar mCalendar;
    private String mFormat;
    private TextView mTimeDisplay;
    private AmPm mAmPm;
    private ContentObserver mFormatChangeObserver;
    private boolean mLive = true;
    private boolean mAttached;

    private final Handler mHandler = new Handler();
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mLive && intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                mCalendar = Calendar.getInstance();
            }
            updateTime();
        }
    };

    static class AmPm {
        private final int mColorOn;
        private final int mColorOff;

        private final LinearLayout mAmPmLayout;
        private TextView mAm;
        private TextView mPm;

        AmPm(View parent) {
            mAmPmLayout = (LinearLayout) parent.findViewById(R.id.am_pm);
            if (mAmPmLayout != null) {
                mAm = (TextView) mAmPmLayout.findViewById(R.id.am);
                mPm = (TextView) mAmPmLayout.findViewById(R.id.pm);
            }

            Resources r = parent.getResources();
            mColorOn = r.getColor(R.color.ampm_on);
            mColorOff = r.getColor(R.color.ampm_off);
        }

        void setShowAmPm(boolean show) {
            mAmPmLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        void setIsMorning(boolean isMorning) {
            mAm.setTextColor(isMorning ? mColorOn : mColorOff);
            mPm.setTextColor(isMorning ? mColorOff : mColorOn);
        }
    }

    private class ClockFormatChangeObserver extends ContentObserver {
        public ClockFormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            setDateFormat();
            updateTime();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTimeDisplay = (TextView) findViewById(R.id.timeDisplay);
        mAmPm = new AmPm(this);
        mCalendar = Calendar.getInstance();

        setDateFormat();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        Log.d(TAG, "onAttachedToWindow " + this);

        if (mAttached) {
            return;
        }
        mAttached = true;

        if (mLive) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            mContext.registerReceiver(mIntentReceiver, filter, null, mHandler);
        }

        mFormatChangeObserver = new ClockFormatChangeObserver();
        mContext.getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, mFormatChangeObserver);

        updateTime();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (!mAttached) {
            return;
        }
        mAttached = false;

        Drawable background = getBackground();
        if (background instanceof AnimationDrawable) {
            ((AnimationDrawable) background).stop();
        }

        if (mLive) {
            mContext.unregisterReceiver(mIntentReceiver);
        }
        mContext.getContentResolver().unregisterContentObserver(mFormatChangeObserver);
    }

    void updateTime(Calendar c) {
        mCalendar = c;
        updateTime();
    }

    private void updateTime() {
        if (mLive) {
            mCalendar.setTimeInMillis(System.currentTimeMillis());
        }

        CharSequence newTime = DateFormat.format(mFormat, mCalendar);
        mTimeDisplay.setText(newTime);
        mAmPm.setIsMorning(mCalendar.get(Calendar.AM_PM) == 0);
    }

    private void setDateFormat() {
        Log.i(TAG, "mContext = " + mContext);
        //mFormat = Alarms.get24HourMode(mContext) ? Alarms.M24 : M12;
        //mAmPm.setShowAmPm(mFormat.equals(M12));
    }

    void setLive(boolean live) {
        mLive = live;
    }*/
}