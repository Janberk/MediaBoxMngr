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
import android.widget.TextView;
import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.content.ItemType;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;

public class ItemFragment extends Fragment {

	public static String ITEM_TYPE;

	private String mUser;
	private Item mItem;

	private EditText mEditItemTitle;
	private TextView mTextItemGenre;
	private TextView mTextItemOriginalTitle;
	private TextView mTextItemCountry;
	private TextView mTextItemYear;
	private EditText mEditItemContent;
	private CheckBox mCheckBoxItemFavorite;

	private CheckBox mCheckBoxGotIt;
	private CheckBox mCheckBoxWishList;
	private Spinner mSpinnerItemGenre;
	private Spinner mSpinnerItemCountry;
	private Spinner mSpinnerItemYear;

	private EditText mEditItemGenre;
	private EditText mEditItemOriginalTitle;
	private EditText mEditItemCountry;
	private EditText mEditItemYear;
	private EditText mEditItemAuthor;
	private EditText mEditItemPublishingHouse;
	private EditText mEditItemEdition;
	private EditText mEditItemIsbn;

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
				.findViewById(R.id.et_fragmentDetails_itemTitle);
		mTextItemGenre = (TextView) view
				.findViewById(R.id.tv_fragmentDetails_itemGenre);
		mTextItemOriginalTitle = (TextView) view
				.findViewById(R.id.tv_fragmentDetails_itemOriginalTitle);
		mTextItemCountry = (TextView) view
				.findViewById(R.id.tv_fragmentDetails_itemCountry);
		mTextItemYear = (TextView) view
				.findViewById(R.id.tv_fragmentDetails_itemYear);
		mEditItemContent = (EditText) view
				.findViewById(R.id.et_fragmentDetails_itemContent);
		mCheckBoxItemFavorite = (CheckBox) view
				.findViewById(R.id.cb_fragmentDetails_itemFavorite);
		// mCheckBoxGotIt = (CheckBox) view
		// .findViewById(R.id.cb_fragmentBasics_gotIt);
		// mCheckBoxWishList = (CheckBox) view
		// .findViewById(R.id.cb_fragmentBasics_wishList);
		// mSpinnerItemGenre = (Spinner) view
		// .findViewById(R.id.sp_fragmentBasics_itemGenre);
		// mSpinnerItemCountry = (Spinner) view
		// .findViewById(R.id.sp_fragmentBasics_itemCountry);
		// mSpinnerItemYear = (Spinner) view
		// .findViewById(R.id.sp_fragmentBasics_itemYear);
		mEditItemGenre = (EditText) view
				.findViewById(R.id.et_fragmentDetails_genre);
		mEditItemOriginalTitle = (EditText) view
				.findViewById(R.id.et_fragmentDetails_original);
		mEditItemCountry = (EditText) view
				.findViewById(R.id.et_fragmentDetails_country);
		mEditItemYear = (EditText) view
				.findViewById(R.id.et_fragmentDetails_year);
		mEditItemAuthor = (EditText) view
				.findViewById(R.id.et_fragmentDetails_author);
		mEditItemPublishingHouse = (EditText) view
				.findViewById(R.id.et_fragmentDetails_publisher);
		mEditItemEdition = (EditText) view
				.findViewById(R.id.et_fragmentDetails_edition);
		mEditItemIsbn = (EditText) view
				.findViewById(R.id.et_fragmentDetails_isbn);
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
			view = inflater
					.inflate(R.layout.fragment_details, container, false);
			break;
		case Book:
			view = inflater
					.inflate(R.layout.fragment_details, container, false);
			break;
		case Movie:
			view = inflater
					.inflate(R.layout.fragment_details, container, false);
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

		mEditItemGenre.setText(mItem.getGenre());
		mEditItemGenre.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mItem.setGenre(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {
				updateItem();
			}
		});

		mEditItemOriginalTitle.setText(mItem.getOriginalTitle());
		mEditItemOriginalTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mItem.setOriginalTitle(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {
				updateItem();
			}
		});

		mEditItemCountry.setText(mItem.getCountry());
		mEditItemCountry.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mItem.setCountry(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {
				updateItem();
			}
		});

		mEditItemYear.setText(mItem.getYearPublished());
		mEditItemYear.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mItem.setYearPublished(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {
				updateItem();
			}
		});

		if (mItem instanceof Book) {
			mEditItemAuthor.setText(((Book) mItem).getAuthor());

			mEditItemAuthor.addTextChangedListener(new TextWatcher() {

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					((Book) mItem).setAuthor(s.toString());
				}

				@Override
				public void afterTextChanged(Editable s) {
					updateItem();
				}
			});
		}

		if (mItem instanceof Book) {
			mEditItemPublishingHouse.setText(((Book) mItem)
					.getPublishingHouse());

			mEditItemPublishingHouse.addTextChangedListener(new TextWatcher() {

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					((Book) mItem).setPublishingHouse(s.toString());
				}

				@Override
				public void afterTextChanged(Editable s) {
					updateItem();
				}
			});
		}

		if (mItem instanceof Book) {
			mEditItemEdition.setText(((Book) mItem).getEdition());

			mEditItemEdition.addTextChangedListener(new TextWatcher() {

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					((Book) mItem).setEdition(s.toString());
				}

				@Override
				public void afterTextChanged(Editable s) {
					updateItem();
				}
			});
		}

		if (mItem instanceof Book) {
			mEditItemIsbn.setText(((Book) mItem).getIsbn());

			mEditItemIsbn.addTextChangedListener(new TextWatcher() {

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					((Book) mItem).setIsbn(s.toString());
				}

				@Override
				public void afterTextChanged(Editable s) {
					updateItem();
				}
			});
		}

		mEditItemContent.setText(mItem.getContent());
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

		// mCheckBoxGotIt.setChecked(mItem.isInPossession());
		// mCheckBoxGotIt
		// .setOnCheckedChangeListener(new OnCheckedChangeListener() {
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// mItem.setInPossession(isChecked);
		// updateItem();
		// }
		// });
		//
		// mSpinnerItemGenre.setAdapter(new CustomSpinnerAdapter(getActivity(),
		// R.layout.custom_spinner, R.id.tv_customSpinner_label,
		// CustomSpinnerAdapter.getContent()));

		String itemGenre = mItem.getGenre();
		String itemOriginalTitle = mItem.getOriginalTitle();
		String itemCountry = mItem.getCountry();
		String itemYear = mItem.getYearPublished();

		if (itemGenre != null && itemOriginalTitle != null
				&& itemCountry != null && itemYear != null) {
			mTextItemGenre.setText(itemGenre);
			mTextItemOriginalTitle.setText(itemOriginalTitle);
			mTextItemCountry.setText(itemCountry);
			mTextItemYear.setText(itemYear);
		} else {
			mTextItemGenre.setText("");
			mTextItemOriginalTitle.setText("");
			mTextItemCountry.setText("");
			mTextItemYear.setText("");
		}
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