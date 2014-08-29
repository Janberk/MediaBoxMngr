package de.canberkdemirkan.mediaboxmngr.data;

import java.util.ArrayList;

import de.canberkdemirkan.mediaboxmngr.content.ItemType;
import de.canberkdemirkan.mediaboxmngr.model.Book;
import de.canberkdemirkan.mediaboxmngr.model.Item;
import de.canberkdemirkan.mediaboxmngr.model.Movie;
import de.canberkdemirkan.mediaboxmngr.model.Music;

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
		int year = 1950;
		String content = "01 Lotus Intro\n" + "02 Army Of Me\n"
				+ "03 Red Hot Kinda Love\n"
				+ "04 Make The World Move feat. CeeLo Green\n"
				+ "05 Your Body\n" + "06 Let There Be Love\n"
				+ "07 Sing For Me\n" + "08 Blank Page\n" + "09 Cease Fire\n"
				+ "10 Around The World\n" + "11 Circles\n" + "12 Best Of Me\n"
				+ "13 Just A Fool with Blake Shelton\n";

		for (int i = 1; i <= 18; i++) {
			if (i < 7) {
				item = new Music();
				item.setType(ItemType.Album);
				item.setUser(mUser);
				item.setTitle("Test item Nr. " + i);
				if ((i % 2) != 0) {
					item.setFavorite(true);
				}
				item.setGenre("Pop");
				item.setCountry("USA");
				item.setYear(String.valueOf(year + i));
				item.setContent(content);
				((Music) item).setArtist("Christina Aguilera");
				((Music) item).setLabel("Livity Sound");
				((Music) item).setFormat("CD");
				((Music) item).setTitleCount("12");
			} else if (i < 13) {
				item = new Book();
				item.setType(ItemType.Book);
				item.setUser(mUser);
				item.setTitle("Test item Nr. " + i);
				if ((i % 2) != 0) {
					item.setFavorite(true);
				}
				item.setGenre("Novel");
				item.setCountry("GB");
				item.setYear(String.valueOf(year + i));
				content = "Jemand musste Josef K. verleumdet haben, "
						+ "denn ohne dass er etwas Böses getan hätte, wurde er eines Morgens verhaftet."
						+ " »Wie ein Hund!« sagte er, es war, als sollte die Scham ihn überleben. Als Gregor "
						+ "Samsa eines Morgens aus unruhigen Träumen erwachte, fand er sich in seinem Bett zu "
						+ "einem ungeheueren Ungeziefer verwandelt. Und es war ihnen wie eine Bestätigung ihrer "
						+ "neuen Träume und guten Absichten, als am Ziele ihrer Fahrt die Tochter als erste sich "
						+ "erhob und ihren jungen Körper dehnte. »Es ist ein eigentümlicher Apparat«, sagte der "
						+ "Offizier zu dem Forschungsreisenden und überblickte mit einem gewissermaßen bewundernden "
						+ "Blick den ihm doch wohlbekannten Apparat. Sie hätten noch ins Boot springen können, aber der "
						+ "Reisende hob ein schweres, geknotetes Tau vom Boden, drohte ihnen damit und hielt sie dadurch "
						+ "von dem Sprunge ab. In den letzten Jahrzehnten ist das Interesse an Hungerkünstlern sehr zurückgegangen."
						+ " Aber sie überwanden sich, umdrängten den Käfig und wollten sich gar nicht fortrühren.Jemand musste "
						+ "Josef K. verleumdet haben, denn ohne dass er etwas Böses getan hätte, wurde er eines Morgens verhaftet. "
						+ "»Wie ein Hund!« sagte er, es war, als sollte die Scham ihn überleben.";
				item.setContent(content);
				((Book) item).setAuthor("Daniel Defoe");
				((Book) item).setPublisher("Manesse");
				((Book) item).setEdition("2. Edition");
				((Book) item).setIsbn("978-3899961454");
			} else if (i < 19) {
				item = new Movie();
				item.setType(ItemType.Movie);
				item.setUser(mUser);
				item.setTitle("Test item Nr. " + i);
				if ((i % 2) != 0) {
					item.setFavorite(true);
				}
				item.setGenre("Action");
				item.setCountry("USA");
				item.setYear(String.valueOf(year + i));
				item.setContent(content);
				((Movie) item).setDirector("Steven Spielberg");
				((Movie) item).setLength("120");
				((Movie) item).setMusic("John Williams");
				((Movie) item).setCast("Tom Hanks\nHelen Hunt");
			}
			list.add(item);
		}

		return list;
	}

	public ArrayList<Item> getItemList() {
		return mItemList;
	}

}