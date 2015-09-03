package com.nntuyen.yourflickr.domain.broadcast;

import com.nntuyen.yourflickr.domain.task.RestAPITask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class UploadPhotoReceiver extends BroadcastReceiver {
	
	private static final String TAG = "UploadPhotoReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		String response = intent.getStringExtra(RestAPITask.HTTP_RESPONSE);
		Toast.makeText(context, response, Toast.LENGTH_LONG).show();
		Log.d(TAG, response);
	}

}
