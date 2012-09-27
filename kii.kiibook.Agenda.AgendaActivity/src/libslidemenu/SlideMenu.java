/*
 * A sliding menu for Android, very much like the Google+ and Facebook apps
 * have. Copyright (C) 2012 CoboltForge Based upon the great work done by
 * stackoverflow user Scirocco (http://stackoverflow.com/a/11367825/361413),
 * thanks a lot! The XML parsing code comes from
 * https://github.com/darvds/RibbonMenu, thanks! Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package libslidemenu;

import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Method;
import java.util.ArrayList;

import kii.kiibook.Student.R;

public class SlideMenu extends LinearLayout {
    
    // keys for saving/restoring instance state
    private final static String KEY_MENUSHOWN       = "menuWasShown";
    private final static String KEY_STATUSBARHEIGHT = "statusBarHeight";
    private final static String KEY_SUPERSTATE      = "superState";
    
    public static class SlideMenuItem {
        
        public int      id;
        public Drawable icon;
        public String   label;
    }
    
    // a simple adapter
    private static class SlideMenuAdapter extends ArrayAdapter<SlideMenuItem> {
        
        Activity        act;
        SlideMenuItem[] items;
        
        class MenuItemHolder {
            
            public TextView  label;
            public ImageView icon;
        }
        
        public SlideMenuAdapter( Activity act, SlideMenuItem[] items ) {
        
            super(act, R.id.menu_label, items);
            this.act = act;
            this.items = items;
        }
        
        @Override
        public View getView( int position, View convertView, ViewGroup parent ) {
        
            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = act.getLayoutInflater();
                rowView = inflater.inflate(R.layout.slidemenu_listitem, null);
                MenuItemHolder viewHolder = new MenuItemHolder();
                viewHolder.label = (TextView) rowView.findViewById(R.id.menu_label);
                viewHolder.icon = (ImageView) rowView.findViewById(R.id.menu_icon);
                rowView.setTag(viewHolder);
            }
            
            MenuItemHolder holder = (MenuItemHolder) rowView.getTag();
            String s = items[position].label;
            holder.label.setText(s);
            holder.icon.setImageDrawable(items[position].icon);
            
            return rowView;
        }
    }
    
    private static boolean                                  menuShown = false;
    private int                                             statusHeight;
    private static View                                     menu;
    private static LinearLayout                             content;
    private static FrameLayout                              parent;
    private static int                                      menuSize;
    private Activity                                        act;
    private int                                             headerImageRes;
    private TranslateAnimation                              slideRightAnim;
    private TranslateAnimation                              slideMenuLeftAnim;
    private TranslateAnimation                              slideContentLeftAnim;
    
    private ArrayList<SlideMenuItem>                        menuItemList;
    private SlideMenuInterface.OnSlideMenuItemClickListener callback;
    
    /**
     * Constructor used by the inflation apparatus. To be able to use the
     * SlideMenu, call the {@link #init init()} method.
     * 
     * @param context
     */
    public SlideMenu( Context context ) {
    
        super(context);
    }
    
    /**
     * Constructor used by the inflation apparatus. To be able to use the
     * SlideMenu, call the {@link #init init()} method.
     * 
     * @param attrs
     */
    public SlideMenu( Context context, AttributeSet attrs ) {
    
        super(context, attrs);
    }
    
    /**
     * Constructs a SlideMenu with the given menu XML.
     * 
     * @param act
     *            The calling activity.
     * @param menuResource
     *            Menu resource identifier.
     * @param cb
     *            Callback to be invoked on menu item click.
     * @param slideDuration
     *            Slide in/out duration in milliseconds.
     */
    public SlideMenu( Activity act, int menuResource, SlideMenuInterface.OnSlideMenuItemClickListener cb, int slideDuration,
                                    DisplayMetrics dm ) {
    
        super(act);
        init(act, cb, slideDuration, dm);
    }
    
    /**
     * Constructs an empty SlideMenu.
     * 
     * @param act
     *            The calling activity.
     * @param cb
     *            Callback to be invoked on menu item click.
     * @param slideDuration
     *            Slide in/out duration in milliseconds.
     */
    public SlideMenu( Activity act, SlideMenuInterface.OnSlideMenuItemClickListener cb, int slideDuration, DisplayMetrics dm ) {
    
        this(act, 0, cb, slideDuration, dm);
    }
    
    /**
     * If inflated from XML, initializes the SlideMenu.
     * 
     * @param act
     *            The calling activity.
     * @param menuResource
     *            Menu resource identifier, can be 0 for an empty SlideMenu.
     * @param cb
     *            Callback to be invoked on menu item click.
     * @param slideDuration
     *            Slide in/out duration in milliseconds.
     */
    public void init( Activity act, SlideMenuInterface.OnSlideMenuItemClickListener cb, int slideDuration, DisplayMetrics me ) {
    
        this.act = act;
        this.callback = cb;
        
        // set size
        menuSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_FRACTION, 250, me);
        // create animations accordingly
        slideRightAnim = new TranslateAnimation(-menuSize, 0, 0, 0);
        slideRightAnim.setDuration(slideDuration);
        slideMenuLeftAnim = new TranslateAnimation(0, -menuSize, 0, 0);
        slideMenuLeftAnim.setDuration(slideDuration);
        slideContentLeftAnim = new TranslateAnimation(menuSize, 0, 0, 0);
        slideContentLeftAnim.setDuration(slideDuration);
        
        // and get our menu
        // parseXml(menuResource);
        menuItemList = new ArrayList<SlideMenuItem>();
        
    }
    
    /**
     * Sets an optional image to be displayed on top of the menu.
     * 
     * @param imageResource
     */
    public void setHeaderImage( int imageResource ) {
    
        headerImageRes = imageResource;
    }
    
    /**
     * Dynamically adds a menu item.
     * 
     * @param item
     */
    public void addMenuItem( SlideMenuItem item ) {
    
        menuItemList.add(item);
    }
    
    /**
     * Empties the SlideMenu.
     */
    public void clearMenuItems() {
    
        menuItemList.clear();
    }
    
    /**
     * Slide the menu in.
     */
    public void show() {
    
        /*
         *  We have to adopt to status bar height in most cases,
         *  but not if there is a support actionbar!
         */
        try {
            Method getSupportActionBar = act.getClass().getMethod("getSupportActionBar", (Class[]) null);
            Object sab = getSupportActionBar.invoke(act, (Object[]) null);
            sab.toString(); // check for null
        }
        catch (Exception es) {
            // there is no support action bar!
            Rect r = new Rect();
            Window window = act.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(r);
            statusHeight = r.top;
        }
        
        /*
         * phew, finally!
         */
        this.show(true);
    }
    
    private void show( boolean animate ) {
    
        // modify content layout params
        content = ((LinearLayout) act.findViewById(android.R.id.content).getParent());
        FrameLayout.LayoutParams parm = new FrameLayout.LayoutParams(-1, -1, 3);
        parm.setMargins(menuSize, 0, -menuSize, 0);
        content.setLayoutParams(parm);
        
        // animation for smooth slide-out
        if (animate)
            content.startAnimation(slideRightAnim);
        
        // add the slide menu to parent
        parent = (FrameLayout) content.getParent();
        LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menu = inflater.inflate(R.layout.slidemenu, null);
        FrameLayout.LayoutParams lays = new FrameLayout.LayoutParams(-1, -1, 3);
        lays.setMargins(0, statusHeight, 0, 0);
        menu.setLayoutParams(lays);
        parent.addView(menu);
        
        // set header
        try {
            // ImageView header = (ImageView)
            // act.findViewById(R.id.menu_header);
            // header.setImageDrawable(act.getResources().getDrawable(headerImageRes));
            TextView header = (TextView) act.findViewById(R.id.menu_header_title);
            header.setText("Disciplinas");
        }
        catch (Exception e) {
            // not found
        }
        
        // connect the menu's listview
        ListView list = (ListView) act.findViewById(R.id.menu_listview);
        SlideMenuItem[] items = menuItemList.toArray(new SlideMenuItem[menuItemList.size()]);
        SlideMenuAdapter adap = new SlideMenuAdapter(act, items);
        list.setAdapter(adap);
        list.setOnItemClickListener(new OnItemClickListener() {
            
            public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
            
                if (callback != null)
                    callback.onSlideMenuItemClick(menuItemList.get(position).id);
                
                hide();
            }
        });
        
        // slide menu in
        if (animate)
            menu.startAnimation(slideRightAnim);
        
        menu.findViewById(R.id.overlay).setOnClickListener(new OnClickListener() {
            
            public void onClick( View v ) {
            
                SlideMenu.this.hide();
            }
        });
        enableDisableViewGroup((LinearLayout) parent.findViewById(android.R.id.content).getParent(), false);
        
        menuShown = true;
    }
    
    /**
     * Slide the menu out.
     */
    public void hide() {
    
        menu.startAnimation(slideMenuLeftAnim);
        parent.removeView(menu);
        
        content.startAnimation(slideContentLeftAnim);
        
        FrameLayout.LayoutParams parm = (FrameLayout.LayoutParams) content.getLayoutParams();
        parm.setMargins(0, 0, 0, 0);
        content.setLayoutParams(parm);
        enableDisableViewGroup((LinearLayout) parent.findViewById(android.R.id.content).getParent(), true);
        
        menuShown = false;
    }
    
    // originally:
    // http://stackoverflow.com/questions/5418510/disable-the-touch-events-for-all-the-views
    // modified for the needs here
    private void enableDisableViewGroup( ViewGroup viewGroup, boolean enabled ) {
    
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            if (view.isFocusable())
                view.setEnabled(enabled);
            if (view instanceof ViewGroup) {
                enableDisableViewGroup((ViewGroup) view, enabled);
            } else if (view instanceof ListView) {
                if (view.isFocusable())
                    view.setEnabled(enabled);
                ListView listView = (ListView) view;
                int listChildCount = listView.getChildCount();
                for (int j = 0; j < listChildCount; j++) {
                    if (view.isFocusable())
                        listView.getChildAt(j).setEnabled(false);
                }
            }
        }
    }
    
    // originally: https://github.com/darvds/RibbonMenu
    // credit where credits due!
    private void parseXml( int menu ) {
    
        menuItemList = new ArrayList<SlideMenuItem>();
        
        try {
            XmlResourceParser xpp = act.getResources().getXml(menu);
            
            xpp.next();
            int eventType = xpp.getEventType();
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                
                if (eventType == XmlPullParser.START_TAG) {
                    
                    String elemName = xpp.getName();
                    
                    if (elemName.equals("item")) {
                        
                        String textId = xpp.getAttributeValue("http://schemas.android.com/apk/res/android", "title");
                        String iconId = xpp.getAttributeValue("http://schemas.android.com/apk/res/android", "icon");
                        String resId = xpp.getAttributeValue("http://schemas.android.com/apk/res/android", "id");
                        
                        SlideMenuItem item = new SlideMenuItem();
                        item.id = Integer.valueOf(resId.replace("@", ""));
                        item.icon = act.getResources().getDrawable(Integer.valueOf(iconId.replace("@", "")));
                        item.label = resourceIdToString(textId);
                        
                        menuItemList.add(item);
                    }
                    
                }
                
                eventType = xpp.next();
                
            }
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private String resourceIdToString( String text ) {
    
        if (!text.contains("@")) {
            return text;
        } else {
            String id = text.replace("@", "");
            return act.getResources().getString(Integer.valueOf(id));
            
        }
    }
    
    @Override
    protected void onRestoreInstanceState( Parcelable state ) {
    
        try {
            
            if (state instanceof Bundle) {
                Bundle bundle = (Bundle) state;
                
                statusHeight = bundle.getInt(KEY_STATUSBARHEIGHT);
                
                if (bundle.getBoolean(KEY_MENUSHOWN))
                    show(false); // show without animation
                    
                super.onRestoreInstanceState(bundle.getParcelable(KEY_SUPERSTATE));
                
                return;
            }
            
            super.onRestoreInstanceState(state);
            
        }
        catch (NullPointerException e) {
            // in case the menu was not declared via XML but added from code
        }
    }
    
    @Override
    protected Parcelable onSaveInstanceState() {
    
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPERSTATE, super.onSaveInstanceState());
        bundle.putBoolean(KEY_MENUSHOWN, menuShown);
        bundle.putInt(KEY_STATUSBARHEIGHT, statusHeight);
        
        return bundle;
    }
    
}
