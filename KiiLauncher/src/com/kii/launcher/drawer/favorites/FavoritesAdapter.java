
package com.kii.launcher.drawer.favorites;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class FavoritesAdapter extends ArrayAdapter<FavoriteItem> {
    
    private final AppFavoriteAdapter  appsAdapter;
    private List<BookFavoriteItem>    books;
    private List<VideoFavoriteItem>   videos;
    private List<PictureFavoriteItem> pictures;
    private List<MusicFavoriteItem>   music;
    
    public FavoritesAdapter( Context context ) {
    
        super(context, android.R.layout.simple_list_item_1);
        
        appsAdapter = new AppFavoriteAdapter(context);
    }
    
    @Override
    public void add( FavoriteItem item ) {
    
        if (item instanceof AppFavoriteItem) {
            appsAdapter.add((AppFavoriteItem) item);
            
        } else if (item instanceof BookFavoriteItem) {
            if (!books.contains(item)) {
                books.add((BookFavoriteItem) item);
            }
        } else if (item instanceof VideoFavoriteItem) {
            if (!videos.contains(item)) {
                videos.add((VideoFavoriteItem) item);
            }
        } else if (item instanceof PictureFavoriteItem) {
            if (!pictures.contains(item)) {
                pictures.add((PictureFavoriteItem) item);
            }
        } else if (item instanceof MusicFavoriteItem) {
            if (!music.contains(item)) {
                music.add((MusicFavoriteItem) item);
            }
        }
        notifyDataSetChanged();
    }
    
    @Override
    public void remove( FavoriteItem item ) {
    
        if (item instanceof AppFavoriteItem) {
            appsAdapter.remove((AppFavoriteItem) item);
            
        } else if (item instanceof BookFavoriteItem) {
            if (!books.contains(item)) {
                books.add((BookFavoriteItem) item);
            }
        } else if (item instanceof VideoFavoriteItem) {
            if (!videos.contains(item)) {
                videos.add((VideoFavoriteItem) item);
            }
        } else if (item instanceof PictureFavoriteItem) {
            if (!pictures.contains(item)) {
                pictures.add((PictureFavoriteItem) item);
            }
        } else if (item instanceof MusicFavoriteItem) {
            if (!music.contains(item)) {
                music.add((MusicFavoriteItem) item);
            }
        }
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
    
        return appsAdapter.isEmpty() ? 0 : 1;
        
        /*return (apps.isEmpty() ? 0 : 1) + (books.isEmpty() ? 0 : 1) + (videos.isEmpty() ? 0 : 1) + (pictures.isEmpty() ? 0 : 1)
                                        + (music.isEmpty() ? 0 : 1);*/
    }
    
    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
    
        switch (position) {
            case 0:
                return appsAdapter.getView(position, convertView, parent);
                
            default:
                return null;
        }
    }
}
