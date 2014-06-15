package de.canberkdemirkan.mediaboxmngr.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import de.canberkdemirkan.mediaboxmngr.data.ProjectConstants;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.MusicAlbum;

public class CustomJSONSerializer {

	private Context mContext;
	private String mFileName;

	public CustomJSONSerializer(Context context, String fileName) {
		mContext = context;
		mFileName = fileName;
	}

	public ArrayList<Item> loadItems() throws IOException, JSONException {
		ArrayList<Item> itemList = new ArrayList<Item>();
		BufferedReader reader = null;
		try {
			// Open and read the file into a StringBuilder
			InputStream in = mContext.openFileInput(mFileName);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				// Line breaks are omitted and irrelevant
				jsonString.append(line);
			}
			// Parse the JSON using JSONTokener
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
					.nextValue();
			// Build the array of items from JSONObjects
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.getJSONObject(i);

				if (item.has(ProjectConstants.TYPE)) {
					ItemType type = ItemType.valueOf(item
							.getString(ProjectConstants.TYPE));
					switch (type) {
					case Album:
						itemList.add(0, new MusicAlbum(array.getJSONObject(i)));
						break;
					case Book:
						itemList.add(0, new Book(array.getJSONObject(i)));
						break;
					case Movie:
						itemList.add(0, new Movie(array.getJSONObject(i)));
						break;

					default:
						break;
					}
				}
			}
		} catch (FileNotFoundException e) {
			// Ignore this one; it happens when starting fresh
		} finally {
			if (reader != null)
				reader.close();
		}
		return itemList;
	}

	public void saveItems(ArrayList<Item> itemList) throws JSONException,
			IOException {

		// Build an array in JSON
		JSONArray array = new JSONArray();

		for (Item item : itemList)
			array.put(item.toJSON());
		// Write the file to disk
		Writer writer = null;

		try {
			OutputStream out = mContext.openFileOutput(mFileName,
					Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
		} finally {
			if (writer != null)
				writer.close();
		}

	}

}