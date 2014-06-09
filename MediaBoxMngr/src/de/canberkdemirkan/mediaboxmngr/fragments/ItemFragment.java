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
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.data.ProjectConstants;
import de.canberkdemirkan.mediaboxmngr.model.Item;

public class ItemFragment extends Fragment {

	private static final String DIALOG_CREATION_DATE = "creation_date";

	private String mUser;
	private Item mItem;

	private EditText mEditItemTitle;
	private EditText mEditItemContent;
	private Button mButtonItemCreationDate;
	private CheckBox mCheckBoxItemFavorite;

	public static ItemFragment newInstance(UUID itemId, String userTag) {
		Bundle args = new Bundle();
		
		args.putSerializable(ProjectConstants.KEY_ITEM_ID, itemId);
		args.putSerializable(ProjectConstants.KEY_USER_TAG, userTag);

		ItemFragment fragment = new ItemFragment();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		UUID itemId = (UUID) getArguments().getSerializable(
				ProjectConstants.KEY_ITEM_ID);
		mUser = (String) getArguments().getSerializable(
				ProjectConstants.KEY_USER_TAG);

		ItemStock itemStock = ItemStock.get(getActivity(), mUser);
		mItem = itemStock.getItem(itemId);
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
				updateItem();
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
				ItemStock.get(getActivity(), mUser).updateItem(mItem);
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
				dialog.setTargetFragment(ItemFragment.this,
						ProjectConstants.REQUEST_CODE);
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
						updateItem();
					}
				});

		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;
		if (requestCode == ProjectConstants.REQUEST_CODE) {
			Date creationDate = (Date) data
					.getSerializableExtra(ProjectConstants.KEY_ITEM_CREATION_DATE);
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
		ItemStock.get(getActivity(), mUser).saveSerializedItems();
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

	private void updateItem() {
		ItemStock.get(getActivity(), mUser).updateItem(mItem);
	}

}