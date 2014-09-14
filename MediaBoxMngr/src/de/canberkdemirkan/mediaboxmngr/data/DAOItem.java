package de.canberkdemirkan.mediaboxmngr.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.content.ItemType;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.Music;
import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class DAOItem {

	public int mColId;
	public int mColSQLiteId;
	public int mColUser;
	public int mColSynced;
	public int mColType;
	public int mColFavorite;
	public int mColCreationDate;
	public int mColTitle;
	public int mColGenre;
	public int mColCountry;
	public int mColYear;
	public int mCContent;
	public int mColRating;
	public int mColCover;
	public int mColDirector;
	public int mColCast;
	public int mColMusic;
	public int mColLength;
	public int mColArtist;
	public int mColLabel;
	public int mColFormat;
	public int mColTitleCount;
	public int mColAuthor;
	public int mColPublisher;
	public int mColEdition;
	public int mColIsbn;

	private Context mContext;
	private SQLiteDatabase mSQLiteDB;
	private DatabaseHelper mDBHelper;

	// constructor
	public DAOItem(Context context) {
		mContext = context;
		mDBHelper = DatabaseHelper.getInstance(mContext);
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
		close();
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "DAOItem - updateTableWithNewList(): "
					+ result);
		}
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

	// create items from SQLite table values
	public Item createItemFromTableValues(Cursor cursor) {
		Item item = null;
		getColumnIndices(cursor);

		ItemType type = ItemType.valueOf(cursor.getString(mColType));

		int synced = (Integer.parseInt(cursor.getString(mColSynced)));
		int favoriteAsInt = (Integer.parseInt(cursor.getString(mColFavorite)));

		int id = Integer.parseInt(cursor.getString(mColSQLiteId));
		// byte[] bytes = cursor.getBlob(iCover);
		// Bitmap cover = CoverUtil.getBitmap(bytes);
		String creationDate = cursor.getString(mColCreationDate);
		String user = cursor.getString(mColUser);
		String title = cursor.getString(mColTitle);
		String genre = cursor.getString(mColGenre);
		String country = cursor.getString(mColCountry);
		String year = cursor.getString(mColYear);
		String content = cursor.getString(mCContent);
		String rating = cursor.getString(mColRating);
		String director = cursor.getString(mColDirector);
		String cast = cursor.getString(mColCast);
		String music = cursor.getString(mColMusic);
		String length = cursor.getString(mColLength);
		String artist = cursor.getString(mColArtist);
		String label = cursor.getString(mColLabel);
		String format = cursor.getString(mColFormat);
		String titleCount = cursor.getString(mColTitleCount);
		String author = cursor.getString(mColAuthor);
		String publisher = cursor.getString(mColPublisher);
		String edition = cursor.getString(mColEdition);
		String isbn = cursor.getString(mColIsbn);

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
		if (item != null) {
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
		mColSQLiteId = cursor.getColumnIndex(Constants.ID);
		mColUser = cursor.getColumnIndex(Constants.USER);
		mColSynced = cursor.getColumnIndex(Constants.SYNCED);
		mColType = cursor.getColumnIndex(Constants.TYPE);
		mColFavorite = cursor.getColumnIndex(Constants.FAVORITE);
		mColCreationDate = cursor.getColumnIndex(Constants.CREATION_DATE);
		mColTitle = cursor.getColumnIndex(Constants.TITLE);
		mColGenre = cursor.getColumnIndex(Constants.GENRE);
		mColCountry = cursor.getColumnIndex(Constants.COUNTRY);
		mColYear = cursor.getColumnIndex(Constants.YEAR);
		mCContent = cursor.getColumnIndex(Constants.CONTENT);
		mColRating = cursor.getColumnIndex(Constants.RATING);
		mColCover = cursor.getColumnIndex(Constants.COVER);
		mColDirector = cursor.getColumnIndex(Constants.DIRECTOR);
		mColCast = cursor.getColumnIndex(Constants.CAST);
		mColMusic = cursor.getColumnIndex(Constants.MUSIC);
		mColLength = cursor.getColumnIndex(Constants.LENGTH);
		mColArtist = cursor.getColumnIndex(Constants.ARTIST);
		mColLabel = cursor.getColumnIndex(Constants.LABEL);
		mColFormat = cursor.getColumnIndex(Constants.FORMAT);
		mColTitleCount = cursor.getColumnIndex(Constants.TITLE_COUNT);
		mColAuthor = cursor.getColumnIndex(Constants.AUTHOR);
		mColPublisher = cursor.getColumnIndex(Constants.PUBLISHER);
		mColEdition = cursor.getColumnIndex(Constants.EDITION);
		mColIsbn = cursor.getColumnIndex(Constants.ISBN);
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

				int synced = (Integer.parseInt(cursor.getString(mColSynced)));
				int favoriteAsInt = (Integer.parseInt(cursor
						.getString(mColFavorite)));

				int id = Integer.parseInt(cursor.getString(mColSQLiteId));
				// byte[] bytes = cursor.getBlob(iCover);
				// Bitmap cover = CoverUtil.getBitmap(bytes);
				String creationDate = cursor.getString(mColCreationDate);
				String user = cursor.getString(mColUser);
				String title = cursor.getString(mColTitle);
				String genre = cursor.getString(mColGenre);
				String country = cursor.getString(mColCountry);
				String year = cursor.getString(mColYear);
				String content = cursor.getString(mCContent);
				String rating = cursor.getString(mColRating);
				String director = cursor.getString(mColDirector);
				String cast = cursor.getString(mColCast);
				String music = cursor.getString(mColMusic);
				String length = cursor.getString(mColLength);
				String label = cursor.getString(mColLabel);
				String artist = cursor.getString(mColArtist);
				String format = cursor.getString(mColFormat);
				String titleCount = cursor.getString(mColTitleCount);
				String edition = cursor.getString(mColEdition);
				String publisher = cursor.getString(mColPublisher);
				String author = cursor.getString(mColAuthor);
				String isbn = cursor.getString(mColIsbn);

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