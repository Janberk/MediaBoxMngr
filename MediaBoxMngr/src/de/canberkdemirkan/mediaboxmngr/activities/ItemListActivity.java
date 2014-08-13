package de.canberkdemirkan.mediaboxmngr.activities;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.adapters.CustomItemAdapter;
import de.canberkdemirkan.mediaboxmngr.content.ListTag;
import de.canberkdemirkan.mediaboxmngr.fragments.CreateItemFragment;
import de.canberkdemirkan.mediaboxmngr.fragments.ItemListFragment;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.interfaces.OnItemCreatedListener;
import de.canberkdemirkan.mediaboxmngr.interfaces.OnRemoveFragmentListener;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class ItemListActivity extends FragmentActivityBuilder implements
		OnRemoveFragmentListener, OnItemCreatedListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3891006760403895847L;

	private FragmentManager mFragmentManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFragmentManager = getSupportFragmentManager();
	}

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

	@Override
	public void onRemoveFragment(String tag) {
		if (tag.equals(CreateItemFragment.TAG_CREATE_ITEM_FRAGMENT)) {
			CreateItemFragment fragment = (CreateItemFragment) mFragmentManager
					.findFragmentById(R.id.fragmentContainer);
			mFragmentManager.beginTransaction().remove(fragment).commit();
		}
	}

	@Override
	public void onItemCreated(String user) {
		ItemListFragment fragment = (ItemListFragment) mFragmentManager
				.findFragmentByTag(ItemListFragment.class.getName());
		ArrayList<Item> list = UtilMethods.createListFromTag(this, user,
				ListTag.ALL);
		CustomItemAdapter adapter = ((CustomItemAdapter) fragment.getListView()
				.getAdapter());
		adapter.refresh(list);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	}

}