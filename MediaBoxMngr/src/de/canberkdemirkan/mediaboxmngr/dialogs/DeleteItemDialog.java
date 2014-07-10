package de.canberkdemirkan.mediaboxmngr.dialogs;

import java.util.ArrayList;

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

public class DeleteItemDialog extends DialogFragment {

	public static final String EXTRA_DIALOG_LIST = "de.canberkdemirkan.mediaboxmngr.extra_dialog_list";

	private int mPosition;
	private ItemListFragment mFragment;
	private ArrayList<Item> mItemList;
	private String mTitle;

	public static DeleteItemDialog newInstance(int position,
			ItemListFragment fragment, ArrayList<Item> itemList, String title) {

		Bundle args = new Bundle();

		args.putSerializable(Constants.KEY_DIALOG_POSITION, position);
		args.putSerializable(Constants.KEY_DIALOG_FRAGMENT, fragment);
		args.putSerializable(Constants.KEY_DIALOG_ITEM_LIST, itemList);
		args.putSerializable(Constants.KEY_DIALOG_TITLE, title);

		DeleteItemDialog dialogFragment = new DeleteItemDialog();
		dialogFragment.setArguments(args);
		return dialogFragment;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mPosition = (Integer) getArguments().getSerializable(
				Constants.KEY_DIALOG_POSITION);
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
								Item item = mItemList.get(mPosition);
								mItemList.remove(mPosition);
								ItemStock
										.get(mFragment.getActivity(),
												mFragment.getUser())
										.getDAOItem().deleteItem(item);
								ItemListFragment.sDeleteMode = false;
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
		i.putExtra(EXTRA_DIALOG_LIST, mItemList);
		getTargetFragment().onActivityResult(getTargetRequestCode(),
				resultCode, i);
	}
}