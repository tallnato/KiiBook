
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;
import com.kii.launcher.PackagePermissions;
import com.kii.launcher.R;
import com.kii.launcher.drawer.favorites.database.AppsFavoriteDataSource;

import java.util.List;

public class AppFavoriteAdapter {
    
    private final Context                context;
    private List<AppFavoriteItem>        objects;
    private final String                 title;
    private final AppsFavoriteDataSource appsDataSource;
    
    private final Adapter                mAdapter;
    
    public AppFavoriteAdapter( Context context ) {
    
        this.context = context;
        title = context.getResources().getString(R.string.drawer_menu_apps);
        appsDataSource = new AppsFavoriteDataSource(context);
        
        appsDataSource.open();
        objects = appsDataSource.getAllApps();
        appsDataSource.close();
        
        mAdapter = new Adapter(context);
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
            
            mAdapter.notifyDataSetChanged();
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
            
            mAdapter.notifyDataSetChanged();
        }
    }
    
    public View getView( View convertView, ViewGroup parent ) {
    
        System.out.println("" + objects.size());
        
        if (convertView == null) {
            convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_kii_drawer_favorites_item, parent, false);
        }
        
        TextView section = (TextView) convertView.findViewById(R.id.fragment_kii_drawer_favorites_section_title);
        HorizontalListView list = (HorizontalListView) convertView.findViewById(R.id.fragment_kii_drawer_favorites_section_list);
        list.setEmptyView(convertView.findViewById(R.id.fragment_kii_drawer_favorites_section_list_empty));
        
        section.setText(title);
        list.setAdapter(mAdapter);
        
        return convertView;
    }
    
    private class Adapter extends ArrayAdapter<AppFavoriteItem> {
        
        public Adapter( Context context ) {
        
            super(context, 0);
        }
        
        @Override
        public AppFavoriteItem getItem( int position ) {
        
            return objects.get(position);
        }
        
        @Override
        public int getCount() {
        
            return objects.size();
        }
        
        @Override
        public View getView( int position, View convertView, ViewGroup parent ) {
        
            if (convertView == null) {
                convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.fragment_kii_drawer_favorites_apps_iconlayout,
                                                parent, false);
            }
            
            AppFavoriteItem item = getItem(position);
            PackagePermissions app = item.getPp();
            
            ImageView iv = (ImageView) convertView.findViewById(R.id.fragment_kii_drawer_favorites_apps_iconlayout_icon);
            TextView tv = (TextView) convertView.findViewById(R.id.fragment_kii_drawer_favorites_apps_iconlayout_name);
            
            iv.setImageBitmap(app.getAppIcon());
            tv.setText(app.getLabel());
            
            iv.setImageBitmap(app.getAppIcon());
            tv.setText(app.getLabel());
            
            convertView.setTag(item);
            
            convertView.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick( View view ) {
                
                    AppFavoriteItem app = (AppFavoriteItem) view.getTag();
                    
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setClassName(app.getPp().getPackage(), app.getPp().getIntentActivity());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    context.startActivity(intent);
                    
                }
            });
            
            convertView.setOnLongClickListener(new OnLongClickListener() {
                
                @Override
                public boolean onLongClick( View view ) {
                
                    ClipData data = ClipData.newPlainText("", "");
                    DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    
                    view.startDrag(data, shadowBuilder, view.getTag(), 0);
                    
                    return true;
                }
            });
            
            return convertView;
        }
        
    }
}
