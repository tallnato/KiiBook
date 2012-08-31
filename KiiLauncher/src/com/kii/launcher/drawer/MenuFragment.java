
package com.kii.launcher.drawer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.kii.launcher.R;
import com.kii.launcher.drawer.util.MenuItem;

import java.util.ArrayList;

public class MenuFragment extends ListFragment {
    
    private ListCallbacks topActivity;
    private MenuAdapter   mMenuAdapter;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        mMenuAdapter = new MenuAdapter(getActivity(), R.layout.fragment_kii_drawer_menu_item, getMenuItems());
        setListAdapter(mMenuAdapter);
    }
    
    @Override
    public void onListItemClick( ListView listView, View view, int position, long id ) {
    
        super.onListItemClick(listView, view, position, id);
        
        mMenuAdapter.setPosition(position);
        topActivity.onItemSelected(mMenuAdapter.getItem(position));
    }
    
    @Override
    public void onAttach( Activity activity ) {
    
        super.onAttach(activity);
        
        topActivity = (ListCallbacks) activity;
    }
    
    protected interface ListCallbacks {
        
        public void onItemSelected( MenuItem menuItem );
        
        public void closeDrawer();
    }
    
    private ArrayList<MenuItem> getMenuItems() {
    
        ArrayList<MenuItem> list = new ArrayList<MenuItem>();
        
        list.add(new MenuItem(new FavoritesFragment()));
        list.add(new MenuItem(new AppsFragment()));
        list.add(new MenuItem(new LibraryFragment()));
        list.add(new MenuItem(new MoviesFragment()));
        list.add(new MenuItem(new PicturesFragment()));
        list.add(new MenuItem(new MusicFragment()));
        list.add(new MenuItem(new DownloadsFragment()));
        
        return list;
    }
}
