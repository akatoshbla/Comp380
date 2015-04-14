package com.comp380.csun.comp380;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.ObjectStreamException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by gdfairclough on 3/24/15.
 */
public class GoalsSavingsDialog extends DialogFragment implements View.OnClickListener{

    EditText amountBox;
    Button addButton;
    Button cancelButton;
    Boolean endOfMonth = false;
    TextView savingsBox;
    Date lastAccessed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle("Add to Savings");
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.dialog_goals_savings,container,false);
        amountBox = (EditText) view.findViewById(R.id.amountEdit);
        addButton = (Button) view.findViewById(R.id.addSavingsButton);
        addButton.setOnClickListener(this);
        cancelButton = (Button) view.findViewById(R.id.savingsCancelButton);
        cancelButton.setOnClickListener(this);

        savingsBox = (TextView) view.findViewById(R.id.savingsText);
        //set text on dialog based on whether or not the endOfMonth flag is set
        if(endOfMonth){
            DatabaseHandler dbHandler = new DatabaseHandler(getActivity(), null, null,1);
            Date currentDate = new Date();

            //test for adding savings at the first of the month
            //Date currentDate = new GregorianCalendar(2015,03,01).getTime();

            //
            double expenseTotal = dbHandler.getExpensesForMonth(lastAccessed,currentDate);
            double budgetTotal = dbHandler.getTotalOfBudgets();

            double amountSaved = budgetTotal - expenseTotal;

            DecimalFormat decFormat = new DecimalFormat("#,###,##0.00");
            String amountSavedString = decFormat.format(amountSaved);

            savingsBox.setText("According to your budgets, you saved " + amountSavedString +
                    " last month. How much would you like to put towards your goal?");
        }

        //disable add button by default, until text inside is changed
        addButton.setEnabled(false);
        addButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));


        //add textWatcher to only allow adding when an amount has been entered
        amountBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    addButton.setBackgroundColor(getResources().getColor(R.color.buttons));
                    addButton.setEnabled(true);
                    if (amountBox.getText().toString().equals("")){
                        addButton.setEnabled(false);
                        addButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.addSavingsButton:
                dismiss();
                //call back to goalsActivity with the amount the user has entered
                double amount = Double.parseDouble(amountBox.getText().toString());

                //call onDialogPositive click with the amount (overloaded method)
                try {
                    ((GoalsActivity)getActivity()).onDialogPositiveClick(this, amount);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (ObjectStreamException e) {
                    e.printStackTrace();
                }

            case R.id.savingsCancelButton:
                dismiss();
                break;
            default:
                dismiss();

        }

    }

    //set up callback to Goals Activity

    public interface SavingsDialogListener {

        public void onDialogPositiveClick(DialogFragment dialog, double amount) throws FileNotFoundException, ObjectStreamException;

    }

    SavingsDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //verify callback is set up in the host activity

        try{
            mListener = (SavingsDialogListener)activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement SavingsDialogListener");
        }


    }

    //set flag for end of the month
    public void setEndOfMonth(Date lastAccessed){

        endOfMonth = true;
        this.lastAccessed = lastAccessed;

    }
}
