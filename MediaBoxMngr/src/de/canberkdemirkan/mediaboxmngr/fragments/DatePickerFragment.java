package de.canberkdemirkan.mediaboxmngr.fragments;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import de.canberkdemirkan.mediaboxmngr.R;

public class DatePickerFragment extends DialogFragment {

	public static final String EXTRA_ITEM_CREATION_DATE = "de.canberk.bignerdranchproject.item_creation_date";

	private Date mCreationDate;

	public static DatePickerFragment newInstance(Date creationDate) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_ITEM_CREATION_DATE, creationDate);

		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mCreationDate = (Date) getArguments().getSerializable(EXTRA_ITEM_CREATION_DATE);
		// Create a Calendar to get the year, month, and day
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mCreationDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_date_picker, null);

		DatePicker datePicker = (DatePicker) view
				.findViewById(R.id.dialog_datePicker);
		datePicker.init(year, month, day, new OnDateChangedListener() {
			public void onDateChanged(DatePicker view, int year, int month,
					int day) {
				// Translate year, month, day into a Date object using a
				// calendar
				mCreationDate = new GregorianCalendar(year, month, day).getTime();
				// Update argument to preserve selected value on rotation
				getArguments().putSerializable(EXTRA_ITEM_CREATION_DATE, mCreationDate);
			}
		});

		return new AlertDialog.Builder(getActivity())
				.setView(view)
				.setTitle(R.string.datePicker_header)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								sendResult(Activity.RESULT_OK);
							}
						}).create();
	}

	private void sendResult(int resultCode) {
		if (getTargetFragment() == null)
			return;
		Intent i = new Intent();
		i.putExtra(EXTRA_ITEM_CREATION_DATE, mCreationDate);
		getTargetFragment().onActivityResult(getTargetRequestCode(),
				resultCode, i);
	}

}