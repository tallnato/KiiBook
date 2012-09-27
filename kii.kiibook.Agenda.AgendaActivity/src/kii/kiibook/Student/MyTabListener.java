
package kii.kiibook.Student;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

public class MyTabListener implements ActionBar.TabListener {
    
    private final ViewPager mViewPager;
    
    public MyTabListener( ViewPager mViewPager ) {
    
        super();
        
        this.mViewPager = mViewPager;
    }
    
    public void onTabSelected( ActionBar.Tab tab, FragmentTransaction fragmentTransaction ) {
    
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }
    
    public void onTabReselected( Tab arg0, FragmentTransaction arg1 ) {
    
        // TODO Auto-generated method stub
        
    }
    
    public void onTabUnselected( Tab tab, FragmentTransaction ft ) {
    
        // TODO Auto-generated method stub
        
    }
    
}
