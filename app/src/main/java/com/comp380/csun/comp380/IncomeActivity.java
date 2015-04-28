package com.comp380.csun.comp380;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class IncomeActivity extends ActionBarActivity implements View.OnClickListener, IncomePickerFragment.IncomePickerListener {

    //the projection of the column names
    String[] mColumns;
    DatabaseHandler dbHandler;
    TextView noIncomes;
    TextView monthText;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        noIncomes = (TextView) findViewById(R.id.noIncomes);
        monthText = (TextView) findViewById(R.id.monthEdit);
        setSupportActionBar(toolbar);

        dbHandler = new DatabaseHandler(this, null, null, 1);

        setPopulateViewer();
    }

    protected void onResume() {
        super.onResume();

        dbHandler = new DatabaseHandler(this, null, null, 1);
        setPopulateViewer();
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.monthEdit:
                showMonthPicker();
                break;
            case R.id.addIncomeButton:
                startActivity(new Intent(this, AddIncomeActivity.class));
                finish();
                break;
            default:
                Log.d("Button Error", "Button doesn't match any known buttons");

        }

    }

    public void showMonthPicker(){

        IncomePickerFragment monthPicker = new IncomePickerFragment();

        //start the budget picker fragment

        monthPicker.show(getFragmentManager(), "Month Picker");

    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String month) {

        //display income in the list view based on the chosen month

        TextView monthText = (TextView)findViewById(R.id.monthEdit);
        monthText.setText(month);
        Intent intent = getIntent();
        intent.putExtra("key", month);
        setPopulateViewer();
    }

    public void setPopulateViewer() {
        Cursor cursor;
        Intent intent = getIntent();
        String sortType = intent.getStringExtra("key");

        if (!sortType.equals("All Months") && !sortType.equals("Current Month")) {
            noIncomes.setVisibility(View.GONE);
            cursor = dbHandler.getAllRowsForMonth(sortType);

            if (!cursor.moveToFirst()) {
                noIncomes.setVisibility(View.VISIBLE);
            }
            monthText.setText(sortType);
        }
        else if (sortType.equals("Current Month")) {
            noIncomes.setVisibility(View.GONE);
            cursor = dbHandler.getAllRowsForMonth(sortType);
        }
        else {
            noIncomes.setVisibility(View.GONE);
            cursor = dbHandler.getAllIncomeRows();
            monthText.setText("All Months");
        }

        populateViewer(cursor);
        calculateTotalIncome(sortType);
    }

    private void populateViewer(Cursor cursor) {
        // Populate listview
        ListView listview = (ListView) findViewById(R.id.incomeListViewTasks);
        mColumns = dbHandler.incomeColumnNames();

        //set up the adapter for the listView
        CustomAdapter adapter = new CustomAdapter(this,cursor);
        listview.setAdapter(adapter);
        dbHandler.close();
    }

    public void calculateTotalIncome(String sort) {
        DecimalFormat decimalFormat = new DecimalFormat("$###,###,##0.00");
        TextView totalIncome = (TextView) findViewById(R.id.totalIncome);
        totalIncome.setText("Total Income: " + decimalFormat.format(dbHandler.getTotalOfIncome(sort)));
    }

    /**
     * Class for extending the adapter, allowing for dynamic headers in the list view
     */
    public class CustomAdapter extends BaseAdapter {

        private Context mContext;
        private Cursor mCursor;

        //row state when needs to show separator
        private static final int SEPARATOR_STATE= 1;

        //row state when does not need to show separator
        private static final int REGULAR_STATE = 2;

        //cache row states based on position
        private int[] mRowStates;

        public CustomAdapter(Context context, Cursor cursor){
            mContext = context;
            mCursor = cursor;
            mRowStates = new int[getCount()];
        }

        @Override
        public int getCount(){
            return mCursor.getCount();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            boolean showSeparator = false;

            mCursor.moveToPosition(position);

            if(convertView == null) {

                LayoutInflater inflater =
                        (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = inflater.inflate(R.layout.income_item,null);
            }
            else {
                view = convertView;
            }

            //set income source, amount, and date
            TextView sourceView = (TextView) view.findViewById(R.id.sourceViewer);
            TextView amountView = (TextView) view.findViewById(R.id.amountViewer);
            TextView dateView = (TextView) view.findViewById(R.id.income_date_separator);

            String source = mCursor.getString(mCursor.getColumnIndex(mColumns[0]));
            Float amount = mCursor.getFloat(mCursor.getColumnIndex(mColumns[1]));
            String date = mCursor.getString(mCursor.getColumnIndex(mColumns[2]));

            String convertedDate = convertDateToMDY(date);

            sourceView.setText(source);
            DecimalFormat decimalFormat = new DecimalFormat("$###,###,##0.00");
            amountView.setText(decimalFormat.format(amount));
            dateView.setText(convertedDate);

            //show separator
            switch (mRowStates[position]){

                case SEPARATOR_STATE:
                    showSeparator=true;
                    break;
                case REGULAR_STATE:
                    showSeparator=false;
                    break;
                default:
                    //first run through, position will be zero
                    if(position == 0){
                        showSeparator = true;
                    }
                    else{
                        //get the previous position of the cursor
                        mCursor.moveToPosition(position-1);

                        String previousDate = mCursor.getString(mCursor.getColumnIndex(mColumns[3]));
                        if(!previousDate.equals(date)){
                            //dates are different, so show the seperator
                            showSeparator = true;
                        }

                        //reset the cursor to its correct position
                        mCursor.moveToPosition(position);


                    }

                    // Cache it with this ternary operator
                    mRowStates[position] = showSeparator ? SEPARATOR_STATE : REGULAR_STATE;

                    break;
            }


            if(showSeparator){
                //show the date of the record at the current cursor position

                dateView.setVisibility(View.VISIBLE);

            }else{
                dateView.setVisibility(View.GONE);
            }

            return view;

        }
    }

    public String convertDateToMDY(String dateString) {

        try{
            //convert to Date
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date result = df.parse(dateString);

            //convert Date to a new String
            DateFormat convert = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
            dateString = convert.format(result);

            return dateString;

        }catch(ParseException pe){

            pe.printStackTrace();
            return null;
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

        // Switch to addExpenseActivity if the plus button is pushed
        if (id == R.id.action_add) {
            startActivity(new Intent(this, AddExpenseActivity.class));
            finish();
        }

        // Switch to GoalsActivity if the goals button is pushed
        if (id == R.id.goals) {
            startActivity(new Intent(this, GoalsActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
