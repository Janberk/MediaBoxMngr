package de.canberkdemirkan.mediaboxmngr.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;

public class LoginFragment extends Fragment {
	
	// Emulator
	public static final String LOGIN_URL = "http://10.0.2.2:80/development/mediaboxmngr_backend/users/log_in.php";
	
	// PC
	// public static final String LOGIN_URL = "http://192.168.1.50:80/development/mediaboxmngr_backend/users/log_in.php";
	
	// Laptop
	// public static final String LOGIN_URL = "http://192.168.0.13:8080/development/mediaboxmngr_backend/users/log_in.php";
	
	public static final int DEFAULT_TIMEOUT = 20 * 1000;

	public static final String KEY_EMAIL = "emailKey";
	public static final String KEY_PASSWORD = "passwordKey";
	public static final String KEY_REMEMBER_USER_EMAIL = "rememberEmailKey";
	public static final String KEY_REMEMBER_USER_PASSWORD = "rememberPasswordKey";

	public static final int RESPONSE_CODE_SUCCESS = 0;
	public static final int RESPONSE_CODE_EMPTY_FIELDS = 1;
	public static final int RESPONSE_CODE_INVALID_DATA = 2;
	public static final int RESPONSE_CODE_INVALID_EMAIL = 3;
	public static final int RESPONSE_CODE_NO_POST = 4;

	public static boolean REMEMBER_ME = false;

	private SharedPreferences mSharedPreferences;

	private EditText mEditEmail;
	private EditText mEditPassword;
	private CheckBox mCheckBoxRememberMe;
	private Button mButtonLogin;
	private TextView mTextSignupLink;

	private ProgressDialog mProgressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginFragment - onCreate()");
		}
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setMessage("Requesting user access. Please wait...");
		mProgressDialog.setCancelable(false);
	}

	private void initElements(View view) {
		mEditEmail = (EditText) view.findViewById(R.id.et_fragmentLogin_email);
		mEditPassword = (EditText) view
				.findViewById(R.id.et_fragmentLogin_password);
		mCheckBoxRememberMe = (CheckBox) view
				.findViewById(R.id.cb_fragmentLogin_rememberMe);
		mButtonLogin = (Button) view.findViewById(R.id.btn_fragmentLogin_login);
		mTextSignupLink = (TextView) view
				.findViewById(R.id.tv_fragmentLogin_signup_link);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginFragment - onCreateView()");
		}

		View view = inflater.inflate(R.layout.fragment_login, null);
		initElements(view);

		rememberUser();

		mButtonLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String email = mEditEmail.getText().toString();
				String password = mEditPassword.getText().toString();

				if (REMEMBER_ME) {
					saveUserToPrefs(email, password);
				} else {
					clearUser();
				}

				RequestParams params = new RequestParams();
				params.put("email", email);
				params.put("password", password);

				AsyncHttpClient client = new AsyncHttpClient();
				client.setTimeout(DEFAULT_TIMEOUT);
				client.post(LOGIN_URL, params, new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						mProgressDialog.show();
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"LoginFragment - onStart()");
						}
					}

					@Override
					public void onSuccess(String response) {
						mProgressDialog.hide();
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
						mProgressDialog.hide();
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"LoginFragment - onFinish()");
						}
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						mProgressDialog.hide();
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

		mCheckBoxRememberMe.setChecked(false);
		mCheckBoxRememberMe
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							REMEMBER_ME = true;
						} else {
							REMEMBER_ME = false;
						}
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
					"Please fill in ALL required fields.", Toast.LENGTH_LONG)
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
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginFragment - login()");
		}
		Editor editor = mSharedPreferences.edit();
		if (mSharedPreferences.contains(KEY_EMAIL)) {
			editor.remove(KEY_EMAIL);
			editor.remove(KEY_PASSWORD);
			editor.commit();
		}
		String email = mEditEmail.getText().toString();
		String password = mEditPassword.getText().toString();
		editor.putString(KEY_EMAIL, email);
		editor.putString(KEY_PASSWORD, password);
		editor.commit();
		Intent intent = new Intent(getActivity(), ItemListActivity.class);
		startActivity(intent);
	}

	/*
	 * 
	 * Logging callback methods for debug purposes
	 */

	@Override
	public void onAttach(Activity activity) {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginFragment - onAttach()");
		}
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginFragment - onActivityCreated()");
		}
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginFragment - onStart()");
		}
		super.onStart();
	}

	@Override
	public void onResume() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginFragment - onResume()");
		}
		mSharedPreferences = getActivity().getSharedPreferences(
				Constants.KEY_MY_PREFERENCES, Context.MODE_PRIVATE);
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
		rememberUser();
		super.onResume();
	}

	private void saveUserToPrefs(String email, String password) {
		Editor editor = mSharedPreferences.edit();
		editor.putString(KEY_REMEMBER_USER_EMAIL, email);
		editor.putString(KEY_REMEMBER_USER_PASSWORD, password);
		editor.commit();
	}

	private void clearUser() {
		mSharedPreferences = getActivity().getSharedPreferences(
				Constants.KEY_MY_PREFERENCES, Context.MODE_PRIVATE);
		if (mSharedPreferences.contains(KEY_REMEMBER_USER_EMAIL)) {
			if (mSharedPreferences.contains(KEY_REMEMBER_USER_PASSWORD)) {
				Editor editor = mSharedPreferences.edit();
				editor.remove(KEY_REMEMBER_USER_EMAIL);
				editor.remove(KEY_REMEMBER_USER_PASSWORD);
				editor.commit();
			}
		}
	}

	private void rememberUser() {
		mSharedPreferences = getActivity().getSharedPreferences(
				Constants.KEY_MY_PREFERENCES, Context.MODE_PRIVATE);
		if (mSharedPreferences.contains(KEY_REMEMBER_USER_EMAIL)) {
			if (mSharedPreferences.contains(KEY_REMEMBER_USER_PASSWORD)) {
				String email = mSharedPreferences.getString(
						KEY_REMEMBER_USER_EMAIL, null);
				String password = mSharedPreferences.getString(
						KEY_REMEMBER_USER_PASSWORD, null);
				mEditEmail.setText(email);
				mEditPassword.setText(password);
				mCheckBoxRememberMe.setChecked(true);
			}
		} else {
			mEditEmail.setText("");
			mEditPassword.setText("");
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
	public void onStop() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginFragment - onStop()");
		}
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginFragment - onDestroyView()");
		}
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginFragment - onDestroy()");
		}
		super.onDestroy();
	}

	@Override
	public void onDetach() {//
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginFragment - onDetach()");
		}
		super.onDetach();
	}

}