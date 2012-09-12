
package com.kii.applocker;

import android.app.ActivityManager;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class AppLockerService extends Service {
    
    public final static String    PREFS_NAME                    = "AppLocker:Prefs";
    public final static String    blockedAppsKey                = "Key:BlockedApps";
    
    public final static String    PARENTAL_BROADCAST            = "com.kii.falconeye.parental";
    public final static String    TEACHER_BROADCAST             = "com.kii.falconeye.teacher";
    
    public final static int       MSG_SET_BLOCKED_APPS_PARENTAL = 0x700;
    
    public final static int       MSG_CHECK_ON_TOP              = 0x800;
    private static final int      CHECK_INTERVAL                = 300;
    
    private final Messenger       mMessenger;
    private final Handler         mHandler;
    private ActivityManager       mActivityManager;
    
    private ArrayList<String>     blockedApps;
    private boolean               classMode;
    
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
        
        classMode = false;
        
        setBlockedAppsSharedPreferences();
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(PARENTAL_BROADCAST);
        filter.addAction(TEACHER_BROADCAST);
        registerReceiver(myReceiver, filter);
    }
    
    @Override
    public void onDestroy() {
    
        super.onDestroy();
        
        mHandler.removeMessages(MSG_CHECK_ON_TOP);
        mHandler.removeMessages(MSG_SET_BLOCKED_APPS_PARENTAL);
        
        Log.e("cenas", "Service Stopped.");
    }
    
    @Override
    public IBinder onBind( Intent intent ) {
    
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
                case MSG_SET_BLOCKED_APPS_PARENTAL:
                    
                    if (classMode) {
                        Log.e("cenas", "in class mode, cannot overide...");
                        break;
                    }
                    
                    Bundle b = msg.getData();
                    if (b == null) {
                        Toast.makeText(getApplicationContext(), "No bundle received...", Toast.LENGTH_SHORT).show();
                        log("No bundle received...");
                        break;
                    }
                    if (!b.containsKey(blockedAppsKey)) {
                        
                        Toast.makeText(getApplicationContext(), "No apps received in bundle...", Toast.LENGTH_SHORT).show();
                        log("No apps received in bundle...");
                        break;
                    }
                    
                    blockedApps = b.getStringArrayList(blockedAppsKey);
                    log("blocked apps setted activity..." + blockedApps);
                    
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_MULTI_PROCESS);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putStringSet(blockedAppsKey, new HashSet<String>(blockedApps));
                    editor.commit();
                    
                    break;
                
                default:
                    super.handleMessage(msg);
            }
        }
    }
    
    private void setBlockedAppsSharedPreferences() {
    
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
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
            } else if (action.equals(TEACHER_BROADCAST)) {
                
                classMode = true;
                Log.e("cenas", "teacher mode started...");
                
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
                log("blockedApps setted broadcast  " + blockedApps);
            }
        }
    }
    
    private void log( String msg ) {
    
        Log.d(AppLockerService.class.toString(), msg);
    }
}
