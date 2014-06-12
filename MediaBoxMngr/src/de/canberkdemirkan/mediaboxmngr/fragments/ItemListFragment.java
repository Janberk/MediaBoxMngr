package de.canberkdemirkan.mediaboxmngr.fragments;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.activities.ItemPagerActivity;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.data.ProjectConstants;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.MusicAlbum;
import de.canberkdemirkan.mediaboxmngr.util.ItemAdapter;
import de.canberkdemirkan.mediaboxmngr.util.ItemType;

public class ItemListFragment extends Fragment implements
		OnItemSelectedListener {

	private String mUser;
	private boolean mEditMode;
	private String mTypeAsString;

	private ListView mListView;
	private ArrayList<Item> mItemList;
	private ItemAdapter mItemAdapter;

	private LinearLayout mEditor;
	private LinearLayout mMenuBar;

	private EditText mEditEditTitle;
	private Spinner mSpinnerItemType;
	private Button mButtonSaveItem;

	private ImageView mImageAllLists;
	private ImageView mImageSettings;
	private ImageView mImageDelete;
	private ImageView mImageLogout;

	private String json;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		// TODO user aus login fragment holen
		// mUser = getUser();
		mUser = "w@w.de";
		mEditMode = false;
		getActivity().setTitle(R.string.itemList_header);
		mItemList = ItemStock.get(getActivity(), mUser).getItemList();
	}

	private void initViews(View view) {
		mListView = (ListView) view
				.findViewById(R.id.listView_fragmentItemList);
		mMenuBar = (LinearLayout) view
				.findViewById(R.id.fragmentItemList_menuBar);
		mEditor = (LinearLayout) view
				.findViewById(R.id.fragmentItemList_editTitle);
		mEditor.setVisibility(View.GONE);
		mEditEditTitle = (EditText) view
				.findViewById(R.id.et_fragmentEditTitle_editTitle);
		mSpinnerItemType = (Spinner) view
				.findViewById(R.id.sp_fragmentEditTitle_itemType);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this.getActivity(), R.array.item_types,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerItemType.setAdapter(adapter);
		mSpinnerItemType.setOnItemSelectedListener(this);
		mButtonSaveItem = (Button) view
				.findViewById(R.id.btn_fragmentEditTitle_saveItem);
		mImageAllLists = (ImageView) view
				.findViewById(R.id.iv_fragmentMenuBar_allLists);
		mImageAllLists.setOnClickListener(new View.OnClickListener() {

			// FragmentManager fragmentManager = getFragmentManager();
			// FragmentTransaction fragmentTransaction = fragmentManager
			// .beginTransaction();

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "All lists", Toast.LENGTH_LONG)
						.show();
				// fragmentTransaction.replace(containerId, selectList);
				// fragmentTransaction.commit();
			}
		});

		mImageSettings = (ImageView) view
				.findViewById(R.id.iv_fragmentMenuBar_settings);
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

		mImageDelete = (ImageView) view
				.findViewById(R.id.iv_fragmentMenuBar_delete);
		mImageDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Delete", Toast.LENGTH_LONG)
						.show();

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

		mImageLogout = (ImageView) view
				.findViewById(R.id.iv_fragmentMenuBar_logout);
		mImageLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Logout", Toast.LENGTH_LONG)
						.show();
				// logout();
			}
		});

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_item_list, null);

		initViews(view);

		mItemAdapter = new ItemAdapter(this.getActivity(), mItemList);
		mItemAdapter.setNotifyOnChange(true);

		mListView.setAdapter(mItemAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {

				Item item = (Item) mListView.getAdapter().getItem(position);
				Intent i = new Intent(getActivity(), ItemPagerActivity.class);
				i.putExtra(ProjectConstants.KEY_ITEM_ID, item.getUniqueId());
				startActivityForResult(i, 0);
			}
		});

		mButtonSaveItem.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ItemType type = ItemType.valueOf(mTypeAsString);
				Item newItem = createItem(type);
				ItemStock.get(getActivity(), mUser).addItem(newItem);
				mItemAdapter.refresh(mItemList);
				mEditEditTitle.setText("");
				switchMode();
			}
		});

		return view;
	}

	private void syncWithRemoteDb() throws JSONException, IOException {
		ItemStock itemStock = ItemStock.get(getActivity(), mUser);
		json = itemStock.getSQLiteAsJSON(mUser);

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		params.put("items", json);
		client.post(
				"http://10.0.2.2:80/development/mediaboxmngr_backend/items/insert_items.php",
				params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						System.out.println("Response from Server: \n" + response);
						try {
							JSONArray jsonArray = new JSONArray(response);
							System.out.println("JSONArray.length: "
									+ jsonArray.length());
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObject = (JSONObject) jsonArray
										.get(i);
								System.out.println(jsonObject.get("sqlite_id"));
								System.out.println(jsonObject.get("title"));
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

	@Override
	public void onResume() {
		super.onResume();
		mItemAdapter.refresh(mItemList);
	}

	@Override
	public void onPause() {//
		super.onPause();
	}

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
				.getSharedPreferences(ProjectConstants.KEY_MY_PREFERENCES,
						Context.MODE_PRIVATE);
		// String user = sharedPreferences.getString(LoginFragment.EMAIL, "");
		String user = sharedPreferences.getString("w@w.de", "");
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

}