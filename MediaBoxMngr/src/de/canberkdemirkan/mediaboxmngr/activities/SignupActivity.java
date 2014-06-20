package de.canberkdemirkan.mediaboxmngr.activities;

import android.support.v4.app.Fragment;
import de.canberkdemirkan.mediaboxmngr.fragments.SignupFragment;

public class SignupActivity extends FragmentActivityBuilder {

	@Override
	protected Fragment createFragment() {
		return new SignupFragment();
	}

}