package de.canberkdemirkan.mediaboxmngr.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.activities.ItemListActivity;
import de.canberkdemirkan.mediaboxmngr.activities.SignupActivity;
import de.canberkdemirkan.mediaboxmngr.data.ProjectConstants;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;

public class LoginFragment extends Fragment {

	public static final String LOGIN_URL = "http://10.0.2.2:80/development/mediaboxmngr_backend/users/log_in.php";
	public static final int DEFAULT_TIMEOUT = 20 * 1000;

	public static final String KEY_EMAIL = "emailKey";
	public static final String KEY_PASSWORD = "passwordKey";

	public static final int RESPONSE_CODE_SUCCESS = 0;
	public static final int RESPONSE_CODE_EMPTY_FIELDS = 1;
	public static final int RESPONSE_CODE_INVALID_DATA = 2;
	public static final int RESPONSE_CODE_INVALID_EMAIL = 3;
	public static final int RESPONSE_CODE_NO_POST = 4;

	private SharedPreferences mSharedPreferences;

	private EditText mEditEmail;
	private EditText mEditPassword;
	private Button mButtonLogin;
	private TextView mTextSignupLink;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginFragment - onCreate()");
		}
	}

	@Override
	public void onPause() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginFragment - onPause()");
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginFragment - onResume()");
		}
		mSharedPreferences = getActivity().getSharedPreferences(
				ProjectConstants.KEY_MY_PREFERENCES, Context.MODE_PRIVATE);
		if (mSharedPreferences.contains(KEY_EMAIL)) {
			if (mSharedPreferences.contains(KEY_PASSWORD)) {
				Intent intent = new Intent(getActivity(),
						ItemListActivity.class);
				startActivity(intent);
			}
		} else {
			this.onCreate(null);
			mEditEmail.setText("");
			mEditPassword.setText("");
		}
		super.onResume();
	}

	private void initElements(View view) {
		mEditEmail = (EditText) view.findViewById(R.id.et_fragmentLogin_email);
		mEditPassword = (EditText) view
				.findViewById(R.id.et_fragmentLogin_password);
		mButtonLogin = (Button) view.findViewById(R.id.btn_fragmentLogin_login);
		mTextSignupLink = (TextView) view
				.findViewById(R.id.tv_fragmentLogin_signup_link);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_login, null);
		initElements(view);

		mButtonLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String email = mEditEmail.getText().toString();
				String password = mEditPassword.getText().toString();

				RequestParams params = new RequestParams();
				params.put("email", email);
				params.put("password", password);

				AsyncHttpClient client = new AsyncHttpClient();
				client.setTimeout(DEFAULT_TIMEOUT);
				client.post(LOGIN_URL, params, new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"LoginFragment - onStart()");
						}
					}

					@Override
					public void onSuccess(String response) {
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"LoginFragment - onSuccess()\n" + response);
						}
						if (response != null && response.matches("-?\\d+")) {
							int key = Integer.valueOf(response);
							checkHttpRequestResult(key);
						}
					}

					@Override
					public void onFinish() {
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"LoginFragment - onFinish()");
						}
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"LoginFragment - onFailure(): " + content);
						}
					}

				});
			}
		});

		mTextSignupLink.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), SignupActivity.class);
				startActivity(intent);
			}
		});

		return view;
	}

	public boolean editTextIsEmpty(EditText edittext) {
		if (edittext.getText().toString().trim().length() < 1) {
			return true;
		} else {
			return false;
		}
	}

	// private boolean validateEmail(String email) {
	// String validEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	// + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	//
	// Pattern pattern = Pattern.compile(validEmail);
	// Matcher matcher = pattern.matcher(email);
	//
	// return matcher.matches();
	// }

	private void checkHttpRequestResult(int key) {
		switch (key) {
		case RESPONSE_CODE_SUCCESS:
			if (BuildConfig.DEBUG) {
				Log.d(Constants.LOG_TAG,
						"LoginFragment - checkHttpRequestResult(): " + key
								+ ": SUCCESS");
			}
			login();
			break;
		case RESPONSE_CODE_EMPTY_FIELDS:
			if (BuildConfig.DEBUG) {
				Log.d(Constants.LOG_TAG,
						"LoginFragment - checkHttpRequestResult(): " + key
								+ ": EMPTY FIELDS");
			}
			Toast.makeText(getActivity().getApplicationContext(),
					"Please fill in all required fields.", Toast.LENGTH_LONG)
					.show();
			break;
		case RESPONSE_CODE_INVALID_EMAIL:
			if (BuildConfig.DEBUG) {
				Log.d(Constants.LOG_TAG,
						"LoginFragment - checkHttpRequestResult(): " + key
								+ ": INVALID EMAIL");
			}
			Toast.makeText(getActivity().getApplicationContext(),
					"The E-mail address you entered is not valid.",
					Toast.LENGTH_LONG).show();
			break;
		case RESPONSE_CODE_INVALID_DATA:
			if (BuildConfig.DEBUG) {
				Log.d(Constants.LOG_TAG,
						"LoginFragment - checkHttpRequestResult(): " + key
								+ ": INVALID DATA");
			}
			Toast.makeText(getActivity().getApplicationContext(),
					"You entered invalid user data.\nPlease try again.",
					Toast.LENGTH_LONG).show();
			break;
		case RESPONSE_CODE_NO_POST:
			if (BuildConfig.DEBUG) {
				Log.d(Constants.LOG_TAG,
						"LoginFragment - checkHttpRequestResult(): " + key
								+ ": NO POST");
			}
			Toast.makeText(getActivity().getApplicationContext(),
					"ERROR while trying to connect to server!",
					Toast.LENGTH_LONG).show();
			break;
		default:
			break;

		}
	}

	public void login() {
		Editor editor = mSharedPreferences.edit();
		String email = mEditEmail.getText().toString();
		String password = mEditPassword.getText().toString();
		editor.putString(KEY_EMAIL, email);
		editor.putString(KEY_PASSWORD, password);
		editor.commit();
		Intent intent = new Intent(getActivity(), ItemListActivity.class);
		startActivity(intent);
	}

}