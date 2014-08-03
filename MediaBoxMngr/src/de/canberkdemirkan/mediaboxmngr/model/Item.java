package de.canberkdemirkan.mediaboxmngr.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import de.canberkdemirkan.mediaboxmngr.content.ItemType;
import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public abstract class Item implements Serializable, Comparable<Item> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1430431442705653406L;

	protected static final String JSON_UID = "uid";
	protected static final String JSON_ID = "id";
	protected static final String JSON_TYPE = "type";
	protected static final String JSON_COVER = "cover";
	protected static final String JSON_SYNCED = "synced";
	protected static final String JSON_USER = "user";
	protected static final String JSON_FAVORITE = "favorite";
	protected static final String JSON_CREATION_DATE = "creation_date";

	protected static final String JSON_TITLE = "title";
	protected static final String JSON_GENRE = "genre";
	protected static final String JSON_COUNTRY = "country";
	protected static final String JSON_YEAR = "year";
	protected static final String JSON_CONTENT = "content";
	protected static final String JSON_RATING = "rating";

	private UUID mUniqueId;
	private long mId = -1;
	private ItemType mType;
	private Bitmap mCover;// = CoverUtil.createDefaultCover();
	private boolean mSynced;
	private String mUser;
	private boolean mFavorite;
	private Date mCreationDate;

	private String mTitle;
	private String mGenre;
	private String mCountry;
	private String mYear;
	private String mContent;
	private String mRating;

	// constructors
	public Item() {
		mUniqueId = UUID.randomUUID();
		setCreationDate(new Date());
		setSynced(false);
	}

	public Item(long id, String user) {
		mUniqueId = UUID.randomUUID();
		setCreationDate(new Date());
		setSynced(false);
		setId(id);
		setUser(user);
	}

	public Item(JSONObject json) throws JSONException {
		if (json.has(JSON_UID)) {
			mUniqueId = UUID.fromString(json.getString(JSON_UID));
		}
		if (json.has(JSON_ID)) {
			mId = json.getLong(JSON_ID);
		}
		if (json.has(JSON_TYPE)) {
			mType = ItemType.valueOf(json.getString(JSON_TYPE));
		}
		// if (json.has(JSON_COVER)) {
		// mCover = json.getBitmap(JSON_COVER);
		// }
		// TODO überprüfen
		if (json.has(JSON_SYNCED)) {
			mSynced = UtilMethods.isTrue(json.getInt(JSON_ID));
		}
		if (json.has(JSON_USER)) {
			mUser = json.getString(JSON_USER);
		}
		if (json.has(JSON_FAVORITE)) {
			mFavorite = json.getBoolean(JSON_FAVORITE);
		}
		if (json.has(JSON_CREATION_DATE)) {
			mCreationDate = new Date(json.getLong(JSON_CREATION_DATE));
		}
		if (json.has(JSON_TITLE)) {
			mTitle = json.getString(JSON_TITLE);
		}
		if (json.has(JSON_GENRE)) {
			mGenre = json.getString(JSON_GENRE);
		}
		if (json.has(JSON_COUNTRY)) {
			mCountry = json.getString(JSON_COUNTRY);
		}
		if (json.has(JSON_YEAR)) {
			mYear = json.getString(JSON_YEAR);
		}
		if (json.has(JSON_CONTENT)) {
			mContent = json.getString(JSON_CONTENT);
		}
		if (json.has(JSON_RATING)) {
			mRating = json.getString(JSON_RATING);
		}
	}

	// getters and setters
	public UUID getUniqueId() {
		return mUniqueId;
	}

	public long getId() {
		return mId;
	}

	public void setId(long id) {
		mId = id;
	}

	public ItemType getType() {
		return mType;
	}

	public void setType(ItemType type) {
		mType = type;
	}

	public Bitmap getCover() {
		return mCover;
	}

	public void setCover(Bitmap cover) {
		mCover = cover;
	}

	public boolean isSynced() {
		return mSynced;
	}

	public void setSynced(boolean synced) {
		mSynced = synced;
	}

	public String getUser() {
		return mUser;
	}

	public void setUser(String user) {
		mUser = user;
	}

	public boolean isFavorite() {
		return mFavorite;
	}

	public void setFavorite(boolean favorite) {
		mFavorite = favorite;
	}

	public Date getCreationDate() {
		return mCreationDate;
	}

	public void setCreationDate(Date creationDate) {
		mCreationDate = creationDate;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getGenre() {
		return mGenre;
	}

	public void setGenre(String genre) {
		mGenre = genre;
	}

	public String getCountry() {
		return mCountry;
	}

	public void setCountry(String country) {
		mCountry = country;
	}

	public String getYear() {
		return mYear;
	}

	public void setYear(String year) {
		mYear = year;
	}

	public String getContent() {
		return mContent;
	}

	public void setContent(String content) {
		mContent = content;
	}

	public String getRating() {
		return mRating;
	}

	public void setRating(String rating) {
		mRating = rating;
	}

	// abstract methods
	public abstract JSONObject toJSON() throws JSONException;

	@Override
	public abstract String toString();

}