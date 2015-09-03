package com.nntuyen.yourflickr.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nntuyen.yourflickr.R;
import com.nntuyen.yourflickr.app.constant.RequestCodeConst;
import com.nntuyen.yourflickr.domain.broadcast.ObservableObject;
import com.nntuyen.yourflickr.ui.presenter.PhotoUploaderPresenter;
import com.nntuyen.yourflickr.ui.presenter.impl.PhotoUploaderPresenterImpl;
import com.nntuyen.yourflickr.ui.view.PhotoUploaderView;

public class PhotoUploaderActivity extends Activity implements PhotoUploaderView, OnClickListener {
	
	private PhotoUploaderPresenter presenter;
	
	private Button btnUpload, btnLogin, btnPickPhoto, btnUseCamera, btnGoToGallery;
	private ProgressBar progressBar;
	private ImageView ivPreview;
	
	private String photoPath = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_uploader);		
		
		initButton();
		progressBar = (ProgressBar)findViewById(R.id.progress);
		ivPreview = (ImageView) findViewById(R.id.ivPreview);
		
		presenter = new PhotoUploaderPresenterImpl(this, this);
		presenter.onCreated();
		presenter.registerReceiver();
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
	
	@Override
	protected void onResume() {
		super.onResume();
		
		
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
		
		photoPath = presenter.onPhotoPickerResult(requestCode, resultCode, data);
		/*try {
            if (requestCode == RequestCodeConst.PICK_PHOTO && resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
 
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                
                Toast.makeText(this, imgDecodableString,
                        Toast.LENGTH_LONG).show();
                enableUploadButton();
            } else {
                Toast.makeText(this, "You haven't picked photo", Toast.LENGTH_LONG).show();
            }
            
            // Camera
            if (requestCode == RequestCodeConst.CAPTURE_PHOTO && resultCode == RESULT_OK && data != null) {
            	Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            	imgView.setImageBitmap(bitmap);
            	
            	ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            	bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = Images.Media.insertImage(this.getContentResolver(), bitmap, Common.generatePhotoName(), null);
                Uri tempUri = Uri.parse(path);
                
                Cursor cursor = getContentResolver().query(tempUri, null, null, null, null); 
                cursor.moveToFirst(); 
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
                imgDecodableString = cursor.getString(idx); 
                
                Log.d("CAMERA", imgDecodableString);
                Toast.makeText(this, imgDecodableString, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error on picking photo", Toast.LENGTH_LONG)
                    .show();
        }*/
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
		if (ObservableObject.getInstance().isAuthenticated()) {
			startActivity(new Intent(this, PhotosListActivity.class));
		} else {
			showMessage("Please login to access your gallery");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnUpload:
			//FlickrHelper.getInstance().uploadPhoto(this, imgDecodableString);
			presenter.uploadPhoto(photoPath);
			break;
		case R.id.btnLogin:
			presenter.login();
			break;
		case R.id.btnPickPhoto:
			presenter.pickPhoto(RequestCodeConst.PICK_PHOTO);
			break;
		case R.id.btnUseCamera:
			//Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			//startActivityForResult(intent, RequestCodeConst.CAPTURE_PHOTO);
			presenter.pickPhoto(RequestCodeConst.CAPTURE_PHOTO);
			break;
		case R.id.btnGoToGallery:
			navigateToGallery();
			break;
		default:
			break;
		}
	}

	@Override
	public void enableUploadButton() {
		btnUpload.setEnabled(true);		
	}

	@Override
	public void disableUploadButton() {
		btnUpload.setEnabled(false);
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

	@Override
	public void setPreviewPhoto(String photoPath) {
		ivPreview.setImageBitmap(BitmapFactory.decodeFile(photoPath));
	}
}
