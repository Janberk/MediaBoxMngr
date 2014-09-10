package de.canberkdemirkan.mediaboxmngr.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.activities.ItemListActivity;
import de.canberkdemirkan.mediaboxmngr.activities.LoginActivity;
import de.canberkdemirkan.mediaboxmngr.fragments.ItemListFragment;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.interfaces.UserAuthenticationConstants;
import de.canberkdemirkan.mediaboxmngr.util.AppContextUtil;

public class AlertDialogLogout extends DialogFragment {

	public static AlertDialogLogout newInstance() {
		AlertDialogLogout dialogFragment = new AlertDialogLogout();
		return dialogFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.fragment_dialog_alert, null);

		return new AlertDialog.Builder(getActivity())
				.setView(view)
				.setIcon(R.drawable.ic_dialog_warning_light)
				.setTitle(
						AppContextUtil.getContext().getResources()
								.getString(R.string.dialog_header_logout))
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dismiss();
								logout();
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

	private void logout() {
		SharedPreferences prefs = AppContextUtil.getContext()
				.getSharedPreferences(Constants.KEY_MY_PREFERENCES,
						Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.remove(UserAuthenticationConstants.KEY_EMAIL);
		editor.remove(UserAuthenticationConstants.KEY_PASSWORD);
		editor.commit();
		if (getActivity() instanceof ItemListActivity) {
			ItemListActivity activity = (ItemListActivity) getActivity();
			if (ItemListFragment.sCreateMode == true) {
				ItemListFragment.sCreateMode = false;
				activity.onFragmentTransaction(ItemListFragment.TAG_ITEMLIST_FRAGMENT);
			}
		}
		getActivity().finish();
		Intent intent = new Intent(getActivity().getApplicationContext(),
				LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}

}