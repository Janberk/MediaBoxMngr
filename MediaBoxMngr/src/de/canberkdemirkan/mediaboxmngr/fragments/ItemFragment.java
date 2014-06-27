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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.util.ItemType;

public class ItemFragment extends Fragment {

	private static final String DIALOG_CREATION_DATE = "creation_date";

	public static String ITEM_TYPE;

	private String mUser;
	private Item mItem;

	private EditText mEditItemTitle;
	private EditText mEditItemContent;
	private Button mButtonItemCreationDate;
	private CheckBox mCheckBoxItemFavorite;

	public static ItemFragment newInstance(UUID itemId, String userTag) {
		Bundle args = new Bundle();

		args.putSerializable(Constants.KEY_ITEM_ID, itemId);
		args.putSerializable(Constants.KEY_USER_TAG, userTag);

		ItemFragment fragment = new ItemFragment();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemFragment - onCreate()");
		}
		setHasOptionsMenu(true);

		UUID itemId = (UUID) getArguments().getSerializable(
				Constants.KEY_ITEM_ID);
		mUser = (String) getArguments().getSerializable(Constants.KEY_USER_TAG);

		ItemStock itemStock = ItemStock.get(getActivity(), mUser);
		mItem = itemStock.getItem(itemId);
		ITEM_TYPE = mItem.getType().toString();
	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemFragment - onCreateView()");
		}

		View view = null;
		switch (ItemType.valueOf(ITEM_TYPE)) {
		case Album:
			view = inflater.inflate(R.layout.test1, container, false);
			break;
		case Book:
			view = inflater.inflate(R.layout.test2, container, false);
			break;
		case Movie:
			view = inflater.inflate(R.layout.test3, container, false);
			break;

		default:
			break;
		}

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
						Constants.REQUEST_CODE);
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
		if (requestCode == Constants.REQUEST_CODE) {
			Date creationDate = (Date) data
					.getSerializableExtra(Constants.KEY_ITEM_CREATION_DATE);
			mItem.setCreationDate(creationDate);
			updateCreationDate();
		}
	}

	private void updateCreationDate() {
		mButtonItemCreationDate.setText(mItem.getCreationDate().toString());
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

	/*
	 * 
	 * Logging callback methods for debug purposes
	 */

	@Override
	public void onAttach(Activity activity) {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemFragment - onAttach()");
		}
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemFragment - onActivityCreated()");
		}
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemFragment - onStart()");
		}
		super.onStart();
	}

	@Override
	public void onResume() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemFragment - onStart()");
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemFragment - onStart()");
		}
		ItemStock.get(getActivity(), mUser).saveSerializedItems();
		super.onPause();
	}

	@Override
	public void onStop() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemFragment - onStop()");
		}
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemFragment - onDestroyView()");
		}
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemFragment - onDestroy()");
		}
		super.onDestroy();
	}

	@Override
	public void onDetach() {//
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemFragment - onDetach()");
		}
		super.onDetach();
	}

}