package com.comp380.csun.comp380;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

/**
 * Created by David on 4/27/2015.
 */
public class IncomePickerFragment extends DialogFragment implements View.OnClickListener {

    NumberPicker picker;
    String[] monthStrings;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

        dismiss();
        int pickerValue = picker.getValue();

        String month;
        if(pickerValue == 0){

            month = "All Months";

        }
        else if (pickerValue == 1) {
            month = "Current Month";
        }

        else{
            month = monthStrings[pickerValue - 2];

        }

        ((IncomeActivity)getActivity()).onDialogPositiveClick(this, month);
    }


    public interface IncomePickerListener {

        public void onDialogPositiveClick(DialogFragment dialog, String category);

    }

    IncomePickerListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //verify callback is set up in the host activity
        try {

            mListener = (IncomePickerListener)activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement IncomePickerListener");

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle("Choose Month");
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.budget_picker,container,false);
        picker = (NumberPicker) view.findViewById(R.id.budgetPicker);
        Button doneButton = (Button)view.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(this);

        //get the array of strings from the database
        DatabaseHandler dbHandler = new DatabaseHandler(getActivity(), null, null,1);

        monthStrings = dbHandler.getIncomeMonths();

        String pickerOptions[] = new String[monthStrings.length + 2];
        pickerOptions[0] = "All Months";
        pickerOptions[1] = "Current Month";
        int i = 2;

        for(String mon : monthStrings){

            pickerOptions[i] = mon;

            i++;
        }


        picker.setMinValue(0);
        picker.setMaxValue(pickerOptions.length - 1); // maybe -2?
        picker.setDisplayedValues(pickerOptions);


        return view;
    }
}
