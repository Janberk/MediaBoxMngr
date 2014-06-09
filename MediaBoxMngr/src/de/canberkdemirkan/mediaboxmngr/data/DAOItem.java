package de.canberkdemirkan.mediaboxmngr.data;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.google.gson.Gson;

import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.MusicAlbum;
import de.canberkdemirkan.mediaboxmngr.util.ItemType;
import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class DAOItem {

	public static int colId;
	public static int colUser;
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
		putValues(item, values);
		values.put(ProjectConstants.CREATION_DATE, UtilMethods
				.dateToFormattedStringConverter(item.getCreationDate()));
		long id = mSQLiteDB.insert(ProjectConstants.TABLE_ITEMS, null, values);
		close();
		item.setId(id);
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
		return result;
	}

	// get all items as a list
	public ArrayList<Item> getAllItems(String user) {
		ArrayList<Item> itemList = new ArrayList<Item>();
		Cursor cursor = null;
		String selectQuery = "SELECT * FROM " + ProjectConstants.TABLE_ITEMS
				+ " WHERE " + ProjectConstants.USER + "=" + '"' + user + '"';

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
		}
		return itemList;
	}

	public String buildJSONfromSQLite(String user) {
		open();
		ArrayList<HashMap<String, String>> values = new ArrayList<HashMap<String, String>>();
		Cursor cursor = null;
		String selectQuery = "SELECT * FROM " + ProjectConstants.TABLE_ITEMS
				+ " WHERE " + ProjectConstants.USER + "=" + '"' + user + '"';
		Gson gson = new Gson();

		try {
			cursor = mSQLiteDB.rawQuery(selectQuery, null);

			if (cursor != null) {

				getColumnIndices(cursor);
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(ProjectConstants.ID, cursor.getString(colId));
					map.put(ProjectConstants.USER, cursor.getString(colUser));
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
		}
		String result = gson.toJson(values);
		System.out.println(result);
		return result;
	}

	public Item createItemFromTableValues(Cursor cursor) {
		Item item = null;
		getColumnIndices(cursor);

		ItemType type = ItemType.valueOf(cursor.getString(colType));

		int favoriteAsInt = (Integer.parseInt(cursor.getString(colFavorite)));
		int inPossessionAsInt = (Integer.parseInt(cursor
				.getString(colInPossession)));
		int deletedAsInt = (Integer.parseInt(cursor.getString(colDeleted)));

		int id = Integer.parseInt(cursor.getString(colId));
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
		return item;
	}

	public void putValues(Item item, ContentValues values) {
		// byte[] bytes = CoverUtil.getByteArray(item.getCover());
		// values.put(ProjectConstants.COVER, bytes);
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

	public void getColumnIndices(Cursor cursor) {
		colId = cursor.getColumnIndex(ProjectConstants.ID);
		colUser = cursor.getColumnIndex(ProjectConstants.USER);
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

}