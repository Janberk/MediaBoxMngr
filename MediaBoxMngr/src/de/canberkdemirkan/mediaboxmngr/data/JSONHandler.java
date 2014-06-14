package de.canberkdemirkan.mediaboxmngr.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.MusicAlbum;
import de.canberkdemirkan.mediaboxmngr.util.ItemType;

public class JSONHandler {

	private Context mContext;

	public JSONHandler(Context context) {
		mContext = context;
	}

	public ArrayList<Item> loadItemsFromJSONArray(String json)
			throws JSONException {
		System.out.println("loadItemsFromJSONArray");
		ArrayList<Item> itemList = new ArrayList<Item>();

		JSONArray array = (JSONArray) new JSONTokener(json).nextValue();
		System.out.println(array);

		for (int i = 0; i < array.length(); i++) {
			JSONObject item = array.getJSONObject(i);

			if (item.has(ProjectConstants.SQLITE_ID)
					|| item.has(ProjectConstants.USER)
					|| item.has(ProjectConstants.TYPE)) {
				long id = item.getLong(ProjectConstants.SQLITE_ID);
				String user = item.getString(ProjectConstants.USER);
				ItemType type = ItemType.valueOf(item
						.getString(ProjectConstants.TYPE));

				switch (type) {
				case Album:
					itemList.add(0, new MusicAlbum(id, user));
					break;
				case Book:
					itemList.add(0, new Book(id, user));
					break;
				case Movie:
					itemList.add(0, new Movie(id, user));
					break;

				default:
					break;
				}
			}
		}
		return itemList;
	}

}