package com.timefourcrime.model;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo {

	private static final String JSON_FILENAME = "filename";
	
	private String mFilename;
	
	//Create a photo with a generated filename
	public Photo() {
		this(UUID.randomUUID().toString() + ".jpg");
	}
	//Create a photo representing an existing file on disk 
	public Photo(String filename) {
		mFilename = filename;
	}
	
	public Photo(JSONObject json) throws JSONException {
		mFilename = json.getString(JSON_FILENAME);
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_FILENAME, mFilename);
		return json;
	}
	
	public String getFilename() {
		return mFilename;
	}
	
	/*
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	
	public int getOrientation() {
		return orientation;
	}
	*/
}
