package com.timefourcrime.model;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

public class CrimeLab {
	private ArrayList<Crime> mCrimes;
	
	private static CrimeLab sCrimeLab;
	private Context mAppContext;
	
	private CrimeLab(Context appContext) {
		mAppContext = appContext;
		mCrimes = new ArrayList<Crime>();
		
		//temp dummies;
		for(int i = 0; i < 100; i++) {
			Crime crime = new Crime();
			crime.setTitle("Crime #" + i);
			crime.setSolved(i%2 == 0);
			mCrimes.add(crime);
		}
	}
	
	public static CrimeLab get(Context c) {
		if(sCrimeLab == null) {
			sCrimeLab = new CrimeLab(c.getApplicationContext());
		}
		return sCrimeLab;
	}
	
	public ArrayList<Crime> getCrimes() {
		return mCrimes;
	}
	
	public Crime getCrime(UUID id) {
		for(Crime crime : mCrimes) {
			if(crime.getId().equals(id)) {
				return crime;
			}
		}
		return null;
	}
}
