package com.example.photogallery;

import android.app.Activity;
import android.support.v4.app.Fragment;

public class PhotoGalleryActivity extends SingleFragmentActivity {

	@Override
	public Fragment createFragment() {
		return new PhotoGalleryFragment();
	}
}
