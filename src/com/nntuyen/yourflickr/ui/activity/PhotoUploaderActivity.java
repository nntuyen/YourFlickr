package com.nntuyen.yourflickr.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nntuyen.yourflickr.R;
import com.nntuyen.yourflickr.app.constant.BroadcastCallbackConst;
import com.nntuyen.yourflickr.app.util.FlickrHelper;
import com.nntuyen.yourflickr.domain.broadcast.UploadPhotoReceiver;
import com.nntuyen.yourflickr.ui.presenter.PhotoUploaderPresenter;
import com.nntuyen.yourflickr.ui.presenter.impl.PhotoUploaderPresenterImpl;
import com.nntuyen.yourflickr.ui.view.PhotoUploaderView;

public class PhotoUploaderActivity extends Activity implements PhotoUploaderView, OnClickListener {
	
	private PhotoUploaderPresenter presenter;
	
	private Button btnUpload, btnLogin, btnPickPhoto, btnUseCamera, btnGoToGallery;
	private ProgressBar progressBar;
	private ImageView imgView;
	
	private static int RESULT_LOAD_IMG = 1;
	private String imgDecodableString = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_uploader);		
		
		initButton();
		progressBar = (ProgressBar)findViewById(R.id.progress);
		imgView = (ImageView) findViewById(R.id.ivPreview);
		
		presenter = new PhotoUploaderPresenterImpl(this, this);
		presenter.onCreated();
	}
	
	private void initButton() {
		btnUpload = (Button)findViewById(R.id.btnUpload);
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnPickPhoto = (Button)findViewById(R.id.btnPickPhoto);
		btnUseCamera = (Button)findViewById(R.id.btnUseCamera);
		btnGoToGallery = (Button)findViewById(R.id.btnGoToGallery);
		
		btnUpload.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		btnPickPhoto.setOnClickListener(this);
		btnUseCamera.setOnClickListener(this);
		btnGoToGallery.setOnClickListener(this);
	}
	UploadPhotoReceiver uploadPhoto = new UploadPhotoReceiver();
	@Override
	protected void onResume() {
		super.onResume();
		
		presenter.registerReceiver();
		registerReceiver(uploadPhoto, 
				new IntentFilter(BroadcastCallbackConst.UPLOAD_PHOTO_CALLBACK));
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
 
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
 
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                
                Toast.makeText(this, imgDecodableString,
                        Toast.LENGTH_LONG).show();
                showUploadButton();
            } else {
                Toast.makeText(this, "You haven't picked photo",
                        Toast.LENGTH_LONG).show();
            }
            
            // Camera
            if (requestCode == 0 && resultCode == RESULT_OK
                    && null != data) {
            	Bitmap bp = (Bitmap) data.getExtras().get("data");
            	imgView.setImageBitmap(bp);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error on picking photo", Toast.LENGTH_LONG)
                    .show();
        }
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
		getActionBar().setTitle(username);
	}

	@Override
	public void navigateToGallery() {
		startActivity(new Intent(this, PhotosListActivity.class));
        finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnUpload:
			FlickrHelper.getInstance().uploadPhoto(this, imgDecodableString);
			break;
		case R.id.btnLogin:
			presenter.login();
			break;
		case R.id.btnPickPhoto:
			/*Intent galleryIntent = new Intent(Intent.ACTION_PICK,
	                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);*/
			presenter.pickPhoto(true);
			break;
		case R.id.btnUseCamera:
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, 0);
			break;
		case R.id.btnGoToGallery:
			navigateToGallery();
			break;
		default:
			break;
		}
	}

	@Override
	public void showUploadButton() {
		btnUpload.setVisibility(View.VISIBLE);		
	}

	@Override
	public void hideUploadButton() {
		btnUpload.setVisibility(View.GONE);
	}

	@Override
	public void changeLoginButtonText(String text) {
		btnLogin.setText(text);
	}

	@Override
	public void disableButtons() {
		btnUpload.setEnabled(false);
		btnLogin.setEnabled(false);
		btnUseCamera.setEnabled(false);
		btnPickPhoto.setEnabled(false);
		btnGoToGallery.setEnabled(false);
	}

	@Override
	public void enableButtons() {
		btnUpload.setEnabled(true);
		btnLogin.setEnabled(true);
		btnUseCamera.setEnabled(true);
		btnPickPhoto.setEnabled(true);
		btnGoToGallery.setEnabled(true);
	}
}
