package de.canberkdemirkan.mediaboxmngr.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.activities.ItemPagerActivity;
import de.canberkdemirkan.mediaboxmngr.data.DAOItem;
import de.canberkdemirkan.mediaboxmngr.data.ItemStock;
import de.canberkdemirkan.mediaboxmngr.model.Item;

public class ItemListFragment extends ListFragment {

	private static final String TAG = "ItemListFragment";

	private ArrayList<Item> mItemList;

	private DAOItem mDAOItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		getActivity().setTitle(R.string.itemList_header);
		mItemList = ItemStock.get(getActivity()).getItemList();
		// mItemList = ItemStock.get(getActivity()).getDAOItem().getAllItems();
		mDAOItem = ItemStock.get(getActivity()).getDAOItem();

		ItemAdapter adapter = new ItemAdapter(mItemList);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Item item = ((ItemAdapter) getListAdapter()).getItem(position);
		Intent i = new Intent(getActivity(), ItemPagerActivity.class);
		i.putExtra(ItemFragment.EXTRA_ITEM_ID, item.getUniqueId());
		startActivity(i);
	}

	private class ItemAdapter extends ArrayAdapter<Item> {

		public ItemAdapter(ArrayList<Item> itemList) {
			super(getActivity(), 0, itemList);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// If we weren't given a view, inflate one
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.list_item, null);
			}

			// Configure the view for this Item
			Item item = getItem(position);

			TextView tv_itemTitle = (TextView) convertView
					.findViewById(R.id.tv_listItem_itemTitle);
			tv_itemTitle.setText(item.getTitle());

			TextView tv_itemDate = (TextView) convertView
					.findViewById(R.id.tv_listItem_itemCreationDate);
			tv_itemDate.setText(item.getCreationDate().toString());

			CheckBox cb_itemFavorite = (CheckBox) convertView
					.findViewById(R.id.cb_listItem_itemFavorite);
			cb_itemFavorite.setChecked(item.isFavorite());

			return convertView;
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		((ItemAdapter) getListAdapter()).notifyDataSetChanged();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_menu_list, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case R.id.menu__newItem:
			Item item = new Item();
			ItemStock.get(getActivity()).addItem(item);
			Intent intent = new Intent(getActivity(), ItemPagerActivity.class);
			intent.putExtra(ItemFragment.EXTRA_ITEM_ID, item.getUniqueId());
			startActivityForResult(intent, 0);
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}

}