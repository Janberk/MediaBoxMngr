package de.canberkdemirkan.mediaboxmngr.util;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.model.Item;

public class ItemAdapter extends ArrayAdapter<Item> {

	// TODO ViewHolder Pattern damit Liste beim Scrollen nicht durcheinander kommt
	static class ViewHolder {
		TextView mTextItemTitle;
		TextView mTextItemCreationDate;
		CheckBox mCheckBoxItemFavorite;
	}

	private final Context mContext;
	@SuppressWarnings("unused")
	private ArrayList<Item> mItemList;

	public ItemAdapter(Context context, ArrayList<Item> itemList) {
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
		holder.mTextItemTitle.setText(item.getTitle());
		holder.mTextItemCreationDate.setText(item.getCreationDate().toString());
		holder.mCheckBoxItemFavorite.setChecked(item.isFavorite());

		return convertView;
	}

	public void refresh(ArrayList<Item> itemList) {
		this.mItemList = itemList;
		notifyDataSetChanged();
	}

}