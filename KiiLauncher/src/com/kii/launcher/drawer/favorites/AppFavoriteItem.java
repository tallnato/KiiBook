
package com.kii.launcher.drawer.favorites;

import android.view.View;

import com.kii.launcher.PackagePermissions;

public class AppFavoriteItem implements FavoriteItem {
    
    private final PackagePermissions pp;
    
    public AppFavoriteItem( PackagePermissions pp ) {
    
        this.pp = pp;
    }
    
    public PackagePermissions getPp() {
    
        return pp;
    }
    
    @Override
    public View getView() {
    
        return null;
    }
    
    @Override
    public boolean equals( Object o ) {
    
        if (!(o instanceof AppFavoriteItem)) {
            return false;
        }
        
        return ((AppFavoriteItem) o).pp == pp;
    }
}
