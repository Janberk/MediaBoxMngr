package de.canberkdemirkan.mediaboxmngr.activities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import de.canberkdemirkan.mediaboxmngr.fragments.SettingsFragment;

public class SettingsActivity extends FragmentActivityBuilder implements
		OnSharedPreferenceChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1300834548951259237L;

	@Override
	protected Fragment createFragment() {
		return new SettingsFragment();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		sharedPreferences.getString("pref_background", "dark");
		if (key.equals("pref_background")) {
            Preference pref = null;
            //pref.
            // Set summary to be the user-description for the selected value
            //pref.setSummary(sharedPreferences.getString(key, ""));
        }
	}

}