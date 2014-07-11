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
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.activities.ItemPagerActivity;
import de.canberkdemirkan.mediaboxmngr.activities.LoginActivity;
import de.canberkdemirkan.mediaboxmngr.content.ItemType;
import de.canberkdemirkan.mediaboxmngr.content.ListTag;
import de.canberkdemirkan.mediaboxmngr.data.DummyDataProvider;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.data.JSONHandler;
import de.canberkdemirkan.mediaboxmngr.data.RemoteDbVersionProvider;
import de.canberkdemirkan.mediaboxmngr.dialogs.AlertDialogDeletion;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.listeners.CustomTabListener;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.MusicAlbum;
import de.canberkdemirkan.mediaboxmngr.util.CustomItemAdapter;
import de.canberkdemirkan.mediaboxmngr.util.CustomSpinnerAdapter;
import de.canberkdemirkan.mediaboxmngr.util.CustomSpinnerAdapter.SpinnerTag;
import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

@SuppressLint("NewApi")
public class ItemListFragment extends Fragment implements Serializable,
		OnItemSelectedListener {

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
	private ActionBar.Tab tabAll, tabAlbums, tabBooks, tabMovies, tabFavorites;

	private String mUser;
	private String mJson;
	private String mTypeAsString;

	private SpinnerTag mTypeSpinner = SpinnerTag.TypeSpinner;
	public static ListTag sListTag;
	public static boolean sEditMode;
	public static boolean sDeleteMode;;

	private ListView mListView;
	private ArrayList<Item> mItemList;
	private CustomItemAdapter mItemAdapter;

	private RelativeLayout mEditor;
	private LinearLayout mListContainer;
	private LinearLayout mMenuBar;

	private EditText mEditEditTitle;
	private Spinner mSpinnerItemType;
	private Button mButtonSaveItem;
	private Button mButtonDeleteSelected;
	private ImageView mImageHome;
	private ImageView mImageSearch;
	private ImageView mImageSettings;
	private ImageView mImageLogout;
	private ImageView mImageItemDelete;
	private CheckBox mCheckBoxConfirmItemDelete;

	public static ItemListFragment newItemListFragment(ListTag listTag) {

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

		sDeleteMode = false;

		mSharedPreferences = getActivity().getSharedPreferences(
				Constants.KEY_MY_PREFERENCES, Context.MODE_PRIVATE);
		mUser = getUserFromPrefs();
		getActivity().setTitle(mUser);

		mFragmentManager = getActivity().getSupportFragmentManager();

		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemListFragment - onCreate(): " + mUser);
		}

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
		mMenuBar = (LinearLayout) view
				.findViewById(R.id.fragmentItemList_menuBar);
		mEditor = (RelativeLayout) view
				.findViewById(R.id.fragmentItemList_editTitle);
		mListContainer = (LinearLayout) view
				.findViewById(R.id.fragmentItemList_listView);
		mEditEditTitle = (EditText) view
				.findViewById(R.id.et_fragmentEditTitle_editTitle);
		mSpinnerItemType = (Spinner) view
				.findViewById(R.id.sp_fragmentEditTitle_itemType);
		mButtonSaveItem = (Button) view
				.findViewById(R.id.btn_fragmentEditTitle_saveItem);
		mButtonDeleteSelected = (Button) view
				.findViewById(R.id.btn_fragmentItemList_deleteSelected);
		mImageHome = (ImageView) view
				.findViewById(R.id.iv_fragmentMenuBar_home);
		mImageSearch = (ImageView) view
				.findViewById(R.id.iv_fragmentMenuBar_search);
		mImageSettings = (ImageView) view
				.findViewById(R.id.iv_fragmentMenuBar_settings);
		mImageLogout = (ImageView) view
				.findViewById(R.id.iv_fragmentMenuBar_logout);
	}

	@SuppressLint("InflateParams")
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

		mEditor.setVisibility(View.GONE);
		mButtonDeleteSelected.setVisibility(View.GONE);

		mSpinnerItemType.setAdapter(new CustomSpinnerAdapter(getActivity(),
				R.layout.custom_spinner, R.id.tv_customSpinner_label,
				CustomSpinnerAdapter.getContent(mTypeSpinner)));
		mSpinnerItemType.setOnItemSelectedListener(this);

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

		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mListView.setAdapter(mItemAdapter);

		addActionBarTabs();

		setListViewClickable(mListView);

		mButtonSaveItem.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				saveItem();
			}
		});

		mButtonDeleteSelected.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteSelectedItems();
			}
		});

		mImageHome.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Home", Toast.LENGTH_LONG).show();
			}
		});

		mImageSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// loadItemsFromRemoteDb();
				Toast.makeText(getActivity(), "Search", Toast.LENGTH_LONG)
						.show();
			}
		});

		mImageSettings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Settings", Toast.LENGTH_LONG)
						.show();
				// try {
				// syncWithRemoteDb();
				// } catch (JSONException e) {
				// e.printStackTrace();
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
			}
		});

		mImageLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Logout", Toast.LENGTH_LONG)
						.show();
				logout();
			}
		});

		return view;
	}

	private void addActionBarTabs() {
		tabAll = mActionBar.newTab().setText(ListTag.ALL.name())
				.setTag(ListTag.ALL);
		tabAlbums = mActionBar.newTab().setText(ListTag.ALBUMS.name())
				.setTag(ListTag.ALBUMS);
		tabBooks = mActionBar.newTab().setText(ListTag.BOOKS.name())
				.setTag(ListTag.BOOKS);
		tabMovies = mActionBar.newTab().setText(ListTag.MOVIES.name())
				.setTag(ListTag.MOVIES);
		tabFavorites = mActionBar.newTab().setText(ListTag.FAVORITES.name())
				.setTag(ListTag.FAVORITES);

		ItemListFragment fragment = (ItemListFragment) mFragmentManager
				.findFragmentById(R.id.fragmentContainer);

		tabAll.setTabListener(new CustomTabListener(getActivity(), fragment));
		tabAlbums
				.setTabListener(new CustomTabListener(getActivity(), fragment));
		tabBooks.setTabListener(new CustomTabListener(getActivity(), fragment));
		tabMovies
				.setTabListener(new CustomTabListener(getActivity(), fragment));
		tabFavorites.setTabListener(new CustomTabListener(getActivity(),
				fragment));

		mActionBar.addTab(tabAll);
		mActionBar.addTab(tabAlbums);
		mActionBar.addTab(tabBooks);
		mActionBar.addTab(tabMovies);
		mActionBar.addTab(tabFavorites);
	}

	private Item createItem(ItemType type) {
		Item item = null;

		switch (type) {
		case Album:
			item = new MusicAlbum();
			break;
		case Book:
			item = new Book();
			break;
		case Movie:
			item = new Movie();
			break;

		default:
			break;
		}
		item.setTitle(mEditEditTitle.getText().toString());
		item.setType(type);
		item.setUser(mUser);
		return item;
	}

	private void saveItem() {
		ItemType type = ItemType.valueOf(mTypeAsString);
		Item newItem = createItem(type);
		ItemStock itemStock = ItemStock.get(getActivity(), mUser);
		itemStock.addItem(newItem);
		ArrayList<Item> list = itemStock.getItemList();
		mItemAdapter.refresh(list);
		mEditEditTitle.setText("");
		switchEditMode();

	}

	private void showItemDelete() {
		sDeleteMode = UtilMethods.modeSwitcher(sDeleteMode);

		int childCount = mListView.getChildCount();

		for (int i = 0; i < childCount; i++) {
			View view = mListView.getChildAt(i);

			if (view != null) {

				mImageItemDelete = (ImageView) view
						.findViewById(R.id.iv_listItem_itemDeleteSingle);
				mCheckBoxConfirmItemDelete = (CheckBox) view
						.findViewById(R.id.cb_listItem_itemDelete);
				mCheckBoxConfirmItemDelete.setChecked(false);

				updateVisibilityOfElements(sDeleteMode);
			}
		}
		if (!sDeleteMode) {
			ItemStock.get(getActivity(), getUser()).getDAOItem()
					.setAllRemovable(false);
		}
	}

	private void deleteAllItems() {
		final String header = getActivity().getResources().getString(
				R.string.dialog_header_delete_all);
		AlertDialogDeletion dialog = AlertDialogDeletion.newInstance(this,
				mItemList, header, 0, AlertDialogDeletion.DIALOG_TAG_ALL);
		dialog.setTargetFragment(this, Constants.REQUEST_LIST_DELETE);
		dialog.show(mFragmentManager, "");
	}

	private void deleteSelectedItems() {
		final String header = getActivity().getResources().getString(
				R.string.dialog_header_delete_selected);
		AlertDialogDeletion dialog = AlertDialogDeletion.newInstance(this,
				mItemList, header, 0, AlertDialogDeletion.DIALOG_TAG_SELECTED);
		dialog.setTargetFragment(this, Constants.REQUEST_LIST_DELETE);
		dialog.show(mFragmentManager, "");
	}

	private void changeAlphaOfView(View view, float from, float to) {
		AlphaAnimation alpha = new AlphaAnimation(from, to);
		alpha.setDuration(0); // Make animation instant
		alpha.setFillAfter(true); // Tell it to persist after the animation ends
		view.startAnimation(alpha);
	}

	private void switchEditMode() {
		if (!sEditMode) {
			sEditMode = true;
			mSpinnerItemType.setSelection(0);
			mEditor.setVisibility(View.VISIBLE);
			changeAlphaOfView(mListContainer, 1.0F, 0.2F);
			mMenuBar.setVisibility(View.GONE);
			mEditEditTitle.requestFocus();
			mListView.setOnItemClickListener(null);
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(mEditEditTitle, InputMethodManager.SHOW_IMPLICIT);
		} else if (sEditMode) {
			mEditor.setVisibility(View.GONE);
			changeAlphaOfView(mListContainer, 0.2F, 1.0F);
			mMenuBar.setVisibility(View.VISIBLE);
			setListViewClickable(mListView);
			sEditMode = false;
		}
	}

	private void logout() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemListFragment - logout()");
		}
		Editor editor = mSharedPreferences.edit();
		editor.remove(LoginFragment.KEY_EMAIL);
		editor.remove(LoginFragment.KEY_PASSWORD);
		editor.commit();
		getActivity().finish();
		Intent intent = new Intent(getActivity().getApplicationContext(),
				LoginActivity.class);
		startActivity(intent);
	}

	private void updateVisibilityOfElements(boolean value) {

		if (value) {
			if (mMenuBar != null) {
				mMenuBar.setVisibility(View.GONE);
			}
			if (mCheckBoxConfirmItemDelete != null) {
				mCheckBoxConfirmItemDelete.setVisibility(View.VISIBLE);
			}
			if (mImageItemDelete != null) {
				mImageItemDelete.setVisibility(View.VISIBLE);
			}
			if (mButtonDeleteSelected != null) {
				mButtonDeleteSelected.setVisibility(View.VISIBLE);
			}

		} else {
			if (mMenuBar != null) {
				mMenuBar.setVisibility(View.VISIBLE);
			}
			if (mCheckBoxConfirmItemDelete != null) {
				mCheckBoxConfirmItemDelete.setVisibility(View.GONE);
			}
			if (mImageItemDelete != null) {
				mImageItemDelete.setVisibility(View.GONE);
			}
			if (mButtonDeleteSelected != null) {
				mButtonDeleteSelected.setVisibility(View.GONE);
			}

		}

	}

	private void setListViewClickable(ListView list) {
		final ListView listView = list;
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {

				Item item = (Item) listView.getAdapter().getItem(position);
				Intent i = new Intent(getActivity(), ItemPagerActivity.class);
				i.putExtra(Constants.KEY_ITEM_ID, item.getUniqueId());
				i.putExtra(Constants.KEY_USER_TAG, mUser);
				i.putExtra(Constants.KEY_TYPE, item.getType().toString());
				startActivityForResult(i, 0);
			}
		});
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
			updateVisibilityOfElements(sDeleteMode);

		}
	}

	/*
	 * 
	 * Options Menu
	 */

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
		case R.id.menu_edit:
			showItemDelete();
			return true;
		case R.id.menu_deleteAll:
			deleteAllItems();
			return true;
		case R.id.menu_dummyData:
			createDummyList();
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
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		mTypeAsString = (String) parent.getItemAtPosition(position);
		RelativeLayout layout = (RelativeLayout) parent.getChildAt(0);
		((TextView) layout.findViewById(R.id.tv_customSpinner_label))
				.setTextAppearance(getActivity(), R.style.spinnerTextStyle);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	/*
	 * 
	 * End Options Menu
	 */

	@Override
	public void onResume() {
		sDeleteMode = false;
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemListFragment - onResume()");
		}
		mItemList = UtilMethods.createListFromTag(getActivity(), mUser,
				sListTag);
		mItemAdapter.refresh(mItemList);
		super.onResume();
	}

	@Override
	public void onPause() {
		sDeleteMode = false;
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemListFragment - onPause()");
		}
		super.onPause();
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
		String user = mSharedPreferences.getString(LoginFragment.KEY_EMAIL, "");
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

}