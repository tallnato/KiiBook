
package kii.kiibook.ParentalControl;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;

public class ServiceCon implements ServiceConnection {
    
    private Messenger mService;
    
    public void onServiceConnected( ComponentName className, IBinder service ) {
    
        Log.d("ServiceCon", "Connected to service...");
        
        mService = new Messenger(service);
    }
    
    public void onServiceDisconnected( ComponentName className ) {
    
        // This is called when the connection with the service has been
        // unexpectedly disconnected - process crashed.
        mService = null;
        Log.e("ServiceCon", "" + className);
    }
    
    public Messenger getService() {
    
        return mService;
    }
}
