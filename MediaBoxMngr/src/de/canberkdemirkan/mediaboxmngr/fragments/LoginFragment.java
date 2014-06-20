package de.canberkdemirkan.mediaboxmngr.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.activities.ItemListActivity;
import de.canberkdemirkan.mediaboxmngr.activities.SignupActivity;
import de.canberkdemirkan.mediaboxmngr.data.ProjectConstants;
import de.canberkdemirkan.mediaboxmngr.util.HTTPRequestHandler;

public class LoginFragment extends Fragment {

	public static final String KEY_EMAIL = "emailKey";
	public static final String KEY_PASSWORD = "passwordKey";

	public static final int RESPONSE_CODE_SUCCESS = 0;
	public static final int RESPONSE_CODE_EMPTY_FIELDS = 1;
	public static final int RESPONSE_CODE_INVALID_DATA = 2;
	public static final int RESPONSE_CODE_NO_POST = 3;

	private HTTPRequestHandler mHttpPost;

	private SharedPreferences mSharedPreferences;

	private TextWatcher mWatcher;

	private EditText mEditEmail;
	private EditText mEditPassword;
	private Button mButtonLogin;
	private TextView mTextSignupLink;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mHttpPost = new HTTPRequestHandler(
				"http://10.0.2.2:80/development/mediaboxmngr_backend/users/log_in.php");
		// httpPost = new HTTPRequestHandler(
		// "http://192.168.1.50:8080/development/mediaboxmngr_backend/users/log_in.php");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_login, null);
		initElements(view);

		mButtonLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AsyncHTTPRequest().execute(mHttpPost.getUri());
			}
		});

		mTextSignupLink.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), SignupActivity.class);
				startActivity(intent);
			}
		});
		mEditEmail.addTextChangedListener(mWatcher);

		return view;
	}

	private void initElements(View view) {
		mEditEmail = (EditText) view.findViewById(R.id.et_fragmentLogin_email);
		mEditPassword = (EditText) view
				.findViewById(R.id.et_fragmentLogin_password);
		mButtonLogin = (Button) view.findViewById(R.id.btn_fragmentLogin_login);
		mButtonLogin.setEnabled(false);
		mTextSignupLink = (TextView) view
				.findViewById(R.id.tv_fragmentLogin_signup_link);
		mWatcher = new LocalTextWatcher();
	}

	public class AsyncHTTPRequest extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			return mHttpPost.readHTTPPostResponse(createParams());
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			checkResult(result);
		}

	}

	private class LocalTextWatcher implements TextWatcher {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			boolean validEmail = validateEmail(mEditEmail.getText().toString());

			if (!validEmail && editTextIsEmpty(mEditEmail)) {
				// et_email.setError("Invalid Email!");
				mButtonLogin.setEnabled(false);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			boolean validEmail = validateEmail(mEditEmail.getText().toString());
			if (validEmail) {
				mButtonLogin.setEnabled(true);
			}
		}
	}

	public boolean editTextIsEmpty(EditText edittext) {
		if (edittext.getText().toString().trim().length() < 1) {
			return true;
		} else {
			return false;
		}
	}

	private boolean validateEmail(String email) {
		String validEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(validEmail);
		Matcher matcher = pattern.matcher(email);

		return matcher.matches();
	}

	private void checkResult(String result) {
		if (result != null && result.matches("-?\\d+")) {
			int key = Integer.valueOf(result);
			switch (key) {
			case RESPONSE_CODE_SUCCESS:
				login();
				break;
			case RESPONSE_CODE_EMPTY_FIELDS:
				Toast.makeText(getActivity().getApplicationContext(),
						"Please fill in required fields.", Toast.LENGTH_LONG)
						.show();
				break;
			case RESPONSE_CODE_INVALID_DATA:
				Toast.makeText(getActivity().getApplicationContext(),
						"You entered invalid user data. Please try again.",
						Toast.LENGTH_LONG).show();
				break;
			case RESPONSE_CODE_NO_POST:
				Toast.makeText(getActivity().getApplicationContext(),
						"ERROR while trying to connect to server!",
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
		Toast.makeText(getActivity().getApplicationContext(), result,
				Toast.LENGTH_LONG).show();
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

	public List<NameValuePair> createParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);

		String email = mEditEmail.getText().toString();
		String password = mEditPassword.getText().toString();

		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));

		return params;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
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
			mButtonLogin.setEnabled(false);
		}
		super.onResume();
	}

}