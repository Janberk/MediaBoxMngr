package de.canberkdemirkan.mediaboxmngr.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.MusicAlbum;
import de.canberkdemirkan.mediaboxmngr.util.ItemType;
import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

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

	public Item createItemFromTableValues(Cursor cursor) {
		Item item = null;

		int iRow = cursor.getColumnIndex(ProjectConstants.ID);
		// int iCover = cursor.getColumnIndex(ProjectConstants.COVER);
		int iCreationDate = cursor
				.getColumnIndex(ProjectConstants.CREATION_DATE);
		int iUser = cursor.getColumnIndex(ProjectConstants.USER);
		int iTitle = cursor.getColumnIndex(ProjectConstants.TITLE);
		int iType = cursor.getColumnIndex(ProjectConstants.TYPE);
		int iGenre = cursor.getColumnIndex(ProjectConstants.GENRE);
		int iFavorite = cursor.getColumnIndex(ProjectConstants.FAVORITE);
		int iInPossession = cursor
				.getColumnIndex(ProjectConstants.IN_POSSESSION);
		int iDeleted = cursor.getColumnIndex(ProjectConstants.DELETED);

		ItemType type = ItemType.valueOf(cursor.getString(iType));

		int favoriteAsInt = (Integer.parseInt(cursor.getString(iFavorite)));
		int inPossessionAsInt = (Integer.parseInt(cursor
				.getString(iInPossession)));
		int deletedAsInt = (Integer.parseInt(cursor.getString(iDeleted)));

		int id = Integer.parseInt(cursor.getString(iRow));
		// byte[] bytes = cursor.getBlob(iCover);
		// Bitmap cover = CoverUtil.getBitmap(bytes);
		String creationDate = cursor.getString(iCreationDate);
		String user = cursor.getString(iUser);
		String title = cursor.getString(iTitle);
		String genre = cursor.getString(iGenre);

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

}