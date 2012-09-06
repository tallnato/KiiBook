
package com.kii.launcher.drawer.favorites;

import com.kii.launcher.drawer.util.LibraryItem;

public class BookFavoriteItem implements FavoriteItem {
    
    private final LibraryItem item;
    private final int         id;
    
    public BookFavoriteItem( int id, LibraryItem item ) {
    
        this.id = id;
        this.item = item;
    }
    
    public BookFavoriteItem( LibraryItem item ) {
    
        id = -1;
        this.item = item;
    }
    
    public LibraryItem getLibraryItem() {
    
        return item;
    }
    
    public int getId() {
    
        return id;
    }
    
    @Override
    public boolean equals( Object o ) {
    
        if (!(o instanceof BookFavoriteItem)) {
            return false;
        }
        
        return ((BookFavoriteItem) o).item.equals(item);
    }
}
