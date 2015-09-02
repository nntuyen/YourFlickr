package com.nntuyen.yourflickr.domain.broadcast;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nntuyen.yourflickr.app.constant.FlickrApiConst;
import com.nntuyen.yourflickr.app.util.Common;
import com.nntuyen.yourflickr.domain.model.Photo;
import com.nntuyen.yourflickr.domain.task.RestAPITask;

public class GetPhotosListReceiver extends BroadcastReceiver {
	
	private static final String TAG = "GetPhotosListReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		String response = intent.getStringExtra(RestAPITask.HTTP_RESPONSE);
		String msg = FlickrApiConst.GET_PHOTOS_SUCCESS_MSG;
		
		if (response != null) {
			response = Common.getFlickrJson(response);
			
			try {
				JSONObject jsRootObj = new JSONObject(response);
				String stat = jsRootObj.optString("stat").toString();
				
				if (stat.equalsIgnoreCase("ok")) {
					JSONObject jsPhotos = jsRootObj.getJSONObject("photos");
					JSONArray jsPhotoArr = jsPhotos.getJSONArray("photo");
					
					List<Photo> photos = new ArrayList<Photo>();
					for (int i = 0; i < jsPhotoArr.length(); i++) {
						JSONObject jsPhoto = jsPhotoArr.getJSONObject(i);
						Photo p = new Photo();
						p.setFarm(jsPhoto.optString("farm").toString());
						p.setServer(jsPhoto.optString("server").toString());
						p.setId(jsPhoto.optString("id").toString());
						p.setSecret(jsPhoto.optString("secret").toString());
						photos.add(p);
					}
					
					ObservableObject.getInstance().setPhotos(photos);
				} else {
					msg = FlickrApiConst.GET_PHOTOS_FAIL_MSG;
				}
			} catch (JSONException e) {
				msg = FlickrApiConst.GET_PHOTOS_FAIL_MSG;
				e.printStackTrace();
			}
		} else {
			msg = FlickrApiConst.GET_PHOTOS_FAIL_MSG;
		}
		
		Log.d(TAG, "Send " + msg + " to presenter");
		ObservableObject.getInstance().updateValue(msg);
	}

}
