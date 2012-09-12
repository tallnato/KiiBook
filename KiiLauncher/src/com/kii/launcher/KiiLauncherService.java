
package com.kii.launcher;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.util.ArrayList;

public class KiiLauncherService extends Service {
    
    public final static String                CalendarBroadcast                   = "com.kii.broadcast.calendar";
    public final static String                MesssageBroadcast                   = "com.kii.broadcast.message";
    public final static String                HomeworkBroadcast                   = "com.kii.broadcast.homework";
    public final static String                NewsBroadcast                       = "com.kii.broadcast.news";
    
    public final static String                CalendarBroadcastClear              = "com.kii.broadcast.calendar.clear";
    public final static String                MesssageBroadcastClear              = "com.kii.broadcast.message.clear";
    public final static String                HomeworkBroadcastClear              = "com.kii.broadcast.homework.clear";
    public final static String                NewsBroadcastClear                  = "com.kii.broadcast.news.clear";
    
    public final static String                NotificationCount                   = "Notification:Count";
    
    private static int                        calendarNotification;
    private static int                        messagesNotification;
    private static int                        homeworkNotification;
    private static int                        newsNotification;
    
    public static final int                   MSG_GET_ALL_NOTIFICATION_COUNT      = 0x6900;
    public static final int                   MSG_GET_CALENDAR_NOTIFICATION_COUNT = 0x6901;
    public static final int                   MSG_GET_MESSAGES_NOTIFICATION_COUNT = 0x6902;
    public static final int                   MSG_GET_HOMEWORK_NOTIFICATION_COUNT = 0x6903;
    public static final int                   MSG_GET_NEWS_NOTIFICATION_COUNT     = 0x6904;
    
    public static final int                   MSG_NEW_CALENDAR_NOTIFICATION       = 0x7000;
    public static final int                   MSG_NEW_MESSAGES_NOTIFICATION       = 0x7001;
    public static final int                   MSG_NEW_HOMEWORK_NOTIFICATION       = 0x7002;
    public static final int                   MSG_NEW_NEWS_NOTIFICATION           = 0x7003;
    
    public static final int                   MSG_REGISTER_CLIENT                 = 0x101;
    public static final int                   MSG_UNREGISTER_CLIENT               = 0x102;
    
    private final ServiceReceiver             myReceiver                          = new ServiceReceiver();
    private final Messenger                   mMessenger                          = new Messenger(new ServiceHandler());
    private static final ArrayList<Messenger> mClients                            = new ArrayList<Messenger>();
    
    @Override
    public int onStartCommand( Intent intent, int flags, int startId ) {
    
        return START_STICKY;
    }
    
    @Override
    public void onCreate() {
    
        IntentFilter filter = new IntentFilter();
        filter.addAction(CalendarBroadcast);
        filter.addAction(MesssageBroadcast);
        filter.addAction(HomeworkBroadcast);
        filter.addAction(NewsBroadcast);
        filter.addAction(CalendarBroadcastClear);
        filter.addAction(MesssageBroadcastClear);
        filter.addAction(HomeworkBroadcastClear);
        filter.addAction(NewsBroadcastClear);
        registerReceiver(myReceiver, filter);
        
        System.out.println("onCreate...");
        
        Intent broadcast = new Intent("kii.falconeye.start");
        broadcast.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(broadcast);
    }
    
    @Override
    public IBinder onBind( Intent arg0 ) {
    
        return mMessenger.getBinder();
    }
    
    private static class ServiceHandler extends Handler {
        
        @Override
        public void handleMessage( Message msg ) {
        
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_GET_ALL_NOTIFICATION_COUNT:
                    try {
                        msg.replyTo.send(Message.obtain(null, MSG_NEW_CALENDAR_NOTIFICATION, calendarNotification, 0));
                        msg.replyTo.send(Message.obtain(null, MSG_NEW_MESSAGES_NOTIFICATION, messagesNotification, 0));
                        msg.replyTo.send(Message.obtain(null, MSG_NEW_HOMEWORK_NOTIFICATION, homeworkNotification, 0));
                        msg.replyTo.send(Message.obtain(null, MSG_NEW_NEWS_NOTIFICATION, newsNotification, 0));
                    }
                    catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    
                    break;
                case MSG_GET_CALENDAR_NOTIFICATION_COUNT:
                    try {
                        msg.replyTo.send(Message.obtain(null, MSG_NEW_CALENDAR_NOTIFICATION, calendarNotification, 0));
                        
                    }
                    catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_GET_MESSAGES_NOTIFICATION_COUNT:
                    try {
                        msg.replyTo.send(Message.obtain(null, MSG_NEW_MESSAGES_NOTIFICATION, messagesNotification, 0));
                        
                    }
                    catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_GET_HOMEWORK_NOTIFICATION_COUNT:
                    try {
                        msg.replyTo.send(Message.obtain(null, MSG_NEW_HOMEWORK_NOTIFICATION, homeworkNotification, 0));
                        
                    }
                    catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_GET_NEWS_NOTIFICATION_COUNT:
                    try {
                        msg.replyTo.send(Message.obtain(null, MSG_NEW_NEWS_NOTIFICATION, newsNotification, 0));
                        
                    }
                    catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
    
    private class ServiceReceiver extends BroadcastReceiver {
        
        @Override
        public void onReceive( Context context, Intent intent ) {
        
            int i = 1;
            if (intent.getExtras() != null && intent.getExtras().containsKey(NotificationCount)) {
                i = intent.getExtras().getInt(NotificationCount, 0);
            }
            String action = intent.getAction();
            if (action.equals(CalendarBroadcast)) {
                calendarNotification += i;
                try {
                    for (Messenger client : mClients) {
                        client.send(Message.obtain(null, MSG_NEW_CALENDAR_NOTIFICATION, calendarNotification, 0));
                    }
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else if (action.equals(MesssageBroadcast)) {
                messagesNotification += i;
                try {
                    for (Messenger client : mClients) {
                        client.send(Message.obtain(null, MSG_NEW_MESSAGES_NOTIFICATION, messagesNotification, 0));
                    }
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else if (action.equals(HomeworkBroadcast)) {
                homeworkNotification += i;
                try {
                    for (Messenger client : mClients) {
                        client.send(Message.obtain(null, MSG_NEW_HOMEWORK_NOTIFICATION, homeworkNotification, 0));
                    }
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else if (action.equals(NewsBroadcast)) {
                newsNotification += i;
                try {
                    for (Messenger client : mClients) {
                        client.send(Message.obtain(null, MSG_NEW_NEWS_NOTIFICATION, newsNotification, 0));
                    }
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else if (action.equals(CalendarBroadcastClear)) {
                calendarNotification = 0;
            } else if (action.equals(MesssageBroadcastClear)) {
                messagesNotification = 0;
            } else if (action.equals(HomeworkBroadcastClear)) {
                homeworkNotification = 0;
            } else if (action.equals(NewsBroadcastClear)) {
                newsNotification = 0;
            }
            
            System.out.println(calendarNotification + " " + messagesNotification + " " + homeworkNotification + " " + newsNotification);
        }
    }
    
    @Override
    public void onDestroy() {
    
        super.onDestroy();
        
        System.out.println("onDestry...");
        unregisterReceiver(myReceiver);
    }
}
