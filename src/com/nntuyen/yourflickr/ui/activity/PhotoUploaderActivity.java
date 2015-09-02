package com.nntuyen.yourflickr.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nntuyen.yourflickr.R;
import com.nntuyen.yourflickr.ui.presenter.PhotoUploaderPresenter;
import com.nntuyen.yourflickr.ui.presenter.impl.PhotoUploaderPresenterImpl;
import com.nntuyen.yourflickr.ui.view.PhotoUploaderView;

public class PhotoUploaderActivity extends Activity implements PhotoUploaderView, OnClickListener {
	
	PhotoUploaderPresenter presenter;
	
	Button btnLogin, btnPickPhoto, btnUseCamera, btnGoToGallery;
	TextView tvWelcome;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_uploader);
		
		presenter = new PhotoUploaderPresenterImpl(this, this);
		
		initButton();
		tvWelcome = (TextView)findViewById(R.id.tvWelcome);
		progressBar = (ProgressBar)findViewById(R.id.progress);
	}
	
	private void initButton() {
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnPickPhoto = (Button)findViewById(R.id.btnPickPhoto);
		btnUseCamera = (Button)findViewById(R.id.btnUseCamera);
		btnGoToGallery = (Button)findViewById(R.id.btnGoToGallery);
		
		btnLogin.setOnClickListener(this);
		btnPickPhoto.setOnClickListener(this);
		btnUseCamera.setOnClickListener(this);
		btnGoToGallery.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		presenter.registerReceiver();
		presenter.onResume(getIntent().getExtras());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_uploader, menu);
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

	@Override
	public void showProgress() {
		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgress() {
		progressBar.setVisibility(View.GONE);
	}

	@Override
	public void showMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void showUser(String username) {
		tvWelcome.setText("Welcome " + username);
	}

	@Override
	public void navigateToGallery() {
		startActivity(new Intent(this, PhotosListActivity.class));
        finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			presenter.login();
			break;
		case R.id.btnPickPhoto:
			break;
		case R.id.btnUseCamera:
			break;
		case R.id.btnGoToGallery:
			navigateToGallery();
			break;
		default:
			break;
		}
	}
}
