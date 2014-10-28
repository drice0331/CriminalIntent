package com.timefourcrime.ui;

import java.util.UUID;

import com.example.criminalintent.R;
import com.timefourcrime.model.Crime;
import com.timefourcrime.model.CrimeLab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class CrimeFragment extends Fragment {
	
	public static final String EXTRA_CRIME_ID = "com.timeforcrime.ui.CrimeFragment.crime_id";
	
	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		UUID crimeId = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
		
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_crime, parent, false);	
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
		mDateButton.setText(mCrime.getDate().toString());
		mDateButton.setEnabled(false);
		
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
	

}
