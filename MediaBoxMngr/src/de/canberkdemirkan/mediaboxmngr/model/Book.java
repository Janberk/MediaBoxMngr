package de.canberkdemirkan.mediaboxmngr.model;

import org.json.JSONException;
import org.json.JSONObject;

import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class Book extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3888624184736706992L;

	private String mAuthor;
	private String mPublisher;
	private String mEdition;
	private String mIsbn;

	// constructors
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
	public String getAuthor() {
		return mAuthor;
	}

	public void setAuthor(String author) {
		mAuthor = author;
	}

	public String getPublisher() {
		return mPublisher;
	}

	public void setPublisher(String publisher) {
		mPublisher = publisher;
	}

	public String getEdition() {
		return mEdition;
	}

	public void setEdition(String edition) {
		mEdition = edition;
	}

	public String getIsbn() {
		return mIsbn;
	}

	public void setIsbn(String isbn) {
		mIsbn = isbn;
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
			return Float.compare(thisRating, anotherRating);
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
		sb.append("Author: " + getAuthor() + "\n");
		sb.append("Publisher: " + getPublisher() + "\n");
		sb.append("Edition: " + getEdition() + "\n");
		sb.append("ISBN: " + getIsbn() + "\n");
		sb.append("Country: " + getCountry() + "\n");
		sb.append("Year: " + getYear() + "\n");
		sb.append("Content: " + getContent() + "\n");
		sb.append("Favorite?: " + isFavorite() + "\n");
		sb.append("Rating: " + getRating() + "\n");

		return sb.toString();
	}

}