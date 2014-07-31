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
import de.canberkdemirkan.mediaboxmngr.model.Music;

public class ItemEditorFragment extends Fragment implements TextWatcher {

	private String mUser;
	private Item mItem;

	private EditText mEditItemContent;

	private EditText mEditItemGenre;
	private EditText mEditItemCountry;
	private EditText mEditItemYear;

	private EditText mEditItemAuthor;
	private EditText mEditItemPublisher;
	private EditText mEditItemEdition;
	private EditText mEditItemIsbn;

	private EditText mEditItemDirector;
	private EditText mEditItemCast;
	private EditText mEditItemMusic;
	private EditText mEditItemLength;

	private EditText mEditItemArtist;
	private EditText mEditItemLabel;
	private EditText mEditItemFormat;
	private EditText mEditItemTitleCount;

	public static ItemEditorFragment newInstance(UUID itemId, String userTag,
			Item item) {
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
			view = inflater.inflate(R.layout.fragment_edit_book, container,
					false);
		}
		if (mItem instanceof Movie) {
			view = inflater.inflate(R.layout.fragment_edit_movie, container,
					false);
		}
		if (mItem instanceof Music) {
			view = inflater.inflate(R.layout.fragment_edit_music, container,
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
		mEditItemCountry = (EditText) view
				.findViewById(R.id.et_fragmentDetails_country);
		mEditItemYear = (EditText) view
				.findViewById(R.id.et_fragmentDetails_year);

		mEditItemAuthor = (EditText) view
				.findViewById(R.id.et_fragmentDetails_author);
		mEditItemPublisher = (EditText) view
				.findViewById(R.id.et_fragmentDetails_publisher);
		mEditItemEdition = (EditText) view
				.findViewById(R.id.et_fragmentDetails_edition);
		mEditItemIsbn = (EditText) view
				.findViewById(R.id.et_fragmentDetails_isbn);

		mEditItemDirector = (EditText) view
				.findViewById(R.id.et_fragmentDetails_director);
		mEditItemCast = (EditText) view
				.findViewById(R.id.et_fragmentDetails_cast);
		mEditItemMusic = (EditText) view
				.findViewById(R.id.et_fragmentDetails_music);
		mEditItemLength = (EditText) view
				.findViewById(R.id.et_fragmentDetails_length);

		mEditItemArtist = (EditText) view
				.findViewById(R.id.et_fragmentDetails_artist);
		mEditItemLabel = (EditText) view
				.findViewById(R.id.et_fragmentDetails_label);
		mEditItemFormat = (EditText) view
				.findViewById(R.id.et_fragmentDetails_format);
		mEditItemTitleCount = (EditText) view
				.findViewById(R.id.et_fragmentDetails_titleCount);
	}

	private void configureTextFields() {
		mEditItemGenre.setText(mItem.getGenre());
		mEditItemCountry.setText(mItem.getCountry());
		mEditItemYear.setText(mItem.getYear());
		mEditItemContent.setText(mItem.getContent());

		mEditItemGenre.addTextChangedListener(this);
		mEditItemCountry.addTextChangedListener(this);
		mEditItemYear.addTextChangedListener(this);
		mEditItemContent.addTextChangedListener(this);

		if (mItem instanceof Book) {
			mEditItemAuthor.setText(((Book) mItem).getAuthor());
			mEditItemAuthor.addTextChangedListener(this);
			mEditItemPublisher.setText(((Book) mItem).getPublisher());
			mEditItemPublisher.addTextChangedListener(this);
			mEditItemEdition.setText(((Book) mItem).getEdition());
			mEditItemEdition.addTextChangedListener(this);
			mEditItemIsbn.setText(((Book) mItem).getIsbn());
			mEditItemIsbn.addTextChangedListener(this);
		}

		if (mItem instanceof Movie) {
			mEditItemDirector.setText(((Movie) mItem).getDirector());
			mEditItemDirector.addTextChangedListener(this);
			mEditItemCast.setText(((Movie) mItem).getCast());
			mEditItemCast.addTextChangedListener(this);
			mEditItemMusic.setText(((Movie) mItem).getMusic());
			mEditItemMusic.addTextChangedListener(this);
			mEditItemLength.setText(((Movie) mItem).getLength());
			mEditItemLength.addTextChangedListener(this);
		}

		if (mItem instanceof Music) {
			mEditItemArtist.setText(((Music) mItem).getArtist());
			mEditItemArtist.addTextChangedListener(this);
			mEditItemLabel.setText(((Music) mItem).getLabel());
			mEditItemLabel.addTextChangedListener(this);
			mEditItemFormat.setText(((Music) mItem).getFormat());
			mEditItemFormat.addTextChangedListener(this);
			mEditItemTitleCount.setText(((Music) mItem).getTitleCount());
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
		if (mEditItemCountry.getText().hashCode() == s.hashCode()) {
			mItem.setCountry(s.toString());
		}
		if (mEditItemYear.getText().hashCode() == s.hashCode()) {
			mItem.setYear(s.toString());
		}
		if (mEditItemContent.getText().hashCode() == s.hashCode()) {
			mItem.setContent(s.toString());
		}
		if (mItem instanceof Book) {
			if (mEditItemAuthor.getText().hashCode() == s.hashCode()) {
				((Book) mItem).setAuthor(s.toString());
			}
			if (mEditItemPublisher.getText().hashCode() == s.hashCode()) {
				((Book) mItem).setPublisher(s.toString());
			}
			if (mEditItemEdition.getText().hashCode() == s.hashCode()) {
				((Book) mItem).setEdition(s.toString());
			}
			if (mEditItemIsbn.getText().hashCode() == s.hashCode()) {
				((Book) mItem).setIsbn(s.toString());
			}
		}
		if (mItem instanceof Movie) {
			if (mEditItemDirector.getText().hashCode() == s.hashCode()) {
				((Movie) mItem).setDirector(s.toString());
			}
			if (mEditItemCast.getText().hashCode() == s.hashCode()) {
				((Movie) mItem).setCast(s.toString());
			}
			if (mEditItemMusic.getText().hashCode() == s.hashCode()) {
				((Movie) mItem).setMusic(s.toString());
			}
			if (mEditItemLength.getText().hashCode() == s.hashCode()) {
				((Movie) mItem).setLength(s.toString());
			}
		}
		if (mItem instanceof Music) {
			if (mEditItemArtist.getText().hashCode() == s.hashCode()) {
				((Music) mItem).setArtist(s.toString());
			}
			if (mEditItemLabel.getText().hashCode() == s.hashCode()) {
				((Music) mItem).setLabel(s.toString());
			}
			if (mEditItemFormat.getText().hashCode() == s.hashCode()) {
				((Music) mItem).setFormat(s.toString());
			}
			if (mEditItemTitleCount.getText().hashCode() == s.hashCode()) {
				((Music) mItem).setTitleCount(s.toString());
			}
		}

	}

	@Override
	public void afterTextChanged(Editable s) {
		ItemStock.get(getActivity(), mUser).updateItem(mItem);
	}

}