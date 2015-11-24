package com.borqs.schedulepoweronoff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;

public class PowerOffReceiver extends BroadcastReceiver {
    private static final String TAG = "PowerOffReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "intent action " + String.valueOf(intent.getAction()));
        // close dialog if existed
        Intent closeDialogIntent = new Intent(
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeDialogIntent);

        //TODO: register next power off alarm if repeated alarm. (because shutdown opertion can be canceled by user)

        // launch shutdown dialog
        Intent shutdownIntent = new Intent(context, ShutdownActivity.class);
        shutdownIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);
        context.startActivity(shutdownIntent);
    }
}
