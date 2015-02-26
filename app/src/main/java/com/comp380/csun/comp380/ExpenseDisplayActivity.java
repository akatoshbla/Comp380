package com.comp380.csun.comp380;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class ExpenseDisplayActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_display);
        populateViewer();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_expense_display, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        //TODO a link to the main page and add expense page could be placed here
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //TODO sort by date with headers
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
