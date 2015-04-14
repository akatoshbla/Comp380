package com.comp380.csun.comp380;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by gdfairclough on 3/10/15.
 */
public class BudgetDialogFragment extends DialogFragment implements View.OnClickListener {

    private String title;
    private String category;
    private EditText limitBox;
    private Bundle args;
    private DatabaseHandler dbHandler;
    Button create;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dbHandler = new DatabaseHandler(getActivity(),null,null,1);

        //create a view for the dialog
        getDialog().setTitle(title);
        getDialog().setCanceledOnTouchOutside(true);
        View v = inflater.inflate(R.layout.dialog_budget_display,container,false);

        create = (Button)v.findViewById(R.id.createCat_button);
        create.setOnClickListener(this);

        //default create button to disabled
        create.setEnabled(false);
        create.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

        Button cancel = (Button)v.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(this);

        TextView categoryView = (TextView) v.findViewById(R.id.categoryTextView);
        limitBox = (EditText) v.findViewById(R.id.budgetLimitEdit);

        categoryView.setText(category);

        limitBox.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //enable the button when the text has been changed
                create.setBackgroundColor(getResources().getColor
                        (R.color.buttons));
                create.setEnabled(true);
                if(limitBox.getText().toString().equals("")){
                    create.setEnabled(false);
                    create.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });


        return v;

    }

    /**
     * Handle clicks from the budget dialog fragment
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.createCat_button:
                DatabaseHandler dbHandler = new DatabaseHandler(getActivity(),null,null,1);

                int limit;

                if(limitBox.getText().toString().equals("")){

                    limit = 10;

                }else{

                    limit = Integer.parseInt(limitBox.getText().toString());
                }


                dbHandler.addCategory(category,limit);

                Intent displayExpenses = new Intent(getActivity(), ExpenseDisplayActivity.class);
                //add to the database
                newExpense();

                //close the dialog box
                this.dismiss();


                startActivity(displayExpenses);
                getActivity().finish();

                break;
            case R.id.cancel_button:
                this.dismiss();
                break;
            default:
                break;

        }

    }


    /**
     * Use arguments passed in from the add expense screen to create the dialog box
     * @param args
     */
    public void setArguments(Bundle args)
    {
        this.title = args.getString("title");
        this.category = args.getString("category");
        this.args = args;

    }

    public void newExpense (){
        Expense expense;
        String category = args.getString("category");
        String vendor = args.getString("vendor");
        String date  = args.getString("date");
        Float cost = Float.parseFloat(args.getString("cost"));


        //check if category field is blank
        if(category.equals("")){
            category = "Uncategorized";
        }
        //check if vendor field is blank
        if(vendor.equals("")){
            vendor = "Unspecified";
        }

        //set default date to today if nothing is in the editText field
        if (!date.equals("")){
            date = convertDateToYMD(date);
            expense = new Expense(category,vendor,cost,date);
        }else{
            expense = new Expense(category,vendor,cost);
        }


        dbHandler.addExpense(expense);


    }

    public String convertDateToYMD(String dateString) {

        try{
            //convert to Date
            DateFormat df = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
            Date result = df.parse(dateString);

            //convert Date to a new String
            DateFormat convert = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            dateString = convert.format(result);

            return dateString;

        }catch(ParseException pe){

            pe.printStackTrace();
            return null;
        }

    }

}

