package de.canberkdemirkan.mediaboxmngr.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
import de.canberkdemirkan.mediaboxmngr.content.ItemType;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.Music;
import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class DAOItem {

	public static int colId;
	public static int colSQLiteId;
	public static int colUser;
	public static int colSynced;
	public static int colType;
	public static int colFavorite;
	public static int colCreationDate;
	public static int colTitle;
	public static int colGenre;
	public static int colCountry;
	public static int colYear;
	public static int colContent;
	public static int colRating;
	public static int colCover;
	public static int colDirector;
	public static int colCast;
	public static int colMusic;
	public static int colLength;
	public static int colArtist;
	public static int colLabel;
	public static int colFormat;
	public static int colTitleCount;
	public static int colAuthor;
	public static int colPublisher;
	public static int colEdition;
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
		values.put(Constants.CREATION_DATE, UtilMethods
				.dateToFormattedStringConverter(item.getCreationDate()));
		long id = mSQLiteDB.insert(Constants.TABLE_ITEMS, null, values);
		close();
		item.setId(id);
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG,
					"DAOItem - insertItem(): \n" + item.toString());
		}
		increaseTableVersion();
		return item;
	}

	// get item by id (row)
	public Item getItemByRow(int id) {
		open();
		Item item = null;
		Cursor cursor = null;
		String selectQuery = "SELECT * FROM " + Constants.TABLE_ITEMS
				+ " WHERE " + Constants.ID + "=" + '"' + id + '"';

		try {
			cursor = mSQLiteDB.rawQuery(selectQuery, null);

			if (cursor != null) {
				cursor.moveToFirst();
				item = createItemFromTableValues(cursor);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			close();
		}
		return item;
	}

	// update existing item
	public boolean updateItem(Item item) {
		open();
		ContentValues values = new ContentValues();
		putValues(item, values);
		boolean result = mSQLiteDB.update(Constants.TABLE_ITEMS, values,
				Constants.ID + " = " + item.getId(), null) > 0;
		close();
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "DAOItem - updateItem(): " + result);
		}
		increaseTableVersion();
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
				result.add(item);
			}
		}
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "DAOItem - getItemsByType(" + type + "): ");
		}
		return result;
	}

	public ArrayList<Item> getFavoriteItems(String user) {
		ArrayList<Item> pre = getAllItems(user);
		ArrayList<Item> result = new ArrayList<Item>();

		for (Item item : pre) {
			if (item.isFavorite()) {
				result.add(item);
			}
		}
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "DAOItem - getFavoriteItems(): ");
		}
		return result;
	}

	public ArrayList<Item> getTopRatedItems(String user) {
		ArrayList<Item> result = getAllItems(user);

		for (Iterator<Item> it = result.iterator(); it.hasNext();) {
			Item item = it.next();
			String ratingString = item.getRating();
			if (ratingString != null) {
				Float rating = Float.valueOf(ratingString);
				if (rating < 1.0F) {
					it.remove();
				}
			} else {
				it.remove();
			}
		}
		Collections.sort(result);
		Collections.reverse(result);
		return result;
	}

	// delete existing item
	public boolean deleteItem(Item item) {
		open();
		boolean result = mSQLiteDB.delete(Constants.TABLE_ITEMS, Constants.ID
				+ " = " + item.getId(), null) > 0;
		close();
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "DAOItem - deleteItem(): " + result);
		}
		increaseTableVersion();
		return result;
	}

	// delete ALL rows of the table 'item'
	public boolean deleteAllItems(String user) {
		String where = Constants.USER + " = " + '"' + user + '"';
		open();
		boolean result = mSQLiteDB.delete(Constants.TABLE_ITEMS, where, null) > 0;
		close();
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "DAOItem - deleteAllItems(): " + result);
		}
		increaseTableVersion();
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
		dropAndRecreateTable(Constants.TABLE_ITEMS,
				Constants.CREATE_TABLE_ITEMS);
		open();
		ContentValues values = new ContentValues();
		boolean result = false;
		for (int i = itemList.size() - 1; i >= 0; i--) {
			Item item = itemList.get(i);
			putValues(item, values);
			values.put(Constants.CREATION_DATE, UtilMethods
					.dateToFormattedStringConverter(item.getCreationDate()));
			result = mSQLiteDB.insert(Constants.TABLE_ITEMS, null, values) > 0;
		}
		// for (Item item : itemList) {
		// putValues(item, values);
		// values.put(Constants.CREATION_DATE, UtilMethods
		// .dateToFormattedStringConverter(item.getCreationDate()));
		// result = mSQLiteDB.insert(Constants.TABLE_ITEMS, null,
		// values) > 0;
		// }
		close();
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "DAOItem - updateTableWithNewList(): "
					+ result);
		}
		increaseTableVersion();
		return result;
	}

	// get ALL items as a list
	public ArrayList<Item> getAllItems(String user) {
		open();
		ArrayList<Item> itemList = new ArrayList<Item>();
		Cursor cursor = null;
		String selectQuery = "SELECT * FROM " + Constants.TABLE_ITEMS
				+ " WHERE " + Constants.USER + " = " + '"' + user + '"';
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
	// json.put(Constants.ID, item.getId());
	// json.put(Constants.USER, item.getUser());
	// json.put(Constants.TITLE, item.getTitle());
	// json.put(Constants.TYPE, item.getType());
	// json.put(Constants.COVER, item.getCover());
	// json.put(Constants.GENRE, item.getGenre());
	// json.put(Constants.FAVORITE, item.isFavorite());
	// json.put(Constants.CREATION_DATE, item.getCreationDate());
	// json.put(Constants.DELETED, item.isDeleted());
	// json.put(Constants.DELETION_DATE, item.getDeletionDate());
	// json.put(Constants.IN_POSSESSION, item.isInPossession());
	// json.put(Constants.ORIGINAL_TITLE, item.getOriginalTitle());
	// json.put(Constants.COUNTRY, item.getCountry());
	// json.put(Constants.YEAR_PUBLISHED, item.getYear());
	// json.put(Constants.CONTENT, item.getContent());
	// json.put(Constants.RATING, item.getRating());
	// if (item instanceof Movie) {
	//
	// json.put(Constants.PRODUCER,
	// ((Movie) item).getProducer());
	// json.put(Constants.DIRECTOR,
	// ((Movie) item).getDirector());
	// json.put(Constants.SCRIPT, ((Movie) item).getScript());
	// json.put(Constants.CAST, ((Movie) item).getCast());
	// json.put(Constants.MUSIC, ((Movie) item).getMusic());
	// json.put(Constants.LENGTH, ((Movie) item).getLength());
	// }
	// if (item instanceof Music) {
	// json.put(Constants.LABEL, ((Music) item).getLabel());
	// json.put(Constants.STUDIO,
	// ((Music) item).getStudio());
	// json.put(Constants.ARTIST,
	// ((Music) item).getArtist());
	// json.put(Constants.FORMAT,
	// ((Music) item).getFormat());
	// json.put(Constants.TITLE_COUNT,
	// ((Music) item).getTitleCount());
	// }
	// if (item instanceof Book) {
	//
	// json.put(Constants.EDITION, ((Book) item).getEdition());
	// json.put(Constants.PUBLISHER,
	// ((Book) item).getPublisher());
	// json.put(Constants.AUTHOR, ((Book) item).getAuthor());
	// json.put(Constants.ISBN, ((Book) item).getIsbn());
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
		String selectQuery = "SELECT * FROM " + Constants.TABLE_ITEMS
				+ " WHERE " + Constants.USER + " = " + '"' + user + '"'
				+ " AND " + Constants.SYNCED + " = " + 0;
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

					map.put(Constants.ID, cursor.getString(colId));
					map.put(Constants.SQLITE_ID, cursor.getString(colSQLiteId));
					map.put(Constants.USER, cursor.getString(colUser));
					// map.put(Constants.SYNCED,
					// cursor.getString(colSynced));
					map.put(Constants.TYPE, cursor.getString(colType));
					map.put(Constants.FAVORITE, cursor.getString(colFavorite));
					map.put(Constants.CREATION_DATE,
							cursor.getString(colCreationDate));
					map.put(Constants.TITLE, cursor.getString(colTitle));
					map.put(Constants.GENRE, cursor.getString(colGenre));
					map.put(Constants.COUNTRY, cursor.getString(colCountry));
					map.put(Constants.YEAR, cursor.getString(colYear));
					map.put(Constants.CONTENT, cursor.getString(colContent));
					map.put(Constants.RATING, cursor.getString(colRating));
					map.put(Constants.COVER, cursor.getString(colCover));
					map.put(Constants.DIRECTOR, cursor.getString(colDirector));
					map.put(Constants.CAST, cursor.getString(colCast));
					map.put(Constants.MUSIC, cursor.getString(colMusic));
					map.put(Constants.LENGTH, cursor.getString(colLength));
					map.put(Constants.ARTIST, cursor.getString(colArtist));
					map.put(Constants.LABEL, cursor.getString(colLabel));
					map.put(Constants.FORMAT, cursor.getString(colFormat));
					map.put(Constants.TITLE_COUNT,
							cursor.getString(colTitleCount));
					map.put(Constants.AUTHOR, cursor.getString(colAuthor));
					map.put(Constants.PUBLISHER, cursor.getString(colPublisher));
					map.put(Constants.EDITION, cursor.getString(colEdition));
					map.put(Constants.ISBN, cursor.getString(colIsbn));
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

		int synced = (Integer.parseInt(cursor.getString(colSynced)));
		int favoriteAsInt = (Integer.parseInt(cursor.getString(colFavorite)));

		int id = Integer.parseInt(cursor.getString(colSQLiteId));
		// byte[] bytes = cursor.getBlob(iCover);
		// Bitmap cover = CoverUtil.getBitmap(bytes);
		String creationDate = cursor.getString(colCreationDate);
		String user = cursor.getString(colUser);
		String title = cursor.getString(colTitle);
		String genre = cursor.getString(colGenre);
		String country = cursor.getString(colCountry);
		String year = cursor.getString(colYear);
		String content = cursor.getString(colContent);
		String rating = cursor.getString(colRating);
		String director = cursor.getString(colDirector);
		String cast = cursor.getString(colCast);
		String music = cursor.getString(colMusic);
		String length = cursor.getString(colLength);
		String artist = cursor.getString(colArtist);
		String label = cursor.getString(colLabel);
		String format = cursor.getString(colFormat);
		String titleCount = cursor.getString(colTitleCount);
		String author = cursor.getString(colAuthor);
		String publisher = cursor.getString(colPublisher);
		String edition = cursor.getString(colEdition);
		String isbn = cursor.getString(colIsbn);

		switch (type) {
		case Album:
			item = new Music(id, user);
			((Music) item).setLabel(label);
			((Music) item).setArtist(artist);
			((Music) item).setFormat(format);
			((Music) item).setTitleCount(titleCount);
			break;
		case Book:
			item = new Book(id, user);
			((Book) item).setEdition(edition);
			((Book) item).setPublisher(publisher);
			((Book) item).setAuthor(author);
			((Book) item).setIsbn(isbn);
			break;
		case Movie:
			item = new Movie(id, user);
			((Movie) item).setDirector(director);
			((Movie) item).setCast(cast);
			((Movie) item).setMusic(music);
			((Movie) item).setLength(length);

			break;
		default:
			break;
		}
		item.setSynced(UtilMethods.isTrue(synced));
		item.setType(type);
		item.setFavorite(UtilMethods.isTrue(favoriteAsInt));
		item.setCreationDate(UtilMethods
				.setCreationDateFromString(creationDate));
		item.setTitle(title);
		item.setGenre(genre);
		item.setCountry(country);
		item.setYear(year);
		item.setContent(content);
		item.setRating(rating);
		// item.setCover(cover);

		return item;
	}

	// put values to insert into SQLite database
	public void putValues(Item item, ContentValues values) {
		// byte[] bytes = CoverUtil.getByteArray(item.getCover());
		// values.put(Constants.COVER, bytes);
		values.put(Constants.USER, item.getUser());
		values.put(Constants.SYNCED, UtilMethods.isTrueAsInt(item.isSynced()));
		values.put(Constants.TYPE, item.getType().toString());
		values.put(Constants.FAVORITE,
				UtilMethods.isTrueAsInt(item.isFavorite()));
		values.put(Constants.TITLE, item.getTitle());
		values.put(Constants.GENRE, item.getGenre());
		values.put(Constants.COUNTRY, item.getCountry());
		values.put(Constants.YEAR, item.getYear());
		values.put(Constants.CONTENT, item.getContent());
		values.put(Constants.RATING, item.getRating());

		if (item instanceof Movie) {
			values.put(Constants.DIRECTOR, ((Movie) item).getDirector());
			values.put(Constants.CAST, ((Movie) item).getCast());
			values.put(Constants.MUSIC, ((Movie) item).getMusic());
			values.put(Constants.LENGTH, ((Movie) item).getLength());
		}

		if (item instanceof Music) {
			values.put(Constants.ARTIST, ((Music) item).getArtist());
			values.put(Constants.LABEL, ((Music) item).getLabel());
			values.put(Constants.FORMAT, ((Music) item).getFormat());
			values.put(Constants.TITLE_COUNT, ((Music) item).getTitleCount());
		}

		if (item instanceof Book) {
			values.put(Constants.AUTHOR, ((Book) item).getAuthor());
			values.put(Constants.PUBLISHER, ((Book) item).getPublisher());
			values.put(Constants.EDITION, ((Book) item).getEdition());
			values.put(Constants.ISBN, ((Book) item).getIsbn());
		}

	}

	// return integer values of cursor query
	public void getColumnIndices(Cursor cursor) {
		colSQLiteId = cursor.getColumnIndex(Constants.ID);
		colUser = cursor.getColumnIndex(Constants.USER);
		colSynced = cursor.getColumnIndex(Constants.SYNCED);
		colType = cursor.getColumnIndex(Constants.TYPE);
		colFavorite = cursor.getColumnIndex(Constants.FAVORITE);
		colCreationDate = cursor.getColumnIndex(Constants.CREATION_DATE);
		colTitle = cursor.getColumnIndex(Constants.TITLE);
		colGenre = cursor.getColumnIndex(Constants.GENRE);
		colCountry = cursor.getColumnIndex(Constants.COUNTRY);
		colYear = cursor.getColumnIndex(Constants.YEAR);
		colContent = cursor.getColumnIndex(Constants.CONTENT);
		colRating = cursor.getColumnIndex(Constants.RATING);
		colCover = cursor.getColumnIndex(Constants.COVER);
		colDirector = cursor.getColumnIndex(Constants.DIRECTOR);
		colCast = cursor.getColumnIndex(Constants.CAST);
		colMusic = cursor.getColumnIndex(Constants.MUSIC);
		colLength = cursor.getColumnIndex(Constants.LENGTH);
		colArtist = cursor.getColumnIndex(Constants.ARTIST);
		colLabel = cursor.getColumnIndex(Constants.LABEL);
		colFormat = cursor.getColumnIndex(Constants.FORMAT);
		colTitleCount = cursor.getColumnIndex(Constants.TITLE_COUNT);
		colAuthor = cursor.getColumnIndex(Constants.AUTHOR);
		colPublisher = cursor.getColumnIndex(Constants.PUBLISHER);
		colEdition = cursor.getColumnIndex(Constants.EDITION);
		colIsbn = cursor.getColumnIndex(Constants.ISBN);
	}

	public int getCountOfSyncedItems() {
		int count = 0;
		Cursor cursor = null;
		open();
		String selectQuery = "SELECT * FROM " + Constants.TABLE_ITEMS
				+ " WHERE " + Constants.SYNCED + "=" + 0;
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
		String updateQuery = "UPDATE " + Constants.TABLE_ITEMS + " SET "
				+ Constants.SYNCED + " = " + status + " WHERE " + Constants.ID
				+ " = " + id;
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "DAOItem - updateSyncStatus(): \n"
					+ updateQuery);
		}
		mSQLiteDB.execSQL(updateQuery);
		close();
		// increaseTableVersion();
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

	private static int TABLE_VERSION = 0;

	public void increaseTableVersion() {
		open();
		ContentValues values = new ContentValues();
		values.put(Constants.VERSION, ++TABLE_VERSION);

		Cursor cursor = mSQLiteDB.query(Constants.VERSION,
				new String[] { Constants.VERSION }, null, null, null, null,
				null);

		if (!cursor.moveToFirst()) {
			mSQLiteDB.insert(Constants.TABLE_VERSION, null, values);
		} else {
			mSQLiteDB.update(Constants.TABLE_VERSION, values, null, null);
		}
		cursor.close();
		close();
	}

	public int getTableVersion(String tableName) {
		open();
		int version = 0;
		Cursor cursor = null;

		try {
			cursor = mSQLiteDB.query(tableName,
					new String[] { Constants.VERSION }, null, null, null, null,
					null);

			if (cursor.moveToFirst() && cursor != null) {
				version = cursor.getInt(cursor
						.getColumnIndex(Constants.VERSION));
				return version;
			} else {
				return version;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			close();
		}
		return version;
	}

	// create items from SQLite table values
	public Item updateItemFromDb(Item item) {
		Cursor cursor = null;
		String selectQuery = "SELECT * FROM " + Constants.TABLE_ITEMS
				+ " WHERE " + Constants.ID + "=" + '"' + item.getId() + '"';

		try {
			cursor = mSQLiteDB.rawQuery(selectQuery, null);

			if (cursor != null) {
				cursor.moveToFirst();
				getColumnIndices(cursor);

				ItemType type = item.getType();

				int synced = (Integer.parseInt(cursor.getString(colSynced)));
				int favoriteAsInt = (Integer.parseInt(cursor
						.getString(colFavorite)));

				int id = Integer.parseInt(cursor.getString(colSQLiteId));
				// byte[] bytes = cursor.getBlob(iCover);
				// Bitmap cover = CoverUtil.getBitmap(bytes);
				String creationDate = cursor.getString(colCreationDate);
				String user = cursor.getString(colUser);
				String title = cursor.getString(colTitle);
				String genre = cursor.getString(colGenre);
				String country = cursor.getString(colCountry);
				String year = cursor.getString(colYear);
				String content = cursor.getString(colContent);
				String rating = cursor.getString(colRating);
				String director = cursor.getString(colDirector);
				String cast = cursor.getString(colCast);
				String music = cursor.getString(colMusic);
				String length = cursor.getString(colLength);
				String label = cursor.getString(colLabel);
				String artist = cursor.getString(colArtist);
				String format = cursor.getString(colFormat);
				String titleCount = cursor.getString(colTitleCount);
				String edition = cursor.getString(colEdition);
				String publisher = cursor.getString(colPublisher);
				String author = cursor.getString(colAuthor);
				String isbn = cursor.getString(colIsbn);

				switch (type) {
				case Album:
					((Music) item).setArtist(artist);
					((Music) item).setLabel(label);
					((Music) item).setFormat(format);
					((Music) item).setTitleCount(titleCount);
					break;
				case Book:
					((Book) item).setAuthor(author);
					((Book) item).setPublisher(publisher);
					((Book) item).setEdition(edition);
					((Book) item).setIsbn(isbn);
					break;
				case Movie:
					((Movie) item).setDirector(director);
					((Movie) item).setCast(cast);
					((Movie) item).setMusic(music);
					((Movie) item).setLength(length);

					break;
				default:
					break;
				}
				item.setSynced(UtilMethods.isTrue(synced));
				item.setType(type);
				item.setFavorite(UtilMethods.isTrue(favoriteAsInt));
				item.setCreationDate(UtilMethods
						.setCreationDateFromString(creationDate));
				item.setTitle(title);
				item.setGenre(genre);
				item.setCountry(country);
				item.setYear(year);
				item.setContent(content);
				item.setRating(rating);
				// item.setCover(cover);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			close();
		}

		return item;
	}

}