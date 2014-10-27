package com.timefourcrime.ui;

import com.example.criminalintent.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;

public class CrimeActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crime);
		
		FragmentManager fragMan = getFragmentManager();
		Fragment fragment = fragMan.findFragmentById(R.id.fragmentContainer);
		
		if(fragment == null) {
			fragment = new CrimeFragment();
			fragMan.beginTransaction()
				.add(R.id.fragmentContainer, fragment)
				.commit();
		}
	}
}
