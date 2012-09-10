
package com.kii.launcher.drawer;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kii.launcher.R;
import com.kii.launcher.drawer.util.MenuItem;

import java.util.ArrayList;

public class MenuAdapter extends ArrayAdapter<MenuItem> {
    
    private int                currentPos;
    private final MenuFragment menuFragment;
    
    public MenuAdapter( MenuFragment menuFragment, int textViewResourceId, ArrayList<MenuItem> objects ) {
    
        super(menuFragment.getActivity(), textViewResourceId, objects);
        this.menuFragment = menuFragment;
        currentPos = 0;
    }
    
    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
    
        final View view;
        TextView text;
        ImageView icon;
        View arrow;
        
        if (convertView == null) {
            view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.fragment_kii_drawer_menu_item, parent, false);
        } else {
            view = convertView;
        }
        
        MenuItem mItem = getItem(position);
        
        text = (TextView) view.findViewById(R.id.fragment_kii_drawer_menu_item_text);
        icon = (ImageView) view.findViewById(R.id.fragment_kii_drawer_menu_item_icon);
        arrow = view.findViewById(R.id.fragment_kii_drawer_menu_item_arrow);
        
        if (currentPos == position) {
            arrow.setVisibility(View.VISIBLE);
            view.findViewById(R.id.fragment_kii_drawer_menu_item).setBackgroundResource(R.color.kii_drawer_menu_selected_menu);
        } else {
            arrow.setVisibility(View.GONE);
            view.findViewById(R.id.fragment_kii_drawer_menu_item).setBackground(null);
        }
        
        if (text != null) {
            text.setText(getContext().getString(mItem.getNameResource()));
        }
        icon.setImageResource(mItem.getIconResource());
        
        view.setTag(mItem);
        
        if (position == 0) { // favorites
        
            view.setOnDragListener(new OnDragListener() {
                
                Drawable original = view.getBackground();
                
                @Override
                public boolean onDrag( View v, DragEvent event ) {
                
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            v.setBackgroundResource(android.R.color.holo_orange_light);
                            break;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            menuFragment.setSelectedItem(0);
                            v.setBackgroundResource(android.R.color.holo_blue_light);
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            v.setBackground(original);
                            break;
                        case DragEvent.ACTION_DROP:
                            
                            MenuItem mi = (MenuItem) v.getTag();
                            FavoritesFragment ff = (FavoritesFragment) mi.getFragment();
                            ff.dropView(event.getLocalState());
                            
                            break;
                        case DragEvent.ACTION_DRAG_ENDED:
                            v.setBackground(original);
                        default:
                            break;
                    }
                    return true;
                }
            });
        }
        
        return view;
    }
    
    public void setPosition( int position ) {
    
        currentPos = position;
        notifyDataSetChanged();
    }
    
    public int getCurrentPosition() {
    
        return currentPos;
    }
}
