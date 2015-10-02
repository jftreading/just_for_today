package com.wordpress.jftreading.db;

import android.content.Context;

import java.util.List;

import com.wordpress.jftreading.contact.Contact;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper implements CRUDOperations {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contact.db";
    private static final String TABLE_NAME = "contactdata";

    private static final String CONTACT_COLUMN_ID = "id";
    private static final String CONTACT_COLUMN_URI = "uri";
    private static final String CONTACT_COLUMN_NAME = "name";
    private static final String CONTACT_COLUMN_NUMBER = "number";
    //private static final String CONTACT_COLUMN_PHOTO = "photo";

    private ContactOpenHelper openHelper;
    private SQLiteDatabase database;

    public DBHelper(Context context) {
        openHelper = new ContactOpenHelper(context);
        database = openHelper.getWritableDatabase();
    }

	@Override
	public void addContactData(Contact contact) {
		Log.d("dbHelper", "Add contact data values.");
		ContentValues contentValues = new ContentValues();
		contentValues.put(CONTACT_COLUMN_URI, contact.getUri());
		contentValues.put(CONTACT_COLUMN_NAME, contact.getName());
		contentValues.put(CONTACT_COLUMN_NUMBER, contact.getNumber());
		database.insert(TABLE_NAME, null, contentValues);		
	}

	@Override
	public Contact getContactData(int id) {		
		Cursor cursor = database.query(TABLE_NAME, new String[] { CONTACT_COLUMN_ID,
				CONTACT_COLUMN_URI, CONTACT_COLUMN_NAME, CONTACT_COLUMN_NUMBER },
				CONTACT_COLUMN_ID + "=?", new String[] { String.valueOf(id) },
				null, null, null, null);		
		if (cursor != null)
			cursor.moveToFirst();
		Contact contact = new Contact(id, cursor.getString(1),
				cursor.getString(2), cursor.getString(3), null);
		if (!cursor.isClosed()) {
			cursor.close();
		}
		return contact;
	}

	@Override
	public List<Contact> getAllContactData() {
		return null;
	}

	@Override
	public int getContactDataCount() {		
		Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
	    int count = cursor.getCount();
		cursor.close();
		return count;
	}

	@Override
	public int updateContactData(Contact contact) {
		return 0;
	}

	@Override
	public void deleteContactData(Contact contact) {	
	}
	
	@Override
	public void deleteAllContactData() {
		database.delete(TABLE_NAME, null, null);	
	}
		
	private class ContactOpenHelper extends SQLiteOpenHelper {
        ContactOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase database) {
            database.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + CONTACT_COLUMN_ID + " INTEGER PRIMARY KEY, "
                + CONTACT_COLUMN_URI + " TEXT, "
                + CONTACT_COLUMN_NAME + " TEXT, "
                + CONTACT_COLUMN_NUMBER + " TEXT )"
            );
        }

        public void onUpgrade(SQLiteDatabase database,
            int oldVersion, int newVersion) {
                database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(database);
        }
    }
}
