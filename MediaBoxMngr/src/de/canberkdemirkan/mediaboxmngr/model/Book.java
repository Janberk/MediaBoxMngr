package de.canberkdemirkan.mediaboxmngr.model;

import org.json.JSONException;
import org.json.JSONObject;

import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class Book extends Item {

	private String mEdition;
	private String mPublishingHouse;
	private String mAuthor;
	private String mIsbn;

	public Book() {
		super();
	}

	public Book(long id, String user) {
		super(id, user);
	}

	public Book(JSONObject json) throws JSONException {
		super(json);
	}

	// getters and setters
	public String getEdition() {
		return mEdition;
	}

	public void setEdition(String edition) {
		mEdition = edition;
	}

	public String getPublishingHouse() {
		return mPublishingHouse;
	}

	public void setPublishingHouse(String publishingHouse) {
		mPublishingHouse = publishingHouse;
	}

	public String getAuthor() {
		return mAuthor;
	}

	public void setAuthor(String author) {
		mAuthor = author;
	}

	public String getIsbn() {
		return mIsbn;
	}

	public void setIsbn(String isbn) {
		mIsbn = isbn;
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
		if (getDeletionDate() == null) {
			sb.append("Deletion date:\n");
		} else {
			sb.append("Deletion date: "
					+ UtilMethods
							.dateToFormattedStringConverter(getDeletionDate())
					+ "\n");
		}
		sb.append("Created by: " + getUser() + "\n");
		sb.append("Title: " + getTitle() + "\n");
		sb.append("Original title: " + getOriginalTitle() + "\n");
		sb.append("Type: " + getType() + "\n");
		sb.append("Genre: " + getGenre() + "\n");
		sb.append("Edition: " + getEdition() + "\n");
		sb.append("Publishing house: " + getPublishingHouse() + "\n");
		sb.append("Country: " + getCountry() + "\n");
		sb.append("Year published: " + getYearPublished() + "\n");
		sb.append("Author: " + getAuthor() + "\n");
		sb.append("ISBN: " + getIsbn() + "\n");
		sb.append("Content: " + getContent() + "\n");
		sb.append("Favorite?: " + isFavorite() + "\n");
		sb.append("Rating: " + getRating() + "\n");
		sb.append("In Possession?: " + isInPossession() + "\n");
		sb.append("Deleted?: " + isDeleted() + "\n");

		return sb.toString();
	}

}