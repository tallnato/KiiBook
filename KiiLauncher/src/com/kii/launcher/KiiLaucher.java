
package com.kii.launcher;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.launcher.drawer.DrawerActivity;
import com.kii.launcher.drawer.apps.AppsFragmentIconAdapter;
import com.kii.launcher.drawer.apps.database.AppsListDataSource;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class KiiLaucher extends Activity {
    
    private ViewPager                            mViewPager;
    private PageViewAdapter                      mPagerAdapter;
    private CirclePageIndicator                  circleIndicator;
    private boolean                              mIsBound;
    
    private static ArrayList<PackagePermissions> homeScreen;
    
    private static Messenger                     mService = null;
    private static Messenger                     mMessenger;
    private static LauncherServiceConnection     mConnection;
    
    public KiiLaucher() {
    
        super();
        
        mMessenger = new Messenger(new LauncherHandler());
        mConnection = new LauncherServiceConnection();
    }
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kii_launcher);
        
        View v = findViewById(R.id.activity_kii_launcher_layout);
        v.setOnLongClickListener(new OnLongClickListener() {
            
            @Override
            public boolean onLongClick( View v ) {
            
                Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
                startActivity(Intent.createChooser(intent, "Select Wallpaper"));
                
                return true;
            }
        });
        
        setButtons();
        
        new InstalledAppsWorker().execute(this);
        
        /*AppWidgetManager awm = AppWidgetManager.getInstance(getApplicationContext());
        List<AppWidgetProviderInfo> list = awm.getInstalledProviders();
        System.out.println("size: " + list.size());
        for (AppWidgetProviderInfo widget : list) {
            System.out.println(widget);
        }*/
        
    }
    
    private void setHomeScreen() {
    
        AppsListDataSource appsDataSource = new AppsListDataSource(getApplicationContext());
        appsDataSource.open();
        List<PackagePermissions> list = appsDataSource.getAllApps();
        appsDataSource.close();
        
        if (homeScreen == null) {
            homeScreen = new ArrayList<PackagePermissions>();
            for (int i = 0; i < 8 * 3; i++) {
                homeScreen.add(list.get((int) (Math.random() * list.size())));
            }
        }
        
        circleIndicator = (CirclePageIndicator) findViewById(R.id.activity_kii_homescree_apps_indicator);
        mViewPager = (ViewPager) findViewById(R.id.activity_kii_homescree_apps_viewpager);
        
        mPagerAdapter = new PageViewAdapter();
        
        mViewPager.setAdapter(mPagerAdapter);
        
        circleIndicator.setViewPager(mViewPager);
        circleIndicator.setCurrentItem(1);
    }
    
    private void setButtons() {
    
        ImageView logo = (ImageView) findViewById(R.id.activity_kii_launcher_logo);
        logo.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_up, android.R.anim.fade_out)
                                                .toBundle();
                startActivity(new Intent(getApplicationContext(), DrawerActivity.class), bundle);
            }
        });
        
        View profile = findViewById(R.id.activity_kii_launcher_profile);
        profile.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Toast.makeText(getApplicationContext(), "mostrar prefil...", Toast.LENGTH_SHORT).show();
            }
        });
        
        TextView day = (TextView) findViewById(R.id.activity_kii_launcher_calendar_day);
        day.setText("" + Calendar.getInstance().get(Calendar.DATE));
        
        View calendarButton = findViewById(R.id.activity_kii_launcher_calendar);
        calendarButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Toast.makeText(getApplicationContext(), "mostrar calendário...", Toast.LENGTH_SHORT).show();
            }
        });
        
        day = (TextView) findViewById(R.id.activity_kii_launcher_calendar_notification_day);
        day.setText("" + Calendar.getInstance().get(Calendar.DATE));
        
        calendarButton = findViewById(R.id.activity_kii_launcher_calendar_notification);
        calendarButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                findViewById(R.id.activity_kii_launcher_calendar).setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);
                ((TextView) v.findViewById(R.id.activity_kii_launcher_calendar_notification_count)).setText("0");
                
                Toast.makeText(getApplicationContext(), "mostrar calendário notif...", Toast.LENGTH_SHORT).show();
            }
        });
        
        View messagesButton = findViewById(R.id.activity_kii_launcher_messages);
        messagesButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Toast.makeText(getApplicationContext(), "mostrar mensagens...", Toast.LENGTH_SHORT).show();
            }
        });
        
        messagesButton = findViewById(R.id.activity_kii_launcher_messages_notification);
        messagesButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                findViewById(R.id.activity_kii_launcher_messages).setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);
                ((TextView) v.findViewById(R.id.activity_kii_launcher_messages_notification_count)).setText("0");
                
                Toast.makeText(getApplicationContext(), "mostrar mensagens notif...", Toast.LENGTH_SHORT).show();
            }
        });
        
        View homeworkButton = findViewById(R.id.activity_kii_launcher_homework);
        homeworkButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Toast.makeText(getApplicationContext(), "mostrar tpc...", Toast.LENGTH_SHORT).show();
            }
        });
        
        homeworkButton = findViewById(R.id.activity_kii_launcher_homework_notification);
        homeworkButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                findViewById(R.id.activity_kii_launcher_homework).setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);
                ((TextView) v.findViewById(R.id.activity_kii_launcher_homework_notification_count)).setText("0");
                
                Toast.makeText(getApplicationContext(), "mostrar tpc notif...", Toast.LENGTH_SHORT).show();
            }
        });
        
        View newsButton = findViewById(R.id.activity_kii_launcher_news);
        newsButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Toast.makeText(getApplicationContext(), "mostrar news...", Toast.LENGTH_SHORT).show();
            }
        });
        
        newsButton = findViewById(R.id.activity_kii_launcher_news_notification);
        newsButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                findViewById(R.id.activity_kii_launcher_news).setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);
                ((TextView) v.findViewById(R.id.activity_kii_launcher_news_notification_count)).setText("0");
                
                Toast.makeText(getApplicationContext(), "mostrar news notif...", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    protected void onPause() {
    
        super.onPause();
        
        if (mIsBound) {
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null, KiiLauncherService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            
            unbindService(mConnection);
            mIsBound = false;
            System.out.println("Unbinding.");
        }
    }
    
    @Override
    protected void onResume() {
    
        super.onResume();
        
        TextView day = (TextView) findViewById(R.id.activity_kii_launcher_calendar_day);
        day.setText("" + Calendar.getInstance().get(Calendar.DATE));
        day = (TextView) findViewById(R.id.activity_kii_launcher_calendar_notification_day);
        day.setText("" + Calendar.getInstance().get(Calendar.DATE));
        
        System.out.println("binding.");
        
        bindService(new Intent(this, KiiLauncherService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
        
        if (mIsBound) {
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null, KiiLauncherService.MSG_GET_ALL_NOTIFICATION_COUNT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                }
                catch (RemoteException e) {}
            }
        }
    }
    
    private void playNotify() {
    
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onBackPressed() {
    
    }
    
    private class LauncherHandler extends Handler {
        
        @Override
        public void handleMessage( Message msg ) {
        
            switch (msg.what) {
            
                case KiiLauncherService.MSG_NEW_CALENDAR_NOTIFICATION: {
                    
                    findViewById(R.id.activity_kii_launcher_calendar_notification).setVisibility(View.VISIBLE);
                    TextView tv = (TextView) findViewById(R.id.activity_kii_launcher_calendar_notification_count);
                    
                    if (msg.arg1 >= 99) {
                        tv.setText(">99");
                    } else {
                        tv.setText(msg.arg1 + "");
                    }
                    
                    findViewById(R.id.activity_kii_launcher_calendar).setVisibility(View.GONE);
                    
                    break;
                }
                case KiiLauncherService.MSG_NEW_MESSAGES_NOTIFICATION: {
                    
                    findViewById(R.id.activity_kii_launcher_messages_notification).setVisibility(View.VISIBLE);
                    TextView tv = (TextView) findViewById(R.id.activity_kii_launcher_messages_notification_count);
                    int count;
                    try {
                        count = Integer.valueOf(tv.getText().toString());
                    }
                    catch (NumberFormatException e) {
                        count = 99;
                    }
                    if (count >= 99) {
                        tv.setText(">99");
                    } else {
                        tv.setText(msg.arg1 + "");
                    }
                    
                    findViewById(R.id.activity_kii_launcher_messages).setVisibility(View.GONE);
                    
                    break;
                }
                case KiiLauncherService.MSG_NEW_HOMEWORK_NOTIFICATION: {
                    findViewById(R.id.activity_kii_launcher_homework_notification).setVisibility(View.VISIBLE);
                    TextView tv = (TextView) findViewById(R.id.activity_kii_launcher_homework_notification_count);
                    int count;
                    try {
                        count = Integer.valueOf(tv.getText().toString());
                    }
                    catch (NumberFormatException e) {
                        count = 99;
                    }
                    if (count >= 99) {
                        tv.setText(">99");
                    } else {
                        tv.setText(msg.arg1 + "");
                    }
                    
                    findViewById(R.id.activity_kii_launcher_homework).setVisibility(View.GONE);
                    break;
                }
                case KiiLauncherService.MSG_NEW_NEWS_NOTIFICATION: {
                    
                    findViewById(R.id.activity_kii_launcher_news_notification).setVisibility(View.VISIBLE);
                    TextView tv = (TextView) findViewById(R.id.activity_kii_launcher_news_notification_count);
                    int count;
                    try {
                        count = Integer.valueOf(tv.getText().toString());
                    }
                    catch (NumberFormatException e) {
                        count = 99;
                    }
                    if (count >= 99) {
                        tv.setText(">99");
                    } else {
                        tv.setText(msg.arg1 + "");
                    }
                    
                    findViewById(R.id.activity_kii_launcher_news).setVisibility(View.GONE);
                    
                    break;
                }
                default:
                    super.handleMessage(msg);
                    return;
            }
            
            // playNotify();
        }
    }
    
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event ) {
    
        switch (keyCode) {
            case KeyEvent.KEYCODE_C: {
                
                Intent broadcast = new Intent();
                broadcast.putExtra(KiiLauncherService.NotificationCount, 1);
                broadcast.setAction(KiiLauncherService.CalendarBroadcast);
                sendBroadcast(broadcast);
                
                return true;
            }
            case KeyEvent.KEYCODE_V: {
                
                Intent broadcast = new Intent();
                broadcast.putExtra(KiiLauncherService.NotificationCount, 1);
                broadcast.setAction(KiiLauncherService.MesssageBroadcast);
                sendBroadcast(broadcast);
                
                return true;
            }
            case KeyEvent.KEYCODE_B: {
                
                Intent broadcast = new Intent();
                broadcast.putExtra(KiiLauncherService.NotificationCount, 1);
                broadcast.setAction(KiiLauncherService.HomeworkBroadcast);
                sendBroadcast(broadcast);
                
                return true;
            }
            case KeyEvent.KEYCODE_N: {
                
                Intent broadcast = new Intent();
                broadcast.putExtra(KiiLauncherService.NotificationCount, 1);
                broadcast.setAction(KiiLauncherService.NewsBroadcast);
                sendBroadcast(broadcast);
                
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    
    private class PageViewAdapter extends PagerAdapter {
        
        @Override
        public int getCount() {
        
            return 3;
        }
        
        @Override
        public Object instantiateItem( View collection, int position ) {
        
            LayoutInflater li = getLayoutInflater();
            GridView gridview = (GridView) li.inflate(R.layout.fragment_kii_drawer_apps_tabs, (ViewGroup) collection, false);
            gridview.setStackFromBottom(true);
            gridview.setAdapter(new AppsFragmentIconAdapter(KiiLaucher.this, R.layout.activity_kii_homescreen_iconlayout, homeScreen
                                            .subList(position * 8, (position + 1) * 8)));
            gridview.setNumColumns(8);
            
            ((ViewPager) collection).addView(gridview);
            
            return gridview;
        }
        
        @Override
        public void destroyItem( View collection, int position, Object view ) {
        
            ((ViewPager) collection).removeView((View) view);
        }
        
        @Override
        public boolean isViewFromObject( View view, Object object ) {
        
            return view == object;
        }
        
        @Override
        public int getItemPosition( Object object ) {
        
            return POSITION_NONE;
        }
    }
    
    private class InstalledAppsWorker extends AsyncTask<Context, Void, Void> {
        
        private ProgressDialog dialog;
        
        @Override
        protected void onPreExecute() {
        
            dialog = ProgressDialog.show(KiiLaucher.this, "", "Loading. Please wait...", true, false);
            dialog.show();
        }
        
        @Override
        protected Void doInBackground( Context... context ) {
        
            AppsListDataSource appsDataSource = new AppsListDataSource(context[0]);
            
            appsDataSource.open();
            
            if (appsDataSource.getCount() == 0) {
                getInstalledApps(context[0], appsDataSource);
            }
            
            appsDataSource.close();
            return null;
        }
        
        @Override
        protected void onPostExecute( Void result ) {
        
            dialog.dismiss();
            setHomeScreen();
        }
    }
    
    private void getInstalledApps( Context context, AppsListDataSource appsDataSource ) {
    
        PackageManager pm = context.getPackageManager();
        
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = pm.queryIntentActivities(mainIntent, 0);
        
        for (ResolveInfo ri : pkgAppsList) {
            
            appsDataSource.createPackagePermissions(ri.activityInfo.packageName, ri.activityInfo.name, ri.loadLabel(pm).toString(),
                                            ri.loadIcon(pm));
        }
    }
    
    private class LauncherServiceConnection implements ServiceConnection {
        
        @Override
        public void onServiceConnected( ComponentName className, IBinder service ) {
        
            mService = new Messenger(service);
            try {
                Message msg = Message.obtain(null, KiiLauncherService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void onServiceDisconnected( ComponentName className ) {
        
            mService = null;
        }
    };
}
