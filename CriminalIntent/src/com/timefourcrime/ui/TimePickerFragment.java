package com.timefourcrime.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.example.criminalintent.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class TimePickerFragment extends DialogFragment {
	
	public static final String EXTRA_TIME = "com.timefourcrime.time";

	private Date mDate;
	
	public static TimePickerFragment newInstance(Date date) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TIME, date);
		
		TimePickerFragment fragment = new TimePickerFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		mDate = (Date)getArguments().getSerializable(EXTRA_TIME);
		
		GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
		calendar.setTime(mDate);
		
		View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);
		
		TimePicker timePicker = (TimePicker)view.findViewById(R.id.dialog_time_timePicker);
		
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR, hourOfDay);
				cal.set(Calendar.MINUTE, minute);
				mDate = cal.getTime();

			}
			
		});
		
		return new AlertDialog.Builder(getActivity())
			.setView(view)
			.setTitle(R.string.time_picker_title)
			.setPositiveButton(
					android.R.string.ok,
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							sendResult(Activity.RESULT_OK);
						}
					})
			.create();
	}
	
	private void sendResult(int resultCode) {
		
		if(getTargetFragment() == null) {
			return;
		}
		
		Intent intent = new Intent();
		intent.putExtra(EXTRA_TIME, mDate);
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
	}	
}
