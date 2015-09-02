package com.nntuyen.yourflickr.ui.presenter;

public interface PhotosListPresenter {

	public void onGetPhotosList();
	public void registerReceiver();
	public void onItemClick(int position);
}
