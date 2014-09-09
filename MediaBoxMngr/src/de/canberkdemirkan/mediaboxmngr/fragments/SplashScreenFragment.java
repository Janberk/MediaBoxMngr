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

	public static SplashScreenFragment newInstance() {
		return new SplashScreenFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_splash_screen, null);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				getActivity().finish();
			}
		}, SPLASH_TIME_OUT);

		return view;
	}

}