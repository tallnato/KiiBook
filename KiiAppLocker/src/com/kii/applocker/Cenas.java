package com.kii.applocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Cenas extends BroadcastReceiver {
    
    @Override
    public void onReceive( Context context, Intent intent ) {
        
        Toast.makeText(context, intent.getAction(), Toast.LENGTH_LONG).show();
        Log.d("cenas", intent.getAction());
    }
    
}
