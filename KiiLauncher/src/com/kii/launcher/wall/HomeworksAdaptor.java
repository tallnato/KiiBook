
package com.kii.launcher.wall;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.launcher.R;
import com.kii.launcher.wall.util.HomeWorkItem;

import java.util.List;

public class HomeworksAdaptor extends ArrayAdapter<HomeWorkItem> {
    
    private final Context context;
    
    public HomeworksAdaptor( Context context, List<HomeWorkItem> objects ) {
    
        super(context, R.layout.activity_kii_wall_messages_item, objects);
        this.context = context;
    }
    
    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
    
        View view = convertView;
        
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.activity_kii_wall_homework_item, parent, false);
        }
        
        TextView date = (TextView) view.findViewById(R.id.activity_kii_wall_homework_item_date);
        TextView course = (TextView) view.findViewById(R.id.activity_kii_wall_homework_item_course);
        TextView homework = (TextView) view.findViewById(R.id.activity_kii_wall_homework_item_homework);
        
        HomeWorkItem item = getItem(position);
        
        date.setText(item.getDeliveryDate());
        course.setText(item.getCourse());
        homework.setText(item.getHomework());
        
        view.setTag(item);
        
        view.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Toast.makeText(context, v.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        
        return view;
    }
}
