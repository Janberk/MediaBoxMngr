package de.canberkdemirkan.mediaboxmngr.activities;

import android.support.v4.app.Fragment;
import de.canberkdemirkan.mediaboxmngr.fragments.ItemListFragment;

public class ItemListActivity extends FragmentActivityBuilder {

	@Override
	protected Fragment createFragment() {
		return new ItemListFragment();
	}

}