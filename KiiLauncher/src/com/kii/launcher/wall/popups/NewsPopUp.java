
package com.kii.launcher.wall.popups;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.launcher.R;
import com.kii.launcher.wall.NewsAdaptor;
import com.kii.launcher.wall.util.NewsItem;

import java.util.ArrayList;

public class NewsPopUp extends WallPopUp {
    
    public NewsPopUp( final Context context, View clickView, int width, int height ) {
    
        super(context, R.layout.activity_kii_wall_news, clickView, width, height);
        
        TextView showAllMessages = (TextView) getContentView().findViewById(R.id.activity_kii_wall_news_show_all);
        showAllMessages.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Toast.makeText(context, "Mostrar todas as noticias...", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        
        ListView lv = (ListView) getContentView().findViewById(R.id.activity_kii_wall_news_list);
        ArrayList<NewsItem> list = new ArrayList<NewsItem>();
        list.add(new NewsItem("Escola secundaria Asdrubal da Silva", "KiiBooks para toda a gente."));
        list.add(new NewsItem("Gervásio Coiso", "Gervasio conquistou 30 troféus nas duas ultimas horas."));
        lv.setAdapter(new NewsAdaptor(context, list));
    }
}
