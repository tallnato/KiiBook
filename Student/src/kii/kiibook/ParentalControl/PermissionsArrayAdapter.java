
package kii.kiibook.ParentalControl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kii.kiibook.Student.R;

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
            for (String s : blocked) {
                for (PackagePermissions pp : objects) {
                    if (pp.getPackageName().equalsIgnoreCase(s)) {
                        pp.setBlocked(true);
                        break;
                    }
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
            row = inflater.inflate(R.layout.fragment_permissions_item_list, parent, false);
        }
        
        // Get item
        PackagePermissions item = getItem(position);
        row.setTag(item);
        
        TextView app_name = (TextView) row.findViewById(R.id.app_name);
        ImageView app_icon = (ImageView) row.findViewById(R.id.app_icon);
        CheckBox cb = (CheckBox) row.findViewById(R.id.checkBox1);
        
        app_name.setText(item.getAppName());
        app_icon.setImageDrawable(item.getAppIcon());
        cb.setChecked(item.isBlocked());
        
        cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
            
                PackagePermissions item = (PackagePermissions) ((View) buttonView.getParent()).getTag();
                item.setBlocked(isChecked);
            }
        });
        
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
    
    public Set<String> getBlockedApps() {
    
        HashSet<String> set = new HashSet<String>();
        
        for (PackagePermissions pp : list) {
            if (pp.isBlocked()) {
                set.add(pp.getPackageName());
            }
        }
        
        return set;
    }
}
