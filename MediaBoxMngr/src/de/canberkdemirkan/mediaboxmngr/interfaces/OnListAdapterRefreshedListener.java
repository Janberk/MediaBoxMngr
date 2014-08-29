package de.canberkdemirkan.mediaboxmngr.interfaces;

import java.util.ArrayList;

import de.canberkdemirkan.mediaboxmngr.model.Item;

public interface OnListAdapterRefreshedListener {
	void onListAdapterRefreshed(ArrayList<Item> itemList);
}