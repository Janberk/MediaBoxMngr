package de.canberkdemirkan.mediaboxmngr.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.activities.LoginActivity;

public class SplashScreenFragment extends Fragment {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 2000;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_splash_screen, null);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent i = new Intent(getActivity(), LoginActivity.class);
				startActivity(i);
				getActivity().finish();
			}
		}, SPLASH_TIME_OUT);

		return view;
	}

}