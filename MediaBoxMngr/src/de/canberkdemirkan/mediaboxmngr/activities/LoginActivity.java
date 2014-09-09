package de.canberkdemirkan.mediaboxmngr.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.fragments.LoginFragment;
import de.canberkdemirkan.mediaboxmngr.fragments.SignupFragment;
import de.canberkdemirkan.mediaboxmngr.interfaces.OnFragmentTransactionListener;

public class LoginActivity extends FragmentActivityBuilder implements
		OnFragmentTransactionListener {

	private FragmentManager mFragmentManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
				ft.addToBackStack(LoginFragment.class.getName());
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
				ft.addToBackStack(SignupFragment.class.getName());
				ft.commit();
			}
		}

	}

	@Override
	public void onBackPressed() {
		int count = mFragmentManager.getBackStackEntryCount();
		String name = null;
		String loginFragment = LoginFragment.class.getName();
		if (count != 0) {
			FragmentManager.BackStackEntry latest = mFragmentManager
					.getBackStackEntryAt(count - 1);
			name = latest.getName();
		}
		if (mFragmentManager.getBackStackEntryCount() > 1
				&& !name.equals(loginFragment)) {
			finish();
		} else {
			super.onBackPressed();
		}
	}

}