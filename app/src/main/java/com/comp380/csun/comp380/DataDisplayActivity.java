package com.comp380.csun.comp380;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;


public class DataDisplayActivity extends ActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        Intent callingIntent = getIntent();
        String tmp = callingIntent.getStringExtra("Ammount");
        TextView ammount = (TextView)findViewById(R.id.textView);
        TextView category = (TextView)findViewById(R.id.textView2);
        TextView vendor = (TextView)findViewById(R.id.textView3);
        ammount.setText(tmp);
        tmp = callingIntent.getStringExtra("Category");
        category.setText(tmp);
        tmp = callingIntent.getStringExtra("Vendor");
        vendor.setText(tmp);


    }
}
