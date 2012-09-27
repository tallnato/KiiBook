
package kii.kiibook.Student;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppWatcher extends BroadcastReceiver {
    
    private Intent serviceIntent;
    
    @Override
    public void onReceive( Context context, Intent intent ) {
    
        serviceIntent = new Intent(context, CommunicationService.class);
        context.startService(serviceIntent);
    }
}
