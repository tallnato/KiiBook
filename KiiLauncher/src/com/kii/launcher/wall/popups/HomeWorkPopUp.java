
package com.kii.launcher.wall.popups;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.launcher.R;
import com.kii.launcher.wall.HomeworksAdaptor;
import com.kii.launcher.wall.util.HomeWorkItem;

import java.util.ArrayList;

public class HomeWorkPopUp extends WallPopUp {
    
    public HomeWorkPopUp( final Context context, View clickView, int width, int height ) {
    
        super(context, R.layout.activity_kii_wall_homework, clickView, width, height);
        
        TextView showAllMessages = (TextView) getContentView().findViewById(R.id.activity_kii_wall_homework_show_all);
        showAllMessages.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Toast.makeText(context, "Mostrar todoss os trabalhos de casa...", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        
        ListView lv = (ListView) getContentView().findViewById(R.id.activity_kii_wall_homework_list);
        ArrayList<HomeWorkItem> list = new ArrayList<HomeWorkItem>();
        list.add(new HomeWorkItem("20 \n Sep", "Português", "Ler cenas de depois resolver cenas da pagina xx"));
        list.add(new HomeWorkItem("24 \n Sep", "Matemática", "Somar uns numeros e uns integrais da pagina 55"));
        lv.setAdapter(new HomeworksAdaptor(context, list));
    }
}
