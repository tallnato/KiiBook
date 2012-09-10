
package kii.kiibook.kiimarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
                fragment = new FeaturedFragment();
                break;
            
            case 1:
                fragment = new BlankFragment();
                break;
            
            case 2:
                fragment = new BlankFragment();
                break;
            
            case 3:
                fragment = new BlankFragment();
                break;
        
        }
        
        return fragment;
    }
    
    @Override
    public int getCount() {
    
        return 4;
    }
}
