package com.nntuyen.yourflickr.domain.interactor.impl;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

import com.nntuyen.yourflickr.app.constant.RequestCodeConst;
import com.nntuyen.yourflickr.app.util.Common;
import com.nntuyen.yourflickr.domain.interactor.PhotoPickerInteractor;

public class PhotoPickerInteractorImpl implements PhotoPickerInteractor {
	
	private Context context;
	
	public PhotoPickerInteractorImpl(Context context) {
		this.context = context;
	}
	
	@Override
	public void fromDevice() {
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity)context).startActivityForResult(galleryIntent, RequestCodeConst.PICK_PHOTO);
	}

	@Override
	public String onResultFromDevice(int requestCode, int resultCode, Intent data) {
		Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String photoPath = cursor.getString(columnIndex);
        cursor.close();
        
        return photoPath;
	}

	@Override
	public void fromCamera() {
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		((Activity)context).startActivityForResult(intent, RequestCodeConst.CAPTURE_PHOTO);
	}

	@Override
	public String onResultFromCamera(int requestCode, int resultCode, Intent data) {
		Bitmap bitmap = (Bitmap) data.getExtras().get("data");
    	
    	ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    	bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = Images.Media.insertImage(context.getContentResolver(), bitmap, Common.generatePhotoName(), null);
        Uri tempUri = Uri.parse(path);
        
        Cursor cursor = context.getContentResolver().query(tempUri, null, null, null, null); 
        cursor.moveToFirst(); 
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
        String photoPath = cursor.getString(idx);
        cursor.close();
        
        return photoPath;
	}

}
