package com.nntuyen.yourflickr.ui.view;

import java.util.List;

import com.nntuyen.yourflickr.domain.model.Photo;

public interface PhotosListView {

	public void setPhotos(List<Photo> photos);
	public void showMessage(String msg);
	public void showProgress();
	public void hideProgress();
}
