
package com.kii.launcher.wall.popups;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.launcher.R;
import com.kii.launcher.wall.MessagesAdaptor;
import com.kii.launcher.wall.util.MessageItem;

import java.util.ArrayList;

public class MessagePopUp extends WallPopUp {
    
    public MessagePopUp( final Context context, View clickView, int width, int height ) {
    
        super(context, R.layout.activity_kii_wall_messages, clickView, width, height);
        
        TextView showAllMessages = (TextView) getContentView().findViewById(R.id.activity_kii_wall_messages_show_all);
        showAllMessages.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Toast.makeText(context, "Mostrar todas as mensages...", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        
        ListView lv = (ListView) getContentView().findViewById(R.id.activity_kii_wall_messages_list);
        ArrayList<MessageItem> list = new ArrayList<MessageItem>();
        list.add(new MessageItem("Pai natal", "Me", "Foste um bom menino, por isso vais receber prendas :D"));
        list.add(new MessageItem("Gervásio", "Turma 5ªA", "Amanha é dia de ir apanhar as couves e as cenouras."));
        list.add(new MessageItem("Prof. Antonieta", "Turma 5ªA", "Cambada de burros..."));
        list.add(new MessageItem("Pai natal", "Me", "Foste um bom menino, por isso vais receber prendas :D"));
        list.add(new MessageItem("Gervásio", "Turma 5ªA", "Amanha é dia de ir apanhar as couves e as cenouras."));
        list.add(new MessageItem("Prof. Antonieta", "Turma 5ªA", "Cambada de burros..."));
        lv.setAdapter(new MessagesAdaptor(context, list));
    }
}
