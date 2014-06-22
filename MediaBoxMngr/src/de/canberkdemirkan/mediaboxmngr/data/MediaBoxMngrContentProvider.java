package de.canberkdemirkan.mediaboxmngr.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;

public class MediaBoxMngrContentProvider extends ContentProvider {

	public static final Uri CONTENT_URI = Uri
			.parse("content://de.canberkdemirkan.provider.mediaboxmngr/elements");

	private static final int ALL_ROWS = 1;
	private static final int SINGLE_ROW = 2;

	private static final UriMatcher sURIMatcher;

	private DatabaseHelper mDBHelper;

	static {
		sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sURIMatcher.addURI("de.canberkdemirkan.provider.mediaboxmngr",
				"elements", ALL_ROWS);
		sURIMatcher.addURI("de.canberkdemirkan.provider.mediaboxmngr",
				"elements/#", SINGLE_ROW);
	}

	@Override
	public boolean onCreate() {
		mDBHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// Open the database.
		SQLiteDatabase db;
		try {
			db = mDBHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = mDBHelper.getReadableDatabase();
		}
		// Replace these with valid SQL statements if necessary.
		String groupBy = null;
		String having = null;

		// Use an SQLite Query Builder to simplify constructing the
		// database query.
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		// If this is a row query, limit the result set to the passed in row.
		switch (sURIMatcher.match(uri)) {
		case SINGLE_ROW:
			String rowID = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(Constants.ID + "=" + rowID);
		default:
			break;
		}
		// Specify the table on which to perform the query. This can
		// be a specific table or a join as required.
		queryBuilder.setTables(Constants.TABLE_ITEMS);
		// Execute the query.
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, groupBy, having, sortOrder);
		// Return the result Cursor.
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		// Return a string that identifies the MIME type
		// for a Content Provider URI
		switch (sURIMatcher.match(uri)) {
		case ALL_ROWS:
			return "vnd.android.cursor.dir/vnd.canberkdemirkan.elemental";
		case SINGLE_ROW:
			return "vnd.android.cursor.item/vnd.canberkdemirkan.elemental";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// Open a read / write database to support the transaction.
		SQLiteDatabase db = mDBHelper.getWritableDatabase();

		// To add empty rows to your database by passing in an empty
		// Content Values object you must use the null column hack
		// parameter to specify the name of the column that can be
		// set to null.
		String nullColumnHack = null;

		// Insert the values into the table
		long id = db.insert(Constants.TABLE_ITEMS, nullColumnHack, values);

		// Construct and return the URI of the newly inserted row.
		if (id > -1) {
			// Construct and return the URI of the newly inserted row.
			Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);
			// Notify any observers of the change in the data set.
			getContext().getContentResolver().notifyChange(insertedId, null);

			return insertedId;
		} else
			return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Open a read / write database to support the transaction.
		SQLiteDatabase db = mDBHelper.getWritableDatabase();

		// If this is a row URI, limit the deletion to the specified row.
		switch (sURIMatcher.match(uri)) {
		case SINGLE_ROW:
			String rowID = uri.getPathSegments().get(1);
			selection = Constants.ID
					+ "="
					+ rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
		default:
			break;
		}

		// To return the number of deleted items you must specify a where
		// clause. To delete all rows and return a value pass in “1”.
		if (selection == null)
			selection = "1";
		// Perform the deletion.
		int deleteCount = db.delete(Constants.TABLE_ITEMS, selection,
				selectionArgs);

		// Notify any observers of the change in the data set.
		getContext().getContentResolver().notifyChange(uri, null);

		// Return the number of deleted items.
		return deleteCount;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		// Open a read / write database to support the transaction.
		SQLiteDatabase db = mDBHelper.getWritableDatabase();

		// If this is a row URI, limit the deletion to the specified row.
		switch (sURIMatcher.match(uri)) {
		case SINGLE_ROW:
			String rowID = uri.getPathSegments().get(1);
			selection = Constants.ID
					+ "="
					+ rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
		default:
			break;
		}
		// Perform the update.
		int updateCount = db.update(Constants.TABLE_ITEMS, values, selection,
				selectionArgs);

		// Notify any observers of the change in the data set.
		getContext().getContentResolver().notifyChange(uri, null);

		return updateCount;
	}

}