package de.canberkdemirkan.mediaboxmngr.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;

public class RemoteDbVersionProvider {

	private int mVersion;

	public RemoteDbVersionProvider() {
		getRemoteDbVersion();
	}

	private void getRemoteDbVersion() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(Constants.RETURN_DB_VERSION_REQUEST,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"ItemListFragment - syncWithRemoteDb() - onSuccess()\n"
											+ response);
						}
						try {
							JSONObject obj = new JSONObject(response);
							JSONArray jsonArray = obj.getJSONArray("version");
							//JSONArray jsonArray = new JSONArray(response);
							Log.d(Constants.LOG_TAG,
									"ItemListFragment - getRemoteDbVersion() - onSuccess(): \n"
											+ jsonArray.length());
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObject = (JSONObject) jsonArray
										.get(i);
								String jsonVersion = jsonObject
										.getString("version");
								setVersion(Integer.valueOf(jsonVersion)
										.intValue());
							}
						} catch (JSONException e) {
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
					}
				});
	}

	public int getVersion() {
		return mVersion;
	}

	public void setVersion(int version) {
		this.mVersion = version;
	}

}