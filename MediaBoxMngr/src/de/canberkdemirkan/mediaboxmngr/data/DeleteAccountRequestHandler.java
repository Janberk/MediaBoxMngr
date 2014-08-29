package de.canberkdemirkan.mediaboxmngr.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.activities.LoginActivity;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.interfaces.UserAuthenticationConstants;

public class DeleteAccountRequestHandler {

	public static final int RESPONSE_CODE_SUCCESS = 0;
	public static final int RESPONSE_CODE_NO_EMAIL = 1;
	public static final int RESPONSE_CODE_FAIL = 2;
	public static final int RESPONSE_CODE_NO_POST = 3;

	private SharedPreferences mSharedPreferences;
	private Context mContext;
	private String mUser;

	public DeleteAccountRequestHandler(Context context) {
		mContext = context;
		mSharedPreferences = mContext.getSharedPreferences(
				Constants.KEY_MY_PREFERENCES, Context.MODE_PRIVATE);
		mUser = mSharedPreferences.getString(
				UserAuthenticationConstants.KEY_EMAIL, "");
	}

	public void deleteUserAccount() {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		params.put("email", mUser);
		client.post(Constants.DELETE_ACCOUNT_REQUEST, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"DeleteAccountRequestHandler - onSuccess()");
						}
						if (response != null) {
							int key = Integer.valueOf(response);
							checkHttpRequestResult(key);
						}
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String response) {
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"DeleteAccountRequestHandler - onFailure()");
						}
					}

				});
	}

	private void checkHttpRequestResult(int key) {
		String message = null;
		switch (key) {
		case RESPONSE_CODE_SUCCESS:
			if (BuildConfig.DEBUG) {
				Log.d(Constants.LOG_TAG,
						"DeleteAccountRequestHandler - checkHttpRequestResult(): "
								+ key + ": SUCCESS");
			}
			message = mContext.getResources()
					.getText(R.string.response_code_delete_success).toString();
			Toast.makeText(mContext.getApplicationContext(), message,
					Toast.LENGTH_LONG).show();
			clearUser();
			Intent intent = new Intent(mContext.getApplicationContext(),
					LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			mContext.startActivity(intent);
			break;
		case RESPONSE_CODE_NO_EMAIL:
			if (BuildConfig.DEBUG) {
				Log.d(Constants.LOG_TAG,
						"DeleteAccountRequestHandler - checkHttpRequestResult(): "
								+ key + ": NO EMAIL");
			}
			message = mContext.getResources()
					.getText(R.string.response_code_no_email).toString();
			Toast.makeText(mContext.getApplicationContext(), message,
					Toast.LENGTH_LONG).show();
			break;
		case RESPONSE_CODE_FAIL:
			if (BuildConfig.DEBUG) {
				Log.d(Constants.LOG_TAG,
						"DeleteAccountRequestHandler - checkHttpRequestResult(): "
								+ key + ": FAILED TO DELETE");
			}
			message = mContext.getResources()
					.getText(R.string.response_code_fail).toString();
			Toast.makeText(mContext.getApplicationContext(), message,
					Toast.LENGTH_LONG).show();
			break;
		case RESPONSE_CODE_NO_POST:
			if (BuildConfig.DEBUG) {
				Log.d(Constants.LOG_TAG,
						"DeleteAccountRequestHandler - checkHttpRequestResult(): "
								+ key + ": NO POST");
			}
			message = mContext.getResources()
					.getText(R.string.response_code_no_post).toString();
			Toast.makeText(mContext.getApplicationContext(), message,
					Toast.LENGTH_LONG).show();
			break;
		default:
			break;

		}
	}

	private void clearUser() {
		if (mSharedPreferences.contains(UserAuthenticationConstants.KEY_EMAIL)) {
			Editor editor = mSharedPreferences.edit();
			editor.remove(UserAuthenticationConstants.KEY_EMAIL);
			editor.commit();
		}
		if (mSharedPreferences
				.contains(UserAuthenticationConstants.KEY_REMEMBER_EMAIL)) {
			Editor editor = mSharedPreferences.edit();
			editor.remove(UserAuthenticationConstants.KEY_REMEMBER_EMAIL);
			editor.commit();
		}
	}

}