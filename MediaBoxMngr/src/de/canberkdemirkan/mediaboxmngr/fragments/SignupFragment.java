package de.canberkdemirkan.mediaboxmngr.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.interfaces.OnFragmentTransactionListener;
import de.canberkdemirkan.mediaboxmngr.interfaces.UserAuthenticationConstants;
import de.canberkdemirkan.mediaboxmngr.util.EmailValidator;

public class SignupFragment extends Fragment implements View.OnClickListener,
		OnFragmentTransactionListener {

	public static final String TAG_SIGNUP_FRAGMENT = "de.canberkdemirkan.mediaboxmngr.tag_signup_fragment";

	private OnFragmentTransactionListener mFragmentTransactionListener;

	private EmailValidator mEmailValidator;

	private EditText mEditFirstname;
	private EditText mEditLastname;
	private EditText mEditEmail;
	private EditText mEditPassword;
	private EditText mEditConfirmPassword;
	private Button mButtonSignup;
	private TextView mTextLoginLink;

	public static SignupFragment newInstance() {
		SignupFragment newInstance = new SignupFragment();
		return newInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_signup, null);
		initViews(view);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}

		mButtonSignup.setOnClickListener(this);
		mTextLoginLink.setOnClickListener(this);

		return view;
	}

	public void initViews(View view) {
		mEditFirstname = (EditText) view
				.findViewById(R.id.et_fragmentSignup_firstname);
		mEditLastname = (EditText) view
				.findViewById(R.id.et_fragmentSignup_lastname);
		mEditEmail = (EditText) view.findViewById(R.id.et_fragmentSignup_email);
		mEditPassword = (EditText) view
				.findViewById(R.id.et_fragmentSignup_password);
		mEditConfirmPassword = (EditText) view
				.findViewById(R.id.et_fragmentSignup_confirmPassword);
		mButtonSignup = (Button) view
				.findViewById(R.id.btn_fragmentSignup_signup);
		mTextLoginLink = (TextView) view
				.findViewById(R.id.tv_fragmentSignup_login_link);
	}

	private void requestSignup() {
		String firstName = mEditFirstname.getText().toString();
		String lastName = mEditLastname.getText().toString();
		String email = mEditEmail.getText().toString();
		String password = mEditPassword.getText().toString();

		RequestParams params = new RequestParams();
		params.put(UserAuthenticationConstants.KEY_REQUEST_PARAMS_FIRSTNAME,
				firstName);
		params.put(UserAuthenticationConstants.KEY_REQUEST_PARAMS_LASTNAME,
				lastName);
		params.put(UserAuthenticationConstants.KEY_REQUEST_PARAMS_EMAIL, email);
		params.put(UserAuthenticationConstants.KEY_REQUEST_PARAMS_PASSWORD,
				password);

		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(UserAuthenticationConstants.DEFAULT_TIMEOUT);
		client.post(UserAuthenticationConstants.SIGNUP_URL, params,
				new AsyncHttpResponseHandler() {

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

	private void checkHttpRequestResult(int key) {
		String message = null;
		switch (key) {
		case UserAuthenticationConstants.RESPONSE_CODE_SUCCESS:
			message = getResources().getText(R.string.response_code_success)
					.toString();
			Toast.makeText(getActivity().getApplicationContext(), message,
					Toast.LENGTH_LONG).show();
			onFragmentTransaction(TAG_SIGNUP_FRAGMENT);
			break;
		case UserAuthenticationConstants.RESPONSE_CODE_NO_POST:
			message = getResources().getText(R.string.response_code_no_post)
					.toString();
			Toast.makeText(getActivity().getApplicationContext(), message,
					Toast.LENGTH_LONG).show();
			break;
		case UserAuthenticationConstants.RESPONSE_CODE_USER_EXISTS:
			message = getResources()
					.getText(R.string.response_code_user_exists).toString();
			Toast.makeText(getActivity().getApplicationContext(), message,
					Toast.LENGTH_LONG).show();
			break;
		case UserAuthenticationConstants.RESPONSE_CODE_ERROR:
			message = getResources().getText(
					R.string.response_code_error_saving).toString();
			Toast.makeText(getActivity().getApplicationContext(), message,
					Toast.LENGTH_LONG).show();
			break;

		default:
			break;

		}
	}

	@Override
	public void onClick(View view) {
		if (view == mButtonSignup) {
			if (clearForRequest()) {
				requestSignup();
			}
		}
		if (view == mTextLoginLink) {
			onFragmentTransaction(TAG_SIGNUP_FRAGMENT);
		}
	}

	private boolean formIsFilled() {
		String firstName = mEditFirstname.getText().toString();
		String lastName = mEditLastname.getText().toString();
		String email = mEditEmail.getText().toString();
		String password = mEditPassword.getText().toString();
		String confirmPassword = mEditConfirmPassword.getText().toString();
		if (firstName.equals("") || lastName.equals("") || email.equals("")
				|| password.equals("") || confirmPassword.equals("")) {
			return false;
		}
		return true;
	}

	private boolean emailIsValid() {
		String email = mEditEmail.getText().toString();
		if (!mEmailValidator.validate(email)) {
			return false;
		} else {
			return true;
		}
	}

	private boolean passwordsMatching() {
		String passOne = mEditPassword.getText().toString();
		String passTwo = mEditConfirmPassword.getText().toString();
		if (passOne.equals(passTwo)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean clearForRequest() {
		String message = null;
		if (!formIsFilled()) {
			message = getResources().getText(
					R.string.response_code_empty_fields).toString();
			Toast.makeText(getActivity().getApplicationContext(), message,
					Toast.LENGTH_LONG).show();
			return false;
		}
		if (!emailIsValid()) {
			message = getResources().getText(
					R.string.response_code_invalid_email).toString();
			Toast.makeText(getActivity().getApplicationContext(), message,
					Toast.LENGTH_LONG).show();
			return false;
		}
		if (!passwordsMatching()) {
			message = getResources().getText(R.string.passwords_not_identical)
					.toString();
			Toast.makeText(getActivity().getApplicationContext(), message,
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			onFragmentTransaction(TAG_SIGNUP_FRAGMENT);
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		try {
			mFragmentTransactionListener = (OnFragmentTransactionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(getActivity().getClass()
					.getSimpleName()
					+ " must implement OnFragmentTransactionListener.");
		}
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mEmailValidator = new EmailValidator();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		setHasOptionsMenu(true);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onResume();
	}

	@Override
	public void onPause() {
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		super.onPause();
	}

	@Override
	public void onFragmentTransaction(String tag) {
		mFragmentTransactionListener.onFragmentTransaction(tag);
	}

}