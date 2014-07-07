package de.canberkdemirkan.mediaboxmngr.activities;

import android.support.v4.app.Fragment;
import de.canberkdemirkan.mediaboxmngr.content.ListTag;
import de.canberkdemirkan.mediaboxmngr.fragments.ItemListFragment;

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