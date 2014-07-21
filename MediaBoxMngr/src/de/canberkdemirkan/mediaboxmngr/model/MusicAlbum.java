package de.canberkdemirkan.mediaboxmngr.model;

import org.json.JSONException;
import org.json.JSONObject;

import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class MusicAlbum extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2488645846887564126L;

	private String mLabel;
	private String mStudio;
	private String mProducer;
	private String mArtist;
	private String mFormat;
	private String mTitleCount;

	public MusicAlbum() {
		super();
	}

	public MusicAlbum(long id, String user) {
		super(id, user);
	}

	public MusicAlbum(JSONObject json) throws JSONException {
		super(json);
	}

	// getters and setters
	public String getLabel() {
		return mLabel;
	}

	public void setLabel(String label) {
		mLabel = label;
	}

	public String getStudio() {
		return mStudio;
	}

	public void setStudio(String studio) {
		mStudio = studio;
	}

	public String getProducer() {
		return mProducer;
	}

	public void setProducer(String producer) {
		mProducer = producer;
	}

	public String getArtist() {
		return mArtist;
	}

	public void setArtist(String artist) {
		mArtist = artist;
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

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_UID, getUniqueId().toString());
		json.put(JSON_ID, getId());
		json.put(JSON_CREATION_DATE, getCreationDate().getTime());
		json.put(JSON_TITLE, getTitle());
		json.put(JSON_FAVORITE, isFavorite());

		return json;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("ID: " + getId() + "\n");
		sb.append("Creation date: "
				+ UtilMethods.dateToFormattedStringConverter(getCreationDate())
				+ "\n");
		sb.append("Created by: " + getUser() + "\n");
		sb.append("Title: " + getTitle() + "\n");
		sb.append("Original title: " + getOriginalTitle() + "\n");
		sb.append("Artist: " + isDeleted() + "\n");
		sb.append("Country: " + getCountry() + "\n");
		sb.append("Year published: " + getYearPublished() + "\n");
		sb.append("Type: " + getType() + "\n");
		sb.append("Genre: " + getGenre() + "\n");
		sb.append("Label: " + isDeleted() + "\n");
		sb.append("Studio: " + isDeleted() + "\n");
		sb.append("Producer: " + getProducer() + "\n");
		sb.append("Format: " + isDeleted() + "\n");
		sb.append("Title count: " + isDeleted() + "\n");
		sb.append("Content: " + getContent() + "\n");
		sb.append("Favorite?: " + isFavorite() + "\n");
		sb.append("Rating: " + getRating() + "\n");
		sb.append("In Possession?: " + isInPossession() + "\n");
		sb.append("Deleted?: " + isDeleted() + "\n");
		if (getDeletionDate() == null) {
			sb.append("Deletion date:\n");
		} else {
			sb.append("Deletion date: "
					+ UtilMethods
					.dateToFormattedStringConverter(getDeletionDate())
					+ "\n");
		}

		return sb.toString();
	}

}