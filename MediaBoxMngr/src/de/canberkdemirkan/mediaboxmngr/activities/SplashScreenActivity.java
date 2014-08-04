package de.canberkdemirkan.mediaboxmngr.activities;

import android.support.v4.app.Fragment;
import de.canberkdemirkan.mediaboxmngr.fragments.SplashScreenFragment;

public class SplashScreenActivity extends FragmentActivityBuilder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4234848505653635030L;

	@Override
	protected Fragment createFragment() {
		return new SplashScreenFragment();
	}

}