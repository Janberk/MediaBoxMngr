package de.canberkdemirkan.mediaboxmngr.activities;

import android.support.v4.app.Fragment;
import android.view.MenuItem;
import de.canberkdemirkan.mediaboxmngr.fragments.SettingsFragment;

public class SettingsActivity extends FragmentActivityBuilder {

	@Override
	protected Fragment createFragment() {
		return new SettingsFragment();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}