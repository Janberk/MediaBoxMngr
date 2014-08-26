package de.canberkdemirkan.mediaboxmngr.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.fragments.LoginFragment;
import de.canberkdemirkan.mediaboxmngr.fragments.SignupFragment;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.interfaces.OnRemoveFragmentListener;

public class LoginActivity extends FragmentActivityBuilder implements
		OnRemoveFragmentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -485217608437205581L;

	private FragmentManager mFragmentManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginActivity - onCreate()");
		}
		mFragmentManager = getSupportFragmentManager();
	}

	@Override
	protected Fragment createFragment() {
		return LoginFragment.newInstance();
	}

	@Override
	public void onRemoveFragment(String tag) {
		if (tag.equals(SignupFragment.TAG_SIGNUP_FRAGMENT)) {
			SignupFragment fragment = (SignupFragment) mFragmentManager
					.findFragmentById(R.id.fragmentContainer);
			mFragmentManager.beginTransaction().remove(fragment).commit();
		}
	}

	/*
	 * 
	 * Logging callback methods for debug purposes
	 */

	@Override
	public void onStart() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginActivity - onStart()");
		}
		super.onStart();
	}

	@Override
	public void onResume() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginActivity - onResume()");
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginActivity - onPause()");
		}
		super.onPause();
	}

	@Override
	public void onStop() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginActivity - onStop()");
		}
		super.onStop();
	}

	@Override
	public void onDestroy() {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "LoginActivity - onDestroy()");
		}
		super.onDestroy();
	}

}