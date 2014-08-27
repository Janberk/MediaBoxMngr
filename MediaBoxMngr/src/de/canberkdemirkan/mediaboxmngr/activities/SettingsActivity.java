package de.canberkdemirkan.mediaboxmngr.activities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.support.v4.app.Fragment;
import de.canberkdemirkan.mediaboxmngr.fragments.SettingsFragment;

public class SettingsActivity extends FragmentActivityBuilder implements
		OnSharedPreferenceChangeListener {

	@Override
	protected Fragment createFragment() {
		return new SettingsFragment();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		sharedPreferences.getString("pref_background", "dark");
		if (key.equals("pref_background")) {
		}
	}

}