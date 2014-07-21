package de.canberkdemirkan.mediaboxmngr.model;

import org.json.JSONException;
import org.json.JSONObject;

import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class Movie extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1938544458752745082L;

	private String mProducer;
	private String mDirector;
	private String mScript;
	private String mActors;
	private String mMusic;
	private String mLength; // in minutes

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
	public String getProducer() {
		return mProducer;
	}

	public void setProducer(String producer) {
		mProducer = producer;
	}

	public String getDirector() {
		return mDirector;
	}

	public void setDirector(String director) {
		mDirector = director;
	}

	public String getScript() {
		return mScript;
	}

	public void setScript(String script) {
		mScript = script;
	}

	public String getActors() {
		return mActors;
	}

	public void setActors(String actors) {
		mActors = actors;
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
		sb.append("Country: " + getCountry() + "\n");
		sb.append("Year published: " + getYearPublished() + "\n");
		sb.append("Type: " + getType() + "\n");
		sb.append("Genre: " + getGenre() + "\n");
		sb.append("Producer: " + getProducer() + "\n");
		sb.append("Director: " + getDirector() + "\n");
		sb.append("Actors: " + getActors() + "\n");
		sb.append("Script: " + getScript() + "\n");
		sb.append("Music: " + getMusic() + "\n");
		sb.append("Length: " + getLength() + "\n");
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