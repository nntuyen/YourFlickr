package com.nntuyen.yourflickr.ui.presenter.impl;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.nntuyen.yourflickr.app.constant.BroadcastCallbackConst;
import com.nntuyen.yourflickr.app.constant.FlickrApiConst;
import com.nntuyen.yourflickr.app.constant.KeyValueConst;
import com.nntuyen.yourflickr.app.constant.RequestCodeConst;
import com.nntuyen.yourflickr.app.util.Common;
import com.nntuyen.yourflickr.app.util.FlickrHelper;
import com.nntuyen.yourflickr.domain.broadcast.GetFrobReceiver;
import com.nntuyen.yourflickr.domain.broadcast.GetTokenReceiver;
import com.nntuyen.yourflickr.domain.broadcast.ObservableObject;
import com.nntuyen.yourflickr.domain.broadcast.UploadPhotoReceiver;
import com.nntuyen.yourflickr.domain.interactor.LoginInteractor;
import com.nntuyen.yourflickr.domain.interactor.PhotoPickerInteractor;
import com.nntuyen.yourflickr.domain.interactor.impl.LoginInteractorImpl;
import com.nntuyen.yourflickr.domain.interactor.impl.PhotoPickerInteractorImpl;
import com.nntuyen.yourflickr.ui.listener.OnLoginFinishedListener;
import com.nntuyen.yourflickr.ui.presenter.PhotoUploaderPresenter;
import com.nntuyen.yourflickr.ui.view.PhotoUploaderView;

public class PhotoUploaderPresenterImpl implements PhotoUploaderPresenter, OnLoginFinishedListener, Observer {
	
	private static final String TAG = "PhotoUploaderPresenterImpl";
	
	private Context context;
	private PhotoUploaderView photoUploaderView;
	private LoginInteractor loginInteractor;
	private PhotoPickerInteractor photoPickerInteractor;
	private GetFrobReceiver getFrobReceiver;
	private GetTokenReceiver getTokenReceiver;
	private UploadPhotoReceiver uploadPhotoReceiver;
	private ObservableObject observableObject = ObservableObject.getInstance();
	
	public PhotoUploaderPresenterImpl(Context context, PhotoUploaderView photoUploaderView) {
		this.context = context;
		this.photoUploaderView = photoUploaderView;
		this.loginInteractor = new LoginInteractorImpl();
		this.photoPickerInteractor = new PhotoPickerInteractorImpl(context);
		this.getFrobReceiver = new GetFrobReceiver();
		this.getTokenReceiver = new GetTokenReceiver();
		this.uploadPhotoReceiver = new UploadPhotoReceiver();
	}
	
	@Override
	public void registerReceiver() {
		observableObject.deleteObservers();
		observableObject.addObserver(this);
		context.registerReceiver(getFrobReceiver, 
				new IntentFilter(BroadcastCallbackConst.GET_FROB_CALLBACK));
		context.registerReceiver(getTokenReceiver, 
				new IntentFilter(BroadcastCallbackConst.GET_TOKEN_CALLBACK));
		context.registerReceiver(uploadPhotoReceiver, 
				new IntentFilter(BroadcastCallbackConst.UPLOAD_PHOTO_CALLBACK));
	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void login() {
		Log.d(TAG, "Login from presenter");
		
		if (!ObservableObject.getInstance().isAuthenticated()) {
			loginInteractor.login(context, this);
			photoUploaderView.showProgress();
			photoUploaderView.disableButtons();
		} else {
			Common.saveDataToSharedPreferences(context, KeyValueConst.FLICKR_TOKEN, "");
			Common.saveDataToSharedPreferences(context, KeyValueConst.FLICKR_USER_ID, "");
			Common.saveDataToSharedPreferences(context, KeyValueConst.FLICKR_USERNAME, "");
			Common.saveDataToSharedPreferences(context, KeyValueConst.FLICKR_FULLNAME, "");
			ObservableObject.getInstance().setAuthenticated(false);
			ObservableObject.getInstance().setUsername("");
			
			photoUploaderView.showUser("Your Flickr");
			photoUploaderView.changeLoginButtonText("Login");
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		String msg = observableObject.getValue();
		Log.d(TAG, msg);
		
		if (msg.equalsIgnoreCase(FlickrApiConst.GET_TOKEN_SUCCESS_MSG)) {
			photoUploaderView.showUser(ObservableObject.getInstance().getUsername());
			photoUploaderView.changeLoginButtonText("Log out");
			photoUploaderView.showMessage("Login successfully");
		} else {
			photoUploaderView.showMessage(msg);
		}
		
		photoUploaderView.hideProgress();
		photoUploaderView.enableButtons();
	}

	@Override
	public void onResume(Bundle bundle) {
		if (bundle != null) {
			String msg = bundle.getString(KeyValueConst.AUTH_INTENT_MSG);
			
			if (msg.equalsIgnoreCase(FlickrApiConst.AUTH_SUCCESS_MSG) && !observableObject.isAuthenticated()) {
				photoUploaderView.disableButtons();
				photoUploaderView.showProgress();
				FlickrHelper.getInstance().getToken(context);
			}
			bundle.clear();
		}
		
		if (ObservableObject.getInstance().isAuthenticated()) {
			photoUploaderView.showUser(ObservableObject.getInstance().getUsername());
		}
	}

	@Override
	public void onCreated() {
		String username = Common.getDataFromSharedPreferences(context, KeyValueConst.FLICKR_USERNAME);
		ObservableObject.getInstance().setUsername(username);
		
		if (username != null && !username.isEmpty()) {
			ObservableObject.getInstance().setAuthenticated(true);
			photoUploaderView.changeLoginButtonText("Log out");
		} else {
			ObservableObject.getInstance().setAuthenticated(false);
			photoUploaderView.changeLoginButtonText("Login");
		}
	}

	@Override
	public void pickPhoto(int requestCode) {
		if (requestCode == RequestCodeConst.PICK_PHOTO) {
			photoPickerInteractor.fromDevice();
		} else {
			photoPickerInteractor.fromCamera();
		}
	}

	@Override
	public void uploadPhoto(String photoPath) {
		if (photoPath == null || photoPath.isEmpty()) {
			photoUploaderView.showMessage("Please pick a photo");
		} else if (!observableObject.isAuthenticated()) {
			photoUploaderView.showMessage("Please login");
		} else {
			photoUploaderView.showProgress();
			FlickrHelper.getInstance().uploadPhoto(context, photoPath);
		}
	}

	@Override
	public String onPhotoPickerResult(int requestCode, int resultCode, Intent data) {
		String photoPath = "";
		
		if (requestCode == RequestCodeConst.PICK_PHOTO && data != null && resultCode == Activity.RESULT_OK) {
			photoPath = photoPickerInteractor.onResultFromDevice(requestCode, resultCode, data);
		} else {
			photoPath = photoPickerInteractor.onResultFromCamera(requestCode, resultCode, data);
		}
		
		photoUploaderView.setPreviewPhoto(photoPath);
		
		return photoPath;
	}
	
}
