
package com.kii.launcher.drawer;

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

import com.kii.launcher.R;
import com.kii.launcher.drawer.util.LibraryItem;

import java.util.List;

public class LibraryAdapter extends ArrayAdapter<LibraryItem> {
    
    public LibraryAdapter( Context context, List<LibraryItem> books ) {
    
        super(context, 0, books);
    }
    
    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
    
        View view = convertView;
        
        if (view == null) {
            view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.fragment_kii_drawer_library_shelf_item, parent, false);
        }
        LibraryItem item = getItem(position);
        
        TextView tv = (TextView) view.findViewById(R.id.fragment_kii_drawer_library_item_text);
        final ImageView iv = (ImageView) view.findViewById(R.id.fragment_kii_drawer_library_item_image);
        
        tv.setText(item.getName());
        iv.setImageDrawable(item.getIcon());
        
        view.setTag(item);
        iv.setTag(item);
        
        view.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View view ) {
            
                LibraryItem li = (LibraryItem) view.getTag();
                
                Intent i = new Intent("KIIREADER_OPEN_BOOK");
                i.putExtra("NAME", li.getName());
                i.putExtra("PAGE", 0);
                i.putExtra("ONTOP", true);
                getContext().sendBroadcast(i);
                
            }
        });
        
        view.setOnLongClickListener(new OnLongClickListener() {
            
            @Override
            public boolean onLongClick( View view ) {
            
                ClipData data = ClipData.newPlainText("", "");
                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(iv);
                
                view.startDrag(data, shadowBuilder, iv.getTag(), 0);
                
                return true;
            }
        });
        
        return view;
    }
}
