package com.nntuyen.yourflickr.domain.interactor.impl;

import android.content.Context;

import com.nntuyen.yourflickr.app.util.FlickrHelper;
import com.nntuyen.yourflickr.domain.interactor.LoginInteractor;
import com.nntuyen.yourflickr.ui.listener.OnLoginFinishedListener;

public class LoginInteractorImpl implements LoginInteractor {

	@Override
	public void login(Context context, OnLoginFinishedListener listener) {
		FlickrHelper.getInstance().getFrob(context);
	}

}
