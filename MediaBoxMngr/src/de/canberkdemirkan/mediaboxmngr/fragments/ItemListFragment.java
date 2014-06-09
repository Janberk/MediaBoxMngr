package de.canberkdemirkan.mediaboxmngr.fragments;

import java.util.ArrayList;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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

	// private static final String TAG = "ItemListFragment";

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
				mEditor.setVisibility(View.GONE);
				changeAlphaOfView(mListView, 0.2F, 1.0F);
			}
		});

		return view;
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

	private void changeAlphaOfView(View view, float from, float to) {
		AlphaAnimation alpha = new AlphaAnimation(from, to);
		alpha.setDuration(0); // Make animation instant
		alpha.setFillAfter(true); // Tell it to persist after the animation ends
		view.startAnimation(alpha);
	}

}