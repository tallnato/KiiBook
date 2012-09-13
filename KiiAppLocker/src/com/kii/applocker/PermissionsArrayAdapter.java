
package com.kii.applocker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermissionsArrayAdapter extends ArrayAdapter<PackagePermissions> {
    
    private final Context                  context;
    private final List<PackagePermissions> list;
    private final List<PackagePermissions> listResult;
    private String                         query = "";
    
    public PermissionsArrayAdapter( Context context, int textViewResourceId, List<PackagePermissions> objects, Set<String> blocked ) {
    
        super(context, textViewResourceId, objects);
        this.context = context;
        list = objects;
        
        Collections.sort(list);
        listResult = new ArrayList<PackagePermissions>();
        
        if (blocked != null) {
            for (PackagePermissions pp : objects) {
                
                if (blocked.contains(pp.getPackageName())) {
                    pp.setBlocked(true);
                    System.out.println(pp);
                } else {
                    pp.setBlocked(false);
                }
            }
        }
    }
    
    @Override
    public int getCount() {
    
        if (query.length() == 0) {
            return list.size();
        } else {
            return listResult.size();
        }
    }
    
    @Override
    public PackagePermissions getItem( int index ) {
    
        if (query.length() == 0) {
            return list.get(index);
        } else {
            return listResult.get(index);
        }
    }
    
    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
    
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.permissions_item_list, parent, false);
        }
        
        // Get item
        final PackagePermissions item = getItem(position);
        row.setTag(item);
        
        TextView app_name = (TextView) row.findViewById(R.id.app_name);
        ImageView app_icon = (ImageView) row.findViewById(R.id.app_icon);
        CheckBox cb = (CheckBox) row.findViewById(R.id.checkBox);
        
        app_name.setText(item.getAppName());
        app_icon.setImageDrawable(item.getAppIcon());
        cb.setChecked(item.isBlocked());
        
        /* row.setOnClickListener(new OnClickListener() {
             
             @Override
             public void onClick( View v ) {
             
                 CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox);
                 cb.setChecked(!item.isBlocked());
                 
                 System.out.println("cenas");
             }
         });
         
         cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
             
             @Override
             public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
             
                 item.setBlocked(isChecked);
                 System.out.println("cenas2");
             }
         });*/
        
        return row;
    }
    
    public void searchPackages( String query ) {
    
        this.query = query;
        
        listResult.clear();
        
        if (query.length() > 0) {
            for (PackagePermissions pp : list) {
                if (pp.getAppName().toLowerCase().contains(query.toLowerCase())) {
                    listResult.add(pp);
                }
            }
        }
        
        notifyDataSetChanged();
    }
    
    public void selectAll() {
    
        for (PackagePermissions pp : list) {
            pp.setBlocked(true);
        }
        notifyDataSetChanged();
    }
    
    public void unSelectAll() {
    
        for (PackagePermissions pp : list) {
            pp.setBlocked(false);
        }
        notifyDataSetChanged();
    }
    
    public ArrayList<String> getBlockedApps() {
    
        HashSet<String> set = new HashSet<String>();
        
        for (PackagePermissions pp : list) {
            if (pp.isBlocked()) {
                set.add(pp.getPackageName());
            }
        }
        
        return new ArrayList<String>(set);
    }
}
