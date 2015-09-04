package com.nntuyen.yourflickr.ui.presenter.impl;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.nntuyen.yourflickr.app.constant.BroadcastCallbackConst;
import com.nntuyen.yourflickr.app.constant.FlickrApiConst;
import com.nntuyen.yourflickr.app.constant.KeyValueConst;
import com.nntuyen.yourflickr.app.util.Common;
import com.nntuyen.yourflickr.app.util.FlickrHelper;
import com.nntuyen.yourflickr.domain.broadcast.GetPhotosListReceiver;
import com.nntuyen.yourflickr.domain.broadcast.ObservableObject;
import com.nntuyen.yourflickr.domain.model.Photo;
import com.nntuyen.yourflickr.ui.activity.PhotoViewerActivity;
import com.nntuyen.yourflickr.ui.presenter.PhotosListPresenter;
import com.nntuyen.yourflickr.ui.view.PhotosListView;

public class PhotosListPresenterImpl implements PhotosListPresenter, Observer {
	
	private static final String TAG = "PhotosListPresenterImpl";
	
	PhotosListView photosListView;
	Context context;
	private ObservableObject observableObject = ObservableObject.getInstance();
	private GetPhotosListReceiver getPhotosListReceiver;
	
	public PhotosListPresenterImpl(Context context, PhotosListView photosListView) {
		this.photosListView = photosListView;
		this.context = context;
		this.getPhotosListReceiver = new GetPhotosListReceiver();
	}
	
	@Override
	public void registerReceiver() {
		observableObject.deleteObservers();
		observableObject.addObserver(this);
		context.registerReceiver(getPhotosListReceiver, 
				new IntentFilter(BroadcastCallbackConst.GET_PHOTOS_CALLBACK));
	}

	@Override
	public void onGetPhotosList() {
		String userId = Common.getDataFromSharedPreferences(context, KeyValueConst.FLICKR_USER_ID);
		
		if (userId != null && !userId.isEmpty()) {
			photosListView.showProgress();
			FlickrHelper.getInstance().getPhotosList(context);
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		String msg = observableObject.getValue();
		Log.d(TAG, msg);
		
		if (msg.equalsIgnoreCase(FlickrApiConst.GET_PHOTOS_SUCCESS_MSG)) {
			photosListView.setPhotos(observableObject.getPhotos());
		} else {
			photosListView.showMessage(msg);
		}
		
		photosListView.hideProgress();
	}

	@Override
	public void onItemClick(int position) {
		Photo p = observableObject.getPhotos().get(position);
		
		Intent intent = new Intent(context, PhotoViewerActivity.class);
		intent.putExtra(KeyValueConst.PHOTO_URL, p.toString());
		context.startActivity(intent);
	}

}
