package de.canberkdemirkan.mediaboxmngr.fragments;

import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.MusicAlbum;

public class ItemEditorFragment extends Fragment implements TextWatcher {

	private String mUser;
	private Item mItem;

	private EditText mEditItemContent;

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

	public static ItemEditorFragment newInstance(UUID itemId, String userTag, Item item) {
		Bundle args = new Bundle();

		args.putSerializable(Constants.EXTRA_DETAILS_ITEM, item);
		args.putSerializable(Constants.KEY_ITEM_UID, itemId);
		args.putSerializable(Constants.KEY_USER_TAG, userTag);

		ItemEditorFragment fragment = new ItemEditorFragment();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);

		mUser = (String) getArguments().getSerializable(Constants.KEY_USER_TAG);

		Bundle bundle = getArguments();

		mItem = (Item) bundle.get(Constants.EXTRA_DETAILS_ITEM);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = null;

		if (mItem instanceof Book) {
			view = inflater.inflate(R.layout.fragment_details_book, container,
					false);
		}
		if (mItem instanceof Movie) {
			view = inflater.inflate(R.layout.fragment_details_movie, container,
					false);
		}
		if (mItem instanceof MusicAlbum) {
			view = inflater.inflate(R.layout.fragment_details_album, container,
					false);
		}

		initViews(view);
		configureTextFields();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}

		return view;
	}

	private void initViews(View view) {
		mEditItemContent = (EditText) view
				.findViewById(R.id.et_fragmentDetails_itemContent);
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
	}

	private void configureTextFields() {
		mEditItemGenre.setText(mItem.getGenre());
		mEditItemOriginalTitle.setText(mItem.getOriginalTitle());
		mEditItemCountry.setText(mItem.getCountry());
		mEditItemYear.setText(mItem.getYearPublished());
		mEditItemContent.setText(mItem.getContent());

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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent();
			intent.putExtra(Constants.KEY_ITEM, mItem);
			getActivity().setResult(Activity.RESULT_OK, intent);
			getActivity().finish();
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}

	// text watcher callback methods
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

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
		ItemStock.get(getActivity(), mUser).updateItem(mItem);
	}

}