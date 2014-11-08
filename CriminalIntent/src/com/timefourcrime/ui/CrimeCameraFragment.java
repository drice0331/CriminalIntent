package com.timefourcrime.ui;

import com.example.criminalintent.R;

import android.annotation.TargetApi;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CrimeCameraFragment extends Fragment {
	private static final String TAG = "CrimeCameraFragment";
	
	private Camera mCamera;
	private SurfaceView mSurfaceView;
	
	@Override
	@SuppressWarnings("deprecation")
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_crime_camera, parent, false);
		
		Button takePictureButton = (Button)view.findViewById(R.id.crime_camera_takePictureButton);
		takePictureButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		
		mSurfaceView = (SurfaceView)view.findViewById(R.id.crime_camera_surfaceView);
		SurfaceHolder holder = mSurfaceView.getHolder();
		//setType() and SURFACE_TYPE_PUSH_BUFFERS are both deprecated,
		//but are required for Camera preview to work on pre-3.0 devices.
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		return view;
	}
	
	@TargetApi(9)
	@Override
	public void onResume() {
		super.onResume();
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			mCamera.open(0);
		}
		else {
			mCamera.open();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}
}
