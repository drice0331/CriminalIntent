package com.example.photogallery;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

public class PhotoGalleryActivity extends SingleFragmentActivity {

	private static final String TAG = "PhotoGalleryActivity";
	
	@Override
	public Fragment createFragment() {
		return new PhotoGalleryFragment();
	}
	
	@Override
	public void onNewIntent(Intent intent) {
		PhotoGalleryFragment fragment = (PhotoGalleryFragment)
				getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
		
		if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			
			PreferenceManager.getDefaultSharedPreferences(this)
			.edit()
			.putString(FlickrFetchr.PREF_SEARCH_QUERY, query)
			.commit();
		}
		
		fragment.updateItems();
	}
}
