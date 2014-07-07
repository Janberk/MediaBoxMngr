package de.canberkdemirkan.mediaboxmngr.util;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.content.ItemType;
import de.canberkdemirkan.mediaboxmngr.model.Item;

public class CustomItemAdapter extends ArrayAdapter<Item> {

	// TODO ViewHolder Pattern prevents list to mess up order while scrolling.
	static class ViewHolder {
		ImageView mImageItemIcon;
		TextView mTextItemTitle;
		TextView mTextItemCreationDate;
		CheckBox mCheckBoxItemFavorite;
	}

	private final Context mContext;
	private ArrayList<Item> mItemList;

	public CustomItemAdapter(Context context, ArrayList<Item> itemList) {
		super(context, android.R.layout.simple_list_item_1, itemList);
		this.mContext = context;
		this.mItemList = itemList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item, parent, false);
			holder = new ViewHolder();
			holder.mImageItemIcon = (ImageView) convertView
					.findViewById(R.id.iv_listItem_itemIcon);
			holder.mTextItemTitle = (TextView) convertView
					.findViewById(R.id.tv_listItem_itemTitle);
			holder.mTextItemCreationDate = (TextView) convertView
					.findViewById(R.id.tv_listItem_itemCreationDate);
			holder.mCheckBoxItemFavorite = (CheckBox) convertView
					.findViewById(R.id.cb_listItem_itemFavorite);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Configure the view for this Item
		Item item = getItem(position);
		ItemType type = (ItemType) mItemList.get(position).getType();

		holder.mTextItemTitle.setText(item.getTitle());
		holder.mTextItemCreationDate.setText(item.getCreationDate().toString());
		holder.mCheckBoxItemFavorite.setChecked(item.isFavorite());
		holder.mImageItemIcon.setFadingEdgeLength(2);
		UtilMethods.setCustomIconToTypeOfMedia(holder.mImageItemIcon, type);

		return convertView;
	}

	public void refresh(ArrayList<Item> itemList) {
		// mItemList = itemList;
		mItemList.clear();
		mItemList.addAll(itemList);
		notifyDataSetChanged();
	}

}