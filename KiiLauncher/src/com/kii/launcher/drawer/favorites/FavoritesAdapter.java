
package com.kii.launcher.drawer.favorites;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.kii.launcher.R;

public class FavoritesAdapter extends ArrayAdapter<FavoriteItem> {
    
    private final AppFavoriteAdapter  appsAdapter;
    private final BookFavoriteAdapter booksAdapter;
    
    public FavoritesAdapter( Context context ) {
    
        super(context, R.layout.fragment_kii_drawer_favorites_apps_iconlayout);
        
        appsAdapter = new AppFavoriteAdapter(context);
        booksAdapter = new BookFavoriteAdapter(context);
    }
    
    @Override
    public void add( FavoriteItem item ) {
    
        if (item instanceof AppFavoriteItem) {
            appsAdapter.add((AppFavoriteItem) item);
            
        } else if (item instanceof BookFavoriteItem) {
            booksAdapter.add((BookFavoriteItem) item);
            
        }
        notifyDataSetChanged();
    }
    
    @Override
    public void remove( FavoriteItem item ) {
    
        if (item instanceof AppFavoriteItem) {
            appsAdapter.remove((AppFavoriteItem) item);
        } else if (item instanceof BookFavoriteItem) {
            booksAdapter.remove((BookFavoriteItem) item);
        }
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
    
        return (appsAdapter.isEmpty() ? 0 : 1) + (booksAdapter.isEmpty() ? 0 : 1);
    }
    
    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
    
        if (getCount() == 2) {
            switch (position) {
                case 0:
                    return appsAdapter.getView(convertView, parent);
                case 1:
                    return booksAdapter.getView(convertView, parent);
            }
        } else if (getCount() == 1) {
            if (!appsAdapter.isEmpty()) {
                return appsAdapter.getView(convertView, parent);
            } else if (!booksAdapter.isEmpty()) {
                return booksAdapter.getView(convertView, parent);
            }
        }
        return null;
    }
}
