package de.canberkdemirkan.mediaboxmngr.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static DatabaseHelper sInstance;

	// database details
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "mediaboxmngr_db.db";

	// static method calling constructor - singleton
	public static DatabaseHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DatabaseHelper(context.getApplicationContext());
		}
		return sInstance;
	}

	// constructor
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// creating tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Constants.CREATE_TABLE_ITEMS);
		db.execSQL(Constants.CREATE_TABLE_USERS);
		db.execSQL(Constants.CREATE_TABLE_VERSION);
	}

	// upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_ITEMS);
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_USERS);
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_VERSION);
		onCreate(db);
	}

}