package com.timefourcrime.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

public class PictureUtils {
	/**
	 * Get a Bitmap Drawable from a local file that is scaled down
	 * to fit the current window size
	 */
	
	@SuppressWarnings("deprecation")
	public static BitmapDrawable getScaledDrawable(Activity activity, String path) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		
		Point point;
		float destWidth = display.getWidth();
		float destHeight = display.getHeight();
		
		//read in the dimensions of the image on disk
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		
		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;
		
		int inSampleSize = 1;
		if(srcHeight > destHeight || srcWidth > destWidth) {
			if(srcWidth > srcHeight) {
				inSampleSize = Math.round((float)srcHeight / (float)destHeight);
			} else {
				inSampleSize = Math.round((float)srcWidth/(float)destWidth);
			}
		}
		
		options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;
		
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		
//		Matrix matrix = new Matrix();
//		matrix.postRotate(orientation);
//      Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		
		
		return new BitmapDrawable(activity.getResources(), bitmap);
	}
	
	public static void cleanImageView(ImageView imageView) {
		if(!(imageView.getDrawable() instanceof BitmapDrawable)) {
			return;
		}
		
		//clean up the view's image for sake of memory
		BitmapDrawable b = (BitmapDrawable)imageView.getDrawable();
		b.getBitmap().recycle();
		imageView.setImageDrawable(null);
	}
}
