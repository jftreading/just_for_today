package com.wordpress.jftreading.contact;

import android.graphics.Bitmap;

public class Contact {
	private int id;
	private String uri;
	private String name;
	private String number; 
	private Bitmap photo;

	public Contact(int id, String uri, String name, String number, Bitmap photo) {
		this.id = id;
		this.uri = uri;
		this.name = name;
		this.number = number;
		this.photo = photo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}
}
