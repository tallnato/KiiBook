
package kii.kiibook.ParentalControl;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import java.util.Set;

public class FalconEyeService extends Service implements ParentalConstants {
    
    private final Messenger  mMessenger;
    private final Handler    mHandler;
    private static boolean   isRunning;
    private Set<String>      blockedApps;
    
    private ActivityManager  mActivityManager;
    
    public static final int  MSG_UPDATE_BLOCKED_APPS = 5;
    public static final int  MSG_CHECK_ON_TOP        = 6;
    
    private static final int CHECK_INTERVAL          = 300;
    
    public FalconEyeService() {
    
        super();
        
        mHandler = new IncomingHandler();
        mMessenger = new Messenger(mHandler);
        isRunning = false;
    }
    
    @Override
    public void onCreate() {
    
        super.onCreate();
        Log.d("MyService", "Service Started.");
        isRunning = true;
        
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    }
    
    @Override
    public int onStartCommand( Intent intent, int flags, int startId ) {
    
        super.onStartCommand(intent, flags, startId);
        Log.d("MyService", "Received start id " + startId + ": " + intent);
        
        mHandler.sendEmptyMessageDelayed(MSG_CHECK_ON_TOP, CHECK_INTERVAL);
        
        return START_STICKY;
    }
    
    public static boolean isRunning() {
    
        return isRunning;
    }
    
    @Override
    public IBinder onBind( Intent intent ) {
    
        return mMessenger.getBinder();
    }
    
    private String getOnTop() {
    
        String strTopPKName = mActivityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
        return strTopPKName;
    }
    
    private void startHome( Context context, String topActivity ) {
    
        Intent i = new Intent(context, NotifDialog.class);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_FORWARD_RESULT | Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(i);
        
    }
    
    private void updateBlockedApps() {
    
        // blockedApps = DataShared.getInstance().getBlockedApps();
        
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        blockedApps = settings.getStringSet(PREFS_NAME_BLOCKED_APPS, null);
        
        if (blockedApps != null) {
            Log.d("cenas", "num apps: " + blockedApps.size());
        } else {
            Log.d("cenas", "cenascenascenascenascenascenascenascenascenascenascenascenascenascenascenascenas");
        }
    }
    
    private boolean isAppBlocked( String packge ) {
    
        if (blockedApps == null || blockedApps.isEmpty()) {
            return false;
        }
        
        return blockedApps.contains(packge);
    }
    
    private void checkOnTop() {
    
        mHandler.sendEmptyMessageDelayed(MSG_CHECK_ON_TOP, CHECK_INTERVAL);
        
        String onTop = getOnTop();
        if (isAppBlocked(onTop)) {
            startHome(getApplicationContext(), onTop);
            System.out.println(onTop);
            try {
                Thread.sleep(2 * 1000);
            }
            catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            
            mActivityManager.killBackgroundProcesses(onTop);
        }
    }
    
    @Override
    public void onDestroy() {
    
        super.onDestroy();
        
        Log.d("MyService", "Service Started.");
        
        Toast.makeText(getApplicationContext(), "Ending service...", Toast.LENGTH_LONG).show();
        
        mHandler.removeMessages(MSG_CHECK_ON_TOP);
        mHandler.removeMessages(MSG_UPDATE_BLOCKED_APPS);
        
        Log.d("MyService", "Service Stopped.");
        isRunning = false;
    }
    
    private class IncomingHandler extends Handler { // Handler of incoming
    
        @Override
        public void handleMessage( Message msg ) {
        
            switch (msg.what) {
                case MSG_UPDATE_BLOCKED_APPS:
                    
                    updateBlockedApps();
                    break;
                
                case MSG_CHECK_ON_TOP:
                    
                    checkOnTop();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
