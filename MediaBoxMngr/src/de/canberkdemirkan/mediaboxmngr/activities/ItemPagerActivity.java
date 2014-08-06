package de.canberkdemirkan.mediaboxmngr.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.adapters.ScreenSlidePagerAdapter;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.fragments.ItemFragment;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Item;

public class ItemPagerActivity extends FragmentActivity implements
		Serializable, ViewPager.OnPageChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6272049898266569961L;

	private Context mContext;
	private ViewPager mViewPager;
	private ArrayList<Item> mItemList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemPagerActivity - onCreate()");
		}

		UUID itemId = (UUID) getIntent().getSerializableExtra(
				Constants.KEY_ITEM_UID);
		String userTag = (String) getIntent().getSerializableExtra(
				Constants.KEY_USER_TAG);

		mContext = getApplicationContext();
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);

		mItemList = ItemStock.get(this, userTag).getItemList();

		FragmentManager fm = getSupportFragmentManager();
		mViewPager
				.setAdapter(new ScreenSlidePagerAdapter(mContext, fm, userTag));

		for (int i = 0; i < mItemList.size(); i++) {
			if (mItemList.get(i).getUniqueId().equals(itemId)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}

		mViewPager.setOnPageChangeListener(this);
	}

	// listener callback methods
	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
		Item item = mItemList.get(pos);
		ItemFragment.sItemType = item.getType().toString();
	}

	@Override
	public void onPageSelected(int pos) {
	}

}