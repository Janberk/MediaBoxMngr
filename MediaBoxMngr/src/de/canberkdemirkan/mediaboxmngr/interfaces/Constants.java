package de.canberkdemirkan.mediaboxmngr.interfaces;

import de.canberkdemirkan.mediaboxmngr.content.ItemType;

public interface Constants {	
	
	// request URLs for MySQL Database
	String BUILD_JSON_REQUEST = "http://10.0.2.2:80/development/mediaboxmngr_backend/items/build_json.php";
	String INSERT_ITEMS_REQUEST = "http://10.0.2.2:80/development/mediaboxmngr_backend/items/insert_items.php";
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
	String KEY_ITEM_ID = "de.canberkdemirkan.mediaboxmngr.item_id";
	String KEY_TYPE = "de.canberkdemirkan.mediaboxmngr.item_type";
	String KEY_ITEM_CREATION_DATE = "de.canberkdemirkan.mediaboxmngr.item_creation_date";
	String KEY_CREATE_NEW_ITEM = "de.canberkdemirkan.mediaboxmngr.new_item";
	String KEY_USER_TAG = "de.canberkdemirkan.mediaboxmngr.user_tag";
	String KEY_LIST_TAG = "de.canberkdemirkan.mediaboxmngr.list_tag";
	String KEY_DIALOG_POSITION = "de.canberkdemirkan.mediaboxmngr.dialog_position";
	String KEY_DIALOG_TAG = "de.canberkdemirkan.mediaboxmngr.dialog_tag";
	String KEY_DIALOG_ITEM_LIST = "de.canberkdemirkan.mediaboxmngr.dialog_item_list";
	String KEY_DIALOG_TITLE = "de.canberkdemirkan.mediaboxmngr.dialog_title";
	String KEY_DIALOG_FRAGMENT = "de.canberkdemirkan.mediaboxmngr.dialog_fragment";
	String EXTRA_DIALOG_LIST = "de.canberkdemirkan.mediaboxmngr.extra_dialog_list";
	
	//String TAG_ALL = ItemType.All.name();
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
	String TITLE = "title";
	String USER = "created_by";
	String TYPE = "type";
	String COVER = "cover";
	String GENRE = "genre";
	String FAVORITE = "favorite";
	String REMOVABLE = "removable";
	String CREATION_DATE = "creation_date"; // also for user
	String DELETED = "deleted"; // also for user
	String DELETION_DATE = "deletion_date"; // also for user
	String IN_POSSESSION = "in_possession";
	String ORIGINAL_TITLE = "original_title";
	String COUNTRY = "country";
	String YEAR_PUBLISHED = "year_published";
	String CONTENT = "content";
	String RATING = "rating";
	// MOVIES
	String PRODUCER = "producer"; // also for ALBUMS
	String DIRECTOR = "director";
	String SCRIPT = "script";
	String ACTORS = "actors";
	String MUSIC = "music";
	String LENGTH = "length";
	// ALBUMS
	String LABEL = "label";
	String STUDIO = "studio";
	String ARTIST = "artist";
	String FORMAT = "format";
	String TITLE_COUNT = "title_count";
	// books
	String EDITION = "edition";
	String PUBLISHING_HOUSE = "publishing_house";
	String AUTHOR = "author";
	String ISBN = "isbn";

	// user table column names
	String FIRST_NAME = "firstname";
	String LAST_NAME = "lastname";
	String USER_NAME = "username";
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
			+ TITLE + " TEXT, "
			+ TYPE + " TEXT, "
			+ COVER + " BLOB, "
			+ GENRE + " TEXT, "
			+ FAVORITE + " INTEGER, "
			+ REMOVABLE + " INTEGER, "
			+ CREATION_DATE + " TEXT, "
			+ DELETED + " INTEGER, "
			+ DELETION_DATE + " TEXT, "
			+ IN_POSSESSION + " INTEGER, "
			+ ORIGINAL_TITLE + " TEXT, "
			+ COUNTRY + " TEXT, "
			+ YEAR_PUBLISHED + " TEXT, "
			+ CONTENT + " TEXT, "
			+ RATING + " TEXT, "
			+ PRODUCER + " TEXT, "
			+ DIRECTOR + " TEXT, "
			+ SCRIPT + " TEXT, "
			+ ACTORS + " TEXT, "
			+ MUSIC + " TEXT, "
			+ LENGTH + " TEXT, "
			+ LABEL + " TEXT, "
			+ STUDIO + " TEXT, "
			+ ARTIST + " TEXT, "
			+ FORMAT + " TEXT, "
			+ TITLE_COUNT + " TEXT, "
			+ EDITION + " TEXT, "
			+ PUBLISHING_HOUSE + " TEXT, "
			+ AUTHOR + " TEXT, "
			+ ISBN + " TEXT"
			+ ")";
	
	// SQL statement table users
	String CREATE_TABLE_USERS = "CREATE TABLE "
			+ TABLE_USERS + "("
			+ ID + " INTEGER PRIMARY KEY,"
			+ CREATION_DATE + " TEXT,"
			+ DELETED + " INTEGER,"
			+ DELETION_DATE + " TEXT,"
			+ FIRST_NAME + " TEXT,"
			+ LAST_NAME + " TEXT,"
			+ USER_NAME + " TEXT,"
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