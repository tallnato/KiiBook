
package objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class PackagePermissions implements Comparable<PackagePermissions>, Serializable {
    
    private static final long serialVersionUID = 1984367383700433342L;
    
    private final String      appName;
    private final String      packageName;
    // private final Bitmap appIcon;
    private boolean           blocked;
    private final byte[]      appIcon;
    
    public PackagePermissions( String appName, String packageName, Drawable icon ) {
    
        this.appName = appName;
        this.packageName = packageName;
        // appIcon = ((BitmapDrawable) icon).getBitmap();
        blocked = false;
        
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ((BitmapDrawable) icon).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
        appIcon = stream.toByteArray();
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
    
    public Bitmap getAppIcon() {
    
        return BitmapFactory.decodeByteArray(appIcon, 0, appIcon.length);
    }
    
    @Override
    public String toString() {
    
        return appName + ", " + packageName + " " + blocked;
    }
    
    @Override
    public int compareTo( PackagePermissions another ) {
    
        return appName.compareTo(another.appName);
    }
}
