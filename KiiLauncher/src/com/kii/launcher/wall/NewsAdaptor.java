
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
import com.kii.launcher.wall.util.NewsItem;

import java.util.List;

public class NewsAdaptor extends ArrayAdapter<NewsItem> {
    
    private final Context context;
    
    public NewsAdaptor( Context context, List<NewsItem> objects ) {
    
        super(context, R.layout.activity_kii_wall_news_item, objects);
        this.context = context;
    }
    
    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
    
        View view = convertView;
        
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.activity_kii_wall_news_item, parent, false);
        }
        
        TextView from = (TextView) view.findViewById(R.id.activity_kii_wall_news_item_from);
        TextView message = (TextView) view.findViewById(R.id.activity_kii_wall_news_item_message);
        
        NewsItem messageItem = getItem(position);
        
        from.setText(messageItem.getFrom());
        message.setText(messageItem.getMessage());
        
        view.setTag(messageItem);
        
        view.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Toast.makeText(context, v.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        
        return view;
    }
}
