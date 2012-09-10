
package com.kii.launcher.wall.popups;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.launcher.R;

public class CalendarPopUp extends WallPopUp {
    
    public CalendarPopUp( final Context context, View clickView, int width, int height ) {
    
        super(context, R.layout.activity_kii_wall_calendar, clickView, width, height);
        
        TextView agenda = (TextView) getContentView().findViewById(R.id.activity_kii_wall_calendar_agenda);
        CalendarView calendar = (CalendarView) getContentView().findViewById(R.id.activity_kii_wall_calendar_view);
        
        agenda.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Toast.makeText(context, "Abrir agenda...", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        
    }
}
