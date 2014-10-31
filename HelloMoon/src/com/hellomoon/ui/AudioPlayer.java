package com.hellomoon.ui;

import com.example.hellomoon.R;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayer {

	private MediaPlayer mPlayer;
	
	public void stop() {
		if(mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
	
	public void play(Context context) {
		//call stop to prevent multiple instances of media player in case user presses play button twice
		stop();
		
		mPlayer = MediaPlayer.create(context, R.raw.one_small_step);
		
		mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {			
			@Override
			public void onCompletion(MediaPlayer mp) {
				stop();
			}
		});
		
		mPlayer.start();
	}
}
