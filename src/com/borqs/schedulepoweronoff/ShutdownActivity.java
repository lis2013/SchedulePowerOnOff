package com.borqs.schedulepoweronoff;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

@SuppressLint("NewApi")
public class ShutdownActivity extends Activity {
    private static final String TAG = "ShutdownActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_COUNT_DOWN_TIME = "count_down_time";
    private static final int THOUSAND_MILLISECONDS = 1000;
    private static final int TEN_SECONDS = 10;

    private AlertDialogFragment mFragment;
    private long mCurrentCountDownTime;

    private CountDownTimer timer = new CountDownTimer(TEN_SECONDS
            * THOUSAND_MILLISECONDS, THOUSAND_MILLISECONDS) {

        @Override
        public void onTick(long millisUntilFinished) {
            mCurrentCountDownTime = millisUntilFinished;
            setMessage(getResources().getString(
                    R.string.shutdown_dialog_message,
                    mCurrentCountDownTime / THOUSAND_MILLISECONDS));
        }

        @Override
        public void onFinish() {
            shutdown();
            finish();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentCountDownTime = TEN_SECONDS * THOUSAND_MILLISECONDS;
        if (savedInstanceState != null) {
            mCurrentCountDownTime = savedInstanceState
                    .getLong(KEY_COUNT_DOWN_TIME);
        }
        showDialog();
        timer.start();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(KEY_COUNT_DOWN_TIME, mCurrentCountDownTime);
        super.onSaveInstanceState(outState);
    }

    private void showDialog() {
        mFragment = AlertDialogFragment
                .newInstance(R.string.shutdown_dialog_title);
        mFragment.show(getFragmentManager(), "dialog");
    }

    private void setMessage(String msg) {
        ((AlertDialog) mFragment.getDialog()).setMessage(msg);
    }

    private void shutdown() {
        Log.i(TAG, "Request shut down the system");
        // Intent intent = new Intent(Intent.ACTION_REQUEST_SHUTDOWN);
        // intent.putExtra(Intent.EXTRA_KEY_CONFIRM, false);
        // intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // startActivity(intent);

    }

    private static class AlertDialogFragment extends DialogFragment {
        private ShutdownActivity mActivity;

        public static AlertDialogFragment newInstance(int titleId) {
            AlertDialogFragment fragment = new AlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt(KEY_TITLE, titleId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            mActivity = ((ShutdownActivity) getActivity());
            int titleId = getArguments().getInt(KEY_TITLE);
            return new AlertDialog.Builder(getActivity())
                    .setTitle(titleId)
                    .setMessage(
                            getResources().getString(
                                    R.string.shutdown_dialog_message,
                                    mActivity.mCurrentCountDownTime
                                            / THOUSAND_MILLISECONDS))
                    .setPositiveButton(android.R.string.ok,
                            new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    mActivity.timer.cancel();
                                    mActivity.finish();
                                    mActivity.shutdown();
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    mActivity.timer.cancel();
                                    mActivity.finish();
                                }
                            }).create();
        }
    }
}
