/*******************************************************************************
 * Copyright (c) 2015 jftreading.wordpress.com.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     jftreading.wordpress.com - email: machinehead449@gmail.com
 ******************************************************************************/
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
