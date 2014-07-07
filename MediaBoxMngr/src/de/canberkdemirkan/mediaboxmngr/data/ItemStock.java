package de.canberkdemirkan.mediaboxmngr.data;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;
import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.util.CustomJSONSerializer;

public class ItemStock {

	public static String USER = null;

	private static final String TAG = "ItemStock";
	private static final String FILE_NAME = "items.json";

	private ArrayList<Item> mItemList;
	private ArrayList<Item> mSerializedItemList;
	private CustomJSONSerializer mSerializer;

	private static ItemStock sItemStock;
	private Context mAppContext;

	private DAOItem mDAOItem;

	public ItemStock(Context appContext, String user) {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemStock - ItemStock()");
		}
		USER = user;
		mAppContext = appContext;

		mDAOItem = new DAOItem(mAppContext);

		mSerializer = new CustomJSONSerializer(mAppContext, FILE_NAME);

		try {
			mSerializedItemList = mSerializer.loadItems();
		} catch (Exception e) {
			mSerializedItemList = new ArrayList<Item>();
			Log.e(TAG, "Error loading items: ", e);
		}
		mItemList = mDAOItem.getAllItems(user);

	}

	public static ItemStock get(Context context, String user) {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemStock - get()");
		}
		if (sItemStock == null || !user.equals(USER)) {
			sItemStock = new ItemStock(context.getApplicationContext(), user);
		}
		return sItemStock;
	}

	public boolean saveSerializedItems() {

		try {
			mSerializer.saveItems(mSerializedItemList);
			Log.d(TAG, "Items saved to file!");
			return true;
		} catch (Exception e) {
			Log.e(TAG, "Error saving items: ", e);
			return false;
		}

	}

	public String getSQLiteAsJSON(String user) {
		return mDAOItem.buildJSONfromSQLite(user);
	}

	public void addItem(Item item) {
		mItemList.add(0, item);
		mDAOItem.insertItem(item);
	}

	public void updateItem(Item item) {
		mDAOItem.updateItem(item);
	}

	public Item getItem(UUID id) {
		for (Item item : mItemList) {
			if (item.getUniqueId().equals(id)) {
				return item;
			}
		}
		return null;
	}

	public ArrayList<Item> getItemList() {
		return mItemList;
	}

	public void setItemList(ArrayList<Item> itemList) {
		mItemList = itemList;
	}

	public DAOItem getDAOItem() {
		return mDAOItem;
	}

}