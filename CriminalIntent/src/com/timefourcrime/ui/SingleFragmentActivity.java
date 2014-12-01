package com.timefourcrime.ui;

import com.example.criminalintent.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
//import android.app.Activity;
//import android.app.Fragment;
//import android.app.FragmentManager;

public abstract class SingleFragmentActivity extends FragmentActivity {
	protected abstract Fragment createFragment();
	
	protected int getLayoutResId() {
		return R.layout.activity_fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResId());
		
		FragmentManager fragMan = getSupportFragmentManager();
		Fragment fragment = fragMan.findFragmentById(R.id.fragmentContainer);
		
		if(fragment == null) {
			fragment = createFragment();
			fragMan.beginTransaction()
				.add(R.id.fragmentContainer, fragment)
				.commit();
		}
	}	
}
