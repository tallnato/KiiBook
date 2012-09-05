
package com.kii.launcher.drawer.favorites;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.kii.launcher.PackagePermissions;
import com.kii.launcher.R;
import com.kii.launcher.drawer.favorites.database.AppsFavoriteDataSource;

import java.util.List;

public class BookFavoriteAdapter {
    
    private final Context                context;
    private List<AppFavoriteItem>        objects;
    private final String                 title;
    private final AppsFavoriteDataSource appsDataSource;
    
    public BookFavoriteAdapter( Context context ) {
    
        this.context = context;
        title = context.getResources().getString(R.string.drawer_menu_apps);
        appsDataSource = new AppsFavoriteDataSource(context);
        
        appsDataSource.open();
        objects = appsDataSource.getAllApps();
        appsDataSource.close();
    }
    
    public String getTitle() {
    
        return title;
    }
    
    public void add( AppFavoriteItem item ) {
    
        if (!objects.contains(item)) {
            appsDataSource.open();
            appsDataSource.createFavoriteApp(item.getPp().getId());
            objects = appsDataSource.getAllApps();
            appsDataSource.close();
        }
    }
    
    public boolean isEmpty() {
    
        return objects.isEmpty();
    }
    
    public void remove( AppFavoriteItem item ) {
    
        if (objects.contains(item)) {
            appsDataSource.open();
            appsDataSource.deleteFavoriteApp(item.getPp().getId());
            objects = appsDataSource.getAllApps();
            appsDataSource.close();
        }
    }
    
    public View getView( int position, View convertView, ViewGroup parent ) {
    
        convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_kii_drawer_favorites_item, parent, false);
        
        TextView section = (TextView) convertView.findViewById(R.id.fragment_kii_drawer_favorites_section_title);
        TableLayout list = (TableLayout) convertView.findViewById(R.id.fragment_kii_drawer_favorites_section_list);
        
        section.setText(title);
        
        TableRow row = new TableRow(context);
        
        for (int i = 0; i < objects.size(); i++) {
            
            AppFavoriteItem item = objects.get(i);
            PackagePermissions app = item.getPp();
            
            LinearLayout ll = (LinearLayout) ((Activity) context).getLayoutInflater().inflate(
                                            R.layout.fragment_kii_drawer_favorites_apps_iconlayout, list, false);
            
            ImageView iv = (ImageView) ll.findViewById(R.id.fragment_kii_drawer_favorites_apps_iconlayout_icon);
            TextView tv = (TextView) ll.findViewById(R.id.fragment_kii_drawer_favorites_apps_iconlayout_name);
            
            iv.setImageBitmap(app.getAppIcon());
            tv.setText(app.getLabel());
            
            ll.setTag(item);
            
            ll.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick( View view ) {
                
                    AppFavoriteItem app = (AppFavoriteItem) view.getTag();
                    
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setClassName(app.getPp().getPackage(), app.getPp().getIntentActivity());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    context.startActivity(intent);
                    
                    ((Activity) context).finish();
                }
            });
            
            ll.setOnLongClickListener(new OnLongClickListener() {
                
                @Override
                public boolean onLongClick( View view ) {
                
                    ClipData data = ClipData.newPlainText("", "");
                    DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    
                    view.startDrag(data, shadowBuilder, view.getTag(), 0);
                    
                    return true;
                }
            });
            row.addView(ll, new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            if ((i + 1) % 10 == 0) {
                list.addView(row);
                row = new TableRow(context);
            }
        }
        list.addView(row);
        
        return convertView;
    }
    
}
