package de.canberkdemirkan.mediaboxmngr.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.data.DeleteAccountRequestHandler;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.interfaces.UserAuthenticationConstants;

public class AlertDialogDeleteAccount extends DialogFragment {

	private String mTitle;

	public static AlertDialogDeleteAccount newInstance(Context context,
			String title) {

		Bundle args = new Bundle();
		if (title != null) {
			args.putSerializable(Constants.KEY_DIALOG_TITLE, title);
		}

		AlertDialogDeleteAccount dialogFragment = new AlertDialogDeleteAccount();
		dialogFragment.setArguments(args);
		return dialogFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

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
								dismiss();
								deleteAllItems();
								DeleteAccountRequestHandler requestHandler = new DeleteAccountRequestHandler(
										getActivity());
								requestHandler.deleteUserAccount();
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

	private void deleteAllItems() {
		SharedPreferences preferences = getActivity().getSharedPreferences(
				Constants.KEY_MY_PREFERENCES, Context.MODE_PRIVATE);
		String user = preferences.getString(
				UserAuthenticationConstants.KEY_EMAIL, "");
		ItemStock.get(getActivity(), user).getDAOItem().deleteAllItems(user);
	}
}