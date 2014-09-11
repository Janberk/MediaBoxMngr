package de.canberkdemirkan.mediaboxmngr.fragments;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.preference.PreferenceFragment;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.dialogs.AlertDialogDeleteAccount;

public class SettingsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {

	SharedPreferences mSharedPreferences;
	Preference mButtonDeleteAccount;

	public static Fragment newInstance() {
		SettingsFragment newInstance = new SettingsFragment();
		return newInstance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
		PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences,
				false);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		mButtonDeleteAccount = (Preference) findPreference("deleteAccount");
		mButtonDeleteAccount.setOnPreferenceClickListener(this);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference == mButtonDeleteAccount) {
			String header = getResources().getString(
					R.string.dialog_header_delete_account);
			AlertDialogDeleteAccount alertDialog = AlertDialogDeleteAccount
					.newInstance(header);
			alertDialog.show(getActivity().getSupportFragmentManager(), "");
			return true;
		}
		return false;
	}

}