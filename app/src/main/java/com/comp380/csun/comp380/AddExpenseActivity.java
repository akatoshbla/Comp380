package com.comp380.csun.comp380;

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

/**
 * Created by gdfairclough on 2/15/15.
 */

    public class AddExpenseActivity extends ActionBarActivity {

        AutoCompleteTextView categoryBox;
        EditText vendorBox;
        EditText costBox;
        EditText dateBox;



        @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);

            //delete database when needed
            this.deleteDatabase("expenseTracker.db");

            DatabaseHandler dbHandler = new DatabaseHandler(this, null, null,1);

            //manually insert hardcoded categories for testing purposees
            //dbHandler.testCategoryValues();



            setContentView(R.layout.activity_main);

            categoryBox = (AutoCompleteTextView) findViewById(R.id.category_input);
            vendorBox = (EditText) findViewById(R.id.vendor_input);
            costBox = (EditText) findViewById(R.id.amount_input);
            dateBox = (EditText) findViewById(R.id.date_input);
            populateViewer();


            //populate autocompletetextview
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(this,R.layout.autocomplete_dropdown_item,
                            dbHandler.getCategoriesStrings());

            categoryBox.setAdapter(adapter);

        }

        public void newExpense (View view){
            DatabaseHandler dbHandler = new DatabaseHandler(this, null, null,1);

            String category = categoryBox.getText().toString();
            String vendor = vendorBox.getText().toString();
            float cost = Float.parseFloat(costBox.getText().toString());
            String date  = dateBox.getText().toString();

            Expense expense = new Expense(category,vendor,cost,date);

            dbHandler.addExpense(expense);
            categoryBox.setText("");
            vendorBox.setText("");
            costBox.setText("");
            dateBox.setText("");
            populateViewer();
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
                Intent display = new Intent(this, DataDisplayActivity.class);
                //add to the database
                display.putExtra("Cost", costBox.getText().toString());
                display.putExtra("Category", categoryBox.getText().toString());
                display.putExtra("Vendor", vendorBox.getText().toString());
                display.putExtra("Date", dateBox.getText().toString());
                newExpense(view);
                startActivity(display);
            }
        }

        public void lookupExpense(View view){
            DatabaseHandler dbHandler = new DatabaseHandler(this,null,null,1);

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

        public void removeExpense(View view){
            DatabaseHandler dbHandler = new DatabaseHandler(this,null,null,1);

            //delete based on primary key from record display screen

        }

        public void populateViewer(){
            DatabaseHandler dbHandler = new DatabaseHandler(this,null,null,1);
            Cursor cursor = dbHandler.getAllRows();
            String[] columns = dbHandler.tableNames();
            int[] ids = new int[] {R.id.checkboxes, R.id.categoryViewer, R.id.vendorViewer, R.id.costViewer, R.id.dateViewer};
            SimpleCursorAdapter myCursorAdapter;
            myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.itemized_layout, cursor, columns, ids,0);
            ListView myList = (ListView) findViewById(R.id.listViewTasks);
            myList.setAdapter(myCursorAdapter);

        }
    }

