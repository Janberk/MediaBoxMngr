package de.canberkdemirkan.mediaboxmngr.fragments;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.activities.ItemPagerActivity;
import de.canberkdemirkan.mediaboxmngr.activities.SettingsActivity;
import de.canberkdemirkan.mediaboxmngr.adapters.CustomItemAdapter;
import de.canberkdemirkan.mediaboxmngr.content.ListTag;
import de.canberkdemirkan.mediaboxmngr.data.DummyDataProvider;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.data.JSONHandler;
import de.canberkdemirkan.mediaboxmngr.data.RemoteDbVersionProvider;
import de.canberkdemirkan.mediaboxmngr.dialogs.AlertDialogDeletion;
import de.canberkdemirkan.mediaboxmngr.dialogs.AlertDialogLogout;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.listeners.CustomTabListener;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

@SuppressLint("NewApi")
public class ItemListFragment extends Fragment implements Serializable,
		AdapterView.OnItemClickListener, MultiChoiceModeListener, TextWatcher {

	/**
	 * 
	 */
	private static final long serialVersionUID = 14051981L;

	// public static int LOKAL_DB_VERSION = 0;
	// public static int REMOTE_DB_VERSION = 0;

	private SharedPreferences mSharedPreferences;
	private FragmentManager mFragmentManager;
	private ProgressDialog mProgressDialog;
	private ActionBar mActionBar;
	private ActionBar.Tab tabAll, tabMusic, tabBooks, tabMovies, tabFavorites,
			tabTopRated;

	private String mUser;
	private String mJson;

	public static ListTag sListTag;
	public static boolean sCreateMode;
	private int mCABSelectionCount = 0;

	private ListView mListView;
	private ArrayList<Item> mItemList;
	private CustomItemAdapter mItemAdapter;

	private ActionMode mActionMode;

	public static ItemListFragment newInstance(ListTag listTag) {

		Bundle passedData = new Bundle();
		passedData.putSerializable(Constants.KEY_LIST_TAG, listTag);

		ItemListFragment itemListFragment = new ItemListFragment();
		itemListFragment.setArguments(passedData);

		return itemListFragment;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);

		mSharedPreferences = getActivity().getSharedPreferences(
				Constants.KEY_MY_PREFERENCES, Context.MODE_PRIVATE);
		mUser = getUserFromPrefs();
		getActivity().setTitle(mUser);

		mFragmentManager = getActivity().getSupportFragmentManager();

		Bundle bundle = getArguments();

		if (bundle == null) {
			sListTag = ListTag.ALL;
		} else {
			sListTag = (ListTag) bundle.get(Constants.KEY_LIST_TAG);
		}

		mItemList = UtilMethods.createListFromTag(getActivity(), mUser,
				sListTag);

		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog
				.setMessage("Synchronizing SQLite DB with Remote MySQL DB. Please wait...");
		mProgressDialog.setCancelable(false);
	}

	private void initViews(View view) {
		mListView = (ListView) view
				.findViewById(R.id.listView_fragmentItemList);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemListFragment - onCreateView()");
		}

		View view = inflater.inflate(R.layout.fragment_item_list, null);

		initViews(view);

		mActionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// LOKAL_DB_VERSION = ItemStock.get(getActivity(),
		// getUser()).getDAOItem()
		// .getTableVersion(Constants.VERSION);
		// REMOTE_DB_VERSION = getRemoteDbVersion();
		//
		// if (LOKAL_DB_VERSION < REMOTE_DB_VERSION) {
		// loadItemsFromRemoteDb();
		// } else if (LOKAL_DB_VERSION > REMOTE_DB_VERSION) {
		// try {
		// syncWithRemoteDb();
		// } catch (JSONException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }

		mItemAdapter = new CustomItemAdapter(getActivity(), this, mItemList);
		mItemAdapter.setNotifyOnChange(true);

		mListView.setAdapter(mItemAdapter);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			// Use floating context menus on Froyo and Gingerbread
			registerForContextMenu(mListView);
		} else {
			// Use contextual action bar on Honeycomb and higher
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

	private void deleteAllItems() {
		final String header = getActivity().getResources().getString(
				R.string.dialog_header_delete_all);
		AlertDialogDeletion dialog = AlertDialogDeletion.newInstance(this,
				mItemList, null, header, AlertDialogDeletion.DIALOG_TAG_ALL);
		dialog.setTargetFragment(this, Constants.REQUEST_LIST_DELETE);
		dialog.show(mFragmentManager, "");
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
		AlertDialogDeletion dialog = null;
		if (count == 1) {
			dialog = AlertDialogDeletion.newInstance(this, selectedItems, null,
					header + "\n" + addition,
					AlertDialogDeletion.DIALOG_TAG_SINGLE);
		}
		if (count > 1) {
			addition = count + " of " + mItemAdapter.getCount();
			dialog = AlertDialogDeletion.newInstance(this, selectedItems, null,
					header + "\n" + addition,
					AlertDialogDeletion.DIALOG_TAG_SELECTED);
		}

		dialog.setTargetFragment(this, Constants.REQUEST_LIST_DELETE);
		dialog.show(mFragmentManager, "");
	}

	private void switchEditMode() {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		CreateItemFragment createItem = CreateItemFragment.newInstance(mUser);
		if (!sCreateMode) {
			sCreateMode = true;
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			ft.add(R.id.fragmentContainer, createItem);
			ft.addToBackStack(null);
			ft.commit();
		} else if (sCreateMode) {
			ft.replace(R.id.fragmentContainer, this);
			ft.addToBackStack(null);
			ft.commit();
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			sCreateMode = false;
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
			// FragmentTransaction ft = mFragmentManager.beginTransaction();
			// ft.replace(R.id.fragmentContainer, new SettingsFragment());
			// ft.addToBackStack(null);
			// ft.commit();
			return true;

		case R.id.menu_logout:
			AlertDialogLogout dialog = AlertDialogLogout
					.newInstance(getActivity());
			dialog.show(mFragmentManager, "");
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
	public void onResume() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemListFragment - onResume()");
		}
		mItemList = UtilMethods.createListFromTag(getActivity(), mUser,
				sListTag);
		mItemAdapter.refresh(mItemList);
		super.onResume();
	}

	public void loadItemsFromRemoteDb() {
		Toast.makeText(getActivity(), "All lists", Toast.LENGTH_LONG).show();
		final JSONHandler handler = new JSONHandler(getActivity());
		RequestParams params = new RequestParams();
		params.put("user", mUser);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(Constants.BUILD_JSON_REQUEST, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						mProgressDialog.show();
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"ItemListFragment - loadItemsFromRemoteDb() - onStart()");
						}
					}

					@Override
					public void onSuccess(String response) {
						mProgressDialog.hide();
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"ItemListFragment - loadItemsFromRemoteDb() - onSuccess()\n"
											+ response);
						}
						try {
							ArrayList<Item> createdList = handler
									.loadItemsFromJSONArray(response);
							handler.setRemoteList(createdList);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						mItemList.clear();
						for (int i = 0; i < handler.getRemoteList().size(); i++) {
							Item item = handler.getRemoteList().get(i);

							mItemList.add(i, item);
						}
						ItemStock.get(getActivity(), mUser).getDAOItem()
								.updateTableWithNewList(mItemList);
					}

					@Override
					public void onFinish() {
						mProgressDialog.hide();
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"ItemListFragment - loadItemsFromRemoteDb() - onFinish()");
						}
						mItemAdapter.refresh(mItemList);
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						mProgressDialog.hide();
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"ItemListFragment - loadItemsFromRemoteDb() - onFailure()\n"
											+ content);
						}
					}

				});
	}

	private void syncWithRemoteDb() throws JSONException, IOException {
		final ItemStock itemStock = ItemStock.get(getActivity(), mUser);
		mJson = itemStock.getSQLiteAsJSON(mUser);

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		params.put("items", mJson);
		client.post(Constants.INSERT_ITEMS_REQUEST, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"ItemListFragment - syncWithRemoteDb() - onSuccess()\n"
											+ response);
						}
						try {
							JSONArray jsonArray = new JSONArray(response);
							Log.d(Constants.LOG_TAG,
									"ItemListFragment - syncWithRemoteDb() - onSuccess(): \n"
											+ jsonArray.length());
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObject = (JSONObject) jsonArray
										.get(i);

								String jsonId = jsonObject.getString("_id");
								String jsonSynced = jsonObject
										.getString("synced");

								long id = Long.valueOf(jsonId).longValue();
								int synced = Integer.valueOf(jsonSynced)
										.intValue();
								itemStock.getDAOItem().updateSyncStatus(id,
										synced);
							}
							Toast.makeText(getActivity(), "DB Sync completed!",
									Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
							Toast.makeText(getActivity(),
									"Error! JSON response might be invalid!",
									Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"ItemListFragment - syncWithRemoteDb() - onFailure()\n"
											+ content);
						}
						if (statusCode == 404) {
							Toast.makeText(getActivity(),
									"404 - Resource not found.",
									Toast.LENGTH_LONG).show();
						} else if (statusCode == 500) {
							Toast.makeText(getActivity(),
									"505 - Server Error.", Toast.LENGTH_LONG)
									.show();
						} else {
							Toast.makeText(
									getActivity(),
									"Unexpected Error! Please check your network connection.",
									Toast.LENGTH_LONG).show();
						}
					}
				});
	}

	private static int getRemoteDbVersion() {
		RemoteDbVersionProvider provider = new RemoteDbVersionProvider();
		return provider.getVersion();
	}

	public String getUserFromPrefs() {
		String user = mSharedPreferences.getString(Constants.KEY_EMAIL, "");
		return user;
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

}