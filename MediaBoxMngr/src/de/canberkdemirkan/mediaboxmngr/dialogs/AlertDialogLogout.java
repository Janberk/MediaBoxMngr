package de.canberkdemirkan.mediaboxmngr.dialogs;

import java.io.Serializable;

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
import de.canberkdemirkan.mediaboxmngr.activities.LoginActivity;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.interfaces.UserAuthenticationConstants;

public class AlertDialogLogout extends DialogFragment {

	private Context mContext;

	public static AlertDialogLogout newInstance(Context context) {
		Bundle args = new Bundle();

		try {
			args.putSerializable(Constants.KEY_DIALOG_CONTEXT,
					(Serializable) context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		AlertDialogLogout dialogFragment = new AlertDialogLogout();
		dialogFragment.setArguments(args);
		return dialogFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mContext = (Context) getArguments().getSerializable(
				Constants.KEY_DIALOG_CONTEXT);
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.fragment_dialog_alert, null);

		return new AlertDialog.Builder(getActivity())
				.setView(view)
				.setIcon(R.drawable.ic_dialog_warning_light)
				.setTitle(
						mContext.getResources().getString(
								R.string.dialog_header_logout))
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
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
		SharedPreferences prefs = mContext.getSharedPreferences(
				Constants.KEY_MY_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.remove(UserAuthenticationConstants.KEY_EMAIL);
		editor.remove(UserAuthenticationConstants.KEY_PASSWORD);
		editor.commit();
		getActivity().finish();
		Intent intent = new Intent(getActivity().getApplicationContext(),
				LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}

}