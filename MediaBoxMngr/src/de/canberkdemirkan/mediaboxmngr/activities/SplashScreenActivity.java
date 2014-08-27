package de.canberkdemirkan.mediaboxmngr.activities;

import android.support.v4.app.Fragment;
import de.canberkdemirkan.mediaboxmngr.fragments.SplashScreenFragment;

public class SplashScreenActivity extends FragmentActivityBuilder {

	@Override
	protected Fragment createFragment() {
		return SplashScreenFragment.newInstance();
	}

}