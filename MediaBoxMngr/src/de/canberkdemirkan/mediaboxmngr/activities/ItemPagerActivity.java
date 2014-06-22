package de.canberkdemirkan.mediaboxmngr.activities;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.fragments.ItemFragment;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Item;

public class ItemPagerActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private ArrayList<Item> mItemList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		UUID itemId = (UUID) getIntent().getSerializableExtra(
				Constants.KEY_ITEM_ID);
		final String userTag = (String) getIntent().getSerializableExtra(
				Constants.KEY_USER_TAG);

		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);

		mItemList = ItemStock.get(this, userTag).getItemList();

		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

			@Override
			public int getCount() {
				return mItemList.size();
			}

			@Override
			public Fragment getItem(int pos) {
				Item item = mItemList.get(pos);
				return ItemFragment.newInstance(item.getUniqueId(), userTag);
			}
		});

		for (int i = 0; i < mItemList.size(); i++) {
			if (mItemList.get(i).getUniqueId().equals(itemId)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}

		mViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					public void onPageScrollStateChanged(int state) {
					}

					public void onPageScrolled(int pos, float posOffset,
							int posOffsetPixels) {
					}

					public void onPageSelected(int pos) {
						Item item = mItemList.get(pos);
						if (item.getTitle() != null) {
							setTitle(item.getTitle());
						}
					}
				});
	}

}