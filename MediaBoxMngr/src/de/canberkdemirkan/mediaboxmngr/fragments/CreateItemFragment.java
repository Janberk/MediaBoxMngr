package de.canberkdemirkan.mediaboxmngr.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.adapters.CustomSpinnerAdapter;
import de.canberkdemirkan.mediaboxmngr.adapters.CustomSpinnerAdapter.SpinnerTag;
import de.canberkdemirkan.mediaboxmngr.content.ItemType;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.interfaces.OnFragmentTransactionListener;
import de.canberkdemirkan.mediaboxmngr.interfaces.OnItemCreatedListener;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.Music;

public class CreateItemFragment extends Fragment implements
		View.OnClickListener, OnItemSelectedListener,
		OnFragmentTransactionListener, OnItemCreatedListener {

	public static final String TAG_CREATE_ITEM_FRAGMENT = "de.canberkdemirkan.mediaboxmngr.tag_create_item_fragment";

	private OnItemCreatedListener mOnItemCreatedListener;
	private OnFragmentTransactionListener mOnFragmentTransactionListener;

	private String mUser;

	private Spinner mSpinnerItemType;
	private SpinnerTag mTypeSpinner = SpinnerTag.TypeSpinner;
	private String mTypeAsString;

	private EditText mEditEditTitle;
	private Button mButtonSaveItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getArguments();
		mUser = (String) bundle.get(Constants.KEY_ITEMLIST_USER);
	}

	public static CreateItemFragment newInstance(String user) {

		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.KEY_ITEMLIST_USER, user);

		CreateItemFragment fragment = new CreateItemFragment();
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_create_item, null);
		initViews(view);

		mEditEditTitle.requestFocus();
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEditEditTitle, InputMethodManager.SHOW_IMPLICIT);

		mSpinnerItemType.setAdapter(new CustomSpinnerAdapter(getActivity(),
				R.layout.custom_spinner, R.id.tv_customSpinner_label,
				CustomSpinnerAdapter.getContent(mTypeSpinner)));
		mSpinnerItemType.setOnItemSelectedListener(this);
		mSpinnerItemType.setSelection(0);

		mButtonSaveItem.setOnClickListener(this);

		return view;
	}

	private void initViews(View view) {
		mEditEditTitle = (EditText) view
				.findViewById(R.id.et_fragmentEditTitle_editTitle);
		mSpinnerItemType = (Spinner) view
				.findViewById(R.id.sp_fragmentEditTitle_itemType);
		mButtonSaveItem = (Button) view
				.findViewById(R.id.btn_fragmentEditTitle_saveItem);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		refreshView(inflater, (ViewGroup) getView());
	}

	private void refreshView(LayoutInflater inflater, ViewGroup viewGroup) {
		viewGroup.removeAllViewsInLayout();
		View view = inflater.inflate(R.layout.fragment_create_item, viewGroup);

		initViews(view);

		mEditEditTitle.requestFocus();
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEditEditTitle, InputMethodManager.SHOW_IMPLICIT);

		mSpinnerItemType.setAdapter(new CustomSpinnerAdapter(getActivity(),
				R.layout.custom_spinner, R.id.tv_customSpinner_label,
				CustomSpinnerAdapter.getContent(mTypeSpinner)));
		mSpinnerItemType.setOnItemSelectedListener(this);
		mSpinnerItemType.setSelection(0);

		mButtonSaveItem.setOnClickListener(this);
	}

	private Item createItem(ItemType type) {
		Item item = null;

		switch (type) {
		case Album:
			item = new Music();
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
		if (item != null) {
			item.setTitle(mEditEditTitle.getText().toString());
			item.setType(type);
			item.setUser(mUser);
		}
		return item;
	}

	private void saveItem() {
		ItemType type = ItemType.valueOf(mTypeAsString);
		Item newItem = createItem(type);
		ItemStock.get(getActivity(), mUser).addItem(newItem);
		mEditEditTitle.setText("");
	}

	@Override
	public void onClick(View view) {
		if (view == mButtonSaveItem) {
			saveItem();
			onItemCreated(mUser);
			onFragmentTransaction(ItemListFragment.TAG_ITEMLIST_FRAGMENT);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		mTypeAsString = (String) parent.getItemAtPosition(position);
		// RelativeLayout layout = (RelativeLayout) parent.getChildAt(0);
		// if (layout != null) {
		// ((TextView) layout.findViewById(R.id.tv_customSpinner_label))
		// .setTextAppearance(getActivity(), R.style.spinnerTextStyle);
		// }
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnFragmentTransactionListener = (OnFragmentTransactionListener) activity;
			mOnItemCreatedListener = (OnItemCreatedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					getActivity().getClass().getSimpleName()
							+ " must implement OnFragmentTransactionListener and OnItemCreatedListener.");
		}
	}

	@Override
	public void onFragmentTransaction(String tag) {
		mOnFragmentTransactionListener.onFragmentTransaction(tag);
	}

	@Override
	public void onItemCreated(String user) {
		mOnItemCreatedListener.onItemCreated(user);
	}

}