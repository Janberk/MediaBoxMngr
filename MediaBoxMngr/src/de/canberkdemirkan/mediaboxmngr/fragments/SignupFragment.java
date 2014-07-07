package de.canberkdemirkan.mediaboxmngr.fragments;

import android.app.Activity;
import android.content.Intent;
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
import de.canberkdemirkan.mediaboxmngr.activities.LoginActivity;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;

public class SignupFragment extends Fragment {

	public static final String SIGNIN_URL = "http://10.0.2.2:80/development/mediaboxmngr_backend/users/sign_in.php";

	private EditText mEditFirstname;
	private EditText mEditLastname;
	private EditText mEditUsername;
	private EditText mEditEmail;
	private EditText mEditPassword;
	private Button mButtonSignup;
	private TextView mTextLoginLink;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "SignupFragment - onCreate()");
		}
	}

	public void initElements(View view) {
		mEditFirstname = (EditText) view
				.findViewById(R.id.et_fragmentSignup_firstname);
		mEditLastname = (EditText) view
				.findViewById(R.id.et_fragmentSignup_lastname);
		mEditUsername = (EditText) view
				.findViewById(R.id.et_fragmentSignup_username);
		mEditEmail = (EditText) view.findViewById(R.id.et_fragmentSignup_email);
		mEditPassword = (EditText) view
				.findViewById(R.id.et_fragmentSignup_password);
		mButtonSignup = (Button) view
				.findViewById(R.id.btn_fragmentSignup_signup);
		mTextLoginLink = (TextView) view
				.findViewById(R.id.tv_fragmentSignup_login_link);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "SignupFragment - onCreateView()");
		}

		View view = inflater.inflate(R.layout.fragment_signup, null);
		initElements(view);

		mButtonSignup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String firstName = mEditFirstname.getText().toString();
				String lastName = mEditLastname.getText().toString();
				String userName = mEditUsername.getText().toString();
				String email = mEditEmail.getText().toString();
				String password = mEditPassword.getText().toString();

				RequestParams params = new RequestParams();
				params.put("firstname", firstName);
				params.put("lastname", lastName);
				params.put("username", userName);
				params.put("email", email);
				params.put("password", password);

				AsyncHttpClient client = new AsyncHttpClient();
				client.setTimeout(LoginFragment.DEFAULT_TIMEOUT);
				client.post(SIGNIN_URL, params, new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"SignupFragment - onStart()");
						}
					}

					@Override
					public void onSuccess(String response) {
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"SignupFragment - onSuccess()\n" + response);
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
									"SignupFragment - onFinish()");
						}
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						if (BuildConfig.DEBUG) {
							Log.d(Constants.LOG_TAG,
									"SignupFragment - onFailure(): " + content);
						}
					}

				});
			}
		});

		mTextLoginLink.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), LoginActivity.class);
				startActivity(intent);
			}
		});

		return view;
	}

	private void checkHttpRequestResult(int key) {
		switch (key) {
		case LoginFragment.RESPONSE_CODE_SUCCESS:
			if (BuildConfig.DEBUG) {
				Log.d(Constants.LOG_TAG,
						"SignupFragment - checkHttpRequestResult(): " + key
								+ ": SUCCESS");
			}
			Toast.makeText(getActivity().getApplicationContext(),
					"User was saved successfully. Back to log in.",
					Toast.LENGTH_LONG).show();
			Intent intent = new Intent(getActivity().getApplicationContext(),
					LoginActivity.class);
			startActivity(intent);
			break;
		case LoginFragment.RESPONSE_CODE_EMPTY_FIELDS:
			if (BuildConfig.DEBUG) {
				Log.d(Constants.LOG_TAG,
						"SignupFragment - checkHttpRequestResult(): " + key
								+ ": EMPTY FIELDS");
			}
			Toast.makeText(getActivity().getApplicationContext(),
					"Please fill in ALL required fields.", Toast.LENGTH_LONG)
					.show();
			break;
		case LoginFragment.RESPONSE_CODE_INVALID_DATA:
			if (BuildConfig.DEBUG) {
				Log.d(Constants.LOG_TAG,
						"SignupFragment - checkHttpRequestResult(): " + key
								+ ": INVALID DATA");
			}
			Toast.makeText(getActivity().getApplicationContext(),
					"You entered invalid data.\nPlease try again.",
					Toast.LENGTH_LONG).show();
			break;
		case LoginFragment.RESPONSE_CODE_NO_POST:
			if (BuildConfig.DEBUG) {
				Log.d(Constants.LOG_TAG,
						"SignupFragment - checkHttpRequestResult(): " + key
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

	/*
	 * 
	 * Logging callback methods for debug purposes
	 */

	@Override
	public void onAttach(Activity activity) {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "SignupFragment - onAttach()");
		}
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "SignupFragment - onActivityCreated()");
		}
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "SignupFragment - onStart()");
		}
		super.onStart();
	}

	@Override
	public void onResume() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "SignupFragment - onResume()");
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "SignupFragment - onPause()");
		}
		super.onPause();
	}

	@Override
	public void onStop() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "SignupFragment - onStop()");
		}
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "SignupFragment - onDestroyView()");
		}
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "SignupFragment - onDestroy()");
		}
		super.onDestroy();
	}

	@Override
	public void onDetach() {//
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "SignupFragment - onDetach()");
		}
		super.onDetach();
	}

}