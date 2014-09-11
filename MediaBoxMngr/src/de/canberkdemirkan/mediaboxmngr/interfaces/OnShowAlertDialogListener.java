package de.canberkdemirkan.mediaboxmngr.interfaces;

import java.util.ArrayList;

import de.canberkdemirkan.mediaboxmngr.model.Item;

public interface OnShowAlertDialogListener {
	void onShowAlertDialog(String tag, String header, String intentionTag,
			ArrayList<Item> items);
}