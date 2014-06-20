package de.canberkdemirkan.mediaboxmngr.activities;

import android.support.v4.app.Fragment;
import de.canberkdemirkan.mediaboxmngr.fragments.LoginFragment;

public class LoginActivity extends FragmentActivityBuilder {

	@Override
	protected Fragment createFragment() {
		return new LoginFragment();
	}

}