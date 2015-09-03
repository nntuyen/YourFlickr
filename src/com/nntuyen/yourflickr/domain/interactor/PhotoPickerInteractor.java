package com.nntuyen.yourflickr.domain.interactor;

import android.content.Context;
import android.content.Intent;

public interface PhotoPickerInteractor {

	public void fromDevice(Context context);
	public void onResultFromDevice(int requestCode, int resultCode, Intent data);
	public void fromCamera(Context context);
	public void onResultFromCamera(int requestCode, int resultCode, Intent data);
}
