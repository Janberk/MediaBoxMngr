package de.canberkdemirkan.mediaboxmngr.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.MusicAlbum;
import de.canberkdemirkan.mediaboxmngr.util.ItemType;
import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class DAOItem {

	public static int colId;
	public static int colSQLiteId;
	public static int colUser;
	public static int colSynced;
	public static int colTitle;
	public static int colType;
	public static int colCover;
	public static int colGenre;
	public static int colFavorite;
	public static int colCreationDate;
	public static int colDeleted;
	public static int colDeletionDate;
	public static int colInPossession;
	public static int colOriginalTitle;
	public static int colCountry;
	public static int colYearPublished;
	public static int colContent;
	public static int colRating;
	public static int colProducer;
	public static int colDirector;
	public static int colScript;
	public static int colActors;
	public static int colMusic;
	public static int colLength;
	public static int colLabel;
	public static int colStudio;
	public static int colArtist;
	public static int colFormat;
	public static int colTitleCount;
	public static int colEdition;
	public static int colPublishingHouse;
	public static int colAuthor;
	public static int colIsbn;

	private Context mAppContext;
	private SQLiteDatabase mSQLiteDB;
	private DatabaseHelper mDBHelper;

	// constructor
	public DAOItem(Context context) {
		this.mAppContext = context;
		mDBHelper = DatabaseHelper.getInstance(mAppContext);
		open();
	}

	// open writable database
	public void open() throws SQLiteException {
		mSQLiteDB = mDBHelper.getWritableDatabase();
	}

	// close database
	public void close() {
		mSQLiteDB.close();
	}

	// insert new item
	public Item insertItem(Item item) {
		open();
		ContentValues values = new ContentValues();
		putValues(item, values);
		values.put(ProjectConstants.CREATION_DATE, UtilMethods
				.dateToFormattedStringConverter(item.getCreationDate()));
		long id = mSQLiteDB.insert(ProjectConstants.TABLE_ITEMS, null, values);
		close();
		item.setId(id);
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG,
					"DAOItem - insertItem(): \n" + item.toString());
		}
		return item;
	}

	// update existing item
	public boolean updateItem(Item item) {
		open();
		ContentValues values = new ContentValues();
		putValues(item, values);
		boolean result = mSQLiteDB.update(ProjectConstants.TABLE_ITEMS, values,
				ProjectConstants.ID + " = " + item.getId(), null) > 0;
		close();
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "DAOItem - updateItem(): " + result);
		}
		return result;
	}

	// get items by specified type
	public ArrayList<Item> getItemsByType(ItemType type, String user) {
		ArrayList<Item> pre = getAllItems(user);
		ArrayList<Item> result = new ArrayList<Item>();
		ItemType fetchedType = null;

		for (Item item : pre) {
			fetchedType = item.getType();
			if (fetchedType == type) {
				// TODO evtl. add(0, item);
				result.add(item);
			}
		}
		return result;
	}

	// delete existing item
	public boolean deleteItem(Item item) {
		open();
		boolean result = mSQLiteDB.delete(ProjectConstants.TABLE_ITEMS,
				ProjectConstants.ID + " = " + item.getId(), null) > 0;
		close();
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "DAOItem - deleteItem(): " + result);
		}
		return result;
	}

	// delete all rows of the table 'item'
	public boolean deleteAllItems(String user) {
		String where = ProjectConstants.USER + " = " + '"' + user + '"';
		open();
		boolean result = mSQLiteDB.delete(ProjectConstants.TABLE_ITEMS, where,
				null) > 0;
		close();
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "DAOItem - deleteAllItems(): " + result);
		}
		return result;
	}

	public void dropAndRecreateTable(String tableName, String sql) {
		open();
		mSQLiteDB.execSQL("DROP TABLE IF EXISTS " + tableName);
		mSQLiteDB.execSQL(sql);
		close();
	}

	// update table with new item list
	public boolean updateTableWithNewList(ArrayList<Item> itemList) {
		dropAndRecreateTable(ProjectConstants.TABLE_ITEMS,
				ProjectConstants.CREATE_TABLE_ITEMS);
		open();
		ContentValues values = new ContentValues();
		boolean result = false;
		for (int i = itemList.size() - 1; i >= 0; i--) {
			Item item = itemList.get(i);
			putValues(item, values);
			values.put(ProjectConstants.CREATION_DATE, UtilMethods
					.dateToFormattedStringConverter(item.getCreationDate()));
			result = mSQLiteDB.insert(ProjectConstants.TABLE_ITEMS, null,
					values) > 0;
		}
		// for (Item item : itemList) {
		// putValues(item, values);
		// values.put(ProjectConstants.CREATION_DATE, UtilMethods
		// .dateToFormattedStringConverter(item.getCreationDate()));
		// result = mSQLiteDB.insert(ProjectConstants.TABLE_ITEMS, null,
		// values) > 0;
		// }
		close();
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "DAOItem - updateTableWithNewList(): "
					+ result);
		}
		return result;
	}

	// get all items as a list
	public ArrayList<Item> getAllItems(String user) {
		open();
		ArrayList<Item> itemList = new ArrayList<Item>();
		Cursor cursor = null;
		String selectQuery = "SELECT * FROM " + ProjectConstants.TABLE_ITEMS
				+ " WHERE " + ProjectConstants.USER + " = " + '"' + user + '"';
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "DAOItem - getAllItems(): \n"
					+ selectQuery);
		}

		try {
			cursor = mSQLiteDB.rawQuery(selectQuery, null);

			if (cursor != null) {

				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					Item item = createItemFromTableValues(cursor);
					itemList.add(0, item);
				}
			}
		} catch (Exception e) {
			itemList = new ArrayList<Item>();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			close();
		}
		return itemList;
	}

	// public String buildJSONfromSQLiteTest(String user) throws JSONException,
	// IOException {
	// ArrayList<Item> itemListFromDb = getAllItems(user);
	// JSONArray jsonArray = new JSONArray();
	// for (Item item : itemListFromDb) {
	// JSONObject json = new JSONObject();
	// json.put(ProjectConstants.ID, item.getId());
	// json.put(ProjectConstants.USER, item.getUser());
	// json.put(ProjectConstants.TITLE, item.getTitle());
	// json.put(ProjectConstants.TYPE, item.getType());
	// json.put(ProjectConstants.COVER, item.getCover());
	// json.put(ProjectConstants.GENRE, item.getGenre());
	// json.put(ProjectConstants.FAVORITE, item.isFavorite());
	// json.put(ProjectConstants.CREATION_DATE, item.getCreationDate());
	// json.put(ProjectConstants.DELETED, item.isDeleted());
	// json.put(ProjectConstants.DELETION_DATE, item.getDeletionDate());
	// json.put(ProjectConstants.IN_POSSESSION, item.isInPossession());
	// json.put(ProjectConstants.ORIGINAL_TITLE, item.getOriginalTitle());
	// json.put(ProjectConstants.COUNTRY, item.getCountry());
	// json.put(ProjectConstants.YEAR_PUBLISHED, item.getYearPublished());
	// json.put(ProjectConstants.CONTENT, item.getContent());
	// json.put(ProjectConstants.RATING, item.getRating());
	// if (item instanceof Movie) {
	//
	// json.put(ProjectConstants.PRODUCER,
	// ((Movie) item).getProducer());
	// json.put(ProjectConstants.DIRECTOR,
	// ((Movie) item).getDirector());
	// json.put(ProjectConstants.SCRIPT, ((Movie) item).getScript());
	// json.put(ProjectConstants.ACTORS, ((Movie) item).getActors());
	// json.put(ProjectConstants.MUSIC, ((Movie) item).getMusic());
	// json.put(ProjectConstants.LENGTH, ((Movie) item).getLength());
	// }
	// if (item instanceof MusicAlbum) {
	// json.put(ProjectConstants.LABEL, ((MusicAlbum) item).getLabel());
	// json.put(ProjectConstants.STUDIO,
	// ((MusicAlbum) item).getStudio());
	// json.put(ProjectConstants.ARTIST,
	// ((MusicAlbum) item).getArtist());
	// json.put(ProjectConstants.FORMAT,
	// ((MusicAlbum) item).getFormat());
	// json.put(ProjectConstants.TITLE_COUNT,
	// ((MusicAlbum) item).getTitleCount());
	// }
	// if (item instanceof Book) {
	//
	// json.put(ProjectConstants.EDITION, ((Book) item).getEdition());
	// json.put(ProjectConstants.PUBLISHING_HOUSE,
	// ((Book) item).getPublishingHouse());
	// json.put(ProjectConstants.AUTHOR, ((Book) item).getAuthor());
	// json.put(ProjectConstants.ISBN, ((Book) item).getIsbn());
	// }
	// jsonArray.put(json);
	// Writer writer = null;
	// try {
	// OutputStream out = mAppContext.openFileOutput(mFileName,
	// Context.MODE_PRIVATE);
	// writer = new OutputStreamWriter(out);
	// writer.write(jsonArray.toString());
	// System.out.println(writer.toString());
	// } finally {
	// if (writer != null)
	// writer.close();
	// }
	// }
	// System.out.println(jsonArray.toString(2));
	// return jsonArray.toString();
	// }

	// build JSONString from SQLite database values
	public String buildJSONfromSQLite(String user) {
		open();
		ArrayList<HashMap<String, String>> values = new ArrayList<HashMap<String, String>>();
		Cursor cursor = null;
		String selectQuery = "SELECT * FROM " + ProjectConstants.TABLE_ITEMS
				+ " WHERE " + ProjectConstants.USER + " = " + '"' + user + '"'
				+ " AND " + ProjectConstants.SYNCED + " = " + 0;
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "DAOItem - buildJSONfromSQLite(): \n"
					+ selectQuery);
		}
		Gson gson = new GsonBuilder().create();

		try {
			cursor = mSQLiteDB.rawQuery(selectQuery, null);

			if (cursor != null) {

				getColumnIndices(cursor);
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					HashMap<String, String> map = new LinkedHashMap<String, String>();

					map.put(ProjectConstants.ID, cursor.getString(colId));
					map.put(ProjectConstants.SQLITE_ID,
							cursor.getString(colSQLiteId));
					map.put(ProjectConstants.USER, cursor.getString(colUser));
					// map.put(ProjectConstants.SYNCED,
					// cursor.getString(colSynced));
					map.put(ProjectConstants.TITLE, cursor.getString(colTitle));
					map.put(ProjectConstants.TYPE, cursor.getString(colType));
					map.put(ProjectConstants.COVER, cursor.getString(colCover));
					map.put(ProjectConstants.GENRE, cursor.getString(colGenre));
					map.put(ProjectConstants.FAVORITE,
							cursor.getString(colFavorite));
					map.put(ProjectConstants.CREATION_DATE,
							cursor.getString(colCreationDate));
					map.put(ProjectConstants.DELETED,
							cursor.getString(colDeleted));
					map.put(ProjectConstants.DELETION_DATE,
							cursor.getString(colDeletionDate));
					map.put(ProjectConstants.IN_POSSESSION,
							cursor.getString(colInPossession));
					map.put(ProjectConstants.ORIGINAL_TITLE,
							cursor.getString(colOriginalTitle));
					map.put(ProjectConstants.COUNTRY,
							cursor.getString(colCountry));
					map.put(ProjectConstants.YEAR_PUBLISHED,
							cursor.getString(colYearPublished));
					map.put(ProjectConstants.CONTENT,
							cursor.getString(colContent));
					map.put(ProjectConstants.RATING,
							cursor.getString(colRating));
					map.put(ProjectConstants.PRODUCER,
							cursor.getString(colProducer));
					map.put(ProjectConstants.DIRECTOR,
							cursor.getString(colDirector));
					map.put(ProjectConstants.SCRIPT,
							cursor.getString(colScript));
					map.put(ProjectConstants.ACTORS,
							cursor.getString(colActors));
					map.put(ProjectConstants.MUSIC, cursor.getString(colMusic));
					map.put(ProjectConstants.LENGTH,
							cursor.getString(colLength));
					map.put(ProjectConstants.LABEL, cursor.getString(colLabel));
					map.put(ProjectConstants.STUDIO,
							cursor.getString(colStudio));
					map.put(ProjectConstants.ARTIST,
							cursor.getString(colArtist));
					map.put(ProjectConstants.FORMAT,
							cursor.getString(colFormat));
					map.put(ProjectConstants.TITLE_COUNT,
							cursor.getString(colTitleCount));
					map.put(ProjectConstants.EDITION,
							cursor.getString(colEdition));
					map.put(ProjectConstants.PUBLISHING_HOUSE,
							cursor.getString(colPublishingHouse));
					map.put(ProjectConstants.AUTHOR,
							cursor.getString(colAuthor));
					map.put(ProjectConstants.ISBN, cursor.getString(colIsbn));
					values.add(map);
				}
			}
		} catch (Exception e) {
			values = new ArrayList<HashMap<String, String>>();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			close();
		}

		return gson.toJson(values);
	}

	// create items from SQLite table values
	public Item createItemFromTableValues(Cursor cursor) {
		Item item = null;
		getColumnIndices(cursor);

		ItemType type = ItemType.valueOf(cursor.getString(colType));

		int favoriteAsInt = (Integer.parseInt(cursor.getString(colFavorite)));
		int inPossessionAsInt = (Integer.parseInt(cursor
				.getString(colInPossession)));
		int deletedAsInt = (Integer.parseInt(cursor.getString(colDeleted)));

		int id = Integer.parseInt(cursor.getString(colSQLiteId));
		// byte[] bytes = cursor.getBlob(iCover);
		// Bitmap cover = CoverUtil.getBitmap(bytes);
		String creationDate = cursor.getString(colCreationDate);
		String user = cursor.getString(colUser);
		String title = cursor.getString(colTitle);
		String genre = cursor.getString(colGenre);

		switch (type) {
		case Album:
			item = new MusicAlbum(id, user);
			break;
		case Book:
			item = new Book(id, user);
			break;
		case Movie:
			item = new Movie(id, user);
			break;
		default:
			break;
		}
		// item.setCover(cover);
		item.setTitle(title);
		item.setType(type);
		item.setGenre(genre);
		item.setFavorite(UtilMethods.isTrue(favoriteAsInt));
		item.setCreationDate(UtilMethods
				.setCreationDateFromString(creationDate));
		item.setInPossession(UtilMethods.isTrue(inPossessionAsInt));
		item.setDeleted(UtilMethods.isTrue(deletedAsInt));
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG,
					"DAOItem - createItemFromTableValues(): \n"
							+ item.toString());
		}
		return item;
	}

	// put values to insert into SQLite database
	public void putValues(Item item, ContentValues values) {
		// byte[] bytes = CoverUtil.getByteArray(item.getCover());
		// values.put(ProjectConstants.COVER, bytes);
		values.put(ProjectConstants.SYNCED,
				UtilMethods.isTrueAsInt(item.isSynced()));
		values.put(ProjectConstants.USER, item.getUser());
		values.put(ProjectConstants.TITLE, item.getTitle());
		values.put(ProjectConstants.TYPE, item.getType().toString());
		values.put(ProjectConstants.GENRE, item.getGenre());
		values.put(ProjectConstants.IN_POSSESSION,
				UtilMethods.isTrueAsInt(item.isInPossession()));
		values.put(ProjectConstants.FAVORITE,
				UtilMethods.isTrueAsInt(item.isFavorite()));
		values.put(ProjectConstants.DELETED,
				UtilMethods.isTrueAsInt(item.isDeleted()));

		if (item.getDeletionDate() != null) {
			values.put(ProjectConstants.DELETION_DATE, UtilMethods
					.dateToFormattedStringConverter(item.getDeletionDate()));
		}

		values.put(ProjectConstants.ORIGINAL_TITLE, item.getOriginalTitle());
		values.put(ProjectConstants.COUNTRY, item.getCountry());
		values.put(ProjectConstants.YEAR_PUBLISHED, item.getYearPublished());
		values.put(ProjectConstants.CONTENT, item.getContent());
		values.put(ProjectConstants.RATING, item.getRating());

		if (item instanceof Movie) {
			values.put(ProjectConstants.PRODUCER, ((Movie) item).getProducer());
			values.put(ProjectConstants.DIRECTOR, ((Movie) item).getDirector());
			values.put(ProjectConstants.SCRIPT, ((Movie) item).getScript());
			values.put(ProjectConstants.ACTORS, ((Movie) item).getActors());
			values.put(ProjectConstants.MUSIC, ((Movie) item).getMusic());
			values.put(ProjectConstants.LENGTH, ((Movie) item).getLength());
		}

		if (item instanceof MusicAlbum) {
			values.put(ProjectConstants.LABEL, ((MusicAlbum) item).getLabel());
			values.put(ProjectConstants.STUDIO, ((MusicAlbum) item).getStudio());
			values.put(ProjectConstants.ARTIST, ((MusicAlbum) item).getArtist());
			values.put(ProjectConstants.FORMAT, ((MusicAlbum) item).getFormat());
			values.put(ProjectConstants.TITLE_COUNT,
					((MusicAlbum) item).getTitleCount());
		}

		if (item instanceof Book) {
			values.put(ProjectConstants.EDITION, ((Book) item).getEdition());
			values.put(ProjectConstants.PUBLISHING_HOUSE,
					((Book) item).getPublishingHouse());
			values.put(ProjectConstants.AUTHOR, ((Book) item).getAuthor());
			values.put(ProjectConstants.ISBN, ((Book) item).getIsbn());
		}

	}

	// return integer values of cursor query
	public void getColumnIndices(Cursor cursor) {
		colSQLiteId = cursor.getColumnIndex(ProjectConstants.ID);
		colUser = cursor.getColumnIndex(ProjectConstants.USER);
		colSynced = cursor.getColumnIndex(ProjectConstants.SYNCED);
		colTitle = cursor.getColumnIndex(ProjectConstants.TITLE);
		colType = cursor.getColumnIndex(ProjectConstants.TYPE);
		colCover = cursor.getColumnIndex(ProjectConstants.COVER);
		colGenre = cursor.getColumnIndex(ProjectConstants.GENRE);
		colFavorite = cursor.getColumnIndex(ProjectConstants.FAVORITE);
		colCreationDate = cursor.getColumnIndex(ProjectConstants.CREATION_DATE);
		colDeleted = cursor.getColumnIndex(ProjectConstants.DELETED);
		colDeletionDate = cursor.getColumnIndex(ProjectConstants.DELETION_DATE);
		colInPossession = cursor.getColumnIndex(ProjectConstants.IN_POSSESSION);
		colOriginalTitle = cursor
				.getColumnIndex(ProjectConstants.ORIGINAL_TITLE);
		colCountry = cursor.getColumnIndex(ProjectConstants.COUNTRY);
		colYearPublished = cursor
				.getColumnIndex(ProjectConstants.YEAR_PUBLISHED);
		colContent = cursor.getColumnIndex(ProjectConstants.CONTENT);
		colRating = cursor.getColumnIndex(ProjectConstants.RATING);
		colProducer = cursor.getColumnIndex(ProjectConstants.PRODUCER);
		colDirector = cursor.getColumnIndex(ProjectConstants.DIRECTOR);
		colScript = cursor.getColumnIndex(ProjectConstants.SCRIPT);
		colActors = cursor.getColumnIndex(ProjectConstants.ACTORS);
		colMusic = cursor.getColumnIndex(ProjectConstants.MUSIC);
		colLength = cursor.getColumnIndex(ProjectConstants.LENGTH);
		colLabel = cursor.getColumnIndex(ProjectConstants.LABEL);
		colStudio = cursor.getColumnIndex(ProjectConstants.STUDIO);
		colArtist = cursor.getColumnIndex(ProjectConstants.ARTIST);
		colFormat = cursor.getColumnIndex(ProjectConstants.FORMAT);
		colTitleCount = cursor.getColumnIndex(ProjectConstants.TITLE_COUNT);
		colEdition = cursor.getColumnIndex(ProjectConstants.EDITION);
		colPublishingHouse = cursor
				.getColumnIndex(ProjectConstants.PUBLISHING_HOUSE);
		colAuthor = cursor.getColumnIndex(ProjectConstants.AUTHOR);
		colIsbn = cursor.getColumnIndex(ProjectConstants.ISBN);
	}

	public int getCountOfSyncedItems() {
		int count = 0;
		Cursor cursor = null;
		open();
		String selectQuery = "SELECT * FROM " + ProjectConstants.TABLE_ITEMS
				+ " WHERE " + ProjectConstants.SYNCED + "=" + 0;
		cursor = mSQLiteDB.rawQuery(selectQuery, null);
		count = cursor.getCount();
		close();
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "DAOItem - getCountOfSyncedItems(): "
					+ count);
		}
		return count;
	}

	public void updateSyncStatus(long id, int status) {
		open();
		String updateQuery = "UPDATE " + ProjectConstants.TABLE_ITEMS + " SET "
				+ ProjectConstants.SYNCED + " = " + status + " WHERE "
				+ ProjectConstants.ID + " = " + id;
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "DAOItem - updateSyncStatus(): \n"
					+ updateQuery);
		}
		mSQLiteDB.execSQL(updateQuery);
		close();
	}

	public String getSyncStatus() {
		String msg = null;
		if (getCountOfSyncedItems() == 0) {
			msg = "SQLite and Remote MySQL DBs are in Sync!";
		} else {
			msg = "DB Sync needed\n";
		}
		return msg;
	}

}