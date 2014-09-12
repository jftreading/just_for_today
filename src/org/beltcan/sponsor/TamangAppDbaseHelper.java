package org.beltcan.sponsor;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TamangAppDbaseHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tamangapp.db";
    private static final String TABLE_NAME = "updatesponsor";

    public static final String TAMANGAPP_COLUMN_ID = "id";
    public static final String TAMANGAPP_COLUMN_URI = "uri";

    private TamangAppOpenHelper openHelper;
    private SQLiteDatabase database;

    public TamangAppDbaseHelper(Context context) {
        openHelper = new TamangAppOpenHelper(context);
        database = openHelper.getWritableDatabase();
    }
    
    public void saveSponsorData(String uri) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(TAMANGAPP_COLUMN_URI, uri);
		database.insert(TABLE_NAME, null, contentValues);
	}
	
	public Cursor getAllSponsorData() {
		return database.rawQuery(
		    "select * from " + TABLE_NAME,
		    null
		);
    }

    private class TamangAppOpenHelper extends SQLiteOpenHelper {
        TamangAppOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase database) {
            database.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + TAMANGAPP_COLUMN_ID + " INTEGER PRIMARY KEY, "
                + TAMANGAPP_COLUMN_URI + " TEXT )"
            );
        }

        public void onUpgrade(SQLiteDatabase database,
            int oldVersion, int newVersion) {
                database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(database);
        }
    }
}
