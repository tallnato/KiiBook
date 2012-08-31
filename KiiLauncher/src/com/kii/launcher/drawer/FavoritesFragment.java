
package com.kii.launcher.drawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kii.launcher.R;
import com.kii.launcher.drawer.util.IDrawerFragment;

public class FavoritesFragment extends Fragment implements IDrawerFragment {
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        View rootView = inflater.inflate(R.layout.fragment_kii_drawer_under_construction, container, false);
        return rootView;
    }
    
    @Override
    public int getNameResource() {
    
        return R.string.drawer_menu_favorites;
    }
    
    @Override
    public int getIconResource() {
    
        return R.drawable.ic_drawer_favorites;
    }
}
