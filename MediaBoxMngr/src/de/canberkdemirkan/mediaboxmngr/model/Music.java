package de.canberkdemirkan.mediaboxmngr.model;

import org.json.JSONException;
import org.json.JSONObject;

import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class Music extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2488645846887564126L;

	private String mArtist;
	private String mLabel;
	private String mFormat;
	private String mTitleCount;

	// constructors
	public Music() {
		super();
	}

	public Music(long id, String user) {
		super(id, user);
	}

	public Music(JSONObject json) throws JSONException {
		super(json);
	}

	// getters and setters
	public String getArtist() {
		return mArtist;
	}

	public void setArtist(String artist) {
		mArtist = artist;
	}

	public String getLabel() {
		return mLabel;
	}

	public void setLabel(String label) {
		mLabel = label;
	}

	public String getFormat() {
		return mFormat;
	}

	public void setFormat(String format) {
		mFormat = format;
	}

	public String getTitleCount() {
		return mTitleCount;
	}

	public void setTitleCount(String titleCount) {
		mTitleCount = titleCount;
	}

	// overwritten methods
	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_UID, getUniqueId().toString());
		json.put(JSON_ID, getId());
		json.put(JSON_TITLE, getTitle());
		json.put(JSON_FAVORITE, isFavorite());
		json.put(JSON_CREATION_DATE, getCreationDate().getTime());

		return json;
	}

	@Override
	public int compareTo(Item another) {
		try {
			String thisRatingString = getRating();
			String anotherRatingString = another.getRating();
			Float thisRating = 0.0F;
			Float anotherRating = 0.0F;
			if (thisRatingString != null) {
				thisRating = Float.valueOf(thisRatingString);
			}
			if (anotherRatingString != null) {
				anotherRating = Float.valueOf(anotherRatingString);
			}

			if (thisRating < anotherRating) {
				return -1;
			} else if (thisRating > anotherRating) {
				return 1;
			} else if (thisRating == anotherRating) {
				return 0;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("ID: " + getId() + "\n");
		sb.append("UID: " + getUniqueId() + "\n");
		sb.append("Type: " + getType() + "\n");
		sb.append("Created by: " + getUser() + "\n");
		sb.append("Creation date: "
				+ UtilMethods.dateToFormattedStringConverter(getCreationDate())
				+ "\n");
		sb.append("Title: " + getTitle() + "\n");
		sb.append("Genre: " + getGenre() + "\n");
		sb.append("Artist: " + getArtist() + "\n");
		sb.append("Label: " + getLabel() + "\n");
		sb.append("Format: " + getFormat() + "\n");
		sb.append("Title count: " + getTitleCount() + "\n");
		sb.append("Country: " + getCountry() + "\n");
		sb.append("Year: " + getYear() + "\n");
		sb.append("Content: " + getContent() + "\n");
		sb.append("Favorite?: " + isFavorite() + "\n");
		sb.append("Rating: " + getRating() + "\n");

		return sb.toString();
	}

}