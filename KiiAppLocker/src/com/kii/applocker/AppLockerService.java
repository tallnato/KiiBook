
package com.kii.applocker;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;

public class AppLockerService extends Service {
    
    public final static String    PREFS_NAME                    = "AppLocker:Prefs";
    public final static String    blockedAppsKey                = "Key:BlockedApps";
    public final static String    stateKey                      = "Key:State";
    public final static String    tempBlockedAppsKey            = "Key:Temp";
    
    public final static String    PARENTAL_BROADCAST            = "com.kii.falconeye.parental";
    public final static String    TEACHER_BROADCAST             = "com.kii.falconeye.teacher";
    public final static String    STATE_BROADCAST               = "kiibook.applocker";
    
    public final static int       MSG_SET_BLOCKED_APPS_PARENTAL = 0x700;
    
    public final static int       MSG_CHECK_ON_TOP              = 0x800;
    private static final int      CHECK_INTERVAL                = 300;
    
    private final Messenger       mMessenger;
    private final Handler         mHandler;
    private ActivityManager       mActivityManager;
    
    private ArrayList<String>     blockedApps;
    private static boolean        classMode                     = false;
    
    private final static int      ParentalID                    = 0x500;
    private final static int      TeacherID                     = 0x501;
    private final static int      ParentalUpdateID              = 0x510;
    private final static int      TeacherUpdateID               = 0x511;
    
    private final ServiceReceiver myReceiver                    = new ServiceReceiver();
    
    public AppLockerService() {
        
        super();
        mHandler = new IncomingHandler();
        mMessenger = new Messenger(mHandler);
    }
    
    @Override
    public int onStartCommand( Intent intent, int flags, int startId ) {
        
        log("Received start id " + startId + ": " + intent);
        return START_STICKY;
    }
    
    @Override
    public void onCreate() {
        
        super.onCreate();
        log("Service Started.");
        Log.e("cenas", "Service Started.");
        mHandler.sendEmptyMessageDelayed(MSG_CHECK_ON_TOP, CHECK_INTERVAL);
        
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        
        
        setBlockedAppsSharedPreferences();
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(PARENTAL_BROADCAST);
        filter.addAction(TEACHER_BROADCAST);
        registerReceiver(myReceiver, filter);
        
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        
        Notification notification = new Notification.Builder(this).setContentTitle("AppLocker a correr").setTicker("Modo aula activo")
                                        .setContentText("oncreate")
                                        .setSmallIcon(android.R.drawable.ic_lock_lock).setAutoCancel(true)
                                        .build();
        
        notificationManager.notify(50, notification);
        
        Intent intent = new Intent();
        intent.setAction(STATE_BROADCAST);
        sendBroadcast(intent);
        
        classMode = false;
        if (!classMode) {
            notifyParental();
        } else {
            notifyTeacher();
        }
    }
    
    @Override
    public void onDestroy() {
        
        super.onDestroy();
        
        mHandler.removeMessages(MSG_CHECK_ON_TOP);
        mHandler.removeMessages(MSG_SET_BLOCKED_APPS_PARENTAL);
        
        Log.e("cenas", "Service Stopped.");
        Log.d("cenas", "Service Stopped.");
        
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        
        Notification noti = new Notification.Builder(this).setSmallIcon(R.drawable.ic_launcher).setTicker("Ticker...")
                                        .setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentTitle("Serviço destruido!")
                                        .setOnlyAlertOnce(true).build();
        
        notificationManager.notify(80, noti);
        
        unregisterReceiver(myReceiver);
    }
    
    @Override
    public IBinder onBind( Intent intent ) {
        
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        
        
        Notification notification = new Notification.Builder(this).setContentTitle("AppLocker a correr").setTicker("Modo aula activo")
                                        .setContentText("onbind").setSmallIcon(android.R.drawable.ic_lock_lock).setAutoCancel(true)
                                        .build();
        
        notificationManager.notify(60, notification);
        
        return mMessenger.getBinder();
    }
    
    private void checkOnTop() {
        
        String onTop = getOnTop();
        if (isAppBlocked(onTop)) {
            startHome(getApplicationContext(), onTop);
            
            mActivityManager.killBackgroundProcesses(onTop);
        }
    }
    
    private String getOnTop() {
        
        String strTopPKName = mActivityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
        return strTopPKName;
    }
    
    private boolean isAppBlocked( String packge ) {
        
        if (blockedApps == null || blockedApps.isEmpty()) {
            return false;
        }
        
        if (packge.contains("kiibook.kiibookreader")) {
            return false;
        }
        if (packge.contains("com.kii.applocker")) {
            return false;
        }
        
        return blockedApps.contains(packge);
    }
    
    private void startHome( Context context, String topActivity ) {
        
        Intent i = new Intent(context, NotifDialog.class);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_FORWARD_RESULT | Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(i);
    }
    
    private class IncomingHandler extends Handler {
        
        @Override
        public void handleMessage( Message msg ) {
            
            switch (msg.what) {
                case MSG_CHECK_ON_TOP:
                    checkOnTop();
                    
                    mHandler.sendEmptyMessageDelayed(MSG_CHECK_ON_TOP, CHECK_INTERVAL);
                    
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
    
    private void setBlockedAppsSharedPreferences() {
        
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_MULTI_PROCESS);
        if (settings.contains(blockedAppsKey)) {
            blockedApps = new ArrayList<String>(settings.getStringSet(blockedAppsKey, new HashSet<String>()));
        } else {
            blockedApps = null;
        }
        
        log("setBlockedAppsSharedPreferences ");
    }
    
    
    private class ServiceReceiver extends BroadcastReceiver {
        
        @Override
        public void onReceive( Context context, Intent intent ) {
            
            String action = intent.getAction();
            
            if (action.equals(PARENTAL_BROADCAST)) {
                setBlockedAppsSharedPreferences();
                
                classMode = false;
                Log.e("cenas", "parental mode started...");
                notifyParental();
                
                notifyBlockedAppsParental();
            } else if (action.equals(TEACHER_BROADCAST)) {
                
                classMode = true;
                Log.e("cenas", "teacher mode started...");
                
                notifyTeacher();
                
                Bundle b = intent.getExtras();
                if (b == null) {
                    log("bundle null");
                    return;
                }
                if (!b.containsKey(blockedAppsKey)) {
                    log("no key null");
                    return;
                }
                
                
                blockedApps = b.getStringArrayList(blockedAppsKey);
                // log("blockedApps setted broadcast  " + blockedApps);
                
                notifyBlockedAppsTeacher();
            }
        }
    }
    
    private void log( String msg ) {
        
        Log.d(AppLockerService.class.toString(), msg);
    }
    
    private void notifyParental() {
        
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                                        50, new Intent(this, AppLocker.class),
                                        0);
        
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(TeacherID);
        
        Notification notification = new Notification.Builder(this)
        .setContentTitle("Controlo parental activo ")
        .setTicker("Controlo parental activo")
        .setSmallIcon(R.drawable.ic_launcher)
        .setAutoCancel(false)
        .setOngoing(true)
        .setContentIntent(contentIntent)
        .build();
        
        notificationManager.notify(ParentalID, notification);
    }
    
    private void notifyBlockedAppsParental() {
        
        
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(ParentalUpdateID);
        notificationManager.cancel(TeacherUpdateID);
        
        String title = "Modo Parental";
        String text = "Aplicações bloqueadas actualizadas...";
        Notification notification = new Notification.Builder(this).setContentTitle(title).setTicker(title).setContentText(text)
                                        .setSmallIcon(R.drawable.ic_launcher)
                                        .build();
        
        notificationManager.notify(ParentalUpdateID, notification);
    }
    
    private void notifyBlockedAppsTeacher() {
        
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(ParentalUpdateID);
        notificationManager.cancel(TeacherUpdateID);
        
        String title = "Modo Aula";
        String text = "Aplicações bloqueadas actualizadas...";
        Notification notification = new Notification.Builder(this).setContentTitle(title).setTicker(title).setContentText(text)
                                        .setSmallIcon(R.drawable.ic_launcher).build();
        
        notificationManager.notify(TeacherUpdateID, notification);
    }
    
    private void notifyTeacher() {
        
        
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(ParentalID);
        
        Notification notification = new Notification.Builder(this)
        .setContentTitle("Modo aula activo")
        .setTicker("Modo aula activo")
        .setSmallIcon(R.drawable.ic_launcher)
        .setAutoCancel(false)
        .setOngoing(true)
        .build();
        
        notificationManager.notify(TeacherID, notification);
    }
}
