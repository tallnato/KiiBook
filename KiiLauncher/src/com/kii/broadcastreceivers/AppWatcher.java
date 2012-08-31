
package com.kii.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppWatcher extends BroadcastReceiver {
    
    @Override
    public void onReceive( Context context, Intent intent ) {
    
        // Toast.makeText(context, "cenas " + intent.getAction() + " " + intent,
        // Toast.LENGTH_SHORT).show();
    }
    
}
