package com.comp380.csun.comp380;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Fragment for choosing dates from a picker
 */
public class DatePickerFragment extends DialogFragment {

    DatePickerDialog.OnDateSetListener mOnDateSet;

    /**
     * Empty constructor
     */
    public DatePickerFragment(){

    }

    public void setCallBack(DatePickerDialog.OnDateSetListener onDate){
        mOnDateSet = onDate;

    }

    private int year,month,day;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        year = args.getInt("year");
        month = args.getInt("month");
        day= args.getInt("day");

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        return new DatePickerDialog(getActivity(),mOnDateSet, year, month, day);
    }
}
