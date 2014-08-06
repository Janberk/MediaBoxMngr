package de.canberkdemirkan.mediaboxmngr.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.canberkdemirkan.mediaboxmngr.R;
import de.canberkdemirkan.mediaboxmngr.content.ItemGenre;
import de.canberkdemirkan.mediaboxmngr.content.ItemType;
import de.canberkdemirkan.mediaboxmngr.util.UtilMethods;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

	public enum SpinnerTag {

		TypeSpinner, GenreSpinner;

	}

	private static ArrayList<String> sContent = null;

	public CustomSpinnerAdapter(Context context, int resource,
			int textViewResourceId, List<String> objects) {
		super(context, resource, textViewResourceId, objects);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.custom_spinner, parent, false);
		TextView label = (TextView) row
				.findViewById(R.id.tv_customSpinner_label);
		label.setText(sContent.get(position));

		ImageView icon = (ImageView) row
				.findViewById(R.id.iv_customSpinner_icon);
		switch (position) {
		case 0:
			UtilMethods.setCustomIconToTypeOfMedia(icon, ItemType.Album,
					UtilMethods.ICON_LIGHT_TAG);
			break;
		case 1:
			UtilMethods.setCustomIconToTypeOfMedia(icon, ItemType.Book,
					UtilMethods.ICON_LIGHT_TAG);
			break;
		case 2:
			UtilMethods.setCustomIconToTypeOfMedia(icon, ItemType.Movie,
					UtilMethods.ICON_LIGHT_TAG);
			break;

		default:
			break;
		}

		return row;
	}

	public static ArrayList<String> getContent(SpinnerTag tag) {
		ArrayList<String> content = new ArrayList<>();
		switch (tag) {
		case TypeSpinner:
			ItemType[] typeValues = ItemType.values();
			for (ItemType value : typeValues) {
				content.add(value.toString());
			}
			break;
		case GenreSpinner:
			ItemGenre[] genreValues = ItemGenre.values();
			for (ItemGenre value : genreValues) {
				content.add(value.toString());
			}
			break;

		default:
			break;
		}
		sContent = content;
		return content;
	}

}