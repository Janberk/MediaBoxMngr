package de.canberkdemirkan.mediaboxmngr.model;

import java.util.Date;
import java.util.UUID;

import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class User {

	private UUID mUniqueId;
	private long mId = -1;
	private Date mCreationDate;

	private String mFirstname;
	private String mLastname;
	private String mUsername;
	private String mEmail;
	private String mPassword;

	// constructors
	public User() {
		mUniqueId = UUID.randomUUID();
		setCreationDate(new Date());
	}

	public User(long id) {
		setId(id);
		mUniqueId = UUID.randomUUID();
		setCreationDate(new Date());
	}

	// getters and setters
	public UUID getUniqueId() {
		return mUniqueId;
	}

	public long getId() {
		return mId;
	}

	public void setId(long id) {
		this.mId = id;
	}

	public Date getCreationDate() {
		return mCreationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.mCreationDate = creationDate;
	}

	public String getFirstname() {
		return mFirstname;
	}

	public void setFirstname(String firstname) {
		this.mFirstname = firstname;
	}

	public String getLastname() {
		return mLastname;
	}

	public void setLastname(String lastname) {
		this.mLastname = lastname;
	}

	public String getUsername() {
		return mUsername;
	}

	public void setUsername(String username) {
		this.mUsername = username;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		this.mEmail = email;
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String password) {
		this.mPassword = password;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("ID: " + getId() + "\n");
		sb.append("UID: " + getUniqueId() + "\n");
		sb.append("Creation date: "
				+ UtilMethods.dateToFormattedStringConverter(getCreationDate())
				+ "\n");
		sb.append("First name: " + getFirstname() + "\n");
		sb.append("Last name: " + getLastname() + "\n");
		sb.append("User name: " + getUsername() + "\n");
		sb.append("E-mail address: " + getEmail() + "\n");
		sb.append("Password: " + getPassword() + "\n");

		return sb.toString();
	}

}