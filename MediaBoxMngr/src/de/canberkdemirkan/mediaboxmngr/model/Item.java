package de.canberkdemirkan.mediaboxmngr.model;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class Item {

	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_CONTENT = "content";
	private static final String JSON_CREATION_DATE = "creation_date";
	private static final String JSON_FAVORITE = "favorite";

	private UUID mUniqueId;
	private long mId = -1;
	private String mTitle;
	private String mContent;
	private Date mCreationDate;
	private boolean mFavorite;

	// constructors
	public Item() {
		mUniqueId = UUID.randomUUID();
		mCreationDate = new Date();
	}

	public Item(long id) {
		setId(id);
		mUniqueId = UUID.randomUUID();
		mCreationDate = new Date();
	}

	public Item(JSONObject json) throws JSONException {
		mUniqueId = UUID.fromString(json.getString(JSON_ID));
		if (json.has(JSON_TITLE)) {
			mTitle = json.getString(JSON_TITLE);
		}
		if (json.has(JSON_CONTENT)) {
			mContent = json.getString(JSON_CONTENT);
		}
		mCreationDate = new Date(json.getLong(JSON_CREATION_DATE));
		mFavorite = json.getBoolean(JSON_FAVORITE);
	}

	// getters and setters
	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getContent() {
		return mContent;
	}

	public void setContent(String content) {
		this.mContent = content;
	}

	public Date getCreationDate() {
		return mCreationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.mCreationDate = creationDate;
	}

	public boolean isFavorite() {
		return mFavorite;
	}

	public void setFavorite(boolean favorite) {
		this.mFavorite = favorite;
	}

	public UUID getUniqueId() {
		return mUniqueId;
	}

	public long getId() {
		return mId;
	}

	public void setId(long id) {
		this.mId = id;
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mUniqueId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_CREATION_DATE, mCreationDate.getTime());
		json.put(JSON_FAVORITE, mFavorite);

		return json;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("ID: " + getId() + "\n");
		sb.append("UID: " + getUniqueId() + "\n");
		sb.append("Creation date: "
				+ UtilMethods.dateToFormattedStringConverter(getCreationDate())
				+ "\n");
		sb.append("Title: " + getTitle() + "\n");
		sb.append("Favorite?: " + isFavorite() + "\n");

		return sb.toString();
	}

}