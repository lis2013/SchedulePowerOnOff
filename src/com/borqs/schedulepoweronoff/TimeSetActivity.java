package com.borqs.schedulepoweronoff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TimePicker;

import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmEntity;
import com.borqs.schedulepoweronoff.alarmdatastorage.AlarmModel;
import com.borqs.schedulepoweronoff.utils.AlarmUtils;

public class TimeSetActivity extends Activity implements TimePickerDialog.OnTimeSetListener, OnClickListener {
	private ListView mList;
	private int mTimeType;
	private ImageButton mResetButton;
	private ImageButton mFinishButton;
	private AlarmModel mAlarmModel;
    private final static int REPEAT_DIALOG = 1;
    boolean[] checked = new boolean[] { false, false, false, false, false, false, false };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_set_layout);
		mAlarmModel = AlarmModel.convertToObj(getIntent().getExtras()
				.getString(AlarmModel.ENTITY));

		mResetButton = (ImageButton) findViewById(R.id.reset);
		mFinishButton = (ImageButton) findViewById(R.id.finish);
		mResetButton.setOnClickListener(this);
		mFinishButton.setOnClickListener(this);
		if (mTimeType == AlarmEntity.POWERON_CLOCK) {
			setTitle(R.string.set_power_on);
		} else {
			setTitle(R.string.set_power_off);
		}
		mList = (ListView) findViewById(android.R.id.list);
		updateListView();
	}

	private void updateListView() {
		SimpleAdapter adapter = getListAdapter();
		mList.setAdapter(adapter);
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					new TimePickerDialog(
							TimeSetActivity.this,
							TimeSetActivity.this,
							mAlarmModel.getEntity().getHour(),
							mAlarmModel.getEntity().getMinute(),
							DateFormat
									.is24HourFormat(TimeSetActivity.this))
							.show();
					break;
				case 1:
					showDialog(REPEAT_DIALOG);
					break;
				}
			}
		});
	}

	private SimpleAdapter getListAdapter() {
		String[] title = { this.getResources().getString(R.string.time_text),
				this.getResources().getString(R.string.repeat) };
		String[] info = { mAlarmModel.getTime(), mAlarmModel.getRepeatedStr(this) };
		int[] imageids = { R.drawable.show_icon_time,
				R.drawable.show_icon_repeat };
		List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < title.length; i++) {
			Map<String, Object> listem = new HashMap<String, Object>();
			listem.put("title", title[i]);
			listem.put("info", info[i]);
			listem.put("imageid", imageids[i]);
			listems.add(listem);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listems,
				R.layout.time_set_list_item, new String[] { "title", "info",
						"imageid" }, new int[] { R.id.title_text,
						R.id.info_text, R.id.show_button }) ;
		return simpleAdapter;
	}

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case REPEAT_DIALOG:
                Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.repeat));
                builder.setMultiChoiceItems(R.array.week_day, checked,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                checked[which] = isChecked;
                            }
                        });
                builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }

                        });
                dialog = builder.create();
                break;
        }
        return dialog;
    }

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		mAlarmModel.setTime(this, hourOfDay, minute);
		updateListView();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.reset:
			finish();
			break;
		case R.id.finish:
			mAlarmModel.enable(this, mAlarmModel.isEnabled());
			AlarmUtils.toastAlarmPeriod(this, mAlarmModel);
			finish();
			break;
		}

	}

}
