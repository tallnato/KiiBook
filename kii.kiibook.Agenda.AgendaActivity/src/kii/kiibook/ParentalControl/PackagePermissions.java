
package kii.kiibook.ParentalControl;

import android.graphics.drawable.Drawable;

public class PackagePermissions implements Comparable<PackagePermissions> {
    
    private final String   appName;
    private final String   packageName;
    private final Drawable appIcon;
    private boolean        blocked;
    
    public PackagePermissions( String appName, String packageName, Drawable icon ) {
    
        super();
        this.appName = appName;
        this.packageName = packageName;
        appIcon = icon;
        blocked = false;
    }
    
    public String getAppName() {
    
        return appName;
    }
    
    public String getPackageName() {
    
        return packageName;
    }
    
    public boolean isBlocked() {
    
        return blocked;
    }
    
    public void setBlocked( boolean blocked ) {
    
        this.blocked = blocked;
    }
    
    public Drawable getAppIcon() {
    
        return appIcon;
    }
    
    @Override
    public String toString() {
    
        return appName + ", " + packageName + " " + blocked;
    }
    
    public int compareTo( PackagePermissions another ) {
    
        return appName.compareTo(another.appName);
    }
}
