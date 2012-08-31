
package com.kii.launcher.drawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;

import com.kii.launcher.R;
import com.kii.launcher.drawer.MenuFragment.ListCallbacks;
import com.kii.launcher.drawer.util.MenuItem;

public class DrawerActivity extends FragmentActivity implements ListCallbacks {
    
    private MenuItem currentMenu = null;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kii_drawer);
        
        ListFragment list = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.activity_kii_launcher_drawer_list);
        
        currentMenu = (MenuItem) list.getListAdapter().getItem(0);
        
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.activity_kii_launcher_drawer_container, currentMenu.getFragment());
        ft.commit();
    }
    
    @Override
    public void onAttachFragment( Fragment fragment ) {
    
        super.onAttachFragment(fragment);
    }
    
    @Override
    public void onItemSelected( MenuItem menuItem ) {
    
        if (currentMenu == menuItem) {
            return;
        }
        
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (currentMenu.getNum() < menuItem.getNum()) {
            ft.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);
        } else {
            ft.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_up);
        }
        ft.replace(R.id.activity_kii_launcher_drawer_container, menuItem.getFragment()).commit();
        
        currentMenu = menuItem;
    }
    
    @Override
    public void closeDrawer() {
    
        finish();
    }
}
