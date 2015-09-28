package com.wordpress.jftreading;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contact.db";
    private static final String TABLE_NAME = "updatecontact";

    public static final String CONTACT_COLUMN_ID = "id";
    public static final String CONTACT_COLUMN_URI = "uri";

    private ContactOpenHelper openHelper;
    private SQLiteDatabase database;

    public DBHelper(Context context) {
        openHelper = new ContactOpenHelper(context);
        database = openHelper.getWritableDatabase();
    }
    
    public void saveContactData(String uri) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(CONTACT_COLUMN_URI, uri);
		database.delete(TABLE_NAME, null, null);
		database.insert(TABLE_NAME, null, contentValues);
	}
	
	public Cursor getAllContactData() {
		return database.rawQuery(
		    "select * from " + TABLE_NAME,
		    null
		);
    }

    private class ContactOpenHelper extends SQLiteOpenHelper {
        ContactOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase database) {
            database.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + CONTACT_COLUMN_ID + " INTEGER PRIMARY KEY, "
                + CONTACT_COLUMN_URI + " TEXT )"
            );
        }

        public void onUpgrade(SQLiteDatabase database,
            int oldVersion, int newVersion) {
                database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(database);
        }
    }
}
