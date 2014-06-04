package de.canberkdemirkan.mediaboxmngr.data;

import java.util.ArrayList;
import java.util.UUID;

import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.util.CustomJSONSerializer;
import android.content.Context;
import android.util.Log;

public class ItemStock {

	private static final String TAG = "ItemStock";
	private static final String FILE_NAME = "items.json";

	private ArrayList<Item> mItemList;
	private CustomJSONSerializer mSerializer;

	private static ItemStock sItemStock;
	private Context mAppContext;

	private DAOItem mDAOItem;

	public ItemStock(Context appContext) {
		mAppContext = appContext;
		mDAOItem = new DAOItem(mAppContext);
//		mSerializer = new CustomJSONSerializer(mAppContext, FILE_NAME);
//		mItemList = new ArrayList<Item>();
//
//		try {
//			mItemList = mSerializer.loadItems();
//		} catch (Exception e) {
//			mItemList = new ArrayList<Item>();
//			Log.e(TAG, "Error loading items: ", e);
//		}
		mItemList = mDAOItem.getAllItems();
	}

	public static ItemStock get(Context context) {
		if (sItemStock == null) {
			sItemStock = new ItemStock(context.getApplicationContext());
		}
		return sItemStock;
	}

	public ArrayList<Item> getItemList() {
		return mItemList;
	}

	public boolean saveItems() {

		try {
			mSerializer.saveItems(mItemList);
			Log.d(TAG, "Items saved to file!");
			return true;
		} catch (Exception e) {
			Log.e(TAG, "Error saving items: ", e);
			return false;
		}

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

	public DAOItem getDAOItem() {
		return mDAOItem;
	}

}