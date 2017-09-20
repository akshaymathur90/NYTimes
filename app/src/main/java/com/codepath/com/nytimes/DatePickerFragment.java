package com.codepath.com.nytimes;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by akshaymathur on 8/11/17.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public final static String TAG = "DatePickerFragment";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int year = 2000;
        int month = 0;
        int day = 1;

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user

        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra("day",day);
        i.putExtra("month",month);
        i.putExtra("year",year);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
    }
}