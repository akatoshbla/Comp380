package com.comp380.csun.comp380;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by David on 3/8/2015.
 */

// TODO: Need to label methods and the first fragment is being drawn twice on startup
public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private ViewPager myPager;
    private SlidingTabLayout myTabs;
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

        // Init myPager and myTabs
        myPager = (ViewPager) findViewById(R.id.pager);
        myPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        myTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        myTabs.setViewPager(myPager);

    }

    // Refreshes Tabs and Fragments when coming from another activity
    public void onResume() {
        super.onResume();
        myPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        myTabs.setViewPager(myPager);
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

        // Switch to addExpenseActivity if the plus button is pushed
        if (id == R.id.action_add) {
            startActivity(new Intent(this, AddExpenseActivity.class));
        }

        // Switch to GoalsActivity if the goals button is pushed
        if (id == R.id.goals) {
            startActivity(new Intent(this, GoalsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    // Custom Page Adapter
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


    // Custom fragment
    public static class MyFragment extends Fragment {

        private TextView textViewFraction;
        private ProgressBar progressBar;
        private TextView textProgressBar;
        private TextView textViewTopOne;
        private TextView textViewTopOneNumber;
        private TextView textViewTopTwo;
        private TextView textViewTopTwoNumber;
        private TextView textViewTopThree;
        private TextView textViewTopThreeNumber;
        private TextView textViewTopFour;
        private TextView textViewTopFourNumber;
        private TextView textViewTopFive;
        private TextView textViewTopFiveNumber;
        private DatabaseHandler db;
        private String[] categories;
        private BudgetReport budgetReport;
        private Button details;
        private MyPieChart myPieChart;
        String tab;

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
                // Declaring variables
                db = new DatabaseHandler(this.getActivity(), null, null, 1);
                categories = db.getCategoriesStrings();

                // Grabs resources to dynamically change the color of the progress bar
                Resources resources = getResources();

                // Inflater for linking the fragment layout xml to a view
                View layout = inflater.inflate(R.layout.fragment_layout, container, false);

                // Linking the declared variables to xml objects
                textViewFraction = (TextView) layout.findViewById(R.id.position);
                progressBar = (ProgressBar) layout.findViewById(R.id.pBar);
                textProgressBar = (TextView) layout.findViewById(R.id.textPBar);
                textViewTopOne = (TextView) layout.findViewById(R.id.numOne);
                textViewTopOneNumber = (TextView) layout.findViewById(R.id.numOneNumber);
                textViewTopTwo = (TextView) layout.findViewById(R.id.numTwo);
                textViewTopTwoNumber = (TextView) layout.findViewById(R.id.numTwoNumber);
                textViewTopThree = (TextView) layout.findViewById(R.id.numThree);
                textViewTopThreeNumber = (TextView) layout.findViewById(R.id.numThreeNumber);
                textViewTopFour = (TextView) layout.findViewById(R.id.numFour);
                textViewTopFourNumber = (TextView) layout.findViewById(R.id.numFourNumber);
                textViewTopFive = (TextView) layout.findViewById(R.id.numFive);
                textViewTopFiveNumber = (TextView) layout.findViewById(R.id.numFiveNumber);

                // Sets the TypeFace (font) so that java string format works for
                // toString prints.
                //textViewFraction.setTypeface(Typeface.MONOSPACE);
                //textViewTopOne.setTypeface(Typeface.MONOSPACE);
                //textViewTopTwo.setTypeface(Typeface.MONOSPACE);
                //textViewTopThree.setTypeface(Typeface.MONOSPACE);
                //textViewTopFour.setTypeface(Typeface.MONOSPACE);
                //textViewTopFive.setTypeface(Typeface.MONOSPACE);

                // Linking details button for fragment
                details = (Button) layout.findViewById(R.id.details);

                // Creates a bundle with arguments from myFragment bundle
                Bundle bundle = getArguments();
                if (bundle != null) {

                    // Creates a BudgetReport object based on current tab (fix double pie created here)
                    if (bundle.getInt("position") == 0) {
                        budgetReport = new BudgetReport(db, "All");
                        tab = "All";
                    } else {
                        budgetReport = new BudgetReport(db, categories[bundle.getInt("position") - 1]);
                        tab = categories[bundle.getInt("position") - 1];
                    }

                    // Sets the top viewer in the Main Screen Fragment
                    DecimalFormat cost = new DecimalFormat("$###,###,###.00");
                    textViewFraction.setText(cost.format(budgetReport.getBudgetCurrent()) + " / " +
                            cost.format(budgetReport.getBudgetMax()));

                    // Setup of the progress bar (color, percent, progress)
                    progressBar.setProgress(budgetReport.getProgressBar());
                    progressBar.setProgressDrawable(resources.getDrawable(budgetReport.getProgressBarColor()));
                    textProgressBar.setText(budgetReport.getProgressBar() + "%");

/*                    // Testing white color percent over the progress color
                    if (budgetReport.getProgressBar() < 1000) {
                        textProgressBar.setTextColor(getResources().getColor(R.color.white));
                    }*/

                    // Sets TextViews for top five expenses
                    textViewTopOne.setText(budgetReport.getTopFiveExpenses(0).getTransactionName());
                    textViewTopOneNumber.setText(budgetReport.getTopFiveExpenses(0).getTransactionCostString());
                    textViewTopTwo.setText(budgetReport.getTopFiveExpenses(1).getTransactionName());
                    textViewTopTwoNumber.setText(budgetReport.getTopFiveExpenses(1).getTransactionCostString());
                    textViewTopThree.setText(budgetReport.getTopFiveExpenses(2).getTransactionName());
                    textViewTopThreeNumber.setText(budgetReport.getTopFiveExpenses(2).getTransactionCostString());
                    textViewTopFour.setText(budgetReport.getTopFiveExpenses(3).getTransactionName());
                    textViewTopFourNumber.setText(budgetReport.getTopFiveExpenses(3).getTransactionCostString());
                    textViewTopFive.setText(budgetReport.getTopFiveExpenses(4).getTransactionName());
                    textViewTopFiveNumber.setText(budgetReport.getTopFiveExpenses(4).getTransactionCostString());
                    String[] names = {budgetReport.getExpenseNames(0).getTransactionName(), budgetReport.getExpenseNames(1).getTransactionName(),
                            budgetReport.getExpenseNames(2).getTransactionName(), budgetReport.getExpenseNames(3).getTransactionName(),
                            budgetReport.getExpenseNames(4).getTransactionName()};

                    // Sets the pie chart up
                    RelativeLayout linearLayout = (RelativeLayout) layout.findViewById(R.id.pieChart);
                    MyPieChart myPieChart = new MyPieChart(this.getActivity(), budgetReport.getMyPieChartData(), names);
                    linearLayout.addView(myPieChart);

                    // Sets the details button up with a custom intent to pass
                    details.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), ExpenseDisplayActivity.class);
                            intent.putExtra("key", tab);
                            startActivity(intent);
                        }
                    });
                }

                db.close();
                return layout;
            }
    }
}
