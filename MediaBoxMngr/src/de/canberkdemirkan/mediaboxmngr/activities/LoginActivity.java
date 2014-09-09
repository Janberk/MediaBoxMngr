package de.canberkdemirkan.mediaboxmngr.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.fragments.LoginFragment;
import de.canberkdemirkan.mediaboxmngr.fragments.SignupFragment;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.interfaces.OnFragmentTransactionListener;

public class LoginActivity extends FragmentActivityBuilder implements
		OnFragmentTransactionListener {

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
	public void onFragmentTransaction(String tag) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		if (tag.equals(LoginFragment.TAG_LOGIN_FRAGMENT)) {
			SignupFragment fragment = (SignupFragment) mFragmentManager
					.findFragmentByTag(SignupFragment.class.getName());
			if (fragment == null) {
				fragment = SignupFragment.newInstance();
			}
			String backStateName = fragment.getClass().getName();
			boolean fragmentPopped = mFragmentManager.popBackStackImmediate(
					backStateName, 0);
			if (!fragmentPopped) {
				ft.replace(R.id.fragmentContainer, fragment, backStateName);
				ft.addToBackStack(null);
				ft.commit();
			}
		}
		if (tag.equals(SignupFragment.TAG_SIGNUP_FRAGMENT)) {
			LoginFragment fragment = (LoginFragment) mFragmentManager
					.findFragmentByTag(LoginFragment.class.getName());
			String backStateName = fragment.getClass().getName();
			boolean fragmentPopped = mFragmentManager.popBackStackImmediate(
					backStateName, 0);
			if (!fragmentPopped) {
				ft.replace(R.id.fragmentContainer, fragment, backStateName);
				ft.addToBackStack(null);
				ft.commit();
			}
		}

	}

	@Override
	public void onBackPressed() {
		if (mFragmentManager.getBackStackEntryCount() > 1) {
			finish();
		} else {
			super.onBackPressed();
		}
	}

}