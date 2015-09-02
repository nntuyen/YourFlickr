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
import com.nntuyen.yourflickr.app.util.FlickrHelper;
import com.nntuyen.yourflickr.domain.task.RestAPITask;

public class GetFrobReceiver extends BroadcastReceiver {
	
	private static final String TAG = "GetFrobReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		String response = intent.getStringExtra(RestAPITask.HTTP_RESPONSE);
		String msg = FlickrApiConst.GET_FROB_SUCCESS_MSG;
		
		if (response != null) {
			response = Common.getFlickrJson(response);
			
			try {
				JSONObject jsRootObj = new JSONObject(response);
				String stat = jsRootObj.optString("stat").toString();
				
				if (stat.equalsIgnoreCase("ok")) {
					JSONObject jsFrob = jsRootObj.getJSONObject("frob");
					String frob = jsFrob.optString("_content").toString();
					Common.saveDataToSharedPreferences(context, KeyValueConst.FLICKR_FROB, frob);
					FlickrHelper.getInstance().auth(context, frob);
				} else {
					msg = FlickrApiConst.GET_FROB_FAIL_MSG;
				}
			} catch (JSONException e) {
				msg = FlickrApiConst.GET_FROB_FAIL_MSG;
				e.printStackTrace();
			}
		} else {
			msg = FlickrApiConst.GET_FROB_FAIL_MSG;
		}
		
		Log.d(TAG, "Send " + msg + " to presenter");
		ObservableObject.getInstance().updateValue(msg);
	}

}
