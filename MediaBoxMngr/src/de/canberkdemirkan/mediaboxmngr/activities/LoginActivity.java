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
import de.canberkdemirkan.mediaboxmngr.interfaces.OnRemoveFragmentListener;
import de.canberkdemirkan.mediaboxmngr.interfaces.OnShowFragmentListener;

public class LoginActivity extends FragmentActivityBuilder implements
		OnShowFragmentListener, OnRemoveFragmentListener {

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
	public void onShowFragment(String tag) {
		if (tag.equals(LoginFragment.TAG_LOGIN_FRAGMENT)) {
			FragmentTransaction ft = mFragmentManager.beginTransaction();
			SignupFragment signupFragment = SignupFragment.newInstance();
			ft.add(R.id.fragmentContainer, signupFragment);
			ft.addToBackStack(null);
			ft.commit();
		}
	}

	@Override
	public void onRemoveFragment(String tag) {
		if (tag.equals(SignupFragment.TAG_SIGNUP_FRAGMENT)) {
			SignupFragment fragment = (SignupFragment) mFragmentManager
					.findFragmentById(R.id.fragmentContainer);
			mFragmentManager.beginTransaction().remove(fragment).commit();
		}
	}

}