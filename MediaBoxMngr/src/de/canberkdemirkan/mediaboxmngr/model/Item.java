package de.canberkdemirkan.mediaboxmngr.model;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import de.canberkdemirkan.mediaboxmngr.util.ItemType;

public abstract class Item {

	protected static final String JSON_UID = "uid";
	protected static final String JSON_ID = "id";
	protected static final String JSON_COVER = "cover";
	protected static final String JSON_CREATION_DATE = "creation_date";
	protected static final String JSON_USER = "user";
	protected static final String JSON_DELETED = "deleted";
	protected static final String JSON_DELETION_DATE = "deletion_date";
	protected static final String JSON_IN_POSSESSION = "in_possession";
	protected static final String JSON_FAVORITE = "favorite";

	protected static final String JSON_TITLE = "title";
	protected static final String JSON_ORIGINAL_TITLE = "original_title";
	protected static final String JSON_COUNTRY = "country";
	protected static final String JSON_TYPE = "type";
	protected static final String JSON_GENRE = "genre";
	protected static final String JSON_YEAR_PUBLISHED = "year_published";
	protected static final String JSON_CONTENT = "content";
	protected static final String JSON_RATING = "rating";

	private UUID mUniqueId;
	private long mId = -1;
	private Bitmap mCover;// = CoverUtil.createDefaultCover();
	private Date mCreationDate;
	private String mUser;
	private boolean mDeleted;
	private Date mDeletionDate;
	private boolean mInPossession;
	private boolean mFavorite;

	private String mTitle;
	private String mOriginalTitle;
	private String mCountry;
	private ItemType mType;
	private String mGenre;
	private String mYearPublished;
	private String mContent;
	private String mRating;

	// constructors
	public Item() {
		mUniqueId = UUID.randomUUID();
		mCreationDate = new Date();
	}

	public Item(long id, String user) {
		mUniqueId = UUID.randomUUID();
		setId(id);
		setUser(user);
		mCreationDate = new Date();
	}

	public Item(JSONObject json) throws JSONException {
		if (json.has(JSON_UID)) {
			mUniqueId = UUID.fromString(json.getString(JSON_UID));
		}
		if (json.has(JSON_ID)) {
			mId = json.getLong(JSON_ID);
		}
		// TODO überprüfen
		if (json.has(JSON_CREATION_DATE)) {
			mCreationDate = new Date(json.getLong(JSON_CREATION_DATE));
		}
//		if (json.has(JSON_COVER)) {
//			mCover = json.getBitmap(JSON_COVER);
//		}
		if (json.has(JSON_USER)) {
			mUser = json.getString(JSON_USER);
		}
		if (json.has(JSON_DELETED)) {
			mDeleted = json.getBoolean(JSON_DELETED);
		}
		// TODO überprüfen
		if (json.has(JSON_DELETION_DATE)) {
			mDeletionDate = new Date(json.getLong(JSON_DELETION_DATE));
		}
		if (json.has(JSON_IN_POSSESSION)) {
			mInPossession = json.getBoolean(JSON_IN_POSSESSION);
		}
		if (json.has(JSON_FAVORITE)) {
			mFavorite = json.getBoolean(JSON_FAVORITE);
		}
		if (json.has(JSON_TITLE)) {
			mTitle = json.getString(JSON_TITLE);
		}
		if (json.has(JSON_ORIGINAL_TITLE)) {
			mOriginalTitle = json.getString(JSON_ORIGINAL_TITLE);
		}
		if (json.has(JSON_COUNTRY)) {
			mCountry = json.getString(JSON_COUNTRY);
		}
		if (json.has(JSON_TYPE)) {
			mType = ItemType.valueOf(json.getString(JSON_TYPE));
		}
		if (json.has(JSON_GENRE)) {
			mGenre = json.getString(JSON_GENRE);
		}
		if (json.has(JSON_YEAR_PUBLISHED)) {
			mYearPublished = json.getString(JSON_YEAR_PUBLISHED);
		}
		if (json.has(JSON_CONTENT)) {
			mContent = json.getString(JSON_CONTENT);
		}
		if (json.has(JSON_RATING)) {
			mRating = json.getString(JSON_RATING);
		}
	}

	// getters and setters
	public long getId() {
		return mId;
	}

	public void setId(long id) {
		mId = id;
	}

	public Bitmap getCover() {
		return mCover;
	}

	public void setCover(Bitmap cover) {
		mCover = cover;
	}

	public Date getCreationDate() {
		return mCreationDate;
	}

	public void setCreationDate(Date creationDate) {
		mCreationDate = creationDate;
	}

	public String getUser() {
		return mUser;
	}

	public void setUser(String user) {
		mUser = user;
	}

	public boolean isDeleted() {
		return mDeleted;
	}

	public void setDeleted(boolean deleted) {
		mDeleted = deleted;
	}

	public Date getDeletionDate() {
		return mDeletionDate;
	}

	public void setDeletionDate(Date deletionDate) {
		mDeletionDate = deletionDate;
	}

	public boolean isInPossession() {
		return mInPossession;
	}

	public void setInPossession(boolean inPossession) {
		mInPossession = inPossession;
	}

	public boolean isFavorite() {
		return mFavorite;
	}

	public void setFavorite(boolean favorite) {
		mFavorite = favorite;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getOriginalTitle() {
		return mOriginalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		mOriginalTitle = originalTitle;
	}

	public String getCountry() {
		return mCountry;
	}

	public void setCountry(String country) {
		mCountry = country;
	}

	public ItemType getType() {
		return mType;
	}

	public void setType(ItemType type) {
		mType = type;
	}

	public String getGenre() {
		return mGenre;
	}

	public void setGenre(String genre) {
		mGenre = genre;
	}

	public String getYearPublished() {
		return mYearPublished;
	}

	public void setYearPublished(String yearPublished) {
		mYearPublished = yearPublished;
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

	public UUID getUniqueId() {
		return mUniqueId;
	}

	public abstract JSONObject toJSON() throws JSONException;

	@Override
	public abstract String toString();

	// @Override
	// public String toString() {
	// StringBuffer sb = new StringBuffer();
	//
	// sb.append("ID: " + getId() + "\n");
	// sb.append("UID: " + getUniqueId() + "\n");
	// sb.append("Creation date: "
	// + UtilMethods.dateToFormattedStringConverter(getCreationDate())
	// + "\n");
	// sb.append("Title: " + getTitle() + "\n");
	// sb.append("Favorite?: " + isFavorite() + "\n");
	//
	// return sb.toString();
	// }

}