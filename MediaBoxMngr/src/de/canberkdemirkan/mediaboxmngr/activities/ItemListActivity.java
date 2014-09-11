package de.canberkdemirkan.mediaboxmngr.activities;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.adapters.CustomItemAdapter;
import de.canberkdemirkan.mediaboxmngr.content.ListTag;
import de.canberkdemirkan.mediaboxmngr.dialogs.AlertDialogDeletion;
import de.canberkdemirkan.mediaboxmngr.dialogs.AlertDialogLogout;
import de.canberkdemirkan.mediaboxmngr.fragments.CreateItemFragment;
import de.canberkdemirkan.mediaboxmngr.fragments.ItemListFragment;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.interfaces.OnFragmentTransactionListener;
import de.canberkdemirkan.mediaboxmngr.interfaces.OnItemCreatedListener;
import de.canberkdemirkan.mediaboxmngr.interfaces.OnListAdapterRefreshedListener;
import de.canberkdemirkan.mediaboxmngr.interfaces.OnShowAlertDialogListener;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class ItemListActivity extends FragmentActivityBuilder implements
		OnFragmentTransactionListener, OnItemCreatedListener,
		OnListAdapterRefreshedListener, OnShowAlertDialogListener {

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
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		if (tag.equals(ItemListFragment.TAG_ITEMLIST_FRAGMENT)) {
			if (ItemListFragment.sCreateMode) {
				ItemListFragment itemListFragment = (ItemListFragment) mFragmentManager
						.findFragmentByTag(ItemListFragment.class.getName());
				String user = itemListFragment.getUser();
				CreateItemFragment createItemFragment = (CreateItemFragment) mFragmentManager
						.findFragmentByTag(CreateItemFragment.class.getName());
				if (createItemFragment == null) {
					createItemFragment = CreateItemFragment.newInstance(user);
				}
				ft.add(R.id.fragmentContainer, createItemFragment,
						CreateItemFragment.class.getName());
				ft.addToBackStack(ItemListFragment.class.getName());
				ft.commit();
			}
			if (!ItemListFragment.sCreateMode) {
				CreateItemFragment createItemFragment = (CreateItemFragment) mFragmentManager
						.findFragmentByTag(CreateItemFragment.class.getName());
				ft.remove(createItemFragment);
				ft.commit();
			}
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
		ItemListFragment.sCreateMode = false;
	}

	@Override
	public void onShowAlertDialog(String tag, String header,
			String intentionTag, ArrayList<Item> items) {
		if (tag.equals(ItemListFragment.TAG_ITEMLIST_FRAGMENT)) {
			if (intentionTag.equals(Constants.TAG_LOGOUT)) {
				AlertDialogLogout dialog = AlertDialogLogout.newInstance();
				dialog.show(mFragmentManager, "");
				return;
			}
			if (intentionTag.equals(AlertDialogDeletion.DIALOG_TAG_SINGLE)) {
				ItemListFragment fragment = (ItemListFragment) mFragmentManager
						.findFragmentByTag(ItemListFragment.class.getName());
				AlertDialogDeletion dialog = AlertDialogDeletion.newInstance(
						fragment, items, null, header, intentionTag);
				dialog.setTargetFragment(fragment,
						Constants.REQUEST_LIST_DELETE);
				dialog.show(mFragmentManager, "");
				return;
			}
			if (intentionTag.equals(AlertDialogDeletion.DIALOG_TAG_SELECTED)) {
				ItemListFragment fragment = (ItemListFragment) mFragmentManager
						.findFragmentByTag(ItemListFragment.class.getName());
				AlertDialogDeletion dialog = AlertDialogDeletion.newInstance(
						fragment, items, null, header, intentionTag);
				dialog.setTargetFragment(fragment,
						Constants.REQUEST_LIST_DELETE);
				dialog.show(mFragmentManager, "");
				return;
			}
			ItemListFragment fragment = (ItemListFragment) mFragmentManager
					.findFragmentByTag(ItemListFragment.class.getName());
			AlertDialogDeletion dialog = AlertDialogDeletion.newInstance(
					fragment, fragment.getItemList(), null, header,
					intentionTag);
			dialog.setTargetFragment(fragment, Constants.REQUEST_LIST_DELETE);
			dialog.show(mFragmentManager, "");
		}
	}

	@Override
	public void onListAdapterRefreshed(ArrayList<Item> itemList) {
		ItemListFragment fragment = (ItemListFragment) mFragmentManager
				.findFragmentByTag(ItemListFragment.class.getName());
		fragment.getItemAdapter().refresh(itemList);
	}

	@Override
	public void onBackPressed() {
		if (getSupportActionBar().getNavigationMode() == ActionBar.NAVIGATION_MODE_STANDARD) {
			getSupportActionBar().setNavigationMode(
					ActionBar.NAVIGATION_MODE_TABS);
		}
		if (ItemListFragment.sCreateMode == true) {
			ItemListFragment.sCreateMode = false;
			onFragmentTransaction(ItemListFragment.TAG_ITEMLIST_FRAGMENT);
			return;
		}
		if (ItemListFragment.sCreateMode == false) {
			AlertDialogLogout dialog = AlertDialogLogout.newInstance();
			dialog.show(mFragmentManager, "");
		}
	}

}