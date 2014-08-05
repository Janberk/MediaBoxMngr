package de.canberkdemirkan.mediaboxmngr.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.fragments.LoginFragment;
import de.canberkdemirkan.mediaboxmngr.fragments.SignupFragment;
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
		mFragmentManager = getSupportFragmentManager();
	}

	@Override
	protected Fragment createFragment() {
		return new LoginFragment();
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