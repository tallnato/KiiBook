
package kii.kiibook.KiiClass;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kii.kiibook.KiiClass.fragments.DocFragment;
import kii.kiibook.KiiClass.fragments.StatsFragment;
import kii.kiibook.KiiClass.fragments.SummariesFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    
    public PagerAdapter( FragmentManager fm ) {
    
        super(fm);
    }
    
    @Override
    public Fragment getItem( int i ) {
    
        Fragment fragment = null;
        Bundle args = new Bundle();
        
        switch (i) {
            case 0:
                fragment = new SummariesFragment();
                break;
            case 1:
                
                fragment = new StatsFragment();
                break;
            
            case 2:
                fragment = new DocFragment();
                break;
            
            case 3:
                fragment = new DocFragment();
                break;
        
        }
        
        return fragment;
    }
    
    @Override
    public int getCount() {
    
        return 4;
    }
    
}
