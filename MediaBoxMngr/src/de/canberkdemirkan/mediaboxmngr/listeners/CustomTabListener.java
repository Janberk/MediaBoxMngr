package de.canberkdemirkan.mediaboxmngr.listeners;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.fragments.ItemListFragment;
import de.canberkdemirkan.mediaboxmngr.fragments.ItemListFragment.ListTag;
import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

@SuppressLint("NewApi")
public class CustomTabListener implements
		android.support.v7.app.ActionBar.TabListener {

	private ItemListFragment mFragment;
	private FragmentActivity mActivity;
	private String mUser;

	public CustomTabListener(FragmentActivity activity,
			ItemListFragment fragment) {
		mFragment = fragment;
		mActivity = activity;
		ItemStock.get(mActivity, mFragment.getUser());
		mUser = mFragment.getUser();
	}

	@Override
	public void onTabReselected(android.support.v7.app.ActionBar.Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(android.support.v7.app.ActionBar.Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		// FragmentManager fm = mActivity.getSupportFragmentManager();
		ListTag tag = (ListTag) tab.getTag();
		mFragment.getItemAdapter().refresh(
				UtilMethods.createListFromTag(mActivity, mUser, tag));
		// fm.beginTransaction().replace(R.id.fragmentContainer, mFragment)
		// .commit();
	}

	@Override
	public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab,
			android.support.v4.app.FragmentTransaction ft) {
		FragmentManager fm = mActivity.getSupportFragmentManager();
		if (mFragment == null) {
			fm.beginTransaction().remove(mFragment).commit();
		}
	}

}