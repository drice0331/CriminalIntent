package com.timefourcrime.adapter;

import java.util.ArrayList;

import com.example.criminalintent.R;
import com.timefourcrime.model.Crime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.app.Activity;


//TODO - unfinished - crimeAdapter inner class in CrimeListFragment is the used one

public class CrimeAdapterNonInner extends ArrayAdapter<Crime>{

	private Context context;
	
	public CrimeAdapterNonInner(Context context, int resource, ArrayList<Crime> objects) {
		super(context, resource, objects);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.list_item_crime, null);
		}
		
		return convertView;
	}

}
