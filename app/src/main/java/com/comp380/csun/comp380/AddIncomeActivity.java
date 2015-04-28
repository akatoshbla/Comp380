package com.comp380.csun.comp380;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
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
 * Created by David on 4/25/2015.
 */
public class AddIncomeActivity extends ActionBarActivity{


    private AutoCompleteTextView sourceBox;
    private EditText amountBox;
    private EditText dateBox;
    private DatabaseHandler dbHandler;
    private Button buttonSubmit;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        dbHandler = new DatabaseHandler(this, null, null,1);

        setContentView(R.layout.activity_addincome);

        // Init ToolBar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        buttonSubmit = (Button) findViewById(R.id.submit_button);
        sourceBox = (AutoCompleteTextView) findViewById(R.id.source_input);
        amountBox = (EditText) findViewById(R.id.amount_input);
        dateBox = (EditText) findViewById(R.id.date_input);

        //disable the button if the text has not been changed
        buttonSubmit.setEnabled(false);
        buttonSubmit.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

        //add text changed listener to the cost field;
        amountBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //enable the button when the text has been changed
                buttonSubmit.setBackgroundColor(getResources().getColor
                        (R.color.buttons));
                buttonSubmit.setEnabled(true);
                if(amountBox.getText().toString().equals("")){
                    buttonSubmit.setEnabled(false);
                    buttonSubmit.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //populate autocompletetextview for sources
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,R.layout.autocomplete_dropdown_item,
                        dbHandler.getIncomeStrings());

        if (adapter.getCount() > 0) {
            sourceBox.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_income, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Switch to AddExpenseActivity if Add button is pushed
        if (id == R.id.action_add) {
            startActivity(new Intent(this, AddExpenseActivity.class));
        }

        // Switch to GoalsActivity if the goals button is pushed
        if (id == R.id.goals) {
            startActivity(new Intent(this, GoalsActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void newIncome (View view){
        Income income;
        String source;
        Float amount;

        //check if source field is blank
        if(sourceBox.getText().toString().equals("")){
            source = "Unspecified";
        }else{
            source = sourceBox.getText().toString();
        }

        //set cost equal to the current value in the editText field
        amount = Float.parseFloat(amountBox.getText().toString());

        //set default date to today if nothing is in the editText field
        if (!dateBox.getText().toString().equals("")){
            String date  = dateBox.getText().toString();
            date = convertDateToYMD(date);
            income = new Income(source, amount, date);
        }else{
            income = new Income(source, amount);
        }


        dbHandler.addIncome(income);
        sourceBox.setText("");
        amountBox.setText("");
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
            sourceBox.setText(null);
            amountBox.setText(null);
            dateBox.setText(null);
        }
        else if(id == R.id.submit_button){

            //create intent for ExpenseDisplayActivity
            Intent displayIncomes = new Intent(this, IncomeActivity.class);

            //add to the database
            newIncome(view);

            // Add sortType key
            displayIncomes.putExtra("key", "All Months");

            startActivity(displayIncomes);
            finish();
        }
        else if(id == R.id.showDatePickerIncome){

            showDatePicker();
        }
    }

    /**
     * display the date picker for the user when the "Choose Date" button is pressed
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
}
