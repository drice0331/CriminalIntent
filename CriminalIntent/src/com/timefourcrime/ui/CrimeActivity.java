package com.timefourcrime.ui;

import java.util.UUID;

import com.example.criminalintent.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.app.Activity;
//import android.app.Fragment;
//import android.app.FragmentManager;

public class CrimeActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		
		UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		return CrimeFragment.newInstance(crimeId);
	}
}
