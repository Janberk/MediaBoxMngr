package de.canberkdemirkan.mediaboxmngr.fragments;

import java.util.Date;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.data.DAOItem;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.model.Item;

public class ItemFragment extends Fragment {

	public static final String EXTRA_ITEM_ID = "de.canberk.bignerdranchproject.item_id";
	private static final String DIALOG_CREATION_DATE = "creation_date";
	private static final int REQUEST_DATE = 0;

	private Item mItem;
	private EditText mEditItemTitle;
	private EditText mEditItemContent;
	private Button mButtonItemCreationDate;
	private CheckBox mCheckBoxItemFavorite;
	private DAOItem mDAOItem;

	public static ItemFragment newInstance(UUID itemId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_ITEM_ID, itemId);
		ItemFragment fragment = new ItemFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDAOItem = ItemStock.get(getActivity()).getDAOItem();
		UUID itemId = (UUID) getArguments().getSerializable(EXTRA_ITEM_ID);
		mItem = ItemStock.get(getActivity()).getItem(itemId);
		setHasOptionsMenu(true);
	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_item, container, false);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}

		mEditItemTitle = (EditText) view
				.findViewById(R.id.et_fragmentItem_itemTitle);
		mEditItemTitle.setText(mItem.getTitle());
		mEditItemTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mItem.setTitle(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {
				ItemStock.get(getActivity()).updateItem(mItem);
			}
		});

		mEditItemContent = (EditText) view
				.findViewById(R.id.et_fragmentItem_itemContent);
		mEditItemContent.setText(mItem.toString());
		mEditItemContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				ItemStock.get(getActivity()).updateItem(mItem);
			}
		});

		mButtonItemCreationDate = (Button) view
				.findViewById(R.id.btn_fragmentItem_itemCreationDate);
		updateCreationDate();
		mButtonItemCreationDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment dialog = DatePickerFragment
						.newInstance(mItem.getCreationDate());
				dialog.setTargetFragment(ItemFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_CREATION_DATE);
			}
		});

		mCheckBoxItemFavorite = (CheckBox) view
				.findViewById(R.id.cb_fragmentItem_itemFavorite);
		mCheckBoxItemFavorite.setChecked(mItem.isFavorite());
		mCheckBoxItemFavorite
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						mItem.setFavorite(isChecked);
					}
				});

		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;
		if (requestCode == REQUEST_DATE) {
			Date creationDate = (Date) data
					.getSerializableExtra(DatePickerFragment.EXTRA_ITEM_CREATION_DATE);
			mItem.setCreationDate(creationDate);
			updateCreationDate();
		}
	}

	private void updateCreationDate() {
		mButtonItemCreationDate.setText(mItem.getCreationDate().toString());
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		// ItemStock.get(getActivity()).saveItems();
		super.onPause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}

}