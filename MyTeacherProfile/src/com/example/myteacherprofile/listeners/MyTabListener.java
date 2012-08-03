
package com.example.myteacherprofile.listeners;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

public class MyTabListener implements ActionBar.TabListener {
    
    private final ViewPager mViewPager;
    
    public MyTabListener( ViewPager mViewPager ) {
    
        super();
        
        this.mViewPager = mViewPager;
    }
    
    @Override
    public void onTabUnselected( ActionBar.Tab tab, FragmentTransaction fragmentTransaction ) {
    
    }
    
    @Override
    public void onTabSelected( ActionBar.Tab tab, FragmentTransaction fragmentTransaction ) {
    
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }
    
    @Override
    public void onTabReselected( ActionBar.Tab tab, FragmentTransaction fragmentTransaction ) {
    
    }
}
