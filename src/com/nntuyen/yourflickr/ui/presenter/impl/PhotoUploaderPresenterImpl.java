package com.nntuyen.yourflickr.ui.presenter.impl;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.nntuyen.yourflickr.app.constant.BroadcastCallbackConst;
import com.nntuyen.yourflickr.app.constant.FlickrApiConst;
import com.nntuyen.yourflickr.app.constant.KeyValueConst;
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
		this.photoPickerInteractor = new PhotoPickerInteractorImpl();
		this.getFrobReceiver = new GetFrobReceiver();
		this.getTokenReceiver = new GetTokenReceiver();
		this.uploadPhotoReceiver = new UploadPhotoReceiver();
	}
	
	@Override
	public void registerReceiver() {
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
		
		if (msg.equalsIgnoreCase(FlickrApiConst.GET_FROB_SUCCESS_MSG)) {
			
		} else if (msg.equalsIgnoreCase(FlickrApiConst.GET_TOKEN_SUCCESS_MSG)) {
			photoUploaderView.showUser(ObservableObject.getInstance().getUsername());
			photoUploaderView.hideProgress();
			photoUploaderView.enableButtons();
			photoUploaderView.changeLoginButtonText("Log out");
			
			photoUploaderView.showMessage("Login successfully");
		} else {
			photoUploaderView.showMessage(msg);
		}
		
		if (msg.equalsIgnoreCase(FlickrApiConst.UPLOAD_PHOTO_FAIL_MSG) ||
			msg.equalsIgnoreCase(FlickrApiConst.UPLOAD_PHOTO_SUCCESS_MSG)) {
			photoUploaderView.hideProgress();
			photoUploaderView.enableUploadButton();
		}
	}

	@Override
	public void onResume(Bundle bundle) {
		if (bundle != null) {
			String msg = bundle.getString(KeyValueConst.AUTH_INTENT_MSG);
			
			if (msg.equalsIgnoreCase(FlickrApiConst.AUTH_SUCCESS_MSG)) {
				photoUploaderView.disableButtons();
				photoUploaderView.showProgress();
				FlickrHelper.getInstance().getToken(context);
			}
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
	public void pickPhoto(boolean fromDevice) {
		if (fromDevice) {
			photoPickerInteractor.fromDevice(context);
		} else {
			// From camera
		}
	}

	@Override
	public void uploadPhoto(String photoPath) {
		photoUploaderView.showProgress();
		FlickrHelper.getInstance().uploadPhoto(context, photoPath);
	}
	
}
