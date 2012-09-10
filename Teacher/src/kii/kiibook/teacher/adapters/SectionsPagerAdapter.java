
package kii.kiibook.teacher.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kii.kiibook.teacher.R;
import kii.kiibook.teacher.fragments.ClassesFragment;
import kii.kiibook.teacher.fragments.MenssageFragment;
import kii.kiibook.teacher.fragments.ProfileFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one
 * of the primary sections of the app.
 */
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
                
                fragment = new ClassesFragment();
                break;
            case 2:
                
                fragment = new ClassesFragment();
                break;
            case 3:
                
                fragment = new MenssageFragment();
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
                return R.drawable.ic_menu_friends;
                
            case 2:
                return R.drawable.ic_menu_classes;
                
            case 3:
                return R.drawable.ic_menu_messages;
                
            default:
                return -1;
        }
    }
}
