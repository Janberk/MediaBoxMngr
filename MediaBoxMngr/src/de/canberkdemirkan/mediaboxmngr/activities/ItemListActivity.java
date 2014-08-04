package de.canberkdemirkan.mediaboxmngr.activities;

import android.support.v4.app.Fragment;
import de.canberkdemirkan.mediaboxmngr.content.ListTag;
import de.canberkdemirkan.mediaboxmngr.fragments.ItemListFragment;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;

public class ItemListActivity extends FragmentActivityBuilder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3891006760403895847L;

	@SuppressWarnings("static-access")
	@Override
	protected Fragment createFragment() {

		ListTag listTag = (ListTag) getIntent().getSerializableExtra(
				Constants.KEY_LIST_TAG);
		if (listTag == null) {
			return new ItemListFragment();
		}
		return new ItemListFragment().newInstance(listTag);
	}

}