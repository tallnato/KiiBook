
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
import com.kii.launcher.wall.util.MessageItem;

import java.util.List;

public class MessagesAdaptor extends ArrayAdapter<MessageItem> {
    
    private final Context context;
    
    public MessagesAdaptor( Context context, List<MessageItem> objects ) {
    
        super(context, R.layout.activity_kii_wall_messages_item, objects);
        this.context = context;
    }
    
    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
    
        View view = convertView;
        
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.activity_kii_wall_messages_item, parent, false);
        }
        
        TextView fromTo = (TextView) view.findViewById(R.id.activity_kii_wall_messages_item_from_to);
        TextView message = (TextView) view.findViewById(R.id.activity_kii_wall_messages_item_message);
        
        MessageItem messageItem = getItem(position);
        
        fromTo.setText(messageItem.getFrom() + " > " + messageItem.getTo());
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
