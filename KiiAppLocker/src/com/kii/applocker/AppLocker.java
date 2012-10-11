
package com.kii.applocker;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppLocker extends ListActivity {
    
    private PermissionsArrayAdapter mAdapter;
    final Messenger                 mMessenger      = new Messenger(new IncomingHandler());
    /*private Messenger               mService        = null;
    private final ServiceConnection mConnection     = new AppLockerServiceConnection();*/
    
    private final int               PASSWORD_DIALOG = 0x01;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        
        super.onCreate(savedInstanceState);
        
        
        SharedPreferences settings = getSharedPreferences(AppLockerService.PREFS_NAME, Context.MODE_MULTI_PROCESS);
        Set<String> temp = null;
        if (settings != null && settings.contains(AppLockerService.blockedAppsKey)) {
            temp = settings.getStringSet(AppLockerService.blockedAppsKey, null);
        }
        
        mAdapter = new PermissionsArrayAdapter(getApplicationContext(), 0, getInstalledApps(), temp);
        setListAdapter(mAdapter);
        
        getListView().setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
                
                PackagePermissions pp = mAdapter.getItem(arg2);
                pp.setBlocked(!pp.isBlocked());
                
                CheckBox cb = (CheckBox) arg1.findViewById(R.id.checkBox);
                cb.setChecked(pp.isBlocked());
            }
        });
        
        startService(new Intent(this, AppLockerService.class));
    }
    
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        
        if (requestCode == PASSWORD_DIALOG) {
            
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }
    
    @Override
    public void onStart() {
        
        super.onStart();
        
        startActivityForResult(new Intent(getApplicationContext(), PasswordDialog.class), PASSWORD_DIALOG);
        // bindService(new Intent(this, AppLockerService.class), mConnection,
        // Context.BIND_AUTO_CREATE);
    }
    
    @Override
    public void onPause() {
        
        super.onPause();
        
        /*try {
            Message msg = Message.obtain(null, AppLockerService.MSG_SET_BLOCKED_APPS_PARENTAL);
            Bundle b = new Bundle();
            b.putStringArrayList(AppLockerService.blockedAppsKey, mAdapter.getBlockedApps());
            msg.setData(b);
            
            mService.send(msg);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        
        unbindService(mConnection);*/
        
        
        SharedPreferences settings = getSharedPreferences(AppLockerService.PREFS_NAME, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(AppLockerService.blockedAppsKey, new HashSet<String>(mAdapter.getBlockedApps()));
        editor.commit();
        
        
        Intent intent = new Intent();
        intent.setAction(AppLockerService.PARENTAL_BROADCAST);
        sendBroadcast(intent);
    }
    
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        
        getMenuInflater().inflate(R.menu.activity_app_locker, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        
        switch (item.getItemId()) {
            case R.id.menu_select_all:
                mAdapter.selectAll();
                return true;
            case R.id.menu_unselect_all:
                mAdapter.unSelectAll();
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private List<PackagePermissions> getInstalledApps() {
        
        PackageManager pm = getPackageManager();
        ArrayList<PackagePermissions> res = new ArrayList<PackagePermissions>();
        
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        
        List<ResolveInfo> pkgAppsList = pm.queryIntentActivities(mainIntent, 0);
        
        for (ResolveInfo ri : pkgAppsList) {
            
            if (ri.loadLabel(pm).toString().contains("KiiLauncher") || ri.loadLabel(pm).toString().contains("AppLocker")) {
                continue;
            }
            PackagePermissions newInfo = new PackagePermissions(ri.loadLabel(pm).toString(), ri.activityInfo.packageName, ri.loadIcon(pm));
            newInfo.setBlocked(true);
            
            res.add(newInfo);
        }
        
        Collections.sort(res);
        
        return res;
    }
    
    private class IncomingHandler extends Handler {
        
        @Override
        public void handleMessage( Message msg ) {
            
            switch (msg.what) {
                default:
                    super.handleMessage(msg);
            }
        }
    }
    
    /*private class AppLockerServiceConnection implements ServiceConnection {
        
        @Override
        public void onServiceConnected( ComponentName className, IBinder service ) {
            
            mService = new Messenger(service);
        }
        
        @Override
        public void onServiceDisconnected( ComponentName className ) {
            
            mService = null;
        }
    };*/
    
}
