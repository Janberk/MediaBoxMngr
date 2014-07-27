package de.canberkdemirkan.mediaboxmngr.fragments;

import java.io.Serializable;
import java.util.UUID;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.TextView;
import android.widget.Toast;
import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.activities.LoginActivity;
import de.canberkdemirkan.mediaboxmngr.content.ItemType;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.dialogs.AlertDialogDeletion;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.MusicAlbum;

public class ItemFragment extends Fragment implements Serializable,
		View.OnClickListener, OnCheckedChangeListener, TextWatcher {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9104738005805814331L;

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
	private TextView mTextItemGenre;
	private TextView mTextItemOriginalTitle;
	private TextView mTextItemCountry;
	private TextView mTextItemYear;
	private EditText mEditItemContent;
	private CheckBox mCheckBoxItemFavorite;

	private EditText mEditItemGenre;
	private EditText mEditItemOriginalTitle;
	private EditText mEditItemCountry;
	private EditText mEditItemYear;

	private EditText mEditItemAuthor;
	private EditText mEditItemPublishingHouse;
	private EditText mEditItemEdition;
	private EditText mEditItemIsbn;

	private EditText mEditItemProducerMovie;
	private EditText mEditItemDirector;
	private EditText mEditItemScript;
	private EditText mEditItemActors;
	private EditText mEditItemMusic;
	private EditText mEditItemLength;

	private EditText mEditItemArtist;
	private EditText mEditItemLabel;
	private EditText mEditItemStudio;
	private EditText mEditItemProducerAlbum;
	private EditText mEditItemFormat;
	private EditText mEditItemTitleCount;

	public static ItemFragment newInstance(UUID itemId, String userTag) {
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

		mEditItemProducerMovie = (EditText) view
				.findViewById(R.id.et_fragmentDetails_producer);
		mEditItemDirector = (EditText) view
				.findViewById(R.id.et_fragmentDetails_director);
		mEditItemScript = (EditText) view
				.findViewById(R.id.et_fragmentDetails_script);
		mEditItemActors = (EditText) view
				.findViewById(R.id.et_fragmentDetails_actors);
		mEditItemMusic = (EditText) view
				.findViewById(R.id.et_fragmentDetails_music);
		mEditItemLength = (EditText) view
				.findViewById(R.id.et_fragmentDetails_length);

		mEditItemArtist = (EditText) view
				.findViewById(R.id.et_fragmentDetails_artist);
		mEditItemLabel = (EditText) view
				.findViewById(R.id.et_fragmentDetails_label);
		mEditItemStudio = (EditText) view
				.findViewById(R.id.et_fragmentDetails_studio);
		mEditItemProducerAlbum = (EditText) view
				.findViewById(R.id.et_fragmentDetails_producer);
		mEditItemFormat = (EditText) view
				.findViewById(R.id.et_fragmentDetails_format);
		mEditItemTitleCount = (EditText) view
				.findViewById(R.id.et_fragmentDetails_titleCount);

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
			view = inflater.inflate(R.layout.fragment_details_album_view,
					container, false);
			break;
		case Book:
			view = inflater.inflate(R.layout.fragment_details_book_view,
					container, false);
			break;
		case Movie:
			view = inflater.inflate(R.layout.fragment_details_movie_view,
					container, false);
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

		configureTextFields();

		mCheckBoxItemFavorite.setChecked(mItem.isFavorite());
		mCheckBoxItemFavorite.setOnCheckedChangeListener(this);

		// mSpinnerItemGenre.setAdapter(new CustomSpinnerAdapter(getActivity(),
		// R.layout.custom_spinner, R.id.tv_customSpinner_label,
		// CustomSpinnerAdapter.getContent()));

		updateItemDetails();

		mImageHome.setOnClickListener(this);
		mImageSearch.setOnClickListener(this);
		mImageSettings.setOnClickListener(this);
		mImageLogout.setOnClickListener(this);

		return view;
	}

	private void updateItemDetails() {
		String itemGenre = mItem.getGenre();
		String itemOriginalTitle = mItem.getOriginalTitle();
		String itemCountry = mItem.getCountry();
		String itemYear = mItem.getYearPublished();

		setTextIfNotNull(mTextItemGenre, itemGenre);
		setTextIfNotNull(mTextItemOriginalTitle, itemOriginalTitle);
		setTextIfNotNull(mTextItemCountry, itemCountry);
		setTextIfNotNull(mTextItemYear, itemYear);
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
			Toast.makeText(getActivity(), "Edit", Toast.LENGTH_LONG).show();
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

	private void updateItem() {
		ItemStock.get(getActivity(), mUser).updateItem(mItem);
	}

	/*
	 * 
	 * Logging callback methods for debug purposes
	 */

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
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
		if (mEditItemGenre.getText().hashCode() == s.hashCode()) {
			mItem.setGenre(s.toString());
		}
		if (mEditItemOriginalTitle.getText().hashCode() == s.hashCode()) {
			mItem.setOriginalTitle(s.toString());
		}
		if (mEditItemCountry.getText().hashCode() == s.hashCode()) {
			mItem.setCountry(s.toString());
		}
		if (mEditItemYear.getText().hashCode() == s.hashCode()) {
			mItem.setYearPublished(s.toString());
		}
		if (mEditItemContent.getText().hashCode() == s.hashCode()) {
			mItem.setContent(s.toString());
		}
		if (mItem instanceof Book) {
			if (mEditItemAuthor.getText().hashCode() == s.hashCode()) {
				((Book) mItem).setAuthor(s.toString());
			}
			if (mEditItemPublishingHouse.getText().hashCode() == s.hashCode()) {
				((Book) mItem).setPublishingHouse(s.toString());
			}
			if (mEditItemEdition.getText().hashCode() == s.hashCode()) {
				((Book) mItem).setEdition(s.toString());
			}
			if (mEditItemIsbn.getText().hashCode() == s.hashCode()) {
				((Book) mItem).setIsbn(s.toString());
			}
		}
		if (mItem instanceof Movie) {
			if (mEditItemProducerMovie.getText().hashCode() == s.hashCode()) {
				((Movie) mItem).setProducer(s.toString());
			}
			if (mEditItemDirector.getText().hashCode() == s.hashCode()) {
				((Movie) mItem).setDirector(s.toString());
			}
			if (mEditItemScript.getText().hashCode() == s.hashCode()) {
				((Movie) mItem).setScript(s.toString());
			}
			if (mEditItemActors.getText().hashCode() == s.hashCode()) {
				((Movie) mItem).setActors(s.toString());
			}
			if (mEditItemMusic.getText().hashCode() == s.hashCode()) {
				((Movie) mItem).setMusic(s.toString());
			}
			if (mEditItemLength.getText().hashCode() == s.hashCode()) {
				((Movie) mItem).setLength(s.toString());
			}
		}
		if (mItem instanceof MusicAlbum) {
			if (mEditItemArtist.getText().hashCode() == s.hashCode()) {
				((MusicAlbum) mItem).setArtist(s.toString());
			}
			if (mEditItemLabel.getText().hashCode() == s.hashCode()) {
				((MusicAlbum) mItem).setLabel(s.toString());
			}
			if (mEditItemStudio.getText().hashCode() == s.hashCode()) {
				((MusicAlbum) mItem).setStudio(s.toString());
			}
			if (mEditItemProducerAlbum.getText().hashCode() == s.hashCode()) {
				((MusicAlbum) mItem).setProducer(s.toString());
			}
			if (mEditItemFormat.getText().hashCode() == s.hashCode()) {
				((MusicAlbum) mItem).setFormat(s.toString());
			}
			if (mEditItemTitleCount.getText().hashCode() == s.hashCode()) {
				((MusicAlbum) mItem).setTitleCount(s.toString());
			}
		}

	}

	@Override
	public void afterTextChanged(Editable s) {
		updateItemDetails();
		updateItem();
	}

	// check box listener callback
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView == mCheckBoxItemFavorite) {
			mItem.setFavorite(isChecked);
			updateItem();
		}
	}

	private void configureTextFields() {
		mEditItemTitle.setText(mItem.getTitle());
		mEditItemGenre.setText(mItem.getGenre());
		mEditItemOriginalTitle.setText(mItem.getOriginalTitle());
		mEditItemCountry.setText(mItem.getCountry());
		mEditItemYear.setText(mItem.getYearPublished());
		mEditItemContent.setText(mItem.getContent());

		mEditItemTitle.addTextChangedListener(this);
		mEditItemGenre.addTextChangedListener(this);
		mEditItemOriginalTitle.addTextChangedListener(this);
		mEditItemCountry.addTextChangedListener(this);
		mEditItemYear.addTextChangedListener(this);
		mEditItemContent.addTextChangedListener(this);

		if (mItem instanceof Book) {
			mEditItemAuthor.setText(((Book) mItem).getAuthor());
			mEditItemAuthor.addTextChangedListener(this);
			mEditItemPublishingHouse.setText(((Book) mItem)
					.getPublishingHouse());
			mEditItemPublishingHouse.addTextChangedListener(this);
			mEditItemEdition.setText(((Book) mItem).getEdition());
			mEditItemEdition.addTextChangedListener(this);
			mEditItemIsbn.setText(((Book) mItem).getIsbn());
			mEditItemIsbn.addTextChangedListener(this);
		}

		if (mItem instanceof Movie) {
			mEditItemProducerMovie.setText(((Movie) mItem).getProducer());
			mEditItemProducerMovie.addTextChangedListener(this);
			mEditItemDirector.setText(((Movie) mItem).getDirector());
			mEditItemDirector.addTextChangedListener(this);
			mEditItemScript.setText(((Movie) mItem).getScript());
			mEditItemScript.addTextChangedListener(this);
			mEditItemActors.setText(((Movie) mItem).getActors());
			mEditItemActors.addTextChangedListener(this);
			mEditItemMusic.setText(((Movie) mItem).getMusic());
			mEditItemMusic.addTextChangedListener(this);
			mEditItemLength.setText(((Movie) mItem).getLength());
			mEditItemLength.addTextChangedListener(this);
		}

		if (mItem instanceof MusicAlbum) {
			mEditItemArtist.setText(((MusicAlbum) mItem).getArtist());
			mEditItemArtist.addTextChangedListener(this);
			mEditItemLabel.setText(((MusicAlbum) mItem).getLabel());
			mEditItemLabel.addTextChangedListener(this);
			mEditItemStudio.setText(((MusicAlbum) mItem).getStudio());
			mEditItemStudio.addTextChangedListener(this);
			mEditItemProducerAlbum.setText(((MusicAlbum) mItem).getProducer());
			mEditItemProducerAlbum.addTextChangedListener(this);
			mEditItemFormat.setText(((MusicAlbum) mItem).getFormat());
			mEditItemFormat.addTextChangedListener(this);
			mEditItemTitleCount.setText(((MusicAlbum) mItem).getTitleCount());
			mEditItemTitleCount.addTextChangedListener(this);
		}
	}

	public String getUser() {
		return mUser;
	}

}