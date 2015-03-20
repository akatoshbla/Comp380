package com.comp380.csun.comp380;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by gdfairclough on 3/7/15.
 */
public class ExpenseDisplayActivity extends Activity implements View.OnClickListener, BudgetPickerFragment.BudgetPickerListener {


    //the projection of the column names
    String[] mColumns;
    DatabaseHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_display);

        dbHandler = new DatabaseHandler(this,null,null,1);
        //set up the database cursor
        Cursor cursor = dbHandler.getAllRows();

        mColumns = dbHandler.tableNames();

        //set up the adapter for the listView
        CustomAdapter adapter = new CustomAdapter(this,cursor);

        ListView listview = (ListView) findViewById(R.id.listViewTasks);
        listview.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.budgetEdit:
                showBudgetPicker();
                break;
            default:
                Log.d("Button Error", "Button doesn't match any known buttons");

        }

    }

    public void showBudgetPicker(){

        BudgetPickerFragment budgetPicker = new BudgetPickerFragment();

        //start the budget picker fragment

        budgetPicker.show(getFragmentManager(), "Budget Picker");

    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String category) {

        //display categories in the list view based on the chosen category

        TextView categoryText = (TextView)findViewById(R.id.budgetEdit);
        categoryText.setText(category);
        Cursor cursor;
        TextView noExpenses = (TextView)findViewById(R.id.noExpenses);

        if(dbHandler.isCategory(category))
        {
            noExpenses.setVisibility(View.GONE);
            cursor = dbHandler.getAllRowsForCategory(dbHandler.getCategoryID(category));
            if(!cursor.moveToFirst()){

                noExpenses.setVisibility(View.VISIBLE);
            }
        }else{
            noExpenses.setVisibility(View.GONE);
            cursor = dbHandler.getAllRows();
        }


        //set up the adapter for the listView
        CustomAdapter adapter = new CustomAdapter(this,cursor);

        ListView listview = (ListView) findViewById(R.id.listViewTasks);
        listview.setAdapter(adapter);


    }

    /**
     * Class for extending the adapter, allowing for dynamic headers in the list view
     */
    public class CustomAdapter extends BaseAdapter{

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

                view = inflater.inflate(R.layout.expense_item,null);
            }
            else {
                view = convertView;
            }

            //set expense category, vendor, cost and date
            TextView categoryView = (TextView) view.findViewById(R.id.categoryViewer);
            TextView vendorView = (TextView) view.findViewById(R.id.vendorViewer);
            TextView costView = (TextView) view.findViewById(R.id.costViewer);
            TextView dateView = (TextView) view.findViewById(R.id.date_separator);

            String category = mCursor.getString(mCursor.getColumnIndex(mColumns[0]));
            String vendor = mCursor.getString(mCursor.getColumnIndex(mColumns[1]));
            String cost = "$" + mCursor.getString(mCursor.getColumnIndex(mColumns[2]));
            String date = mCursor.getString(mCursor.getColumnIndex(mColumns[3]));

            String convertedDate = convertDateToMDY(date);

            categoryView.setText(category);
            vendorView.setText(vendor);
            costView.setText(cost);
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
