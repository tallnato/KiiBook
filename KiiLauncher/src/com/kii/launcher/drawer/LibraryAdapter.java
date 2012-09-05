
package com.kii.launcher.drawer;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.launcher.R;
import com.kii.launcher.drawer.util.LibraryItem;

import java.util.List;

public class LibraryAdapter extends ArrayAdapter<LibraryItem> {
    
    public LibraryAdapter( Context context, List<LibraryItem> books ) {
    
        super(context, 0, books);
        int dif = 20 - books.size();
        if (dif > 0) {
            for (int i = 0; i < dif; i++) {
                books.add(new LibraryItem());
            }
        }
    }
    
    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
    
        View view = convertView;
        
        if (view == null) {
            view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.fragment_kii_drawer_library_shelf_item, parent, false);
        }
        
        TextView tv = (TextView) view.findViewById(R.id.fragment_kii_drawer_library_item_text);
        ImageView iv = (ImageView) view.findViewById(R.id.fragment_kii_drawer_library_item_image);
        View v = view.findViewById(R.id.fragment_kii_drawer_library_item_divider);
        
        LibraryItem item = getItem(position);
        if (item.isEmpty()) {
            tv.setVisibility(View.INVISIBLE);
            iv.setVisibility(View.INVISIBLE);
            v.setVisibility(View.INVISIBLE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(item.getName());
            iv.setVisibility(View.VISIBLE);
            v.setVisibility(View.VISIBLE);
            
            if (position % 2 == 0) {
                iv.setImageResource(R.drawable.book);
            } else {
                iv.setImageResource(R.drawable.book2);
            }
        }
        
        iv.setTag(item);
        
        iv.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View view ) {
            
                Toast.makeText(getContext(), "abrir o livro...", Toast.LENGTH_SHORT).show();
            }
        });
        
        iv.setOnLongClickListener(new OnLongClickListener() {
            
            @Override
            public boolean onLongClick( View view ) {
            
                ClipData data = ClipData.newPlainText("", "");
                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                
                view.startDrag(data, shadowBuilder, view.getTag(), 0);
                
                return true;
            }
        });
        
        return view;
    }
}
