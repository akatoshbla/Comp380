package com.comp380.csun.comp380;

import android.app.DatePickerDialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;


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

        //dbHandler.testCategoryValues();
        //dbHandler.testVendors();
        //dbHandler.testExpenseValues();

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
                        (R.color.buttons));
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

        //check if category is in the database, if not, show a dialog for choosing the budget
        if(dbHandler.getCategoryID(categoryBox.getText().toString()) < 0){
            //show a dialog fragment for choosing a budget

        }

        //set cost equal to the current value in the editText field
        cost = Float.parseFloat(costBox.getText().toString());

        //set default date to today if nothing is in the editText field
        if (!dateBox.getText().toString().equals("")){
            String date  = dateBox.getText().toString();
            date = convertDateToYMD(date);
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
        else if(id == R.id.showDatePicker){

            showDatePicker();
        }
    }

    /**
     * display the date oicker for the user when the "Choose Date" button is pressed
     */
    private void showDatePicker(){

        DatePickerFragment date = new DatePickerFragment();

        //set up the current date in the dialog box (based on Los Angeles)
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("America/Los_Angeles"));
        Bundle args = new Bundle();
        args.putInt("year",calendar.get(Calendar.YEAR));
        args.putInt("month",calendar.get(Calendar.MONTH));
        args.putInt("day",calendar.get(Calendar.DAY_OF_MONTH));

        date.setArguments(args);

        //set call back to capt
        // ure the selected date
        date.setCallBack(onDate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


            //convert the date to a string
            String dateString = String.format("%02d",monthOfYear+1)+"-"
                    +String.format("%02d",dayOfMonth)+"-"+String.valueOf(year);

            //insert this date into the date box in month-day-year format
            dateBox.setText(dateString);
        }
    };

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