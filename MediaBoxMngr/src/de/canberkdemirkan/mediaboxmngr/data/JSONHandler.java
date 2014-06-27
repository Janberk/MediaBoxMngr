package de.canberkdemirkan.mediaboxmngr.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.content.ItemType;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.MusicAlbum;
import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class JSONHandler {

	@SuppressWarnings("unused")
	private Context mContext;
	private ArrayList<Item> mRemoteList;

	public JSONHandler(Context context) {
		mContext = context;
	}

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

			if (item.has(Constants.SQLITE_ID) || item.has(Constants.USER)
					|| item.has(Constants.TITLE) || item.has(Constants.TYPE)
					|| item.has(Constants.CREATION_DATE)) {
				long id = item.getLong(Constants.SQLITE_ID);
				String user = item.getString(Constants.USER);
				String title = item.getString(Constants.TITLE);
				ItemType type = ItemType
						.valueOf(item.getString(Constants.TYPE));
				String creationDate = item.getString(Constants.CREATION_DATE);

				Item newItem = null;
				switch (type) {
				case Album:
					newItem = new MusicAlbum(id, user);
					break;
				case Book:
					newItem = new Book(id, user);
					break;
				case Movie:
					newItem = new Movie(id, user);
					break;

				default:
					break;
				}
				newItem.setTitle(title);
				newItem.setType(type);
				newItem.setCreationDate(UtilMethods
						.setCreationDateFromString(creationDate));
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