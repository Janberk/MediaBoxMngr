package de.canberkdemirkan.mediaboxmngr.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.activities.LoginActivity;
import de.canberkdemirkan.mediaboxmngr.util.HTTPRequestHandler;

public class SignupFragment extends Fragment {

	private HTTPRequestHandler mHttpPost;

	private EditText mEditFirstname;
	private EditText mEditLastname;
	private EditText mEditUsername;
	private EditText mEditEmail;
	private EditText mEditPassword;
	private Button mButtonSignup;
	private TextView mTextLoginLink;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mHttpPost = new HTTPRequestHandler(
				"http://10.0.2.2:80/development/mediaboxmngr_backend/users/sign_in.php");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_signup, null);
		initElements(view);

		mButtonSignup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AsyncHTTPRequest().execute(mHttpPost.getUri());
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

	private void checkResult(String result) {
		if (result != null) {
			int key = Integer.valueOf(result);
			switch (key) {
			case LoginFragment.RESPONSE_CODE_SUCCESS:
				Toast.makeText(getActivity().getApplicationContext(),
						"User was saved successfully. Back to log in.",
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent(getActivity()
						.getApplicationContext(), LoginActivity.class);
				startActivity(intent);
				break;
			case LoginFragment.RESPONSE_CODE_EMPTY_FIELDS:
				Toast.makeText(getActivity().getApplicationContext(),
						"Please fill in all fields.", Toast.LENGTH_LONG).show();
				break;
			case LoginFragment.RESPONSE_CODE_INVALID_DATA:
				Toast.makeText(getActivity().getApplicationContext(),
						"You entered invalid data. Please try again.",
						Toast.LENGTH_LONG).show();
				break;
			case LoginFragment.RESPONSE_CODE_NO_POST:
				Toast.makeText(getActivity().getApplicationContext(),
						"ERROR while trying to connect to server!",
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
	}

	public List<NameValuePair> createParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);

		String firstname = mEditFirstname.getText().toString();
		String lastname = mEditLastname.getText().toString();
		String username = mEditUsername.getText().toString();
		String email = mEditEmail.getText().toString();
		String password = mEditPassword.getText().toString();

		params.add(new BasicNameValuePair("firstname", firstname));
		params.add(new BasicNameValuePair("lastname", lastname));
		params.add(new BasicNameValuePair("username", username));
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
		super.onResume();
	}

}