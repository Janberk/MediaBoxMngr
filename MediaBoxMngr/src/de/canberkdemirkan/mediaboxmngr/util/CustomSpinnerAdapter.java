package de.canberkdemirkan.mediaboxmngr.util;

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

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

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
		label.setText(getContent().get(position));

		ImageView icon = (ImageView) row
				.findViewById(R.id.iv_customSpinner_icon);
		icon.setImageResource(R.drawable.ic_launcher);

		return row;
	}

	public static ArrayList<String> getContent() {
		ArrayList<String> content = new ArrayList<>();
		ItemGenre[] allValues = ItemGenre.values();
		for (ItemGenre value : allValues) {
			content.add(value.toString());
		}
		return content;
	}

}