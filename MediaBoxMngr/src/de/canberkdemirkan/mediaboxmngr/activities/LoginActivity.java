package de.canberkdemirkan.mediaboxmngr.activities;

import android.support.v4.app.Fragment;
import de.canberkdemirkan.mediaboxmngr.fragments.LoginFragment;

public class LoginActivity extends FragmentActivityBuilder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -485217608437205581L;

	@Override
	protected Fragment createFragment() {
		return new LoginFragment();
	}

}