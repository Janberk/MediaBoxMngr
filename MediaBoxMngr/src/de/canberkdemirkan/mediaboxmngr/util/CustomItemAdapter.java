package de.canberkdemirkan.mediaboxmngr.util;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.content.ItemType;
import de.canberkdemirkan.mediaboxmngr.dialogs.AlertDialogDeletion;
import de.canberkdemirkan.mediaboxmngr.fragments.ItemListFragment;
import de.canberkdemirkan.mediaboxmngr.interfaces.Constants;
import de.canberkdemirkan.mediaboxmngr.model.Item;

public class CustomItemAdapter extends ArrayAdapter<Item> {

	// TODO ViewHolder Pattern prevents list to mess up order while scrolling.
	static class ViewHolder {
		ImageView mImageItemIcon;
		// ImageView mImageItemDelete;
		TextView mTextItemTitle;
		TextView mTextItemCreationDate;
		CheckBox mCheckBoxItemFavorite;
		// CheckBox mCheckBoxConfirmItemDelete;
	}

	private final Context mContext;
	private ItemListFragment mFragment;
	private ArrayList<Item> mItemList;
	private FragmentManager mFragmentManager;

	public CustomItemAdapter(Context context, ItemListFragment fragment,
			ArrayList<Item> itemList) {
		super(context, android.R.layout.simple_list_item_multiple_choice,
				itemList);
		this.mContext = context;
		this.mFragment = fragment;
		this.mItemList = itemList;

		if (getContext() instanceof FragmentActivity) {
			mFragmentManager = ((FragmentActivity) context)
					.getSupportFragmentManager();
		}

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
			// holder.mImageItemDelete = (ImageView) convertView
			// .findViewById(R.id.iv_listItem_itemDeleteSingle);
			holder.mTextItemTitle = (TextView) convertView
					.findViewById(R.id.tv_listItem_itemTitle);
			holder.mTextItemCreationDate = (TextView) convertView
					.findViewById(R.id.tv_listItem_itemCreationDate);
			holder.mCheckBoxItemFavorite = (CheckBox) convertView
					.findViewById(R.id.cb_listItem_itemFavorite);
			// holder.mCheckBoxConfirmItemDelete = (CheckBox) convertView
			// .findViewById(R.id.cb_listItem_itemDelete);
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
		// holder.mCheckBoxConfirmItemDelete.setChecked(false);
		holder.mImageItemIcon.setFadingEdgeLength(2);
		UtilMethods.setCustomIconToTypeOfMedia(holder.mImageItemIcon, type,
				UtilMethods.ICON_DARK_TAG);

		// if (ItemListFragment.sDeleteMode) {
		// holder.mCheckBoxConfirmItemDelete.setVisibility(View.VISIBLE);
		// holder.mImageItemDelete.setVisibility(View.VISIBLE);
		// } else {
		// holder.mCheckBoxConfirmItemDelete.setVisibility(View.GONE);
		// holder.mImageItemDelete.setVisibility(View.GONE);
		// }

		// setClickListenerCheckBox(holder.mCheckBoxConfirmItemDelete, item,
		// position);
		// setClickListenerImageView(holder.mImageItemDelete, item, position);

		return convertView;
	}

	public void setClickListenerCheckBox(final CheckBox checkBox,
			final Item item, final int position) {
		checkBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkBox.isChecked()) {
				} else if (!checkBox.isChecked()) {
				}
			}
		});
	}

	public void setClickListenerImageView(final ImageView image,
			final Item item, final int position) {
		final String header = mContext.getResources().getString(
				R.string.dialog_header_delete);
		image.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialogDeletion dialog = AlertDialogDeletion.newInstance(
						mFragment, mItemList, header,
						AlertDialogDeletion.DIALOG_TAG_SINGLE);
				dialog.setTargetFragment(mFragment,
						Constants.REQUEST_LIST_DELETE);
				dialog.show(mFragmentManager, "");
			}
		});
	}

	public void refresh(ArrayList<Item> itemList) {
		mItemList.clear();
		mItemList.addAll(itemList);
		notifyDataSetChanged();
	}

}