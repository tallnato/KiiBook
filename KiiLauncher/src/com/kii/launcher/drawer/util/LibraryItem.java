
package com.kii.launcher.drawer.util;

import android.graphics.drawable.Drawable;

public class LibraryItem {
    
    private final String name;
    private final String path;
    private Drawable     icon;
    
    public LibraryItem( String name, String path, Drawable icon ) {
    
        this.name = name;
        this.path = path;
        this.icon = icon;
    }
    
    public LibraryItem( String name, String path ) {
    
        this.name = name;
        this.path = path;
        icon = null;
    }
    
    public String getName() {
    
        return name;
    }
    
    public Drawable getIcon() {
    
        return icon;
    }
    
    public void setIcon( Drawable icon ) {
    
        this.icon = icon;
    }
    
    public String getPath() {
    
        return path;
    }
    
    @Override
    public boolean equals( Object o ) {
    
        if (!(o instanceof LibraryItem)) {
            return false;
        }
        
        return ((LibraryItem) o).path.equals(path);
    }
}
