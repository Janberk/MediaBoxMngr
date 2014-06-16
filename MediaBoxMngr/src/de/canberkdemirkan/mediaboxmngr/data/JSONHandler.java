package de.canberkdemirkan.mediaboxmngr.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.MusicAlbum;
import de.canberkdemirkan.mediaboxmngr.util.ItemType;

public class JSONHandler {

	@SuppressWarnings("unused")
	private Context mContext;
	private ArrayList<Item> mRemoteList;

	public JSONHandler(Context context) {
		mContext = context;
	}

	public ArrayList<Item> loadItemsFromJSONArray(String response)
			throws JSONException {
		System.out.println("loadItemsFromJSONArray()");
		ArrayList<Item> itemList = new ArrayList<Item>();

		JSONObject json = new JSONObject(response);
		// JSONArray array = (JSONArray) new JSONTokener(json).nextValue();
		JSONArray array = json.getJSONArray("items");
		System.out.println(array);

		for (int i = 0; i < array.length(); i++) {
			JSONObject item = array.getJSONObject(i);

			if (item.has(ProjectConstants.SQLITE_ID)
					|| item.has(ProjectConstants.USER)
					|| item.has(ProjectConstants.TITLE)
					|| item.has(ProjectConstants.TYPE)) {
				long id = item.getLong(ProjectConstants.SQLITE_ID);
				String user = item.getString(ProjectConstants.USER);
				String title = item.getString(ProjectConstants.TITLE);
				ItemType type = ItemType.valueOf(item
						.getString(ProjectConstants.TYPE));

				Item newItem = null;
				switch (type) {
				case Album:
					newItem = new MusicAlbum(id, user);
					newItem.setTitle(title);
					itemList.add(0, newItem);
					break;
				case Book:
					newItem = new Book(id, user);
					newItem.setTitle(title);
					itemList.add(0, newItem);
					break;
				case Movie:
					newItem = new Movie(id, user);
					newItem.setTitle(title);
					itemList.add(0, newItem);
					break;

				default:
					break;
				}
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