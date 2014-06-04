package de.canberkdemirkan.mediaboxmngr.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static DatabaseHelper sInstance;

	// database details
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "mediaboxmngr_db.db";
	
	// database table and column names
	public static final String TABLE_ITEMS = "items";
	public static final String ITEM_ID = "_id";
	public static final String ITEM_TITLE = "title";
	
	// SQL statement creating table items
	public static final String CREATE_TABLE_ITEMS = "CREATE TABLE "
			+ TABLE_ITEMS + "("
			+ ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ ITEM_TITLE + " TEXT"
			+ ")";

	public static DatabaseHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DatabaseHelper(context.getApplicationContext());
		}
		return sInstance;
	}
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// creating tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_ITEMS);
	}

	// upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
		onCreate(db);
	}

}