package com.comp380.csun.comp380;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
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
        else if(id == R.id.action_add){
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
    public void onButtonClick(View view){
        EditText amount = (EditText) findViewById(R.id.amount_input);
        EditText category = (EditText) findViewById(R.id.category_input);
        EditText vendor = (EditText) findViewById(R.id.vendor_input);
        int id = view.getId();

        if(id == R.id.reset_button){
            amount.setText(null);
            category.setText(null);
            vendor.setText(null);
        }
        else if(id == R.id.submit_button){
            Intent display = new Intent(this, DataDisplayActivity.class);
            display.putExtra("Amount", amount.getText().toString());
            display.putExtra("Category", category.getText().toString());
            display.putExtra("Vendor", vendor.getText().toString());
            startActivity(display);
        }
    }
}
