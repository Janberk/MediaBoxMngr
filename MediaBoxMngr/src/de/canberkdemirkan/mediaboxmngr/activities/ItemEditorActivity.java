package de.canberkdemirkan.mediaboxmngr.activities;

import java.util.UUID;

import android.support.v4.app.Fragment;
import de.canberkdemirkan.mediaboxmngr.fragments.ItemEditorFragment;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Item;

public class ItemEditorActivity extends FragmentActivityBuilder {

	@Override
	protected Fragment createFragment() {

		UUID itemId = (UUID) getIntent().getSerializableExtra(
				Constants.KEY_ITEM_UID);
		String userTag = (String) getIntent().getSerializableExtra(
				Constants.KEY_USER_TAG);
		Item item = (Item) getIntent().getSerializableExtra(
				Constants.EXTRA_DETAILS_ITEM);

		return ItemEditorFragment.newInstance(itemId, userTag, item);
	}

}