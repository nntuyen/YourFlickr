package com.nntuyen.yourflickr.ui.view;

public interface PhotoUploaderView {

	public void showUser(String username);
	public void showProgress();
    public void hideProgress();
    public void showMessage(String msg);
    public void navigateToGallery();
}
