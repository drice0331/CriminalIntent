package com.timefourcrime.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.example.criminalintent.R;
import com.timefourcrime.model.Crime;
import com.timefourcrime.model.CrimeLab;
import com.timefourcrime.model.Photo;
import com.timefourcrime.util.PictureUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
//import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CrimeFragment extends Fragment {
	
	private static final String CRIME_KEY = "Crime key";
	private static final String TAG = "CrimeFragment";
	public static final String EXTRA_CRIME_ID = "com.timeforcrime.ui.CrimeFragment.crime_id";
	private static final String DIALOG_DATE = "date";
	private static final String DIALOG_TIME = "time";
	private static final String DIALOG_IMAGE = "image";
	private static final int REQUEST_DATE = 0;
	private static final int REQUEST_TIME = 4;
	private static final int REQUEST_PHOTO = 1;
	private static final int REQUEST_CONTACT = 2;
	
	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;
	private ImageButton mPhotoButton;
	private ImageView mPhotoView;
	private Button reportButton;
	private Button mSuspectButton;
	
	private Callbacks mCallbacks;
	
	public interface Callbacks {
		void onCrimeUpdated(Crime crime);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (Callbacks)activity;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}
	
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
				mCallbacks.onCrimeUpdated(mCrime);
				getActivity().setTitle(mCrime.getTitle());
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
				mCallbacks.onCrimeUpdated(mCrime);
			}
		});
		
		mPhotoButton = (ImageButton)view.findViewById(R.id.crime_imageButton);
		mPhotoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CrimeCameraActivity.class);
				startActivityForResult(intent, REQUEST_PHOTO);
			}
			
		});
		
		mPhotoView = (ImageView)view.findViewById(R.id.crime_imageView);
		mPhotoView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Photo photo = mCrime.getPhoto();
				if(photo == null) {
					return;
				}
				
				FragmentManager fm = getActivity().getSupportFragmentManager();
				String path = getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
				ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
			}
		});
		
		//If camera not available then disable camera functionality
		PackageManager pm = getActivity().getPackageManager();
		if(!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) && 
				!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
			mPhotoButton.setEnabled(false);
		}
		
		reportButton = (Button)view.findViewById(R.id.crime_reportButton);
		reportButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
				intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
				intent = Intent.createChooser(intent, getString(R.string.send_report));
				startActivity(intent);
			}
		});
		
		mSuspectButton = (Button)view.findViewById(R.id.crime_suspectButton);
		mSuspectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, 
						ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, REQUEST_CONTACT);
			}
		});
		
		if(mCrime.getSuspect() != null) {
			mSuspectButton.setText(mCrime.getSuspect());
		}
		
		return view;
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		showPhoto();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		//saving crimes to sd card (frag -> crimelab -> criminalintentjsonserializer -> write to sd card)
		CrimeLab.get(getActivity()).saveCrimes();
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		//savedInstanceState.putSerializable(CRIME_KEY, mCrime);
		super.onSaveInstanceState(savedInstanceState);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		PictureUtils.cleanImageView(mPhotoView);
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
			mCallbacks.onCrimeUpdated(mCrime);
			updateDate();
			
			FragmentManager fm = getActivity()
					.getSupportFragmentManager();
			TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
			dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
			dialog.show(fm, DIALOG_TIME);
			
		} else if(requestCode == REQUEST_TIME) {
			Date timedate = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
			Date date = mCrime.getDate();
			Calendar datecal = Calendar.getInstance();
			Calendar timecal = Calendar.getInstance();
			datecal.setTime(date);
			timecal.setTime(timedate);
			datecal.set(Calendar.HOUR, timecal.get(Calendar.HOUR));
			datecal.set(Calendar.MINUTE, timecal.get(Calendar.MINUTE));
			datecal.set(Calendar.AM_PM, timecal.get(Calendar.AM_PM));
			
			date = datecal.getTime();
			mCrime.setDate(date);
			mCallbacks.onCrimeUpdated(mCrime);
			updateDate();
			
			//Date date = mCrime.getDate();
			//date.setHours(timedate.getHours());
			//date.setMinutes(timedate.getMinutes());
			//mCrime.setDate(date);
			//updateDate();
		} else if(requestCode == REQUEST_PHOTO) {
			//Create a new photo object and attach it to the crime
			String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
			if(filename != null) {

				Photo photo = new Photo(filename);
				mCrime.setPhoto(photo);
				mCallbacks.onCrimeUpdated(mCrime);
				showPhoto();
			}
		} else if(requestCode == REQUEST_CONTACT) {
			Uri contactUri = data.getData();
			
			//Specify which fields you want your query to return values for.
			String[] queryFields = new String[] {
					ContactsContract.Contacts.DISPLAY_NAME
			};
			//Perform your query - the contactUri is like a "where" clause
			Cursor c = getActivity().getContentResolver()
					.query(contactUri, queryFields, null, null, null);
			
			//Double-check that you actually got results
			if(c.getCount() == 0) {
				c.close();
				return;
			}
			
			//Pull out the first column of the first row of data that
			//is your suspect's name
			c.moveToFirst();
			String suspect = c.getString(0);
			mCrime.setSuspect(suspect);
			mCallbacks.onCrimeUpdated(mCrime);
			mSuspectButton.setText(suspect);
			c.close();
		}
	}
	
	private void updateDate() {
		mDateButton.setText(mCrime.getDate().toString());
		//mTimeButton.setText(mCrime.getDate().getHours() + ":" + mCrime.getDate().getMinutes());
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
	
	private void showPhoto() {
		//(Re)set the image button image based on our photo
		Photo photo = mCrime.getPhoto();
		BitmapDrawable b = null;
		if(photo != null) {
			String path = getActivity()
					.getFileStreamPath(photo.getFilename()).getAbsolutePath();
			b = PictureUtils.getScaledDrawable(getActivity(), path);
		}
		mPhotoView.setImageDrawable(b);
	}
	
	private String getCrimeReport() {
		String solvedString = null;
		if(mCrime.isSolved()) {
			solvedString = getString(R.string.crime_report_solved);
		} else {
			solvedString = getString(R.string.crime_report_unsolved);
		}
		
		String dateFormat = "EEE, MMM dd";
		String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
		
		String suspect = mCrime.getSuspect();
		if(suspect == null) {
			suspect = getString(R.string.crime_report_no_suspect);
		} else {
			suspect = getString(R.string.crime_report_suspect, suspect);
		}
		
		String report = getString(R.string.crime_report, 
				mCrime.getTitle(), dateString, solvedString, suspect);
		
		return report;
		
	}
}
