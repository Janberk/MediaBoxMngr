package de.canberkdemirkan.mediaboxmngr.data;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.activities.ItemListActivity;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.interfaces.OnListAdapterRefreshedListener;
import de.canberkdemirkan.mediaboxmngr.model.Item;

public class DatabaseSynchronizer implements OnListAdapterRefreshedListener {

	private OnListAdapterRefreshedListener mListener;
	private ProgressDialog mProgressDialog;
	private Context mContext;
	private String mJson;
	private String mUser;

	public DatabaseSynchronizer(Context context, String user) {
		mContext = context;
		mUser = user;
		if (mContext instanceof ItemListActivity) {
			mListener = (OnListAdapterRefreshedListener) mContext;
		}
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog
				.setMessage("Synchronizing SQLite DB with Remote MySQL DB. Please wait...");
		mProgressDialog.setCancelable(false);
	}

	public void syncWithRemoteDb() throws JSONException, IOException {
		final ItemStock itemStock = ItemStock.get(mContext, mUser);
		mJson = itemStock.getSQLiteAsJSON(mUser);

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		params.put("items", mJson);
		client.post(Constants.INSERT_ITEMS_REQUEST, params,
		// client.post("http://10.0.2.2:80/development/mediaboxmngr_backend/items/insert_items2.php",
		// params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"DatabaseSynchronizer - syncWithRemoteDb() - onSuccess()\n"
											+ response);
						}
						try {
							JSONArray jsonArray = new JSONArray(response);
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObject = (JSONObject) jsonArray
										.get(i);

								String jsonId = jsonObject.getString("_id");
								String jsonSynced = jsonObject
										.getString("synced");

								long id = Long.valueOf(jsonId).longValue();
								int synced = Integer.valueOf(jsonSynced)
										.intValue();
								itemStock.getDAOItem().updateSyncStatus(id,
										synced);
							}
							Toast.makeText(mContext, "DB Sync completed!",
									Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
							Toast.makeText(mContext,
									"Error! JSON response might be invalid!",
									Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"ItemListFragment - syncWithRemoteDb() - onFailure()\n"
											+ content);
						}
						if (statusCode == 404) {
							Toast.makeText(mContext,
									"404 - Resource not found.",
									Toast.LENGTH_LONG).show();
						} else if (statusCode == 500) {
							Toast.makeText(mContext, "505 - Server Error.",
									Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(
									mContext,
									"Unexpected Error! Please check your network connection.",
									Toast.LENGTH_LONG).show();
						}
					}
				});
	}

	public ArrayList<Item> loadItemsFromRemoteDb() {
		ItemStock.get(mContext, mUser).getDAOItem().deleteAllItems(mUser);
		final JSONHandler handler = new JSONHandler();
		final ArrayList<Item> itemList = new ArrayList<Item>();
		RequestParams params = new RequestParams();
		params.put("user", mUser);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(Constants.BUILD_JSON_REQUEST, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						mProgressDialog.show();
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"ItemListFragment - loadItemsFromRemoteDb() - onStart()");
						}
					}

					@Override
					public void onSuccess(String response) {
						mProgressDialog.dismiss();
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"ItemListFragment - loadItemsFromRemoteDb() - onSuccess()\n"
											+ response);
						}
						try {
							ArrayList<Item> createdList = handler
									.loadItemsFromJSONArray(response);
							handler.setRemoteList(createdList);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						for (int i = handler.getRemoteList().size() - 1; i >= 0; i--) {
							Item item = handler.getRemoteList().get(i);
							ItemStock.get(mContext, mUser).getDAOItem()
									.insertItem(item);
							itemList.add(item);
						}
					}

					@Override
					public void onFinish() {
						mProgressDialog.dismiss();
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"ItemListFragment - loadItemsFromRemoteDb() - onFinish()");
						}
						onListAdapterRefreshed(itemList);
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						mProgressDialog.dismiss();
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"ItemListFragment - loadItemsFromRemoteDb() - onFailure()\n"
											+ content);
						}
					}

				});
		return itemList;
	}

	@Override
	public void onListAdapterRefreshed(ArrayList<Item> itemList) {
		mListener.onListAdapterRefreshed(itemList);
	}

	// private static int getRemoteDbVersion() {
	// RemoteDbVersionProvider provider = new RemoteDbVersionProvider();
	// return provider.getVersion();
	// }

}