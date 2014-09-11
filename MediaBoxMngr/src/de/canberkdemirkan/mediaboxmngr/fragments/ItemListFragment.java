package de.canberkdemirkan.mediaboxmngr.fragments;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.ListView;
import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.activities.ItemPagerActivity;
import de.canberkdemirkan.mediaboxmngr.activities.SettingsActivity;
import de.canberkdemirkan.mediaboxmngr.adapters.CustomItemAdapter;
import de.canberkdemirkan.mediaboxmngr.content.ListTag;
import de.canberkdemirkan.mediaboxmngr.data.DummyDataProvider;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.dialogs.AlertDialogDeletion;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.interfaces.CustomTabListener;
import de.canberkdemirkan.mediaboxmngr.interfaces.OnFragmentTransactionListener;
import de.canberkdemirkan.mediaboxmngr.interfaces.OnShowAlertDialogListener;
import de.canberkdemirkan.mediaboxmngr.interfaces.UserAuthenticationConstants;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class ItemListFragment extends Fragment implements Serializable,
		AdapterView.OnItemClickListener, MultiChoiceModeListener, TextWatcher,
		OnFragmentTransactionListener, OnShowAlertDialogListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1188941681931560001L;

	public static final String TAG_ITEMLIST_FRAGMENT = "de.canberkdemirkan.mediaboxmngr.tag_itemlist_fragment";

	private OnFragmentTransactionListener mFragmentTransactionListener;
	private OnShowAlertDialogListener mShowAlertDialogListener;

	public static boolean sCreateMode;
	public static ListTag sListTag;

	private String mUser;
	private int mCABSelectionCount = 0;

	private SharedPreferences mSharedPreferences;
	private FragmentManager mFragmentManager;
	private ActionBar mActionBar;
	private ActionBar.Tab tabAll, tabMusic, tabBooks, tabMovies, tabFavorites,
			tabTopRated;

	private ListView mListView;
	private ArrayList<Item> mItemList;
	private CustomItemAdapter mItemAdapter;

	private ActionMode mActionMode;

	public static ItemListFragment newInstance(ListTag listTag) {
		Bundle args = new Bundle();
		args.putSerializable(Constants.KEY_LIST_TAG, listTag);

		ItemListFragment fragment = new ItemListFragment();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);

		mFragmentManager = getActivity().getSupportFragmentManager();
		mSharedPreferences = getActivity().getSharedPreferences(
				Constants.KEY_MY_PREFERENCES, Context.MODE_PRIVATE);
		mUser = mSharedPreferences.getString(
				UserAuthenticationConstants.KEY_EMAIL, "");

		Bundle bundle = getArguments();

		if (bundle == null) {
			sListTag = ListTag.ALL;
		} else {
			sListTag = (ListTag) bundle.get(Constants.KEY_LIST_TAG);
		}

		mItemList = UtilMethods.createListFromTag(getActivity(), mUser,
				sListTag);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_item_list, null);

		mListView = (ListView) view
				.findViewById(R.id.listView_fragmentItemList);

		mActionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mItemAdapter = new CustomItemAdapter(getActivity(), mItemList);
		mItemAdapter.setNotifyOnChange(true);

		mListView.setAdapter(mItemAdapter);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			registerForContextMenu(mListView);
		} else {
			mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			mListView.setMultiChoiceModeListener(this);
		}

		mListView.setOnItemClickListener(this);
		mListView.setItemsCanFocus(false);

		addActionBarTabs();

		return view;
	}

	public void addActionBarTabs() {
		tabAll = mActionBar.newTab().setText(ListTag.ALL.name())
				.setTag(ListTag.ALL);
		tabMusic = mActionBar.newTab().setText(ListTag.MUSIC.name())
				.setTag(ListTag.MUSIC);
		tabBooks = mActionBar.newTab().setText(ListTag.BOOKS.name())
				.setTag(ListTag.BOOKS);
		tabMovies = mActionBar.newTab().setText(ListTag.MOVIES.name())
				.setTag(ListTag.MOVIES);
		tabFavorites = mActionBar.newTab().setText(ListTag.FAVORITES.name())
				.setTag(ListTag.FAVORITES);
		tabTopRated = mActionBar.newTab().setText("Top Rated")
				.setTag(ListTag.TOPRATED);

		ItemListFragment fragment = (ItemListFragment) mFragmentManager
				.findFragmentById(R.id.fragmentContainer);

		tabAll.setTabListener(new CustomTabListener(getActivity(), fragment));
		tabMusic.setTabListener(new CustomTabListener(getActivity(), fragment));
		tabBooks.setTabListener(new CustomTabListener(getActivity(), fragment));
		tabMovies
				.setTabListener(new CustomTabListener(getActivity(), fragment));
		tabFavorites.setTabListener(new CustomTabListener(getActivity(),
				fragment));
		tabTopRated.setTabListener(new CustomTabListener(getActivity(),
				fragment));

		mActionBar.addTab(tabAll);
		mActionBar.addTab(tabBooks);
		mActionBar.addTab(tabMovies);
		mActionBar.addTab(tabMusic);
		mActionBar.addTab(tabFavorites);
		mActionBar.addTab(tabTopRated);
	}

	private void showItemDetails() {
		int count = 0;

		for (int i = 0; i < mItemAdapter.getCount(); i++) {
			if (getListView().isItemChecked(i)) {
				count++;
				if (count == 1) {
					Item selectedItem = mItemAdapter.getItem(i);
					Intent intent = new Intent(getActivity(),
							ItemPagerActivity.class);
					intent.putExtra(Constants.KEY_ITEM_UID,
							selectedItem.getUniqueId());
					intent.putExtra(Constants.KEY_USER_TAG, mUser);
					intent.putExtra(Constants.KEY_TYPE, selectedItem.getType()
							.toString());
					startActivityForResult(intent, 0);
				}
			}
		}
	}

	private void deleteAllItems() {
		String header = getActivity().getResources().getString(
				R.string.dialog_header_delete_all);
		onShowAlertDialog(TAG_ITEMLIST_FRAGMENT, header,
				AlertDialogDeletion.DIALOG_TAG_ALL, null);
	}

	private void deleteSelectedItems() {
		final String header = getActivity().getResources().getString(
				R.string.dialog_header_delete_selected);
		ArrayList<Item> selectedItems = new ArrayList<Item>();
		String addition = null;
		int count = 0;

		for (int i = 0; i < mItemAdapter.getCount(); i++) {
			if (getListView().isItemChecked(i)) {
				count++;
				Item selectedItem = mItemAdapter.getItem(i);
				selectedItems.add(selectedItem);
				String title = selectedItem.getTitle();
				if (count == 1) {
					addition = title;
				}
			}
		}

		if (count == 1) {
			onShowAlertDialog(TAG_ITEMLIST_FRAGMENT, header + "\n" + addition,
					AlertDialogDeletion.DIALOG_TAG_SINGLE, selectedItems);
		}
		if (count > 1) {
			addition = count + " of " + mItemAdapter.getCount();
			onShowAlertDialog(TAG_ITEMLIST_FRAGMENT, header + "\n" + addition,
					AlertDialogDeletion.DIALOG_TAG_SELECTED, selectedItems);
		}

	}

	private void switchEditMode() {
		if (!sCreateMode) {
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			sCreateMode = true;
			onFragmentTransaction(TAG_ITEMLIST_FRAGMENT);
		} else if (sCreateMode) {
			sCreateMode = false;
			onFragmentTransaction(TAG_ITEMLIST_FRAGMENT);
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;
		if (requestCode == Constants.REQUEST_LIST_DELETE) {

			try {
				mItemList = (ArrayList<Item>) data
						.getSerializableExtra(Constants.EXTRA_DIALOG_LIST);
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
			mItemList = UtilMethods.createListFromTag(getActivity(), mUser,
					sListTag);
			mItemAdapter.refresh(mItemList);

		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_menu_list, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case R.id.menu_newItem:
			switchEditMode();
			return true;
		case R.id.menu_deleteAll:
			deleteAllItems();
			return true;
		case R.id.menu_dummyData:
			createDummyList();
			return true;
		case R.id.menu_settings:
			Intent i = new Intent(getActivity(), SettingsActivity.class);
			startActivity(i);
			return true;
		case R.id.menu_logout:
			String header = getActivity().getResources().getString(
					R.string.dialog_header_logout);
			onShowAlertDialog(TAG_ITEMLIST_FRAGMENT, header,
					Constants.TAG_LOGOUT, null);
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}

	private void createDummyList() {
		DummyDataProvider data = new DummyDataProvider(mUser);
		ArrayList<Item> list = data.getItemList();
		ItemStock itemStock = ItemStock.get(getActivity(), mUser);
		for (Item item : list) {
			itemStock.addItem(item);
		}
		mItemAdapter.refresh(itemStock.getItemList());
	}

	@Override
	public void onAttach(Activity activity) {
		try {
			mFragmentTransactionListener = (OnFragmentTransactionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(getActivity().getClass()
					.getSimpleName()
					+ " must implement OnFragmentTransactionListener.");
		}
		try {
			mShowAlertDialogListener = (OnShowAlertDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(getActivity().getClass()
					.getSimpleName()
					+ " must implement OnShowAlertDialogListener.");
		}
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemListFragment - onResume()");
		}
		mItemList = UtilMethods.createListFromTag(getActivity(), mUser,
				sListTag);
		mItemAdapter.refresh(mItemList);
		super.onResume();
	}

	public String getUser() {
		return mUser;
	}

	public ListView getListView() {
		return mListView;
	}

	public ListTag getListTag() {
		return sListTag;
	}

	public CustomItemAdapter getItemAdapter() {
		return mItemAdapter;
	}

	public ArrayList<Item> getItemList() {
		return mItemList;
	}

	// click listeners
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == mListView) {
			if (mActionMode == null) {
				Item item = (Item) mListView.getAdapter().getItem(position);
				Intent i = new Intent(getActivity(), ItemPagerActivity.class);
				i.putExtra(Constants.KEY_ITEM_UID, item.getUniqueId());
				i.putExtra(Constants.KEY_USER_TAG, mUser);
				startActivityForResult(i, 0);
			}
		}
	}

	// ActionMode Callbacks
	@Override
	public void onItemCheckedStateChanged(ActionMode mode, int position,
			long id, boolean checked) {
		if (checked) {
			mCABSelectionCount++;
		} else {
			mCABSelectionCount--;
		}

		mode.invalidate();
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.fragment_menu_cab, menu);
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		if (mCABSelectionCount < 2) {
			MenuItem item = menu.findItem(R.id.menu_editItem);
			item.setVisible(true);
			return true;
		} else {
			MenuItem item = menu.findItem(R.id.menu_editItem);
			item.setVisible(false);
			return true;
		}
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_editItem:
			showItemDetails();
			mode.finish();
			return true;
		case R.id.menu_deleteItem:
			deleteSelectedItems();
			mItemAdapter.refresh(ItemStock.get(getActivity(), mUser)
					.getItemList());
			mode.finish();
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		mCABSelectionCount = 0;
	}

	// text watcher callback methods
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		mItemAdapter.notifyDataSetChanged();
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void onFragmentTransaction(String tag) {
		mFragmentTransactionListener.onFragmentTransaction(tag);
	}

	@Override
	public void onShowAlertDialog(String tag, String header,
			String intentionTag, ArrayList<Item> items) {
		mShowAlertDialogListener.onShowAlertDialog(tag, header, intentionTag,
				items);
	}

}