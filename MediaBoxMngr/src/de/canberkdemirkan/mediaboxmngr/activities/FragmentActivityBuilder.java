package de.canberkdemirkan.mediaboxmngr.activities;

import java.io.Serializable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import de.canberkdemirkan.mediaboxmngr.R;

public abstract class FragmentActivityBuilder extends ActionBarActivity
		implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3550279454379572403L;

	protected abstract Fragment createFragment();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);

		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

		if (fragment == null) {
			fragment = createFragment();
			fm.beginTransaction().add(R.id.fragmentContainer, fragment)
					.commit();
		}
	}

}