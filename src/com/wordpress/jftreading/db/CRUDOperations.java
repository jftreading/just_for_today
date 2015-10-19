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
package com.wordpress.jftreading.db;

import java.util.List;

import com.wordpress.jftreading.contact.Contact;

public interface CRUDOperations {
	public void addContactData(Contact contact);
	public Contact getContactData(int id);
	public List<Contact> getAllContactData();
	public int getContactDataCount();
	public int updateContactData(Contact contact);
	public void deleteContactData(Contact contact);
	public void deleteAllContactData();
}
