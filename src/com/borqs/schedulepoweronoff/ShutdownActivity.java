package com.borqs.schedulepoweronoff;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

public class ShutdownActivity extends Activity {
    private static final String TAG = "ShutdownActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void cancelCountDownTimer() {
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Log.d(TAG, "onCreateDialog");
		return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
    }

}
