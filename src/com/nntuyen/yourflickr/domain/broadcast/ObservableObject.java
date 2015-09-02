package com.nntuyen.yourflickr.domain.broadcast;

import java.util.List;
import java.util.Observable;

import com.nntuyen.yourflickr.domain.model.Photo;

public class ObservableObject extends Observable {

	private String value;
	private List<Photo> photos;

	private static ObservableObject instance = new ObservableObject();

    public static ObservableObject getInstance() {
       return instance;
    }

    private ObservableObject() {
    }

    public void updateValue(String newValue) {
       value = newValue;

       synchronized (this) {
          setChanged();
          notifyObservers();
       }
    }

    public String getValue() {
       return value;
    }
    
    public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}
}
