package de.canberkdemirkan.mediaboxmngr.interfaces;

import de.canberkdemirkan.mediaboxmngr.content.ItemType;

public interface Constants {
	
	// Emulator
	// String LOGIN_URL = "http://10.0.2.2:80/development/mediaboxmngr_backend/users/log_in.php";

	// PC
	String LOGIN_URL = "http://192.168.1.52:80/development/mediaboxmngr_backend/users/log_in.php";

	// Notebook
	// String LOGIN_URL = "http://192.168.0.13:8080/development/mediaboxmngr_backend/users/log_in.php";
	
	// request URLs for MySQL Database
	String BUILD_JSON_REQUEST = "http://10.0.2.2:80/development/mediaboxmngr_backend/items/build_json.php";
	String INSERT_ITEMS_REQUEST = "http://10.0.2.2:80/development/mediaboxmngr_backend/items/insert_items.php";
	String DELETE_ACCOUNT_REQUEST = "http://10.0.2.2:80/development/mediaboxmngr_backend/users/delete_account.php";
	String RETURN_DB_VERSION_REQUEST = "http://10.0.2.2:80/development/mediaboxmngr_backend/items/return_db_version.php";
	
	// log tag used for debugging purposes
	String LOG_TAG = "de.canberkdemirkan.mediaboxmngr";
	
	// result/request codes
	int REQUEST_CODE = 001;
	int REQUEST_LIST_DELETE = 002;
	
	// spinner indices
	int ALBUM = 0;
	int BOOK = 1;
	int MOVIE = 2;	
	
	// intent extra keys/tags
	String KEY_MY_PREFERENCES = "de.canberkdemirkan.mediaboxmngr.app_prefs";
	String KEY_ITEM = "de.canberkdemirkan.mediaboxmngr.item";
	String KEY_ITEM_UID = "de.canberkdemirkan.mediaboxmngr.item_uid";
	String KEY_ITEM_ID = "de.canberkdemirkan.mediaboxmngr.item_id";
	String KEY_TYPE = "de.canberkdemirkan.mediaboxmngr.item_type";
	String KEY_ITEM_CREATION_DATE = "de.canberkdemirkan.mediaboxmngr.item_creation_date";
	String KEY_CREATE_NEW_ITEM = "de.canberkdemirkan.mediaboxmngr.new_item";
	String KEY_USER_TAG = "de.canberkdemirkan.mediaboxmngr.user_tag";
	String KEY_LIST_TAG = "de.canberkdemirkan.mediaboxmngr.list_tag";
	String KEY_DIALOG_TAG = "de.canberkdemirkan.mediaboxmngr.dialog_tag";
	String KEY_DIALOG_ITEM_LIST = "de.canberkdemirkan.mediaboxmngr.dialog_item_list";
	String KEY_DIALOG_ITEM = "de.canberkdemirkan.mediaboxmngr.dialog_item";
	String KEY_DIALOG_TITLE = "de.canberkdemirkan.mediaboxmngr.dialog_title";
	String KEY_DIALOG_FRAGMENT = "de.canberkdemirkan.mediaboxmngr.dialog_fragment";
	String EXTRA_DIALOG_LIST = "de.canberkdemirkan.mediaboxmngr.extra_dialog_list";
	String EXTRA_DETAILS_ITEM = "de.canberkdemirkan.mediaboxmngr.extra_details_item";
	String EXTRA_DETAILS_CONTENT = "de.canberkdemirkan.mediaboxmngr.extra_details_content";
	String EXTRA_DETAILS_GENRE = "de.canberkdemirkan.mediaboxmngr.extra_details_genre";
	String EXTRA_DETAILS_ORIGINAL_TITLE = "de.canberkdemirkan.mediaboxmngr.extra_details_original_title";
	String EXTRA_DETAILS_COUNTRY = "de.canberkdemirkan.mediaboxmngr.extra_details_country";
	String EXTRA_DETAILS_YEAR = "de.canberkdemirkan.mediaboxmngr.extra_details_year";
	String KEY_ITEMLIST_CONTEXT = "de.canberkdemirkan.mediaboxmngr.itemlist_context";
	String KEY_ITEMLIST_USER = "de.canberkdemirkan.mediaboxmngr.itemlist_user";
	
	String TAG_ALBUM = ItemType.Album.name();
	String TAG_BOOK = ItemType.Book.name();
	String TAG_MOVIE = ItemType.Movie.name();

	// table names
	String TABLE_USERS = "users";
	String TABLE_ITEMS = "items";
	String TABLE_VERSION = "version";

	// item table column names
	// common
	String ID = "_id";
	String SQLITE_ID = "sqlite_id";
	String SYNCED = "synced";
	String USER = "created_by";
	String TYPE = "type";
	String TITLE = "title";
	String GENRE = "genre";
	String FAVORITE = "favorite";
	String CREATION_DATE = "creation_date";
	String COUNTRY = "country";
	String YEAR = "year";
	String CONTENT = "content";
	String RATING = "rating";
	String COVER = "cover";
	// MOVIE
	String DIRECTOR = "director";
	String CAST = "cast";
	String MUSIC = "music";
	String LENGTH = "length";
	// MUSIC
	String ARTIST = "artist";
	String LABEL = "label";
	String FORMAT = "format";
	String TITLE_COUNT = "title_count";
	// BOOK
	String AUTHOR = "author";
	String PUBLISHER = "publisher";
	String EDITION = "edition";
	String ISBN = "isbn";

	// user table column names
	String FIRST_NAME = "firstname";
	String LAST_NAME = "lastname";
	String EMAIL = "email";
	String PASSWORD = "password";
	
	// version table column name
	String VERSION = "version";
	
	// SQL statement items table
	String CREATE_TABLE_ITEMS = "CREATE TABLE "
			+ TABLE_ITEMS + "("
			+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ USER + " TEXT, "
			+ SYNCED + " INTEGER, "
			+ TYPE + " TEXT, "
			+ FAVORITE + " INTEGER, "
			+ CREATION_DATE + " TEXT, "
			+ TITLE + " TEXT, "
			+ GENRE + " TEXT, "
			+ COUNTRY + " TEXT, "
			+ YEAR + " TEXT, "
			+ CONTENT + " TEXT, "
			+ RATING + " TEXT, "
			+ COVER + " BLOB, "
			+ DIRECTOR + " TEXT, "
			+ CAST + " TEXT, "
			+ MUSIC + " TEXT, "
			+ LENGTH + " TEXT, "
			+ ARTIST + " TEXT, "
			+ LABEL + " TEXT, "
			+ FORMAT + " TEXT, "
			+ TITLE_COUNT + " TEXT, "
			+ AUTHOR + " TEXT, "
			+ PUBLISHER + " TEXT, "
			+ EDITION + " TEXT, "
			+ ISBN + " TEXT"
			+ ")";
	
	// SQL statement table users
	String CREATE_TABLE_USERS = "CREATE TABLE "
			+ TABLE_USERS + "("
			+ ID + " INTEGER PRIMARY KEY,"
			+ CREATION_DATE + " TEXT,"
			+ FIRST_NAME + " TEXT,"
			+ LAST_NAME + " TEXT,"
			+ EMAIL + " TEXT UNIQUE,"
			+ PASSWORD + " TEXT"
			+ ")";
	
	// SQL statement table version
	String CREATE_TABLE_VERSION = "CREATE TABLE "
			+ TABLE_VERSION + "("
			+ ID + " INTEGER PRIMARY KEY,"
			+ VERSION + " INTEGER"
			+ ")";
	
	String INSERT_VERSION_0 = "INSERT INTO " + TABLE_VERSION + " (" + VERSION + ") " + "VALUES (0);";

}