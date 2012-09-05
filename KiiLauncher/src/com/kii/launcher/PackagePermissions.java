
package com.kii.launcher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class PackagePermissions implements Comparable<PackagePermissions>, Serializable {
    
    private static final long serialVersionUID = 1984367383700433342L;
    
    private final int         id;
    private final String      label;
    private final String      packageName;
    private final String      intentActivity;
    private boolean           blocked;
    private final byte[]      appIcon;
    
    public PackagePermissions( int id, String packageName, String intentActivity, String appName, Drawable icon ) {
    
        super();
        this.id = id;
        label = appName;
        this.packageName = packageName;
        this.intentActivity = intentActivity;
        blocked = false;
        
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ((BitmapDrawable) icon).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
        appIcon = stream.toByteArray();
    }
    
    public PackagePermissions( int id, String packageName, String intentActivity, String appName, byte[] icon ) {
    
        super();
        this.id = id;
        label = appName;
        this.packageName = packageName;
        this.intentActivity = intentActivity;
        blocked = false;
        
        appIcon = icon;
    }
    
    public String getLabel() {
    
        return label;
    }
    
    public String getPackage() {
    
        return packageName;
    }
    
    public boolean isBlocked() {
    
        return blocked;
    }
    
    public void setBlocked( boolean blocked ) {
    
        this.blocked = blocked;
    }
    
    public String getIntentActivity() {
    
        return intentActivity;
    }
    
    public int getId() {
    
        return id;
    }
    
    public Bitmap getAppIcon() {
    
        return BitmapFactory.decodeByteArray(appIcon, 0, appIcon.length);
    }
    
    @Override
    public String toString() {
    
        return "PackagePermissions [id=" + id + ", label=" + label + ", packageName=" + packageName + ", intentActivity=" + intentActivity
                                        + ", blocked=" + blocked + "]";
    }
    
    @Override
    public int compareTo( PackagePermissions another ) {
    
        return label.compareToIgnoreCase(another.label);
    }
}
