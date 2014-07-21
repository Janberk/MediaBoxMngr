package de.canberkdemirkan.mediaboxmngr.data;

import java.util.ArrayList;

import de.canberkdemirkan.mediaboxmngr.content.ItemType;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.MusicAlbum;

public class DummyDataProvider {

	private String mUser;
	private ArrayList<Item> mItemList;

	public DummyDataProvider(String user) {
		mUser = user;
		mItemList = createDummyItemList();
	}

	private ArrayList<Item> createDummyItemList() {
		ArrayList<Item> list = new ArrayList<Item>();
		Item item = null;

		for (int i = 1; i <= 30; i++) {
			if (i < 11) {
				item = new MusicAlbum();
				item.setType(ItemType.Album);
				item.setUser(mUser);
				item.setTitle("Test item Nr. " + i);
				if ((i % 2) != 0) {
					item.setFavorite(true);
				}
				item.setGenre(i + " Action");
				item.setOriginalTitle("OT Test item Nr. " + i);
				item.setCountry(i + " USA");
				item.setYearPublished(i + " 1981");
				item.setContent(item.toString());
			} else if (i < 21) {
				item = new Book();
				item.setType(ItemType.Book);
				item.setUser(mUser);
				item.setTitle("Test item Nr. " + i);
				if ((i % 2) != 0) {
					item.setFavorite(true);
				}
				item.setGenre(i + " Action");
				item.setOriginalTitle("OT Test item Nr. " + i);
				item.setCountry(i + " USA");
				item.setYearPublished(i + " 1981");
				item.setContent(item.toString());
			} else if (i < 31) {
				item = new Movie();
				item.setType(ItemType.Movie);
				item.setUser(mUser);
				item.setTitle("Test item Nr. " + i);
				if ((i % 2) != 0) {
					item.setFavorite(true);
				}
				item.setGenre(i + " Action");
				item.setOriginalTitle("OT Test item Nr. " + i);
				item.setCountry(i + " USA");
				item.setYearPublished(i + " 1981");
				item.setContent(item.toString());
			}
			list.add(item);
		}

		return list;
	}

	public ArrayList<Item> getItemList() {
		return mItemList;
	}

}