package com.myvetpath.myvetpath;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
//This class is the fragment that displays a calendar that the user can select a date from. It is used in CreateSubActivity for each of the three calendars
public class DatePickerFragment extends DialogFragment {
    static final int SAMPLE_COLLECTED_DATE = 1;
    static final int BIRTH_DATE = 1;
    static final int DEATH_DATE = 2;
    private int mChosenDate;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
    }


}
