package de.canberkdemirkan.mediaboxmngr.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.interfaces.UserAuthenticationConstants;

public class LoginFragment extends Fragment implements View.OnClickListener,
		OnCheckedChangeListener {

	private FragmentManager mFragmentManager;
	private SharedPreferences mSharedPreferences;
	private ProgressDialog mProgressDialog;

	private EditText mEditEmail;
	private EditText mEditPassword;
	private CheckBox mCheckBoxRememberMe;
	private Button mButtonLogin;
	private TextView mTextSignupLink;

	public boolean mRememberMe = false;

	// public static String sEmailState = null;
	// public static String sPasswordState = null;

	public static LoginFragment newInstance() {
		Bundle args = new Bundle();
		LoginFragment newInstance = new LoginFragment();
		newInstance.setArguments(args);
		return newInstance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginFragment - onCreate()");
		}
		// if (savedInstanceState != null) {
		// sEmailState = savedInstanceState
		// .getString(Constants.KEY_EMAIL_STATE);
		// sPasswordState = savedInstanceState
		// .getString(Constants.KEY_PASSWORD_STATE);
		// }
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setMessage(getResources().getText(
				R.string.progress_dialog_text).toString());
		mProgressDialog.setCancelable(false);
		mFragmentManager = getActivity().getSupportFragmentManager();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginFragment - onCreateView()");
		}

		View view = inflater.inflate(R.layout.fragment_login, null);
		initViews(view);

		rememberUser();

		mButtonLogin.setOnClickListener(this);
		mTextSignupLink.setOnClickListener(this);
		mCheckBoxRememberMe.setChecked(false);
		mCheckBoxRememberMe.setOnCheckedChangeListener(this);

		return view;
	}

	private void initViews(View view) {
		mEditEmail = (EditText) view.findViewById(R.id.et_fragmentLogin_email);
		mEditPassword = (EditText) view
				.findViewById(R.id.et_fragmentLogin_password);
		mCheckBoxRememberMe = (CheckBox) view
				.findViewById(R.id.cb_fragmentLogin_rememberMe);
		mButtonLogin = (Button) view.findViewById(R.id.btn_fragmentLogin_login);
		mTextSignupLink = (TextView) view
				.findViewById(R.id.tv_fragmentLogin_signup_link);
	}

	private void checkHttpRequestResult(int key) {
		String message = null;
		switch (key) {
		case UserAuthenticationConstants.RESPONSE_CODE_SUCCESS:
			if (BuildConfig.DEBUG) {
				Log.d(Constants.LOG_TAG,
						"LoginFragment - checkHttpRequestResult(): " + key
								+ ": SUCCESS");
			}
			login();
			break;
		case UserAuthenticationConstants.RESPONSE_CODE_EMPTY_FIELDS:
			if (BuildConfig.DEBUG) {
				Log.d(Constants.LOG_TAG,
						"LoginFragment - checkHttpRequestResult(): " + key
								+ ": EMPTY FIELDS");
			}
			message = getResources().getText(
					R.string.response_code_empty_fields).toString();
			Toast.makeText(getActivity().getApplicationContext(), message,
					Toast.LENGTH_LONG).show();
			break;
		case UserAuthenticationConstants.RESPONSE_CODE_INVALID_EMAIL:
			if (BuildConfig.DEBUG) {
				Log.d(Constants.LOG_TAG,
						"LoginFragment - checkHttpRequestResult(): " + key
								+ ": INVALID EMAIL");
			}
			message = getResources().getText(
					R.string.response_code_invalid_email).toString();
			Toast.makeText(getActivity().getApplicationContext(), message,
					Toast.LENGTH_LONG).show();
			break;
		case UserAuthenticationConstants.RESPONSE_CODE_INVALID_DATA:
			if (BuildConfig.DEBUG) {
				Log.d(Constants.LOG_TAG,
						"LoginFragment - checkHttpRequestResult(): " + key
								+ ": INVALID DATA");
			}
			message = getResources().getText(
					R.string.response_code_invalid_data).toString();
			Toast.makeText(getActivity().getApplicationContext(), message,
					Toast.LENGTH_LONG).show();
			break;
		case UserAuthenticationConstants.RESPONSE_CODE_NO_POST:
			if (BuildConfig.DEBUG) {
				Log.d(Constants.LOG_TAG,
						"LoginFragment - checkHttpRequestResult(): " + key
								+ ": NO POST");
			}
			message = getResources().getText(R.string.response_code_no_post)
					.toString();
			Toast.makeText(getActivity().getApplicationContext(), message,
					Toast.LENGTH_LONG).show();
			break;
		default:
			break;

		}
	}

	private void requestLogin() {
		String email = mEditEmail.getText().toString();
		String password = mEditPassword.getText().toString();

		if (mRememberMe) {
			saveUserToPrefs(email, password);
		} else {
			clearUser();
		}

		RequestParams params = new RequestParams();
		params.put(UserAuthenticationConstants.KEY_REQUEST_PARAMS_EMAIL, email);
		params.put(UserAuthenticationConstants.KEY_REQUEST_PARAMS_PASSWORD,
				password);

		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(UserAuthenticationConstants.DEFAULT_TIMEOUT);
		client.post(Constants.LOGIN_URL, params,
				new AsyncHttpResponseHandler() {

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
						mProgressDialog.dismiss();
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
						mProgressDialog.dismiss();
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"LoginFragment - onFinish()");
						}
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						mProgressDialog.dismiss();
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"LoginFragment - onFailure(): " + content);
						}
					}

				});
	}

	private void login() {
		Editor editor = mSharedPreferences.edit();
		if (mSharedPreferences.contains(UserAuthenticationConstants.KEY_EMAIL)) {
			editor.remove(UserAuthenticationConstants.KEY_EMAIL);
			editor.remove(UserAuthenticationConstants.KEY_PASSWORD);
			editor.commit();
		}
		String email = mEditEmail.getText().toString();
		String password = mEditPassword.getText().toString();
		editor.putString(UserAuthenticationConstants.KEY_EMAIL, email);
		editor.putString(UserAuthenticationConstants.KEY_PASSWORD, password);
		editor.commit();
		Intent intent = new Intent(getActivity(), ItemListActivity.class);
		startActivity(intent);
	}

	private void signup() {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		SignupFragment signupFragment = SignupFragment.newInstance();
		ft.add(R.id.fragmentContainer, signupFragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	private void saveUserToPrefs(String email, String password) {
		Editor editor = mSharedPreferences.edit();
		editor.putString(UserAuthenticationConstants.KEY_REMEMBER_EMAIL, email);
		editor.putString(UserAuthenticationConstants.KEY_REMEMBER_PASSWORD,
				password);
		editor.commit();
	}

	private void clearUser() {
		mSharedPreferences = getActivity().getSharedPreferences(
				Constants.KEY_MY_PREFERENCES, Context.MODE_PRIVATE);
		if (mSharedPreferences
				.contains(UserAuthenticationConstants.KEY_REMEMBER_EMAIL)) {
			if (mSharedPreferences
					.contains(UserAuthenticationConstants.KEY_REMEMBER_PASSWORD)) {
				Editor editor = mSharedPreferences.edit();
				editor.remove(UserAuthenticationConstants.KEY_REMEMBER_EMAIL);
				editor.remove(UserAuthenticationConstants.KEY_REMEMBER_PASSWORD);
				editor.commit();
			}
		}
	}

	private void rememberUser() {
		mSharedPreferences = getActivity().getSharedPreferences(
				Constants.KEY_MY_PREFERENCES, Context.MODE_PRIVATE);
		if (mSharedPreferences
				.contains(UserAuthenticationConstants.KEY_REMEMBER_EMAIL)) {
			if (mSharedPreferences
					.contains(UserAuthenticationConstants.KEY_REMEMBER_PASSWORD)) {
				String email = mSharedPreferences.getString(
						UserAuthenticationConstants.KEY_REMEMBER_EMAIL, null);
				String password = mSharedPreferences
						.getString(
								UserAuthenticationConstants.KEY_REMEMBER_PASSWORD,
								null);
				mEditEmail.setText(email);
				mEditPassword.setText(password);
				mCheckBoxRememberMe.setChecked(true);
			}
		} else {
			mEditEmail.setText("");
			mEditPassword.setText("");
		}
	}

	/*
	 * 
	 * Logging callback methods for debug purposes
	 */

	// @Override
	// public void onSaveInstanceState(Bundle savedInstanceState) {
	// if (BuildConfig.DEBUG) {
	// Log.d(Constants.LOG_TAG, "LoginFragment - onSaveInstanceState()");
	// }
	// savedInstanceState.putString(Constants.KEY_EMAIL_STATE, mEditEmail
	// .getText().toString());
	//
	// savedInstanceState.putString(Constants.KEY_PASSWORD_STATE,
	// mEditPassword.getText().toString());
	// super.onSaveInstanceState(savedInstanceState);
	// }

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
		// if (savedInstanceState != null) {
		// if (sEmailState != null) {
		// mEditEmail.setText(sEmailState);
		// }
		// if (sPasswordState != null) {
		// mEditPassword.setText(sPasswordState);
		// }
		// }
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
		if (mSharedPreferences.contains(UserAuthenticationConstants.KEY_EMAIL)) {
			if (mSharedPreferences
					.contains(UserAuthenticationConstants.KEY_PASSWORD)) {
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
	public void onDetach() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginFragment - onDetach()");
		}
		super.onDetach();
	}

	// listener callback methods
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView == mCheckBoxRememberMe) {
			if (isChecked) {
				mRememberMe = true;
			} else {
				mRememberMe = false;
			}
		}
	}

	@Override
	public void onClick(View view) {
		if (view == mButtonLogin) {
			requestLogin();
		}
		if (view == mTextSignupLink) {
			signup();
		}
	}

}