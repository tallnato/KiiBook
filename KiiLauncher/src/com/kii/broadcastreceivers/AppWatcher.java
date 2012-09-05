
package com.kii.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.kii.launcher.drawer.apps.database.AppsListDataSource;

public class AppWatcher extends BroadcastReceiver {
    
    @Override
    public void onReceive( Context context, Intent intent ) {
    
        if (intent.getAction() == Intent.ACTION_PACKAGE_ADDED) {
            Toast.makeText(context, "ACTION_PACKAGE_ADDED \n" + intent.getDataString() + "  " + intent, Toast.LENGTH_SHORT).show();
        } else if (intent.getAction() == Intent.ACTION_PACKAGE_REMOVED) {
            deletePackage(context, intent.getDataString().replace("package:", ""));
            
        } else if (intent.getAction() == Intent.ACTION_PACKAGE_REPLACED) {
            Toast.makeText(context, "ACTION_PACKAGE_REPLACED \n" + intent.getDataString().replace("package:", "") + " ", Toast.LENGTH_LONG)
                                            .show();
        } else if (intent.getAction() == Intent.ACTION_PACKAGE_CHANGED) {
            Toast.makeText(context, "ACTION_PACKAGE_REPLACED " + " " + intent, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, intent.toString(), Toast.LENGTH_SHORT).show();
        }
        System.out.println(intent.toString());
    }
    
    private void deletePackage( Context context, String packageName ) {
    
        AppsListDataSource alds = new AppsListDataSource(context);
        alds.open();
        
        alds.deletePackage(packageName);
        
        alds.close();
    }
}
