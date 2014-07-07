package de.canberkdemirkan.mediaboxmngr.activities;

import android.support.v4.app.Fragment;
import de.canberkdemirkan.mediaboxmngr.fragments.ItemListFragment;
import de.canberkdemirkan.mediaboxmngr.fragments.ItemListFragment.ListTag;

public class ItemListActivity extends FragmentActivityBuilder {

	@SuppressWarnings("static-access")
	@Override
	protected Fragment createFragment() {

		ListTag listTag = (ListTag) getIntent().getSerializableExtra(
				ItemListFragment.KEY_LIST_TAG);
		if (listTag == null) {
			return new ItemListFragment();
		}
		return new ItemListFragment().newItemListFragment(listTag);
	}

}