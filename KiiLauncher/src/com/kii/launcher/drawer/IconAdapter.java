
package com.kii.launcher.drawer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kii.launcher.PackagePermissions;
import com.kii.launcher.R;

import java.util.List;

public class IconAdapter extends ArrayAdapter<PackagePermissions> {
    
    private final int     layoutResourceId;
    private final Context context;
    
    public IconAdapter( Context context, int viewResourceId, List<PackagePermissions> objects ) {
    
        super(context, viewResourceId, objects);
        
        layoutResourceId = viewResourceId;
        this.context = context;
    }
    
    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
    
        View row = convertView;
        PackagePermissions app;
        
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }
        
        app = getItem(position);
        
        ImageView iv = (ImageView) row.findViewById(R.id.activity_kii_launcher_gridview_iconlayout_icon);
        TextView tv = (TextView) row.findViewById(R.id.activity_kii_launcher_gridview_iconlayout_name);
        
        iv.setImageBitmap(app.getAppIcon());
        tv.setText(app.getLabel());
        
        row.setTag(app);
        
        return row;
    }
}
