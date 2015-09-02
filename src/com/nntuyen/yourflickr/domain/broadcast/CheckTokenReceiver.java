package com.nntuyen.yourflickr.domain.broadcast;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nntuyen.yourflickr.app.constant.FlickrApiConst;
import com.nntuyen.yourflickr.app.constant.KeyValueConst;
import com.nntuyen.yourflickr.app.util.Common;
import com.nntuyen.yourflickr.domain.task.RestAPITask;

public class CheckTokenReceiver extends BroadcastReceiver {
	
	private static final String TAG = "CheckTokenReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		String response = intent.getStringExtra(RestAPITask.HTTP_RESPONSE);
		String msg = FlickrApiConst.CHECK_TOKEN_SUCCESS_MSG;
		
		if (response != null) {
			response = Common.getFlickrJson(response);
			
			try {
				JSONObject jsRootObj = new JSONObject(response);
				String stat = jsRootObj.optString("stat").toString();
				
				if (stat.equalsIgnoreCase("ok")) {
					JSONObject jsAuth = jsRootObj.getJSONObject("auth");
					JSONObject jsToken = jsAuth.getJSONObject("token");
					JSONObject jsUser = jsAuth.getJSONObject("user");
					
					String token = jsToken.optString("_content").toString();
					String userId = jsUser.optString("nsid").toString();
					String username = jsUser.optString("username").toString();
					String fullname = jsUser.optString("fullname").toString();
					
					Common.saveDataToSharedPreferences(context, KeyValueConst.FLICKR_TOKEN, token);
					Common.saveDataToSharedPreferences(context, KeyValueConst.FLICKR_USER_ID, userId);
					Common.saveDataToSharedPreferences(context, KeyValueConst.FLICKR_USERNAME, username);
					Common.saveDataToSharedPreferences(context, KeyValueConst.FLICKR_FULLNAME, fullname);
				} else {
					msg = FlickrApiConst.CHECK_TOKEN_FAIL_MSG;
				}
			} catch (JSONException e) {
				msg = FlickrApiConst.CHECK_TOKEN_FAIL_MSG;
				e.printStackTrace();
			}
		} else {
			msg = FlickrApiConst.CHECK_TOKEN_FAIL_MSG;
		}
		
		Log.d(TAG, "Send " + msg + " to presenter");
		ObservableObject.getInstance().updateValue(msg);
	}

}
