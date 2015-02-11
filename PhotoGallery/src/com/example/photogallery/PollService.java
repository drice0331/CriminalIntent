package com.example.photogallery;

import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class PollService extends IntentService {

	private static final String TAG = "Poll Service";
	
	private static final int POLL_INTERVAL = 1000*600; //600 SECONDS = 10 min
	
	public PollService() {
		super(TAG);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		boolean isNetworkAvailable = cm.getBackgroundDataSetting() && cm.getActiveNetworkInfo() != null;
		
		if(!isNetworkAvailable) return;
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String query = prefs.getString(FlickrFetchr.PREF_SEARCH_QUERY, null);
		String lastResultId = prefs.getString(FlickrFetchr.PREF_LAST_RESULT_ID, null);
		
		ArrayList<GalleryItem> items;
		if(query != null) {
			items = new FlickrFetchr().search(query);
		} else {
			items = new FlickrFetchr().fetchItems();
		}
		
		if(items.size() == 0) return;
		
		String resultId = items.get(0).getId();
		
		if(!resultId.equals(lastResultId)) {
			Log.i(TAG, "Got a new result: " + resultId);
			
			Resources res = getResources();
			PendingIntent pendingIntent = PendingIntent
					.getActivity(this, 0, new Intent(this, PhotoGalleryActivity.class), 0);
			
			Notification notification = new NotificationCompat.Builder(this)
				.setTicker(res.getString(R.string.new_pictures_title))
				.setSmallIcon(android.R.drawable.ic_menu_report_image)
				.setContentTitle(res.getString(R.string.new_pictures_title))
				.setContentText(res.getString(R.string.new_pictures_text))
				.setContentIntent(pendingIntent)
				.setAutoCancel(true)
				.build();
			
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			
			notificationManager.notify(0, notification);
		} else {
			Log.i(TAG, "Got an old result: " + resultId);
		}
		
		prefs.edit()
			.putString(FlickrFetchr.PREF_LAST_RESULT_ID, resultId)
			.commit();
		
		Log.i(TAG, "Received an intent: " + intent);
	}
	
	public static void setServiceAlarm(Context context, boolean isOn) {
		Intent intent = new Intent(context, PollService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
		
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		
		if(isOn) {
			alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 
					POLL_INTERVAL, pendingIntent);
		} else {
			alarmManager.cancel(pendingIntent);
			pendingIntent.cancel();
		}
	}
	
	public static boolean isServiceAlarmOn(Context context) {
		Intent intent = new Intent(context, PollService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
		return pendingIntent != null;//if null, then alarm is not set
	}

}
