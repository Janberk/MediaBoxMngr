package de.canberkdemirkan.mediaboxmngr.dialogs;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.fragments.ItemListFragment;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Item;

public class AlertDialogDeletion extends DialogFragment {

	public static final String DIALOG_TAG_ALL = "de.canberkdemirkan.mediaboxmngr.delete_all";
	public static final String DIALOG_TAG_SINGLE = "de.canberkdemirkan.mediaboxmngr.delete_single";

	private int mPosition;
	private ItemListFragment mFragment;
	private ArrayList<Item> mItemList;
	private String mTitle;
	private String mTag;

	public static AlertDialogDeletion newInstance(ItemListFragment fragment,
			ArrayList<Item> itemList, String title, int position, String tag) {

		Bundle args = new Bundle();

		args.putSerializable(Constants.KEY_DIALOG_FRAGMENT, fragment);
		args.putSerializable(Constants.KEY_DIALOG_ITEM_LIST, itemList);
		args.putSerializable(Constants.KEY_DIALOG_TITLE, title);
		args.putSerializable(Constants.KEY_DIALOG_POSITION, position);
		args.putSerializable(Constants.KEY_DIALOG_TAG, tag);

		AlertDialogDeletion dialogFragment = new AlertDialogDeletion();
		dialogFragment.setArguments(args);
		return dialogFragment;

	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("unchecked")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mFragment = (ItemListFragment) getArguments().getSerializable(
				Constants.KEY_DIALOG_FRAGMENT);
		try {
			mItemList = (ArrayList<Item>) getArguments().getSerializable(
					Constants.KEY_DIALOG_ITEM_LIST);
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		mTitle = (String) getArguments().getSerializable(
				Constants.KEY_DIALOG_TITLE);
		mPosition = (Integer) getArguments().getSerializable(
				Constants.KEY_DIALOG_POSITION);
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
									Item item = mItemList.get(mPosition);
									mItemList.remove(mPosition);
									ItemStock
											.get(mFragment.getActivity(),
													mFragment.getUser())
											.getDAOItem().deleteItem(item);
									ItemListFragment.sDeleteMode = false;
								} else if (mTag.equals(DIALOG_TAG_ALL)) {
									mItemList.clear();
									ItemStock
											.get(mFragment.getActivity(),
													mFragment.getUser())
											.getDAOItem()
											.deleteAllItems(mFragment.getUser());
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

	private void sendResult(int resultCode) {
		if (getTargetFragment() == null)
			return;
		Intent i = new Intent();
		i.putExtra(Constants.EXTRA_DIALOG_LIST, mItemList);
		getTargetFragment().onActivityResult(getTargetRequestCode(),
				resultCode, i);
	}

}