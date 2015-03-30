package com.comp380.csun.comp380;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

/**
 * Created by gdfairclough on 3/17/15.
 */
public class BudgetPickerFragment extends DialogFragment implements View.OnClickListener{

    NumberPicker picker;
    String[] categoryStrings;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

        dismiss();
        int pickerValue = picker.getValue();

        String category;
        if(pickerValue == 0){

            category = "All Categories";

        }else{
            category = categoryStrings[pickerValue - 1];

        }

        ((ExpenseDisplayActivity)getActivity()).onDialogPositiveClick(this, category);
    }


    public interface BudgetPickerListener {

        public void onDialogPositiveClick(DialogFragment dialog, String category);

    }

    BudgetPickerListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //verify callback is set up in the host activity
        try {

            mListener = (BudgetPickerListener)activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement BudgetPickerListener");

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle("Choose Category");
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.budget_picker,container,false);
        picker = (NumberPicker) view.findViewById(R.id.budgetPicker);
        Button doneButton = (Button)view.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(this);

        //get the array of strings from the database
        DatabaseHandler dbHandler = new DatabaseHandler(getActivity(), null, null,1);

        categoryStrings = dbHandler.getCategoriesStrings();

        String pickerOptions[] = new String[categoryStrings.length + 1];
        pickerOptions[0] = "All Categories";
        int i = 1;

        for(String cat : categoryStrings){

            pickerOptions[i] = cat;

            i++;
        }


        picker.setMinValue(0);
        picker.setMaxValue(pickerOptions.length - 1);
        picker.setDisplayedValues(pickerOptions);


        return view;
    }



}
