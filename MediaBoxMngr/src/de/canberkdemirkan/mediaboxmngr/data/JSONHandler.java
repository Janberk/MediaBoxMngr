package de.canberkdemirkan.mediaboxmngr.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.content.ItemType;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.Music;
import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class JSONHandler {

	private ArrayList<Item> mRemoteList;

	public ArrayList<Item> loadItemsFromJSONArray(String response)
			throws JSONException {
		ArrayList<Item> itemList = new ArrayList<Item>();

		JSONObject json = new JSONObject(response);
		JSONArray array = json.getJSONArray("items");
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG,
					"JSONHandler - loadItemsFromJSONArray(): \n" + array);
		}

		for (int i = 0; i < array.length(); i++) {
			JSONObject item = array.getJSONObject(i);

			long id = item.getLong(Constants.SQLITE_ID);
			String user = item.getString(Constants.USER);
			int synced = (Integer.parseInt(item.getString(Constants.SYNCED)));
			ItemType type = ItemType.valueOf(item.getString(Constants.TYPE));
			int favoriteAsInt = (Integer.parseInt(item
					.getString(Constants.FAVORITE)));
			String creationDate = item.getString(Constants.CREATION_DATE);
			String title = item.getString(Constants.TITLE);
			String genre = item.getString(Constants.GENRE);
			String country = item.getString(Constants.COUNTRY);
			String year = item.getString(Constants.YEAR);
			String content = item.getString(Constants.CONTENT);
			String rating = item.getString(Constants.RATING);
			// String cover = item.getString(Constants.COVER);
			String director = item.getString(Constants.DIRECTOR);
			String cast = item.getString(Constants.CAST);
			String music = item.getString(Constants.MUSIC);
			String length = item.getString(Constants.LENGTH);
			String artist = item.getString(Constants.ARTIST);
			String label = item.getString(Constants.LABEL);
			String format = item.getString(Constants.FORMAT);
			String titleCount = item.getString(Constants.TITLE_COUNT);
			String author = item.getString(Constants.AUTHOR);
			String publisher = item.getString(Constants.PUBLISHER);
			String edition = item.getString(Constants.EDITION);
			String isbn = item.getString(Constants.ISBN);

			Item newItem = null;
			switch (type) {
			case Album:
				newItem = new Music(id, user);
				((Music) newItem).setLabel(label);
				((Music) newItem).setArtist(artist);
				((Music) newItem).setFormat(format);
				((Music) newItem).setTitleCount(titleCount);
				break;
			case Book:
				newItem = new Book(id, user);
				((Book) newItem).setEdition(edition);
				((Book) newItem).setPublisher(publisher);
				((Book) newItem).setAuthor(author);
				((Book) newItem).setIsbn(isbn);
				break;
			case Movie:
				newItem = new Movie(id, user);
				((Movie) newItem).setDirector(director);
				((Movie) newItem).setCast(cast);
				((Movie) newItem).setMusic(music);
				((Movie) newItem).setLength(length);

				break;
			default:
				break;
			}
			if (newItem != null) {
				newItem.setSynced(UtilMethods.isTrue(synced));
				newItem.setType(type);
				newItem.setFavorite(UtilMethods.isTrue(favoriteAsInt));
				newItem.setCreationDate(UtilMethods
						.setCreationDateFromString(creationDate));
				newItem.setTitle(title);
				newItem.setGenre(genre);
				newItem.setCountry(country);
				newItem.setYear(year);
				newItem.setContent(content);
				newItem.setRating(rating);
				// newItem.setCover(cover);
				itemList.add(0, newItem);
			}
		}

		return itemList;
	}

	public ArrayList<Item> getRemoteList() {
		return mRemoteList;
	}

	public void setRemoteList(ArrayList<Item> remoteList) {
		mRemoteList = remoteList;
	}

}