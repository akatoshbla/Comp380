package com.comp380.csun.comp380;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
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



            DatabaseHandler dbHandler = new DatabaseHandler(this, null, null,1);

            //manually insert hardcoded categories for testing purposees
            dbHandler.testCategoryValues();
            dbHandler.testExpenseValues();
            dbHandler.deleteCategory(4);

        //manually insert hardcoded categories for testing purposees
        this.deleteDatabase("expenseTracker.db");
        dbHandler.testCategoryValues();
        dbHandler.testExpenseValues();
        dbHandler.testVendors();
        //dbHandler.deleteCategory(4);

            setContentView(R.layout.activity_main);

            categoryBox = (AutoCompleteTextView) findViewById(R.id.category_input);
            vendorBox = (EditText) findViewById(R.id.vendor_input);
            costBox = (EditText) findViewById(R.id.amount_input);
            dateBox = (EditText) findViewById(R.id.date_input);
            populateViewer();

        buttonSubmit = (Button) findViewById(R.id.submit_button);
        categoryBox = (AutoCompleteTextView) findViewById(R.id.category_input);
        vendorBox = (AutoCompleteTextView) findViewById(R.id.vendor_input);
        costBox = (EditText) findViewById(R.id.amount_input);
        dateBox = (EditText) findViewById(R.id.date_input);

            //populate autocompletetextview
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(this,R.layout.autocomplete_dropdown_item,
                            dbHandler.getCategoriesStrings());

            categoryBox.setAdapter(adapter);

        }

        public void newExpense (View view){
            DatabaseHandler dbHandler = new DatabaseHandler(this, null, null,1);
            Expense expense;
            String category;
            String vendor;
            Float cost;

                //enable the button when the text has been changed
                buttonSubmit.setBackgroundColor(getResources().getColor
                        (R.color.buttons));
                buttonSubmit.setEnabled(true);
                if(costBox.getText().toString().equals("")){
                    buttonSubmit.setEnabled(false);
                    buttonSubmit.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
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

        //populate autocompletetextview
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,R.layout.autocomplete_dropdown_item,
                        dbHandler.getCategoriesStrings());

            populateViewer();
        }

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

                //Intent display = new Intent(this, DataDisplayActivity.class);
                //add to the database
                //display.putExtra("Cost", costBox.getText().toString());
                //display.putExtra("Category", categoryBox.getText().toString());
                //display.putExtra("Vendor", vendorBox.getText().toString());
                //display.putExtra("Date", dateBox.getText().toString());

        //check if category field is blank
        if(categoryBox.getText().toString().equals("")){
            category = "Uncategorized";
        }else{
            category = categoryBox.getText().toString();
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

            if (expense != null){
                categoryBox.setText(String.valueOf(expense.getCategory()));
                vendorBox.setText(String.valueOf(expense.getVendor()));
                costBox.setText(String.valueOf(expense.getCost()));
                dateBox.setText(String.valueOf(expense.getDate()));
            }else{
                categoryBox.setText("No match found");
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

        public void removeExpense(View view){
            DatabaseHandler dbHandler = new DatabaseHandler(this,null,null,1);

            //delete based on primary key from record display screen

        }

        public void populateViewer(){
            DatabaseHandler dbHandler = new DatabaseHandler(this,null,null,1);
            Cursor cursor = dbHandler.getAllRows();
            String[] columns = dbHandler.tableNames();
            int[] ids = new int[] {R.id.categoryViewer, R.id.vendorViewer, R.id.costViewer, R.id.dateViewer};
            SimpleCursorAdapter myCursorAdapter;
            //can change what is displayed using the last parameter here
            myCursorAdapter = new SimpleCursorAdapter(this, R.layout.itemized_layout, cursor, columns, ids,0);
            ListView myList = (ListView) findViewById(R.id.listViewTasks);
            myList.setAdapter(myCursorAdapter);

        }
    }

