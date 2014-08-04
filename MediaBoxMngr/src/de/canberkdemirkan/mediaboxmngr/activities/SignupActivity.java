package de.canberkdemirkan.mediaboxmngr.activities;

import android.support.v4.app.Fragment;
import de.canberkdemirkan.mediaboxmngr.fragments.SignupFragment;

public class SignupActivity extends FragmentActivityBuilder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6420815305175736658L;

	@Override
	protected Fragment createFragment() {
		return new SignupFragment();
	}

}