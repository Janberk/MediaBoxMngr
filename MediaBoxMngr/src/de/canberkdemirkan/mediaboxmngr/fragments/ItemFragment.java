package de.canberkdemirkan.mediaboxmngr.fragments;

import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.content.ItemType;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.util.CustomSpinnerAdapter;

public class ItemFragment extends Fragment {

	public static String ITEM_TYPE;

	private String mUser;
	private Item mItem;

	private EditText mEditItemTitle;
	private EditText mEditItemOriginalTitle;
	private EditText mEditItemContent;
	private CheckBox mCheckBoxItemFavorite;
	private CheckBox mCheckBoxGotIt;
	private CheckBox mCheckBoxWishList;
	private Spinner mSpinnerItemGenre;
	private Spinner mSpinnerItemCountry;
	private Spinner mSpinnerItemYear;

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

	private void initViews(View view) {
		mEditItemTitle = (EditText) view
				.findViewById(R.id.et_fragmentBasics_itemTitle);
		mEditItemOriginalTitle = (EditText) view
				.findViewById(R.id.et_fragmentBasics_itemOriginalTitle);
		mEditItemContent = (EditText) view
				.findViewById(R.id.et_fragmentBasics_itemContent);
		mCheckBoxItemFavorite = (CheckBox) view
				.findViewById(R.id.cb_fragmentBasics_itemFavorite);
		mCheckBoxGotIt = (CheckBox) view
				.findViewById(R.id.cb_fragmentBasics_gotIt);
		mCheckBoxWishList = (CheckBox) view
				.findViewById(R.id.cb_fragmentBasics_wishList);
		mSpinnerItemGenre = (Spinner) view
				.findViewById(R.id.sp_fragmentBasics_itemGenre);
		mSpinnerItemCountry = (Spinner) view
				.findViewById(R.id.sp_fragmentBasics_itemCountry);
		mSpinnerItemYear = (Spinner) view
				.findViewById(R.id.sp_fragmentBasics_itemYear);
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
			view = inflater.inflate(R.layout.fragment_item, container, false);
			break;
		case Book:
			view = inflater.inflate(R.layout.fragment_item, container, false);
			break;
		case Movie:
			view = inflater.inflate(R.layout.fragment_item, container, false);
			break;

		default:
			break;
		}

		initViews(view);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}

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

		mEditItemContent.setText(mItem.toString());
		mEditItemContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mItem.setContent(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {
				updateItem();
			}
		});

		mCheckBoxItemFavorite.setChecked(mItem.isFavorite());
		mCheckBoxItemFavorite
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						mItem.setFavorite(isChecked);
						updateItem();
					}
				});

		mCheckBoxGotIt.setChecked(mItem.isInPossession());
		mCheckBoxGotIt
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						mItem.setInPossession(isChecked);
						updateItem();
					}
				});

		mSpinnerItemGenre.setAdapter(new CustomSpinnerAdapter(getActivity(),
				R.layout.custom_spinner, R.id.tv_customSpinner_label,
				CustomSpinnerAdapter.getContent()));

		return view;
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