package com.nntuyen.yourflickr.domain.interactor;

import android.content.Intent;

public interface PhotoPickerInteractor {

	public void fromDevice();
	public String onResultFromDevice(int requestCode, int resultCode, Intent data);
	public void fromCamera();
	public String onResultFromCamera(int requestCode, int resultCode, Intent data);
}
