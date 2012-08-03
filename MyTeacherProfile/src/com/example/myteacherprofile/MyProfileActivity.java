
package com.example.myteacherprofile;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myteacherprofile.adapters.SectionsPagerAdapter;
import com.example.myteacherprofile.listeners.MyTabListener;

public class MyProfileActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    
    private final static String  TAG = "MyProfileActivity";
    
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager            mViewPager;
    private MyTabListener        mTabListener;
    private ActionBar            actionBar;
    private int                  fragmentSelected;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        
        // Set up the action bar.
        actionBar = getActionBar();
        
        fragmentSelected = getIntent().getIntExtra(ProfileWidget.VALUE, 0);
        
        Log.d(TAG, "frag: " + fragmentSelected);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
        
        mTabListener = new MyTabListener(mViewPager);
        
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setTabListener(mTabListener).setIcon(mSectionsPagerAdapter.getFragmentIcon(i)));
        }
        
        actionBar.selectTab(actionBar.getTabAt(fragmentSelected));
        
    }
    
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
    
        getMenuInflater().inflate(R.menu.activity_my_profile, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    
        switch (item.getItemId()) {
            case R.id.menu_settings:
                showDialogSettings();
                return true;
        }
        return false;
    }
    
    private void showDialogSettings() {
    
        String[] items = { "Setting 1", "Setting 2" };
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Definições");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick( DialogInterface dialog, int item ) {
            
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    @Override
    public void onPageScrollStateChanged( int arg0 ) {
    
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onPageScrolled( int arg0, float arg1, int arg2 ) {
    
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onPageSelected( int arg0 ) {
    
        // TODO Auto-generated method stub
        actionBar.setSelectedNavigationItem(arg0);
    }
}
