package com.nntuyen.yourflickr.domain.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nntuyen.yourflickr.app.constant.FlickrApiConst;
import com.nntuyen.yourflickr.domain.task.RestAPITask;

public class UploadPhotoReceiver extends BroadcastReceiver {
	
	private static final String TAG = "UploadPhotoReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		String response = intent.getStringExtra(RestAPITask.HTTP_RESPONSE);
		String msg = FlickrApiConst.UPLOAD_PHOTO_SUCCESS_MSG;
		
		if (!response.contains("ok")) {
			msg = FlickrApiConst.UPLOAD_PHOTO_FAIL_MSG;
		}
		
		Log.d(TAG, "Send " + msg + " to presenter");
		ObservableObject.getInstance().updateValue(msg);
	}

}
