
package com.kii.launcher.drawer.util;

import android.support.v4.app.Fragment;

public class MenuItem {
    
    public final static String MENUITEMKEY    = "MenuItem:Key";
    
    private static int         nMenuItemTotal = 0;
    
    private final int          name;
    private final int          iconResource;
    private Fragment           fragment;
    private final int          num;
    
    public MenuItem( IDrawerFragment ifrag ) {
    
        name = ifrag.getNameResource();
        iconResource = ifrag.getIconResource();
        fragment = (Fragment) ifrag;
        num = nMenuItemTotal++;
    }
    
    public MenuItem( int name, int iconResource, Fragment fragment ) {
    
        this.name = name;
        this.iconResource = iconResource;
        this.fragment = fragment;
        num = nMenuItemTotal++;
    }
    
    public int getName() {
    
        return name;
    }
    
    public int getNum() {
    
        return num;
    }
    
    public int getNameResource() {
    
        return name;
    }
    
    public int getIconResource() {
    
        return iconResource;
    }
    
    public Fragment getFragment() {
    
        return fragment;
    }
    
    public void setFragment( Fragment frag ) {
    
        fragment = frag;
    }
}
