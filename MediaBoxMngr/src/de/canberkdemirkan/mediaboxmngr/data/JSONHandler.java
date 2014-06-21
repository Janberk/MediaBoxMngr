package de.canberkdemirkan.mediaboxmngr.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.MusicAlbum;
import de.canberkdemirkan.mediaboxmngr.util.ItemType;
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
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "JSONHandler - loadItemsFromJSONArray()");
		}
		ArrayList<Item> itemList = new ArrayList<Item>();

		JSONObject json = new JSONObject(response);
		// JSONArray array = (JSONArray) new JSONTokener(json).nextValue();
		JSONArray array = json.getJSONArray("items");
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG,
					"JSONHandler - loadItemsFromJSONArray(): \n" + array);
		}

		for (int i = 0; i < array.length(); i++) {
			JSONObject item = array.getJSONObject(i);

			if (item.has(ProjectConstants.SQLITE_ID)
					|| item.has(ProjectConstants.USER)
					|| item.has(ProjectConstants.TITLE)
					|| item.has(ProjectConstants.TYPE)
					|| item.has(ProjectConstants.CREATION_DATE)) {
				long id = item.getLong(ProjectConstants.SQLITE_ID);
				String user = item.getString(ProjectConstants.USER);
				String title = item.getString(ProjectConstants.TITLE);
				ItemType type = ItemType.valueOf(item
						.getString(ProjectConstants.TYPE));
				String creationDate = item
						.getString(ProjectConstants.CREATION_DATE);

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