package com.nntuyen.yourflickr.ui.presenter;

import android.os.Bundle;

public interface PhotoUploaderPresenter {

	public void login();
	public void registerReceiver();
	public void onResume(Bundle bundle);
	public void onCreated();
	public void pickPhoto(boolean fromDevice); // true for from device, false for from camera
}
