
package com.kii.launcher.drawer;

import java.io.File;

public class PositionHelper {
    
    private final File root;
    private File       currentPath;
    
    public PositionHelper( File root, File currentPath ) {
    
        this.root = root;
        this.currentPath = currentPath;
    }
    
    public File getRoot() {
    
        return root;
    }
    
    public File getCurrentPath() {
    
        return currentPath;
    }
    
    public void setCurrentPath( File currentPath ) {
    
        this.currentPath = currentPath;
    }
    
    public boolean equal() {
    
        return root.equals(currentPath);
    }
}
