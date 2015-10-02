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
