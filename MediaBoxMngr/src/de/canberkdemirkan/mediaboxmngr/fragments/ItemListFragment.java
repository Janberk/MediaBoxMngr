package de.canberkdemirkan.mediaboxmngr.fragments;

import java.util.ArrayList;

import android.content.Intent;
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
import de.canberkdemirkan.mediaboxmngr.data.DAOItem;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.util.ItemAdapter;

public class ItemListFragment extends Fragment implements
		OnItemSelectedListener {

	private static final String TAG = "ItemListFragment";

	private boolean mEditMode;

	private ArrayList<Item> mItemList;
	private ListView mListView;
	private ItemAdapter mItemAdapter;

	private LinearLayout mEditor;
	private EditText mEditEditTitle;
	private Spinner mSpinnerItemType;
	private Button mButtonSaveItem;

	private DAOItem mDAOItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mEditMode = false;
		getActivity().setTitle(R.string.itemList_header);
		mItemList = ItemStock.get(getActivity()).getItemList();
		// mItemList = ItemStock.get(getActivity()).getDAOItem().getAllItems();
		mDAOItem = ItemStock.get(getActivity()).getDAOItem();

		// ItemAdapter adapter = new ItemAdapter(mItemList);
		// setListAdapter(adapter);
	}

	private void initViews(View view) {
		mListView = (ListView) view
				.findViewById(R.id.listView_fragmentItemList);
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
				i.putExtra(ItemFragment.EXTRA_ITEM_ID, item.getUniqueId());
				startActivity(i);
			}
		});

		mButtonSaveItem.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Item newItem = new Item();
				newItem.setTitle(mEditEditTitle.getText().toString());
				ItemStock.get(getActivity()).addItem(newItem);
				mItemAdapter.refresh(mItemList);

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
			// Intent intent = new Intent(getActivity(),
			// ItemPagerActivity.class);
			// intent.putExtra(ItemFragment.EXTRA_ITEM_ID, item.getUniqueId());
			// startActivityForResult(intent, 0);			
			
			if (!mEditMode) {
				mEditMode = true;
				mEditor.setVisibility(View.VISIBLE);
				changeAlphaOfView(mListView, 1.0F, 0.2F);
			} else if (mEditMode) {
				mEditMode = false;
				mEditor.setVisibility(View.GONE);
				changeAlphaOfView(mListView, 0.2F, 1.0F);
			}
			
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	private void changeAlphaOfView(View view, float from, float to) {
		AlphaAnimation alpha = new AlphaAnimation(from, to);
		alpha.setDuration(0); // Make animation instant
		alpha.setFillAfter(true); // Tell it to persist after the animation
									// ends
		view.startAnimation(alpha);
	}

}