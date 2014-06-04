package de.canberkdemirkan.mediaboxmngr.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import de.canberkdemirkan.mediaboxmngr.model.Item;

public class DAOItem {

	private Context mAppContext;

	private SQLiteDatabase mSQLiteDB;
	private DatabaseHelper mDBHelper;

	// constructor
	public DAOItem(Context context) {
		this.mAppContext = context;
		mDBHelper = DatabaseHelper.getInstance(mAppContext);
		open();
	}

	public void open() throws SQLiteException {
		mSQLiteDB = mDBHelper.getWritableDatabase();
	}

	public void close() {
		mSQLiteDB.close();
	}

	// insert new item
	public Item insertItem(Item item) {
		open();
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.ITEM_TITLE, item.getTitle());
		long id = mSQLiteDB.insert(DatabaseHelper.TABLE_ITEMS, null, values);
		close();
		item.setId(id);
		return item;
	}

	// update existing item
	public boolean updateItem(Item item) {
		open();
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.ITEM_TITLE, item.getTitle());
		boolean result = mSQLiteDB.update(DatabaseHelper.TABLE_ITEMS, values,
				DatabaseHelper.ITEM_ID + " = " + item.getId(), null) > 0;
		close();
		return result;
	}

	// delete existing item
	public boolean deleteItem(Item item) {
		open();
		boolean result = mSQLiteDB.delete(DatabaseHelper.TABLE_ITEMS,
				DatabaseHelper.ITEM_ID + " = " + item.getId(), null) > 0;
		close();
		return result;
	}

	// get all items as a list
	public ArrayList<Item> getAllItems() {
		ArrayList<Item> itemList = new ArrayList<Item>();
		Cursor cursor = null;
		// String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ITEMS
		// + " WHERE " + DatabaseHelper.USER + "=" + '"' + user + '"';
		String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ITEMS;

		try {
			cursor = mSQLiteDB.rawQuery(selectQuery, null);

			if (cursor != null) {

				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					Item item = new Item();

					int iRow = cursor.getColumnIndex(DatabaseHelper.ITEM_ID);
					int iTitle = cursor.getColumnIndex(DatabaseHelper.ITEM_TITLE);

					long id = Long.parseLong(cursor.getString(iRow));
					String title = cursor.getString(iTitle);

					item.setId(id);
					item.setTitle(title);

					itemList.add(0, item);
				}
			}
		} catch (Exception e) {
			itemList = new ArrayList<Item>();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return itemList;
	}

}