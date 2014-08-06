package de.canberkdemirkan.mediaboxmngr.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import de.canberkdemirkan.mediaboxmngr.BuildConfig;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.fragments.ItemFragment;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Item;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

	private Context mContext;
	private String mUser;
	private ArrayList<Item> mItemList;

	public ScreenSlidePagerAdapter(Context context, FragmentManager fm,
			String userTag) {
		super(fm);
		mUser = userTag;
		mContext = context;
		mItemList = ItemStock.get(mContext, userTag).getItemList();
	}

	@Override
	public int getCount() {
		return mItemList.size();
	}

	@Override
	public Fragment getItem(int pos) {
		if (BuildConfig.DEBUG) {
			Log.d(Constants.LOG_TAG, "ItemPagerActivity - getItem()");
		}
		Item item = mItemList.get(pos);
		return ItemFragment.newInstance(item.getUniqueId(), mUser);
	}

}