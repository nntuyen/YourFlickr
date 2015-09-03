package com.nntuyen.yourflickr.app.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.nntuyen.yourflickr.app.constant.FlickrApiConst;

public class Common {
	
	public static final String FLICKR_PREFS = "FLICKR_PREFS";

	public static String md5(String str) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes());

		byte byteData[] = md.digest();

		// convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}
	
	public static String getFlickrJson(String response) {
		response = response.replaceFirst(FlickrApiConst.FLICKR_CALLBACK, "");
		response = response.substring(1, response.length() - 1);
		
		return response;
	}
	
	public static void saveDataToSharedPreferences(Context context, String key, String value) {
		SharedPreferences settings;
	    Editor editor;
	    settings = context.getSharedPreferences(FLICKR_PREFS, Context.MODE_PRIVATE);
	    editor = settings.edit();
	 
	    editor.putString(key, value);
	    editor.commit();
	}
	
	public static String getDataFromSharedPreferences(Context context, String key) {
		SharedPreferences settings;
		String value;
	    settings = context.getSharedPreferences(FLICKR_PREFS, Context.MODE_PRIVATE);
	    value = settings.getString(key, null);
	    
	    return value;
	}
	
	public static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size=1024;
        
        try {
            byte[] bytes = new byte[buffer_size];
            for(;;) {
              int count = is.read(bytes, 0, buffer_size);
              if (count == -1) {
                  break;
              }
              
              os.write(bytes, 0, count);
            }
        } catch(Exception ex){
        	ex.printStackTrace();
        }
    }
	
	public static String generatePhotoName() {
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH));
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String minute = String.valueOf(c.get(Calendar.MINUTE));
		String second = String.valueOf(c.get(Calendar.SECOND));
		
		return "YourFlick_" + year + month + day + hour + minute + second;
	}
}
