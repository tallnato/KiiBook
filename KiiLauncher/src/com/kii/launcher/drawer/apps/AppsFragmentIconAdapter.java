
package com.kii.launcher.drawer.apps;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kii.launcher.PackagePermissions;
import com.kii.launcher.R;

import java.util.List;

public class AppsFragmentIconAdapter extends ArrayAdapter<PackagePermissions> {
    
    private final int                      layoutResourceId;
    private final List<PackagePermissions> objects;
    
    public AppsFragmentIconAdapter( Context context, int viewResourceId, List<PackagePermissions> objects ) {
    
        super(context, viewResourceId, objects);
        
        layoutResourceId = viewResourceId;
        this.objects = objects;
    }
    
    @Override
    public void add( PackagePermissions app ) {
    
        if (!objects.contains(app)) {
            super.add(app);
        }
    }
    
    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
    
        View icon = convertView;
        PackagePermissions app;
        
        if (icon == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            icon = inflater.inflate(layoutResourceId, parent, false);
        }
        
        app = getItem(position);
        
        ImageView iv = (ImageView) icon.findViewById(R.id.activity_kii_launcher_gridview_iconlayout_icon);
        TextView tv = (TextView) icon.findViewById(R.id.activity_kii_launcher_gridview_iconlayout_name);
        
        iv.setImageBitmap(app.getAppIcon());
        tv.setText(app.getLabel());
        
        icon.setTag(app);
        
        icon.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View view ) {
            
                PackagePermissions app = (PackagePermissions) view.getTag();
                
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setClassName(app.getPackage(), app.getIntentActivity());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                getContext().startActivity(intent);
                
            }
        });
        
        icon.setOnLongClickListener(new OnLongClickListener() {
            
            @Override
            public boolean onLongClick( View view ) {
            
                ClipData data = ClipData.newPlainText("", "");
                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                
                view.startDrag(data, shadowBuilder, view.getTag(), 0);
                
                return true;
            }
        });
        
        return icon;
    }
}
