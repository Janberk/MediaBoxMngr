package de.canberkdemirkan.mediaboxmngr.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import de.canberkdemirkan.mediaboxmngr.R;

public abstract class FragmentActivityBuilder extends FragmentActivity {

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