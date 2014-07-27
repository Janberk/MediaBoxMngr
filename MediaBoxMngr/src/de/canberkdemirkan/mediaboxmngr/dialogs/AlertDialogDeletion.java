package de.canberkdemirkan.mediaboxmngr.dialogs;

import java.io.Serializable;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.content.ListTag;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.fragments.ItemFragment;
import de.canberkdemirkan.mediaboxmngr.fragments.ItemListFragment;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.listeners.CustomTabListener;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class AlertDialogDeletion extends DialogFragment {

	public static final String DIALOG_TAG_ALL = "de.canberkdemirkan.mediaboxmngr.delete_all";
	public static final String DIALOG_TAG_SELECTED = "de.canberkdemirkan.mediaboxmngr.delete_selected";
	public static final String DIALOG_TAG_SINGLE = "de.canberkdemirkan.mediaboxmngr.delete_single";
	public static final String DIALOG_TAG_DETAIL_SINGLE = "de.canberkdemirkan.mediaboxmngr.delete_detail_single";

	private ItemListFragment mItemListFragment;
	private ItemFragment mItemFragment;
	private ArrayList<Item> mItemList;
	private Item mItem;
	private String mTitle;
	private String mTag;

	public static AlertDialogDeletion newInstance(Fragment fragment,
			ArrayList<Item> itemList, Item item, String title, String tag) {

		Bundle args = new Bundle();

		try {
			args.putSerializable(Constants.KEY_DIALOG_FRAGMENT,
					(Serializable) fragment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (itemList != null) {
			args.putSerializable(Constants.KEY_DIALOG_ITEM_LIST, itemList);
		}
		if (item != null) {
			args.putSerializable(Constants.KEY_DIALOG_ITEM, item);
		}
		if (title != null) {
			args.putSerializable(Constants.KEY_DIALOG_TITLE, title);
		}
		if (tag != null) {
			args.putSerializable(Constants.KEY_DIALOG_TAG, tag);
		}

		AlertDialogDeletion dialogFragment = new AlertDialogDeletion();
		dialogFragment.setArguments(args);
		return dialogFragment;

	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("unchecked")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		if (getArguments().getSerializable(Constants.KEY_DIALOG_FRAGMENT) instanceof ItemListFragment) {
			mItemListFragment = (ItemListFragment) getArguments()
					.getSerializable(Constants.KEY_DIALOG_FRAGMENT);
			try {
				mItemList = (ArrayList<Item>) getArguments().getSerializable(
						Constants.KEY_DIALOG_ITEM_LIST);
			} catch (ClassCastException e) {
				e.printStackTrace();
			}

		} else if (getArguments()
				.getSerializable(Constants.KEY_DIALOG_FRAGMENT) instanceof ItemFragment) {
			mItemFragment = (ItemFragment) getArguments().getSerializable(
					Constants.KEY_DIALOG_FRAGMENT);
			mItem = (Item) getArguments().getSerializable(
					Constants.KEY_DIALOG_ITEM);
		}

		mTitle = (String) getArguments().getSerializable(
				Constants.KEY_DIALOG_TITLE);

		mTag = (String) getArguments()
				.getSerializable(Constants.KEY_DIALOG_TAG);

		View view = getActivity().getLayoutInflater().inflate(
				R.layout.fragment_dialog_alert, null);

		return new AlertDialog.Builder(getActivity())
				.setView(view)
				.setIcon(R.drawable.ic_dialog_warning_light)
				.setTitle(mTitle)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								if (mTag.equals(DIALOG_TAG_SINGLE)) {
									deleteSelectedItem();
								} else if (mTag
										.equals(DIALOG_TAG_DETAIL_SINGLE)) {
									deleteItem();
								} else if (mTag.equals(DIALOG_TAG_SELECTED)) {
									deleteSelectedItem();
								} else if (mTag.equals(DIALOG_TAG_ALL)) {
									deleteAllItems();
								}
								sendResult(Activity.RESULT_OK);
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dismiss();
							}
						}).create();
	}

	private void deleteSelectedItem() {
		for (Item item : mItemList) {
			ItemStock.get(getActivity(), mItemListFragment.getUser())
					.getDAOItem().deleteItem(item);
		}
		if (CustomTabListener.sTag != null) {
			mItemList.clear();
			switch (CustomTabListener.sTag) {
			case ALL:
				mItemList = UtilMethods.createListFromTag(getActivity(),
						mItemListFragment.getUser(), ListTag.ALL);
				break;
			case ALBUMS:
				mItemList = UtilMethods.createListFromTag(getActivity(),
						mItemListFragment.getUser(), ListTag.ALBUMS);
				break;
			case BOOKS:
				mItemList = UtilMethods.createListFromTag(getActivity(),
						mItemListFragment.getUser(), ListTag.BOOKS);
				break;
			case MOVIES:
				mItemList = UtilMethods.createListFromTag(getActivity(),
						mItemListFragment.getUser(), ListTag.MOVIES);
				break;

			case FAVORITES:
				mItemList = UtilMethods.createListFromTag(getActivity(),
						mItemListFragment.getUser(), ListTag.FAVORITES);
				break;

			default:
				mItemList = UtilMethods.createListFromTag(getActivity(),
						mItemListFragment.getUser(), ListTag.ALL);
				break;
			}
		}
	}

	private void deleteAllItems() {
		mItemList.clear();
		ItemStock
				.get(mItemListFragment.getActivity(),
						mItemListFragment.getUser()).getDAOItem()
				.deleteAllItems(mItemListFragment.getUser());
	}

	private void deleteItem() {
		ItemStock.get(getActivity(), mItemFragment.getUser()).getDAOItem()
				.deleteItem(mItem);
		getActivity().finish();
	}

	private void sendResult(int resultCode) {
		if (getTargetFragment() == null)
			return;
		Intent i = new Intent();
		i.putExtra(Constants.EXTRA_DIALOG_LIST, mItemList);
		getTargetFragment().onActivityResult(getTargetRequestCode(),
				resultCode, i);
	}

}