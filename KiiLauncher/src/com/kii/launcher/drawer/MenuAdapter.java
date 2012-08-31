
package com.kii.launcher.drawer;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kii.launcher.R;
import com.kii.launcher.drawer.util.MenuItem;

import java.util.ArrayList;

public class MenuAdapter extends ArrayAdapter<MenuItem> {
    
    private int currentPos;
    
    public MenuAdapter( Context context, int textViewResourceId, ArrayList<MenuItem> objects ) {
    
        super(context, textViewResourceId, objects);
        currentPos = 0;
    }
    
    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
    
        View view;
        TextView text;
        ImageView icon;
        
        if (currentPos != position) {
            view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.fragment_kii_drawer_menu_item, parent, false);
        } else {
            view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.fragment_kii_drawer_menu_item_selected, parent, false);
        }
        
        MenuItem mItem = getItem(position);
        
        text = (TextView) view.findViewById(R.id.fragment_kii_drawer_menu_item_text);
        icon = (ImageView) view.findViewById(R.id.fragment_kii_drawer_menu_item_icon);
        
        text.setText(getContext().getString(mItem.getNameResource()));
        icon.setImageResource(mItem.getIconResource());
        
        view.setTag(mItem);
        
        return view;
    }
    
    public void setPosition( int position ) {
    
        currentPos = position;
        notifyDataSetChanged();
    }
}
