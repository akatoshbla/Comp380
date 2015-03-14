package com.comp380.csun.comp380;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by David on 3/8/2015.
 */

// TODO: Need to label methods
public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init DataBaseHandler
        db = new DatabaseHandler(this, null, null, 1);

        // Init ToolBar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        // Init mPager and mTabs
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setViewPager(mPager);
    }

    // TODO: Need to repopulate tabs on insert of new category
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        // Switch to addexpenseactivity if the plus button is pushed
        if (id == R.id.action_add) {
            startActivity(new Intent(this, AddExpenseActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<String> tabs;
        private String[] temp;

        // This method populates the Tabs and Fragments
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = new ArrayList<>();
            if (tabs.size() == 0) {
                tabs.add("All");
            }

            if (db.getCategoriesStrings() != null) {
                temp = new String[db.getCategoriesStrings().length];
                temp = db.getCategoriesStrings();
                for (int i = 0; i < db.getCategoriesStrings().length; i++) {
                    tabs.add(temp[i]);
                }
            }
        }

        @Override
        public Fragment getItem(int position) {
            MyFragment myFragment = MyFragment.getInstance(position);
            return myFragment;
        }

        public CharSequence getPageTitle(int position) {
            return tabs.get(position);
        }

        @Override
        public int getCount() {
            return tabs.size();
        }
    }

    public static class MyFragment extends Fragment {

        //private TextView textViewStatus;
        private TextView textViewFraction;
        private ProgressBar progressBar;
        private TextView textViewTopOne;
        private TextView textViewTopTwo;
        private TextView textViewTopThree;
        private TextView textViewTopFour;
        private TextView textViewtopFive;
        private DatabaseHandler db;
        private String[] categories;
        private BudgetReport budgetReport;

        public static MyFragment getInstance(int position) {
            MyFragment myFragment = new MyFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            myFragment.setArguments(args);
            return myFragment;
        }

        // Edit Fragments here
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            db = new DatabaseHandler(this.getActivity(), null, null, 1);
            categories = db.getCategoriesStrings();
            Resources resources = getResources();
            View layout = inflater.inflate(R.layout.fragment_layout, container, false);

            textViewFraction = (TextView) layout.findViewById(R.id.position);
            progressBar = (ProgressBar) layout.findViewById(R.id.pBar);
            textViewTopOne = (TextView) layout.findViewById(R.id.numOne);
            textViewTopTwo = (TextView) layout.findViewById(R.id.numTwo);
            textViewTopThree = (TextView) layout.findViewById(R.id.numThree);
            textViewTopFour = (TextView) layout.findViewById(R.id.numFour);
            textViewtopFive = (TextView) layout.findViewById(R.id.numFive);

            Bundle bundle = getArguments();
            if (bundle != null) {
                if (bundle.getInt("position") == 0) {
                    budgetReport = new BudgetReport(db, "All");
                    textViewFraction.setText(budgetReport.getBudgetCurrent() + "/" + budgetReport.getBudgetMax());
                    progressBar.setProgress(budgetReport.getProgressBar());
                    progressBar.setProgressDrawable(resources.getDrawable(budgetReport.getStatus()));
                    textViewTopOne.setText(budgetReport.getTopFiveCategories(0));
                    textViewTopTwo.setText(budgetReport.getTopFiveCategories(1));
                    textViewTopThree.setText(budgetReport.getTopFiveCategories(2));
                    textViewTopFour.setText(budgetReport.getTopFiveCategories(3));
                    textViewtopFive.setText(budgetReport.getTopFiveCategories(4));
                }
                else {
                    budgetReport = new BudgetReport(db, categories[bundle.getInt("position") - 1]);
                    textViewFraction.setText(budgetReport.getBudgetCurrent() + "/" + budgetReport.getBudgetMax());
                    progressBar.setProgress(budgetReport.getProgressBar());
                    progressBar.setProgressDrawable(resources.getDrawable(budgetReport.getStatus()));
                    textViewTopOne.setText(budgetReport.getTopFiveVendors(0));
                    textViewTopTwo.setText(budgetReport.getTopFiveVendors(1));
                    textViewTopThree.setText(budgetReport.getTopFiveVendors(2));
                    textViewTopFour.setText(budgetReport.getTopFiveVendors(3));
                    textViewtopFive.setText(budgetReport.getTopFiveVendors(4));
                }
            }
            return layout;
        }
    }
}
