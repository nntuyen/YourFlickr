package com.nntuyen.yourflickr.domain.interactor.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.nntuyen.yourflickr.domain.interactor.PhotoPickerInteractor;

public class PhotoPickerInteractorImpl implements PhotoPickerInteractor {
	
	private static int RESULT_LOAD_IMG = 1;

	@Override
	public void fromDevice(Context context) {
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity)context).startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
	}

	@Override
	public void onResultFromDevice(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fromCamera(Context context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResultFromCamera(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
	}

}
