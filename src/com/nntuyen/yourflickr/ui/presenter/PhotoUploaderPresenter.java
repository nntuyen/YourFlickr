package com.nntuyen.yourflickr.ui.presenter;

import android.content.Intent;
import android.os.Bundle;

public interface PhotoUploaderPresenter {

	public void login();
	public void registerReceiver();
	public void onResume(Bundle bundle);
	public void onCreated();
	public void pickPhoto(int requestCode);
	public void uploadPhoto(String photoPath);
	public String onPhotoPickerResult(int requestCode, int resultCode, Intent data);
}
