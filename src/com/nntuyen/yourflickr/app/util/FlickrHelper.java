package com.nntuyen.yourflickr.app.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nntuyen.yourflickr.app.constant.BroadcastCallbackConst;
import com.nntuyen.yourflickr.app.constant.FlickrApiConst;
import com.nntuyen.yourflickr.app.constant.KeyValueConst;
import com.nntuyen.yourflickr.domain.task.RestAPITask;
import com.nntuyen.yourflickr.ui.activity.FlickrAuthActivity;

@SuppressWarnings("deprecation")
public final class FlickrHelper {
	
	private static final String TAG = "FlickrHelper";

	private static FlickrHelper instance = null;
	
	private FlickrHelper() {

	}
	
	public static FlickrHelper getInstance() {
		if (instance == null) {
			instance = new FlickrHelper();
		}

		return instance;
	}
	
	public void getFrob(Context context) {
		try {
			HttpGet httpGet = new HttpGet(new URI(buildUrlParams(FlickrApiConst.BASE_URL,
					FlickrApiConst.API_KEY_PARAM, FlickrApiConst.API_KEY,
					FlickrApiConst.FORMAT_PARAM, FlickrApiConst.JSON_FORMAT_VALUE,
					FlickrApiConst.METHOD_PARAM, FlickrApiConst.GET_FROB_METHOD_VALUE)));
			RestAPITask task = new RestAPITask(context, BroadcastCallbackConst.GET_FROB_CALLBACK);
			task.execute(httpGet);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public void auth(Context context, String frob) {
		String url = buildUrlParams(FlickrApiConst.AUTH_URL,
				FlickrApiConst.API_KEY_PARAM, FlickrApiConst.API_KEY, 
				FlickrApiConst.FROB_PARAM, frob, 
				FlickrApiConst.PERMS_PARAM, FlickrApiConst.WRITE_PERMS_VALUE);
		Log.d(TAG, "Authenticate " + url);
		
		Intent intent = new Intent(context, FlickrAuthActivity.class);
		intent.putExtra(KeyValueConst.FULL_AUTH_URL, url);
		context.startActivity(intent);
		((Activity)context).finish();
	}
	
	private String buildUrlParams(String url, String... params) {
		StringBuffer sb = new StringBuffer();
		sb.append(url);
		
		int paramsSize = params.length;
		for (int i = 0; i < paramsSize; i++) {
			sb.append(params[i]);
			
			if (i % 2 == 0) {
				sb.append("=");
			} else {
				sb.append("&");
			}
		}
		
		sb.append(FlickrApiConst.API_SIG_PARAM + "=" + getApiSigKey(params));
		
		return sb.toString();
	}
	
	private String getApiSigKey(String... params) {
		StringBuffer sb = new StringBuffer();
		
		int paramsSize = params.length;
		sb.append(FlickrApiConst.API_SEC);
		for (int i = 0; i < paramsSize; i++) {
			sb.append(params[i]);
		}
		
		String apiSigVal = "";
		try {
			apiSigVal = Common.md5(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return apiSigVal;
	}
	
	public void getToken(Context context) {
		try {
			String frob = Common.getDataFromSharedPreferences(context, KeyValueConst.FLICKR_FROB);
			HttpGet httpGet = new HttpGet(new URI(buildUrlParams(FlickrApiConst.BASE_URL, 
					FlickrApiConst.API_KEY_PARAM, FlickrApiConst.API_KEY, 
					FlickrApiConst.FORMAT_PARAM, FlickrApiConst.JSON_FORMAT_VALUE,
					FlickrApiConst.FROB_PARAM, frob, 
					FlickrApiConst.METHOD_PARAM, FlickrApiConst.GET_TOKEN_METHOD_VALUE)));
			RestAPITask task = new RestAPITask(context, BroadcastCallbackConst.GET_TOKEN_CALLBACK);
			task.execute(httpGet);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public void checkToken(Context context) {
		try {
			String token = Common.getDataFromSharedPreferences(context, KeyValueConst.FLICKR_TOKEN);
			HttpGet httpGet = new HttpGet(new URI(buildUrlParams(FlickrApiConst.BASE_URL,
					FlickrApiConst.API_KEY_PARAM, FlickrApiConst.API_KEY, 
					FlickrApiConst.AUTH_TOKEN_PARAM, token,
					FlickrApiConst.FORMAT_PARAM, FlickrApiConst.JSON_FORMAT_VALUE, 
					FlickrApiConst.METHOD_PARAM, FlickrApiConst.CHECK_TOKEN_METHOD_VALUE)));
			RestAPITask task = new RestAPITask(context, BroadcastCallbackConst.CHECK_TOKEN_CALLBACK);
			task.execute(httpGet);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public void getPhotosList(Context context) {
		try {
			String userId = Common.getDataFromSharedPreferences(context, KeyValueConst.FLICKR_USER_ID);
			HttpGet httpGet = new HttpGet(new URI(buildUrlParams(FlickrApiConst.BASE_URL, 
					FlickrApiConst.API_KEY_PARAM, FlickrApiConst.API_KEY, 
					FlickrApiConst.FORMAT_PARAM, FlickrApiConst.JSON_FORMAT_VALUE, 
					FlickrApiConst.METHOD_PARAM, FlickrApiConst.GET_PHOTOS_METHOD_VALUE,
					FlickrApiConst.USER_ID_PARAM, userId)));
			RestAPITask task = new RestAPITask(context, BroadcastCallbackConst.GET_PHOTOS_CALLBACK);
			task.execute(httpGet);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public void uploadPhoto(Context context, String photoPath) {
		try {
			String token = Common.getDataFromSharedPreferences(context, KeyValueConst.FLICKR_TOKEN);
			HttpPost httpPost = new HttpPost(new URI(FlickrApiConst.UPLOAD_URL));
			File file = new File(photoPath);
			FileBody fileBody = new FileBody(file);
			
			MultipartEntity multiPart = new MultipartEntity((HttpMultipartMode.BROWSER_COMPATIBLE));
			multiPart.addPart(FlickrApiConst.PHOTO_PARAM, fileBody);
			multiPart.addPart(FlickrApiConst.API_KEY_PARAM, new StringBody(FlickrApiConst.API_KEY));
			multiPart.addPart(FlickrApiConst.AUTH_TOKEN_PARAM, new StringBody(token));
			multiPart.addPart(FlickrApiConst.API_SIG_PARAM, 
	        		new StringBody(getApiSigKey(FlickrApiConst.API_KEY_PARAM, FlickrApiConst.API_KEY, 
						     FlickrApiConst.AUTH_TOKEN_PARAM, token)));
			
	        httpPost.setEntity(multiPart);
			RestAPITask task = new RestAPITask(context, BroadcastCallbackConst.UPLOAD_PHOTO_CALLBACK);
			task.execute(httpPost);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
}
