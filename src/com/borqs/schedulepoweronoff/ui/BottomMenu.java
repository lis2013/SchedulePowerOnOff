package com.borqs.schedulepoweronoff.ui;

import com.borqs.schedulepoweronoff.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class BottomMenu extends LinearLayout {
    public static final String TAG = "BottomMenu";
    public static final int FINISH_MENU = 0;

    public BottomMenu(Context context) {
        super(context);
        initView(context);
    }

    public BottomMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.bottom_menu_layout, this, true);
    }
}
