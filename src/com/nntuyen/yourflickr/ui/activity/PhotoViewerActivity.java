package com.nntuyen.yourflickr.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.nntuyen.yourflickr.R;
import com.nntuyen.yourflickr.app.constant.KeyValueConst;
import com.nntuyen.yourflickr.app.util.ImageLoader;
import com.nntuyen.yourflickr.ui.custom.TouchImageView;

public class PhotoViewerActivity extends Activity {

	TouchImageView ivPhoto;
	ImageLoader imageLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_viewer);
		
		ivPhoto = (TouchImageView)findViewById(R.id.ivTouchPhoto);
		imageLoader = new ImageLoader(this);
		
		Intent intent = getIntent();
		String url = intent.getStringExtra(KeyValueConst.PHOTO_URL);
		
		imageLoader.DisplayImage(url, R.drawable.ic_launcher, ivPhoto);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_viewer, menu);
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
}
