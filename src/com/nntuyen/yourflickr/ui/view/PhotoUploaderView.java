package com.nntuyen.yourflickr.ui.view;

public interface PhotoUploaderView {

	public void showUser(String username);
	public void showProgress();
    public void hideProgress();
    public void showMessage(String msg);
    public void navigateToGallery();
    public void enableUploadButton();
    public void disableUploadButton();
    public void changeLoginButtonText(String text);
    public void disableButtons();
    public void enableButtons();
    public void setPreviewPhoto(String photoPath);
}
