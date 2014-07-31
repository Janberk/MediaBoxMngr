package de.canberkdemirkan.mediaboxmngr.model;

import org.json.JSONException;
import org.json.JSONObject;

import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class Movie extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1938544458752745082L;

	private String mDirector;
	private String mCast;
	private String mMusic;
	private String mLength; // in minutes
	
	// constructors
	public Movie() {
		super();
	}

	public Movie(long id, String user) {
		super(id, user);
	}

	public Movie(JSONObject json) throws JSONException {
		super(json);
	}

	// getters and setters
	public String getDirector() {
		return mDirector;
	}

	public void setDirector(String director) {
		mDirector = director;
	}

	public String getCast() {
		return mCast;
	}

	public void setCast(String cast) {
		mCast = cast;
	}

	public String getMusic() {
		return mMusic;
	}

	public void setMusic(String music) {
		mMusic = music;
	}

	public String getLength() {
		return mLength;
	}

	public void setLength(String length) {
		mLength = length;
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
		sb.append("Director: " + getDirector() + "\n");
		sb.append("Cast: " + getCast() + "\n");
		sb.append("Music: " + getMusic() + "\n");
		sb.append("Length: " + getLength() + "\n");
		sb.append("Country: " + getCountry() + "\n");
		sb.append("Year: " + getYear() + "\n");
		sb.append("Content: " + getContent() + "\n");
		sb.append("Favorite?: " + isFavorite() + "\n");
		sb.append("Rating: " + getRating() + "\n");

		return sb.toString();
	}

}