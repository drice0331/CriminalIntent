package com.timefourcrime.ui;

import java.util.Date;
import java.util.UUID;

import com.example.criminalintent.R;
import com.timefourcrime.model.Crime;
import com.timefourcrime.model.CrimeLab;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
//import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class CrimeFragment extends Fragment {
	
	public static final String EXTRA_CRIME_ID = "com.timeforcrime.ui.CrimeFragment.crime_id";
	private static final String DIALOG_DATE = "date";
	private static final int REQUEST_DATE = 0;
	
	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;
	
	public static CrimeFragment newInstance(UUID crimeId) {
		Bundle args = new Bundle();
		
		args.putSerializable(EXTRA_CRIME_ID, crimeId);
		CrimeFragment fragment = new CrimeFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
		
		UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
		
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_crime, parent, false);	
		
		//set home action bar button as up/back button
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if(NavUtils.getParentActivityName(getActivity()) != null ) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		mTitleField = (EditText)view.findViewById(R.id.crime_title);
		mTitleField.setText(mCrime.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence c, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence c, int start, int before,
					int count) {
				mCrime.setTitle(c.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		mDateButton = (Button)view.findViewById(R.id.crime_date);
		updateDate();
		mDateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity()
						.getSupportFragmentManager();
				DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
			}
		});
		
		mSolvedCheckBox = (CheckBox)view.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mCrime.setSolved(isChecked);
				
			}
		});
		return view;
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		//saving crimes to sd card (frag -> crimelab -> criminalintentjsonserializer -> write to sd card)
		CrimeLab.get(getActivity()).saveCrimes();
	}
	
	public void returnResult() {
		getActivity().setResult(Activity.RESULT_OK, null);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK) {
			return;
		}
		
		if(requestCode == REQUEST_DATE) {
			Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			updateDate();
		}
		
	}
	
	private void updateDate() {
		mDateButton.setText(mCrime.getDate().toString());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuitem) {
		switch(menuitem.getItemId()) {
			case android.R.id.home:
				if(NavUtils.getParentActivityName(getActivity()) != null) {
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			default:
				return super.onOptionsItemSelected(menuitem);
		}
		
 	}
	
}
