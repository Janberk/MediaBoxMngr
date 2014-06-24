package de.canberkdemirkan.mediaboxmngr.fragments;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.activities.ItemPagerActivity;
import de.canberkdemirkan.mediaboxmngr.activities.LoginActivity;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.data.JSONHandler;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.MusicAlbum;
import de.canberkdemirkan.mediaboxmngr.util.ItemAdapter;
import de.canberkdemirkan.mediaboxmngr.util.ItemType;

public class ItemListFragment extends Fragment implements
		OnItemSelectedListener {

	private SharedPreferences mSharedPreferences;

	private String mUser;
	private boolean mEditMode;
	private String mTypeAsString;

	private ListView mListView;
	private ArrayList<Item> mItemList;
	private ItemAdapter mItemAdapter;

	private ArrayAdapter<CharSequence> mArrayAdapter;

	private RelativeLayout mEditor;
	private LinearLayout mMenuBar;

	private EditText mEditEditTitle;
	private Spinner mSpinnerItemType;
	private Button mButtonSaveItem;

	private ImageView mImageAllLists;
	private ImageView mImageSettings;
	private ImageView mImageDelete;
	private ImageView mImageLogout;

	private String json;

	ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
		mEditMode = false;
		mSharedPreferences = getActivity().getSharedPreferences(
				Constants.KEY_MY_PREFERENCES, Context.MODE_PRIVATE);
		mUser = getUser();
		getActivity().setTitle(mUser);

		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemListFragment - onCreate(): " + mUser);
		}

		mItemList = ItemStock.get(getActivity(), mUser).getItemList();

		progressDialog = new ProgressDialog(getActivity());
		progressDialog
				.setMessage("Synchronizing SQLite DB with Remote MySQL DB. Please wait...");
		progressDialog.setCancelable(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemListFragment - onCreateView()");
		}

		View view = inflater.inflate(R.layout.fragment_item_list, null);

		initViews(view);

		mEditor.setVisibility(View.GONE);
		mArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerItemType.setAdapter(mArrayAdapter);
		mSpinnerItemType.setOnItemSelectedListener(this);

		int localDbVersion = ItemStock.get(getActivity(), getUser())
				.getDAOItem().getTableVersion(Constants.VERSION);
		int remoteDbVersion = 0;

		if (localDbVersion < remoteDbVersion) {
			loadItemsFromRemoteDb();
		} else if (localDbVersion > remoteDbVersion) {
			try {
				syncWithRemoteDb();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		mItemAdapter = new ItemAdapter(this.getActivity(), mItemList);
		mItemAdapter.setNotifyOnChange(true);

		mListView.setAdapter(mItemAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {

				Item item = (Item) mListView.getAdapter().getItem(position);
				Intent i = new Intent(getActivity(), ItemPagerActivity.class);
				i.putExtra(Constants.KEY_ITEM_ID, item.getUniqueId());
				i.putExtra(Constants.KEY_USER_TAG, mUser);
				startActivityForResult(i, 0);
			}
		});

		mButtonSaveItem.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ItemType type = ItemType.valueOf(mTypeAsString);
				Item newItem = createItem(type);
				ItemStock itemStock = ItemStock.get(getActivity(), mUser);
				itemStock.addItem(newItem);
				ArrayList<Item> list = itemStock.getItemList();
				mItemAdapter.refresh(list);
				mEditEditTitle.setText("");
				switchMode();
			}
		});

		mImageAllLists.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// loadItemsFromRemoteDb();
			}
		});

		mImageSettings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Settings", Toast.LENGTH_LONG)
						.show();
				try {
					syncWithRemoteDb();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		mImageDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Delete", Toast.LENGTH_LONG)
						.show();
				ItemStock.get(getActivity(), mUser).getDAOItem()
						.deleteAllItems(mUser);
				mItemList.clear();
				mItemAdapter.refresh(mItemList);

				// editMode = UtilMethods.modeSwitcher(editMode);
				//
				// int childCount = listView.getChildCount();
				//
				// for (int i = 0; i < childCount; i++) {
				// View view = listView.getChildAt(i);
				//
				// if (view != null) {
				// CheckBox cb_itemDelete = (CheckBox) view
				// .findViewById(R.id.cb_itemDelete);
				// ImageView iv_deleteSingleItem = (ImageView) view
				// .findViewById(R.id.iv_deleteSingleItem);
				// cb_itemDelete.setChecked(false);
				//
				// if (cb_itemDelete.getVisibility() == View.GONE) {
				// cb_itemDelete.setVisibility(View.VISIBLE);
				// iv_deleteSingleItem.setVisibility(View.VISIBLE);
				// } else {
				// cb_itemDelete.setVisibility(View.GONE);
				// iv_deleteSingleItem.setVisibility(View.GONE);
				// }
				// }
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

	private void initViews(View view) {
		mListView = (ListView) view
				.findViewById(R.id.listView_fragmentItemList);
		mMenuBar = (LinearLayout) view
				.findViewById(R.id.fragmentItemList_menuBar);
		mEditor = (RelativeLayout) view
				.findViewById(R.id.fragmentItemList_editTitle);

		mEditEditTitle = (EditText) view
				.findViewById(R.id.et_fragmentEditTitle_editTitle);
		mSpinnerItemType = (Spinner) view
				.findViewById(R.id.sp_fragmentEditTitle_itemType);
		mArrayAdapter = ArrayAdapter.createFromResource(this.getActivity(),
				R.array.item_types, android.R.layout.simple_spinner_item);
		mButtonSaveItem = (Button) view
				.findViewById(R.id.btn_fragmentEditTitle_saveItem);
		mImageAllLists = (ImageView) view
				.findViewById(R.id.iv_fragmentMenuBar_allLists);
		mImageSettings = (ImageView) view
				.findViewById(R.id.iv_fragmentMenuBar_settings);
		mImageDelete = (ImageView) view
				.findViewById(R.id.iv_fragmentMenuBar_delete);
		mImageLogout = (ImageView) view
				.findViewById(R.id.iv_fragmentMenuBar_logout);
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
		case R.id.menu__newItem:
			switchMode();
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		mTypeAsString = (String) parent.getItemAtPosition(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	/*
	 * 
	 * End Options Menu
	 */

	public Item createItem(ItemType type) {
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

	public String getUser() {
		SharedPreferences sharedPreferences = getActivity()
				.getSharedPreferences(Constants.KEY_MY_PREFERENCES,
						Context.MODE_PRIVATE);
		String user = sharedPreferences.getString(LoginFragment.KEY_EMAIL, "");
		return user;
	}

	private void switchMode() {
		if (!mEditMode) {
			mEditMode = true;
			mEditor.setVisibility(View.VISIBLE);
			changeAlphaOfView(mListView, 1.0F, 0.2F);
			mMenuBar.setVisibility(View.GONE);
		} else if (mEditMode) {
			mEditor.setVisibility(View.GONE);
			changeAlphaOfView(mListView, 0.2F, 1.0F);
			mMenuBar.setVisibility(View.VISIBLE);
			mEditMode = false;
		}
	}

	private void changeAlphaOfView(View view, float from, float to) {
		AlphaAnimation alpha = new AlphaAnimation(from, to);
		alpha.setDuration(0); // Make animation instant
		alpha.setFillAfter(true); // Tell it to persist after the animation ends
		view.startAnimation(alpha);
	}

	public void logout() {
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

	/*
	 * 
	 * Logging callback methods for debug purposes
	 */

	@Override
	public void onAttach(Activity activity) {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemListFragment - onAttach()");
		}
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemListFragment - onActivityCreated()");
		}
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemListFragment - onStart()");
		}
		super.onStart();
	}

	@Override
	public void onResume() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemListFragment - onResume()");
		}
		mItemAdapter.refresh(mItemList);
		super.onResume();
	}

	@Override
	public void onPause() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemListFragment - onPause()");
		}
		super.onPause();
	}

	@Override
	public void onStop() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemListFragment - onStop()");
		}
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemListFragment - onDestroyView()");
		}
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemListFragment - onDestroy()");
		}
		super.onDestroy();
	}

	@Override
	public void onDetach() {//
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemListFragment - onDetach()");
		}
		super.onDetach();
	}

	/*
	 * 
	 * End Logging callback methods for debug purposes
	 */

	public void loadItemsFromRemoteDb() {
		Toast.makeText(getActivity(), "All lists", Toast.LENGTH_LONG).show();
		final JSONHandler handler = new JSONHandler(getActivity());
		RequestParams params = new RequestParams();
		params.put("user", mUser);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(ItemStock.URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				progressDialog.show();
				if (BuildConfig.DEBUG) {
					Log.d(Constants.LOG_TAG,
							"ItemListFragment - loadItemsFromRemoteDb() - onStart()");
				}
			}

			@Override
			public void onSuccess(String response) {
				progressDialog.hide();
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
				progressDialog.hide();
				if (BuildConfig.DEBUG) {
					Log.d(Constants.LOG_TAG,
							"ItemListFragment - loadItemsFromRemoteDb() - onFinish()");
				}
				mItemAdapter.refresh(mItemList);
			}

			@Override
			public void onFailure(int statusCode, Throwable error,
					String content) {
				progressDialog.hide();
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
		json = itemStock.getSQLiteAsJSON(mUser);

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		params.put("items", json);
		client.post(
				"http://10.0.2.2:80/development/mediaboxmngr_backend/items/insert_items.php",
				params, new AsyncHttpResponseHandler() {
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

}