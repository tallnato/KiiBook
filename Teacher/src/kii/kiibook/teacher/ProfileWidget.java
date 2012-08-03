
package kii.kiibook.teacher;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class ProfileWidget extends AppWidgetProvider {
    
    private static final String TAG                         = "Widget";
    public static String        ACTION_WIDGET_CONFIGURE_PRO = "ConfigureWidget_PRO";
    public static String        ACTION_WIDGET_CONFIGURE_CAL = "ConfigureWidget_CAL";
    public static String        ACTION_WIDGET_RECEIVER_UP   = "ActionReceiverWidget_Up";
    
    public final static int     FRAG_PROFILE                = 0;
    public final static int     FRAG_CLASSES                = 1;
    public final static int     FRAG_CALENDAR               = 2;
    public final static int     FRAG_MENSSAGE               = 3;
    public static final String  VALUE                       = "frag_selected";
    
    private boolean             isPortrait;
    private Intent              intent;
    private PendingIntent       pendingIntent;
    private RemoteViews         remoteViews;
    private AppWidgetManager    appWidgetManager;
    private int[]               appWidgetIds;
    private int                 i                           = 0;
    
    @Override
    public void onUpdate( Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds ) {
    
        Log.d(TAG, "onUpdate");
        
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        
        intent = new Intent(context, MainActivity.class);
        intent.setAction(ACTION_WIDGET_CONFIGURE_PRO);
        intent.putExtra(VALUE, FRAG_PROFILE);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.imageProfile, pendingIntent);
        
        intent = new Intent(context, MainActivity.class);
        intent.setAction(ACTION_WIDGET_CONFIGURE_CAL);
        intent.putExtra(VALUE, FRAG_CALENDAR);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.click_calendar, pendingIntent);
        
        intent = new Intent(context, ProfileWidget.class);
        intent.setAction(ACTION_WIDGET_RECEIVER_UP);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.button1, pendingIntent);
        
        Log.d(TAG, "onUpdate  - " + appWidgetIds);
        
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }
    
    @Override
    public void onReceive( Context context, Intent intent ) {
    
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_WIDGET_RECEIVER_UP)) {
            Log.d(TAG, "onReceive");
            i++;
        }
        
    }
}
