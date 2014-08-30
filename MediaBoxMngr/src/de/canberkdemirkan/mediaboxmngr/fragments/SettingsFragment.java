package de.canberkdemirkan.mediaboxmngr.fragments;

import java.util.Locale;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.preference.PreferenceFragment;
import android.util.DisplayMetrics;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.dialogs.AlertDialogDeleteAccount;

public class SettingsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {

	SharedPreferences mSharedPreferences;
	Preference mButtonDeleteAccount;

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
		switch (key) {
		case "l10n":
			Preference pref = findPreference(key);

			if (pref instanceof ListPreference) {
				ListPreference listPref = (ListPreference) pref;
				String entry = listPref.getEntry().toString();
				if (entry.equals("English")) {
					if (isAdded()) {
						setLocale("en_US");
					}
				}
				if (entry.equals("German")) {
					if (isAdded()) {
						setLocale("de");
					}
				}
			}
			break;

		default:
			break;
		}

	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference == mButtonDeleteAccount) {
			String header = getResources().getString(
					R.string.dialog_header_delete_account);
			AlertDialogDeleteAccount alertDialog = AlertDialogDeleteAccount
					.newInstance(getActivity(), header);
			alertDialog.show(getActivity().getSupportFragmentManager(), "");
			return true;
		}
		return false;
	}

	public void setLocale(String lang) {
		Locale locale = new Locale(lang);
		Resources resources = getResources();
		DisplayMetrics displayMetrics = resources.getDisplayMetrics();
		Configuration configuration = resources.getConfiguration();
		configuration.locale = locale;
		resources.updateConfiguration(configuration, displayMetrics);
	}

}