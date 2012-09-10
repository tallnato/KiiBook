
package com.kii.launcher.drawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.kii.launcher.PackagePermissions;
import com.kii.launcher.R;
import com.kii.launcher.drawer.favorites.AppFavoriteItem;
import com.kii.launcher.drawer.favorites.BookFavoriteItem;
import com.kii.launcher.drawer.favorites.FavoriteItem;
import com.kii.launcher.drawer.favorites.FavoritesAdapter;
import com.kii.launcher.drawer.util.IDrawerFragment;
import com.kii.launcher.drawer.util.LibraryItem;

public class FavoritesFragment extends Fragment implements IDrawerFragment {
    
    private FavoritesAdapter mAdapter;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        final View rootView = inflater.inflate(R.layout.fragment_kii_drawer_favorites, container, false);
        
        final ListView list = (ListView) rootView.findViewById(R.id.fragment_kii_drawer_favorites_list);
        mAdapter = new FavoritesAdapter(getActivity());
        list.setAdapter(mAdapter);
        list.setEmptyView(rootView.findViewById(R.id.fragment_kii_drawer_favorites_list_empty));
        
        return rootView;
    }
    
    @Override
    public void onStop() {
    
        super.onStop();
        
    }
    
    public void addFavorite( FavoriteItem item ) {
    
        mAdapter.add(item);
    }
    
    public void removeFavorite( FavoriteItem item ) {
    
        mAdapter.remove(item);
    }
    
    public void dropView( Object dropObject ) {
    
        if (dropObject instanceof PackagePermissions) {
            mAdapter.add(new AppFavoriteItem((PackagePermissions) dropObject));
        } else if (dropObject instanceof LibraryItem) {
            mAdapter.add(new BookFavoriteItem((LibraryItem) dropObject));
        } else {
            
            Toast.makeText(getActivity(), "Unkown " + dropObject, Toast.LENGTH_LONG).show();
        }
    }
    
    @Override
    public int getNameResource() {
    
        return R.string.drawer_menu_favorites;
    }
    
    @Override
    public int getIconResource() {
    
        return R.drawable.ic_drawer_favorites;
    }
    
    @Override
    public boolean goUp() {
    
        return false;
    }
}
