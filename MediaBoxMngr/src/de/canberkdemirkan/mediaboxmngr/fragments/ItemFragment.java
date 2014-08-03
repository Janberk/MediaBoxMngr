package de.canberkdemirkan.mediaboxmngr.fragments;

import java.io.Serializable;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.activities.ItemEditorActivity;
import de.canberkdemirkan.mediaboxmngr.activities.LoginActivity;
import de.canberkdemirkan.mediaboxmngr.content.ItemType;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.dialogs.AlertDialogDeletion;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.Music;

public class ItemFragment extends Fragment implements Serializable,
		View.OnClickListener, OnCheckedChangeListener,
		OnRatingBarChangeListener, TextWatcher {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9104738005805814331L;

	private static final int REQUEST_CODE = 0;

	private SharedPreferences mSharedPreferences;
	private FragmentManager mFragmentManager;

	private Item mItem;
	private UUID mItemId;
	public static String sItemType;
	private String mUser;

	private ImageView mImageHome;
	private ImageView mImageSearch;
	private ImageView mImageSettings;
	private ImageView mImageLogout;

	private EditText mEditItemTitle;
	private TextView mTextItemContent;
	private TextView mTextItemGenre;
	private TextView mTextItemCountryYear;

	private TextView mTextItemAuthor;
	private TextView mTextItemPublisherEdition;
	private TextView mTextItemIsbn;

	private TextView mTextItemDirector;
	private TextView mTextItemCast;
	private TextView mTextItemLength;
	private TextView mTextItemMusic;

	private TextView mTextItemArtist;
	private TextView mTextItemLabel;
	private TextView mTextItemFormatTitleCount;

	private CheckBox mCheckBoxItemFavorite;
	private RatingBar mRatingBarItemRating;

	public static ItemFragment newInstance(UUID itemId, String userTag) {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemFragment - onCreate()");
		}
		Bundle args = new Bundle();

		args.putSerializable(Constants.KEY_ITEM_UID, itemId);
		args.putSerializable(Constants.KEY_USER_TAG, userTag);

		ItemFragment fragment = new ItemFragment();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);

		mSharedPreferences = getActivity().getSharedPreferences(
				Constants.KEY_MY_PREFERENCES, Context.MODE_PRIVATE);

		mItemId = (UUID) getArguments().getSerializable(Constants.KEY_ITEM_UID);
		mUser = (String) getArguments().getSerializable(Constants.KEY_USER_TAG);

		mFragmentManager = getActivity().getSupportFragmentManager();

		mItem = ItemStock.get(getActivity(), mUser).getItem(mItemId);

		sItemType = mItem.getType().toString();
	}

	private void initViews(View view) {
		mEditItemTitle = (EditText) view
				.findViewById(R.id.et_fragmentDetails_itemTitle);
		mTextItemContent = (TextView) view
				.findViewById(R.id.tv_fragmentDetails_itemContent);
		mTextItemGenre = (TextView) view
				.findViewById(R.id.tv_fragmentDetails_itemGenre);
		mTextItemCountryYear = (TextView) view
				.findViewById(R.id.tv_fragmentDetails_itemCountryYear);

		mTextItemAuthor = (TextView) view
				.findViewById(R.id.tv_fragmentDetails_itemAuthor);
		mTextItemPublisherEdition = (TextView) view
				.findViewById(R.id.tv_fragmentDetails_itemPublisherEdition);
		mTextItemIsbn = (TextView) view
				.findViewById(R.id.tv_fragmentDetails_itemIsbn);

		mTextItemDirector = (TextView) view
				.findViewById(R.id.tv_fragmentDetails_itemDirector);
		mTextItemCast = (TextView) view
				.findViewById(R.id.tv_fragmentDetails_itemCast);
		mTextItemMusic = (TextView) view
				.findViewById(R.id.tv_fragmentDetails_itemMusic);
		mTextItemLength = (TextView) view
				.findViewById(R.id.tv_fragmentDetails_itemLength);

		mTextItemArtist = (TextView) view
				.findViewById(R.id.tv_fragmentDetails_itemArtist);
		mTextItemLabel = (TextView) view
				.findViewById(R.id.tv_fragmentDetails_itemLabel);
		mTextItemFormatTitleCount = (TextView) view
				.findViewById(R.id.tv_fragmentDetails_itemFormatTracks);

		mCheckBoxItemFavorite = (CheckBox) view
				.findViewById(R.id.cb_fragmentDetails_itemFavorite);
		mRatingBarItemRating = (RatingBar) view
				.findViewById(R.id.rb_fragmentDetails_itemRating);
		// mSpinnerItemGenre = (Spinner) view
		// .findViewById(R.id.sp_fragmentBasics_itemGenre);
		// mSpinnerItemCountry = (Spinner) view
		// .findViewById(R.id.sp_fragmentBasics_itemCountry);
		// mSpinnerItemYear = (Spinner) view
		// .findViewById(R.id.sp_fragmentBasics_itemYear);

		mImageHome = (ImageView) view
				.findViewById(R.id.iv_fragmentMenuBar_home);
		mImageSearch = (ImageView) view
				.findViewById(R.id.iv_fragmentMenuBar_search);
		mImageSettings = (ImageView) view
				.findViewById(R.id.iv_fragmentMenuBar_settings);
		mImageLogout = (ImageView) view
				.findViewById(R.id.iv_fragmentMenuBar_logout);
	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = null;
		switch (ItemType.valueOf(sItemType)) {
		case Album:
			view = inflater.inflate(R.layout.fragment_view_music, container,
					false);
			break;
		case Book:
			view = inflater.inflate(R.layout.fragment_view_book, container,
					false);
			break;
		case Movie:
			view = inflater.inflate(R.layout.fragment_view_movie, container,
					false);
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
		mEditItemTitle.addTextChangedListener(this);

		mCheckBoxItemFavorite.setChecked(mItem.isFavorite());
		mCheckBoxItemFavorite.setOnCheckedChangeListener(this);

		try {
			String ratingString = mItem.getRating();
			if (ratingString != null) {
				Float rating = Float.valueOf(ratingString);
				mRatingBarItemRating.setRating(rating);
			} else {
				mRatingBarItemRating.setRating(0.0F);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		mRatingBarItemRating.setOnRatingBarChangeListener(this);

		// mSpinnerItemGenre.setAdapter(new CustomSpinnerAdapter(getActivity(),
		// R.layout.custom_spinner, R.id.tv_customSpinner_label,
		// CustomSpinnerAdapter.getContent()));

		updateItemDetails(mItem);

		mImageHome.setOnClickListener(this);
		mImageSearch.setOnClickListener(this);
		mImageSettings.setOnClickListener(this);
		mImageLogout.setOnClickListener(this);

		return view;
	}

	private void setTextIfNotNull(TextView view, String s) {
		if (s != null) {
			view.setText(s);
		} else {
			view.setText("");
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_menu_details, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		case R.id.menu_editItem:
			Intent intent = new Intent(getActivity(), ItemEditorActivity.class);
			intent.putExtra(Constants.KEY_USER_TAG, mUser);
			intent.putExtra(Constants.KEY_ITEM_UID, mItem.getUniqueId());
			intent.putExtra(Constants.EXTRA_DETAILS_ITEM, mItem);
			startActivityForResult(intent, REQUEST_CODE);
			return true;
		case R.id.menu_deleteItem:
			final String header = getActivity().getResources().getString(
					R.string.dialog_header_delete);
			AlertDialogDeletion dialog = AlertDialogDeletion.newInstance(this,
					null, mItem, header,
					AlertDialogDeletion.DIALOG_TAG_DETAIL_SINGLE);
			dialog.setTargetFragment(this, Constants.REQUEST_LIST_DELETE);
			dialog.show(mFragmentManager, "");

			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		mItem = (Item) data.getSerializableExtra(Constants.KEY_ITEM);
		if (requestCode == REQUEST_CODE) {
			updateItemDetails(mItem);
		}
	}

	private void updateItemDetails(Item item) {
		String itemContent = item.getContent();
		String itemGenre = item.getGenre();
		String itemCountryYear = item.getCountry() + ", " + item.getYear();

		setTextIfNotNull(mTextItemContent, itemContent);
		setTextIfNotNull(mTextItemGenre, itemGenre);
		setTextIfNotNull(mTextItemCountryYear, itemCountryYear);

		// Book
		String itemAuthor = null;
		String itemPublisherEdition = null;
		String itemIsbn = null;
		if (item instanceof Book) {
			itemAuthor = ((Book) item).getAuthor();
			itemPublisherEdition = ((Book) item).getPublisher() + ", "
					+ ((Book) item).getEdition();
			itemIsbn = ((Book) item).getIsbn();

			setTextIfNotNull(mTextItemAuthor, itemAuthor);
			setTextIfNotNull(mTextItemPublisherEdition, itemPublisherEdition);
			setTextIfNotNull(mTextItemIsbn, itemIsbn);
		}

		// Movie
		String itemDirector = null;
		String itemCast = null;
		String itemLength = null;
		String itemMusic = null;
		if (item instanceof Movie) {
			itemDirector = ((Movie) item).getDirector();
			itemCast = ((Movie) item).getCast();
			itemLength = ((Movie) item).getLength() + " min.";
			itemMusic = ((Movie) item).getMusic();

			setTextIfNotNull(mTextItemDirector, itemDirector);
			setTextIfNotNull(mTextItemCast, itemCast);
			setTextIfNotNull(mTextItemLength, itemLength);
			setTextIfNotNull(mTextItemMusic, itemMusic);
		}

		// Music
		String itemArtist = null;
		String itemLabel = null;
		String itemFormatTracks = null;
		if (item instanceof Music) {
			itemArtist = ((Music) item).getArtist();
			itemLabel = ((Music) item).getLabel();
			itemFormatTracks = ((Music) item).getFormat() + ", "
					+ ((Music) item).getTitleCount();

			setTextIfNotNull(mTextItemArtist, itemArtist);
			setTextIfNotNull(mTextItemLabel, itemLabel);
			setTextIfNotNull(mTextItemFormatTitleCount, itemFormatTracks);
		}

	}

	private void updateItem() {
		ItemStock.get(getActivity(), mUser).updateItem(mItem);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemFragment - onPause()");
		}
		ItemStock.get(getActivity(), mUser).saveSerializedItems();
		super.onPause();
	}

	private void logout() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemFragment - logout()");
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

	// click listeners
	@Override
	public void onClick(View view) {

		if (view == mImageHome) {
			Toast.makeText(getActivity(), "Home", Toast.LENGTH_LONG).show();
		}
		if (view == mImageSearch) {
			// loadItemsFromRemoteDb();
			Toast.makeText(getActivity(), "Search", Toast.LENGTH_LONG).show();
		}
		if (view == mImageSettings) {
			Toast.makeText(getActivity(), "Settings", Toast.LENGTH_LONG).show();
			// try {
			// syncWithRemoteDb();
			// } catch (JSONException e) {
			// e.printStackTrace();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}
		if (view == mImageLogout) {
			Toast.makeText(getActivity(), "Logout", Toast.LENGTH_LONG).show();
			logout();
		}

	}

	// text watcher callback methods
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		if (mEditItemTitle.getText().hashCode() == s.hashCode()) {
			mItem.setTitle(s.toString());
		}

	}

	@Override
	public void afterTextChanged(Editable s) {
		updateItemDetails(mItem);
		new UpdateItemTask().execute();
	}

	// check box listener callback
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView == mCheckBoxItemFavorite) {
			mItem.setFavorite(isChecked);
			new UpdateItemTask().execute();
		}
	}

	public String getUser() {
		return mUser;
	}

	public class UpdateItemTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... params) {
			updateItem();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		}

	}

	// rating bar callback
	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		if (ratingBar == mRatingBarItemRating) {
			mItem.setRating(String.valueOf(rating));
			new UpdateItemTask().execute();
		}
	}

}