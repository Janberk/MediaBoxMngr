package de.canberkdemirkan.mediaboxmngr.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.util.AppContextUtil;

public abstract class FragmentActivityBuilder extends ActionBarActivity {

	protected abstract Fragment createFragment();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppContextUtil.setContext(getApplicationContext());
		setContentView(R.layout.activity_container);

		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

		if (fragment == null) {
			fragment = createFragment();
			String tag = fragment.getClass().getName();
			fm.beginTransaction().add(R.id.fragmentContainer, fragment, tag)
					.commit();
		}
	}

}