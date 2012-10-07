
package kii.kiibook.Student.adapters_items;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kii.kiibook.Student.R;
import kii.kiibook.Student.fragments.FriendsFragment;
import kii.kiibook.Student.fragments.NewsFragment;
import kii.kiibook.Student.fragments.ProfileAcademicFragment;
import kii.kiibook.Student.fragments.ProfileFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    
    public SectionsPagerAdapter( FragmentManager fm ) {
    
        super(fm);
    }
    
    @Override
    public Fragment getItem( int i ) {
    
        Fragment fragment = null;
        
        switch (i) {
            case 0:
                
                fragment = new ProfileFragment();
                break;
            case 1:
                
                fragment = new ProfileAcademicFragment();
                break;
            case 2:
                
                fragment = new FriendsFragment();
                break;
            case 3:
                
                fragment = new NewsFragment();
                break;
        
        }
        
        return fragment;
    }
    
    @Override
    public int getCount() {
    
        return 4;
    }
    
    public int getFragmentIcon( int i ) {
    
        switch (i) {
            case 0:
                return R.drawable.ic_menu_profile;
                
            case 1:
                return android.R.drawable.ic_menu_info_details;
                
            case 2:
                return R.drawable.ic_menu_friends;
                
            case 3:
                return R.drawable.ic_menu_messages;
                
            default:
                return -1;
        }
    }
}
