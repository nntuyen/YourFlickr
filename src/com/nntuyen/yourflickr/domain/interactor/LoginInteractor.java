package com.nntuyen.yourflickr.domain.interactor;

import android.content.Context;

import com.nntuyen.yourflickr.ui.listener.OnLoginFinishedListener;

public interface LoginInteractor {

	public void login(Context context, OnLoginFinishedListener listener);
}
