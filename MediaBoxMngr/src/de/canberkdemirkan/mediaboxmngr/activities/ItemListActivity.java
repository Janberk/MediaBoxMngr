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
import de.canberkdemirkan.mediaboxmngr.interfaces.OnFragmentTransactionListener;
import de.canberkdemirkan.mediaboxmngr.interfaces.OnItemCreatedListener;
import de.canberkdemirkan.mediaboxmngr.interfaces.OnListAdapterRefreshedListener;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class ItemListActivity extends FragmentActivityBuilder implements
		OnFragmentTransactionListener, OnItemCreatedListener,
		OnListAdapterRefreshedListener {

	private FragmentManager mFragmentManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFragmentManager = getSupportFragmentManager();
	}

	@Override
	protected Fragment createFragment() {

		ListTag listTag = (ListTag) getIntent().getSerializableExtra(
				Constants.KEY_LIST_TAG);
		if (listTag == null) {
			return ItemListFragment.newInstance(ListTag.ALL);
		}
		return ItemListFragment.newInstance(listTag);
	}

	@Override
	public void onFragmentTransaction(String tag) {
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
		int count = 0;
		for (Item item : list) {
			if (!item.isSynced()) {
				count++;
			}
		}
		CustomItemAdapter adapter = ((CustomItemAdapter) fragment.getListView()
				.getAdapter());
		adapter.refresh(list);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		if (count > 0) {
			fragment.showSyncStatus();
		}
	}

	@Override
	public void onListAdapterRefreshed(ArrayList<Item> itemList) {
		ItemListFragment fragment = (ItemListFragment) mFragmentManager
				.findFragmentByTag(ItemListFragment.class.getName());
		fragment.getItemAdapter().refresh(itemList);
	}

}