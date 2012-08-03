
package com.example.myteacherprofile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myteacherprofile.R;

import utils.ItemFalconEye;

import java.util.ArrayList;

public class AdapterFalconEyeView extends BaseAdapter {
    
    private final LayoutInflater           mInflater;
    private final ArrayList<ItemFalconEye> itens;
    private final Context                  context;
    
    public AdapterFalconEyeView( Context context, ArrayList<ItemFalconEye> arrayList ) {
    
        itens = arrayList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }
    
    @Override
    public int getCount() {
    
        return itens.size();
    }
    
    @Override
    public ItemFalconEye getItem( int position ) {
    
        return itens.get(position);
    }
    
    @Override
    public long getItemId( int position ) {
    
        return position;
    }
    
    @Override
    public void notifyDataSetChanged() {
    
        // TODO Auto-generated method stub
        super.notifyDataSetChanged();
    }
    
    @Override
    public View getView( int position, View view, ViewGroup parent ) {
    
        ItemFalconEye item = itens.get(position);
        
        /*
        if (view == null) {
            view = mInflater.inflate(R.layout.hour_item_calendar, null);
            listElem = (LinearLayout) view.findViewById(R.id._calendar_hour_elements);
        } else {
            listElem = (LinearLayout) view.findViewById(R.id._calendar_hour_elements);
            for (int index = 0; index < listElem.getChildCount(); index++) {
                TextView text = (TextView) listElem.getChildAt(index);
                text.setText("");
            }
        }*/
        
        view = mInflater.inflate(R.layout.item_falcon_eye_listview, null);
        // TODO complete views
        ((TextView) view.findViewById(R.id.falcon_eye_item_name)).setText(item.getStudent().getName());
        return view;
    }
}
