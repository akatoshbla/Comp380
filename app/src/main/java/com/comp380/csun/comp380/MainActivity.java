package com.comp380.csun.comp380;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by David on 3/8/2015.
 */
public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init ToolBar
        toolbar= (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        // Init mPager and mTabs
        mPager= (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs= (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setViewPager(mPager);


    }

    // Need to repopulate tabs on insert of new category
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

        private DatabaseHandler db;
        private ArrayList<String> tabs;
        String[] temp;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = new ArrayList<>();
            if (tabs.size() == 0) {
                tabs.add("All");
                tabs.add("tab2");
                tabs.add("tab3");
                tabs.add("tab4");
                tabs.add("tab5");
                tabs.add("tab6");
                tabs.add("tab7");
                tabs.add("tab8");
                tabs.add("tab9");
                tabs.add("tab10");
            }
            try {
                temp = new String[db.getCategoriesStrings().length];
                temp = db.getCategoriesStrings();
                Toast.makeText(getApplicationContext(), "Working! " +temp[0],
                        Toast.LENGTH_LONG).show();
                for (int i = 0; i < db.getCategoriesStrings().length;i++) {
                tabs.add(temp[i]);
                }
            } catch(NullPointerException e) {
                Toast.makeText(getApplicationContext(), "DataBase has no Categories",
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public Fragment getItem(int position) {
            MyFragment myFragment=MyFragment.getInstance(position);
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
        private TextView textView;
        public static MyFragment getInstance(int position)  {
            MyFragment myFragment=new MyFragment();
            Bundle args=new Bundle();
            args.putInt("position", position);
            myFragment.setArguments(args);
            return myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout=inflater.inflate(R.layout.fragment_layout, container, false);
            textView= (TextView) layout.findViewById(R.id.position);
            Bundle bundle=getArguments();
            if(bundle!=null) {
                textView.setText("The page selected is "+bundle.getInt("position"));
            }
            return layout;
        }
    }
}
