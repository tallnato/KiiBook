
package kii.kiibook.managerclass;

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

import objects.PackagePermissions;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import kii.kiibook.teacher.R;

public class PermissionsArrayAdapter extends ArrayAdapter<PackagePermissions> {
    
    private final Context                  context;
    private final List<PackagePermissions> list;
    
    public PermissionsArrayAdapter( Context context, int textViewResourceId, List<PackagePermissions> objects, Set<String> blocked ) {
    
        super(context, textViewResourceId, objects);
        this.context = context;
        list = objects;
        
        Collections.sort(list);
        
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
        app_icon.setImageBitmap(item.getAppIcon());
        cb.setChecked(item.isBlocked());
        
        cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
            
                PackagePermissions item = (PackagePermissions) ((View) buttonView.getParent()).getTag();
                item.setBlocked(isChecked);
            }
        });
        
        return row;
    }
    
    public List<PackagePermissions> getBlockedApps() {
    
        return list;
    }
}
