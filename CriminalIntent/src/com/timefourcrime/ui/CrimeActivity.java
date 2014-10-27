package com.timefourcrime.ui;

import com.example.criminalintent.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.app.Activity;
//import android.app.Fragment;
//import android.app.FragmentManager;

public class CrimeActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new CrimeFragment();
	}
}
