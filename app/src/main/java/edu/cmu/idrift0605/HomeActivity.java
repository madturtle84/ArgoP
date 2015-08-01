package edu.cmu.idrift0605;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import edu.cmu.idrift0605.View.FindPositionFragment;
import edu.cmu.idrift0605.View.MyLocationFragment;
import edu.cmu.idrift0605.View.SensorInfoFragment;

//import android.app.Fragment;

public class HomeActivity extends ActionBarActivity
        implements
            ActionBar.TabListener,
            SensorInfoFragment.OnFragmentInteractionListener,
            MyLocationFragment.OnFragmentInteractionListener,
            FindPositionFragment.OnFragmentInteractionListener
{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private MyLocationFragment mMyLocationFragment =null;
    private FindPositionFragment mFindPositionFragment=null;
    private SensorInfoFragment mSensorInfoFragment =null;
    static private HomeActivity sInstance;

    static public HomeActivity getSingleton(){
        if(sInstance==null) {
            Log.e("[getSingleton] ", "Call singleton before created");
        }
        return sInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /* Set up Fragments */
        sInstance = this;
        mSensorInfoFragment = new SensorInfoFragment();
        mMyLocationFragment = new MyLocationFragment();
        mFindPositionFragment = new FindPositionFragment();

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        /* Sensor info tab */
        //actionBar.addTab(actionBar.newTab().setText("My Sensor").setTabListener(this));
        /* Location tab */
        actionBar.addTab(actionBar.newTab().setText("Save Parking Position").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Find Parking Space").setTabListener(this));


    }

    public void onFragmentInteraction(String id){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        int selected = tab.getPosition();

        switch (selected){
            case 0:
                //mMyLocationFragment.onFocusChange(null, false);
                mMyLocationFragment.onFocusChange(null, true);
                break;
            case 1:
                //mMyLocationFragment.onFocusChange(null, true);
                mFindPositionFragment.onFocusChange(null,true);
                break;
        }
        mViewPager.setCurrentItem(selected);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    /**
     * A {@link Fragment Pager Adapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.i("[Adapter::getItem]","~~~~~~~~~ position = "+Integer.toString(position));
            switch (position) {
                case 0:
                //    return mSensorInfoFragment;
                    return mMyLocationFragment;
                case 1:
                 //   return mMyLocationFragment;
                    return  mFindPositionFragment;

            }
            return mSensorInfoFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
        //@Override
        public void onFragmentInteraction(Uri uri) {
        }
    }
}
