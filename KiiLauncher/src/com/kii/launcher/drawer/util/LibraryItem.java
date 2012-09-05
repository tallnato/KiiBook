
package com.kii.launcher.drawer.util;

public class LibraryItem {
    
    private final String  name;
    private final String  path;
    private final boolean empty;
    
    public LibraryItem( String name, String path ) {
    
        this.name = name;
        this.path = path;
        empty = false;
    }
    
    public LibraryItem() {
    
        name = null;
        path = null;
        empty = true;
    }
    
    public String getName() {
    
        return name;
    }
    
    public String getPath() {
    
        return path;
    }
    
    public boolean isEmpty() {
    
        return empty;
    }
}
