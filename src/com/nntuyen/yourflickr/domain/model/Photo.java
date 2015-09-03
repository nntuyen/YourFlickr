package com.nntuyen.yourflickr.domain.model;

public class Photo {

	private String farm;
	private String id;
	private String isPrimary;
	private String secret;
	private String server;
	private String title;

	public Photo() {
		
	}
	
	public String getFarm() {
		return farm;
	}
	
	public void setFarm(String farm) {
		this.farm = farm;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getIsPrimary() {
		return isPrimary;
	}
	
	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}
	
	public String getSecret() {
		return secret;
	}
	
	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	public String getServer() {
		return server;
	}
	
	public void setServer(String server) {
		this.server = server;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return "http://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret + "_b.jpg";
	}
	
}
