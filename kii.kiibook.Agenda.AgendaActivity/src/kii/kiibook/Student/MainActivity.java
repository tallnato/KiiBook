
package kii.kiibook.Student;

import static util.Constants.TAG;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import falconeye.tcp.server.ComunicationManager;
import falconeye.udp.UdpListener;
import objects.Student;
import util.Constants;
import kii.kiibook.Student.adapters_items.SectionsPagerAdapter;
import kii.kiibook.Student.database.DataShared;

public class MainActivity extends FragmentActivity implements Constants {
    
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager            mViewPager;
    private MyTabListener        mTabListener;
    private ActionBar            actionBar;
    private UdpListener          ul;
    private Student              me;
    private Thread               t;
    private String               ip;
    
    private Intent               service;
    protected MenuItem           menuItem;
    
    public ComunicationManager   mComManager;
    protected Messenger          mService   = null;
    final Messenger              mMessenger = new Messenger(new ActivityServiceHandler());
    private boolean              mIsBound;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        this.me = DataShared.getInstance().getMyProfile();
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        bindService(new Intent(this, CommunicationService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
        
        // Set up the action bar.
        actionBar = getActionBar();
        
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
        
        // Create the adapter that will return a fragment for each section
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            
            @Override
            public void onPageSelected( int position ) {
            
                actionBar.setSelectedNavigationItem(position);
            }
        });
        
        mTabListener = new MyTabListener(mViewPager);
        
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setTabListener(mTabListener).setIcon(mSectionsPagerAdapter.getFragmentIcon(i)));
        }
    }
    
    @Override
    public void onDestroy() {
    
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            doUnbindService();
        }
        catch (Throwable t) {
            Log.e(TAG, "Failed to unbind from the service", t);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
    
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    void doUnbindService() {
    
        if (mIsBound) {
            // If we have received the service, and hence registered with it,
            // then now is the time to unregister.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null, CommunicationService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                }
                catch (RemoteException e) {
                    // There is nothing special we need to do if the service has
                    // crashed.
                }
            }
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
            
        }
    }
    
    public class ActivityServiceHandler extends Handler {
        
        public static final int UPDATE_LIST = 90;
        
        @Override
        public void handleMessage( Message msg ) {
        
            switch (msg.what) {
                case CommunicationService.MSG_FALCONEYE_TURNOFF:
                    Toast.makeText(getApplicationContext(), "DISABLE FALCON EYE" + TAG, Toast.LENGTH_SHORT).show();
                    
                    doUnbindService();
                    
                    break;
                
                default:
                    // Toast.makeText(getApplicationContext(), "nothing... " +
                    // msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    super.handleMessage(msg);
            }
        }
        
    }
    
    private final ServiceConnection mConnection = new ServiceConnection() {
                                                    
                                                    public void onServiceConnected( ComponentName className, IBinder service ) {
                                                    
                                                        mService = new Messenger(service);
                                                        
                                                        try {
                                                            Message msg = Message.obtain(null, CommunicationService.MSG_REGISTER_CLIENT);
                                                            msg.replyTo = mMessenger;
                                                            mService.send(msg);
                                                        }
                                                        catch (RemoteException e) {
                                                            // In this case the
                                                            // service has
                                                            // crashed before we
                                                            // could even do
                                                            // anything with it
                                                        }
                                                    }
                                                    
                                                    public void onServiceDisconnected( ComponentName className ) {
                                                    
                                                        // This is called when
                                                        // the connection with
                                                        // the service has been
                                                        // unexpectedly
                                                        // disconnected -
                                                        // process crashed.
                                                        mService = null;
                                                        
                                                    }
                                                };
}
