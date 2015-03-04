package com.comp380.csun.comp380;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by gdfairclough on 2/15/15.
 */

public class AddExpenseActivity extends ActionBarActivity {

    private AutoCompleteTextView categoryBox;
    private AutoCompleteTextView vendorBox;
    private EditText costBox;
    private EditText dateBox;
    private DatabaseHandler dbHandler;
    private Button buttonSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);



        dbHandler = new DatabaseHandler(this, null, null,1);



        //manually insert hardcoded categories for testing purposees
        this.deleteDatabase("expenseTracker.db");
        dbHandler.testCategoryValues();
        dbHandler.testExpenseValues();
        dbHandler.testVendors();
        //dbHandler.deleteCategory(4);


        setContentView(R.layout.activity_addexpense);

        buttonSubmit = (Button) findViewById(R.id.submit_button);
        categoryBox = (AutoCompleteTextView) findViewById(R.id.category_input);
        vendorBox = (AutoCompleteTextView) findViewById(R.id.vendor_input);
        costBox = (EditText) findViewById(R.id.amount_input);
        dateBox = (EditText) findViewById(R.id.date_input);

        //disable the button if the text has not been changed
        buttonSubmit.setEnabled(false);
        buttonSubmit.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

        //add text changed listener to the cost field;
        costBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //enable the button when the text has been changed
                buttonSubmit.setBackgroundColor(getResources().getColor
                        (R.color.coin_gold));
                buttonSubmit.setEnabled(true);
                if(costBox.getText().toString().equals("")){
                    buttonSubmit.setEnabled(false);
                    buttonSubmit.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //populate autocompletetextview for category
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,R.layout.autocomplete_dropdown_item,
                        dbHandler.getCategoriesStrings());

        categoryBox.setAdapter(adapter);

        //populate autocompletetextview for vendor
        adapter = new ArrayAdapter<String>(this,R.layout.autocomplete_dropdown_item,
                dbHandler.getVendorStrings());

        vendorBox.setAdapter(adapter);

    }

    public void newExpense (View view){
        Expense expense;
        String category;
        String vendor;
        Float cost;


        //check if category field is blank
        if(categoryBox.getText().toString().equals("")){
            category = "Uncategorized";
        }else{
            category = categoryBox.getText().toString();
        }
        //check if vendor field is blank
        if(vendorBox.getText().toString().equals("")){
            vendor = "Unspecified";
        }else{
            vendor = vendorBox.getText().toString();
        }

        //set cost equal to the current value in the editText field
        cost = Float.parseFloat(costBox.getText().toString());

        //set default date to today if nothing is in the editText field
        if (!dateBox.getText().toString().equals("")){
            String date  = dateBox.getText().toString();
            expense = new Expense(category,vendor,cost,date);
        }else{
            expense = new Expense(category,vendor,cost);
        }


        dbHandler.addExpense(expense);
        categoryBox.setText("");
        vendorBox.setText("");
        costBox.setText("");
        dateBox.setText("");

    }




    public void onButtonClick(View view){

        int id = view.getId();

        if(id == R.id.reset_button){
            categoryBox.setText(null);
            vendorBox.setText(null);
            dateBox.setText(null);
            costBox.setText(null);
        }
        else if(id == R.id.submit_button){

            //create intent for ExpenseDisplayActivity
            Intent displayExpenses = new Intent(this, ExpenseDisplayActivity.class);

            //add to the database
            newExpense(view);

            startActivity(displayExpenses);
        }
    }

    public void lookupExpense(View view){

        Expense expense = dbHandler.findExpense(categoryBox.getText().toString());

        if (expense != null){
            categoryBox.setText(String.valueOf(expense.getCategory()));
            vendorBox.setText(String.valueOf(expense.getVendor()));
            costBox.setText(String.valueOf(expense.getCost()));
            dateBox.setText(String.valueOf(expense.getDate()));
        }else{
            categoryBox.setText("No match found");
        }

    }




}

