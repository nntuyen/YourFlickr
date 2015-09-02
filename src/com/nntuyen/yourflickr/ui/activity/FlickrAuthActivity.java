package com.nntuyen.yourflickr.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nntuyen.yourflickr.R;
import com.nntuyen.yourflickr.app.constant.FlickrApiConst;
import com.nntuyen.yourflickr.app.constant.KeyValueConst;

public class FlickrAuthActivity extends Activity {
	
	private static final String TAG = "FlickrAuthActivity";
	
	WebView browser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flickr_auth);
		getActionBar().hide();
		
		browser = (WebView)findViewById(R.id.browser);
		browser.setWebViewClient(new FlickrBrowser());
		browser.loadUrl(getUrl());
	}
	
	private String getUrl() {
		String url = "";
		
		Bundle bundle = getIntent().getExtras();
		
		if (bundle != null) {
			url = bundle.getString(KeyValueConst.FULL_AUTH_URL);
		}
		
		return url;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.flickr_auth, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class FlickrBrowser extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			Log.d(TAG, "Start " + url);
			
			if (url.equalsIgnoreCase(FlickrApiConst.AUTH_PATH)) {
				Log.d(TAG, "Send " + FlickrApiConst.AUTH_SUCCESS_MSG + " to PhotoUploaderActivity");
				Intent intent = new Intent(FlickrAuthActivity.this, PhotoUploaderActivity.class);
				intent.putExtra(KeyValueConst.AUTH_INTENT_MSG, FlickrApiConst.AUTH_SUCCESS_MSG);
				startActivity(intent);
			}
		}
	}
	
}
