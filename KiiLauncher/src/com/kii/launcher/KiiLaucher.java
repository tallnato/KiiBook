
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
import com.kii.launcher.drawer.apps.database.AppsListDataSource;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class KiiLaucher extends Activity {
    
    private ViewPager                            mViewPager;
    private PageViewAdapter                      mPagerAdapter;
    private CirclePageIndicator                  circleIndicator;
    private static boolean                       mIsBound;
    
    private static ArrayList<PackagePermissions> centerScreen;
    private static ArrayList<PackagePermissions> rightScreen;
    private static ArrayList<PackagePermissions> leftScreen;
    
    private static Messenger                     mService = null;
    private static Messenger                     mMessenger;
    private static LauncherServiceConnection     mConnection;
    
    private ProgressDialog                       dialog;
    
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
        
        Intent i = new Intent();
        i.setClassName("com.kii.applocker", "com.kii.applocker.AppLockerService");
        startService(i);
    }
    
    private void setHomeScreen() {
    
        AppsListDataSource appsDataSource = new AppsListDataSource(getApplicationContext());
        appsDataSource.open();
        List<PackagePermissions> list = appsDataSource.getAllApps();
        appsDataSource.close();
        
        if (centerScreen == null || rightScreen == null || leftScreen == null) {
            centerScreen = new ArrayList<PackagePermissions>();
            rightScreen = new ArrayList<PackagePermissions>();
            leftScreen = new ArrayList<PackagePermissions>();
            
            for (PackagePermissions pp : list) {
                
                if (pp.getLabel().equals("Calculadora") || pp.getLabel().equals("Chrome") || pp.getLabel().equals("Gmail")
                                                || pp.getLabel().equals("Polaris Office") || pp.getLabel().equals("KiiRoom")
                                                || pp.getLabel().equals("Dropbox") || pp.getLabel().equals("Evernote")
                                                || pp.getLabel().equals("Gestor de Turma") || pp.getLabel().equals("Area das Disciplinas")) {
                    centerScreen.add(pp);
                }
                
                if (pp.getLabel().equals("Angry Birds") || pp.getLabel().equals("Logo quiz") || pp.getLabel().equals("Português")
                                                || pp.getLabel().equals("iMathematics") || pp.getLabel().equals("Jewels")
                                                || pp.getLabel().contains("Portuguese English")
                                                || pp.getLabel().equals("Visual Anatomy Free") || pp.getLabel().equals("Tradutor")) {
                    rightScreen.add(pp);
                }
                if (pp.getLabel().equals("Google+") || pp.getLabel().equals("Reader") || pp.getLabel().equals("IMDb")
                                                || pp.getLabel().equals("KiiMarket") || pp.getLabel().equals("YouTube")
                                                || pp.getLabel().equals("Facebook") || pp.getLabel().equals("Skype")
                                                || pp.getLabel().equals("Play Music")) {
                    leftScreen.add(pp);
                }
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
            
                Intent i;
                PackageManager manager = getPackageManager();
                i = manager.getLaunchIntentForPackage("kii.profile");
                if (i == null) {
                    Toast.makeText(getApplicationContext(), "Perfil não instalado...", Toast.LENGTH_SHORT).show();
                    return;
                }
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_up, android.R.anim.fade_out)
                                                .toBundle();
                
                startActivity(i, bundle);
            }
        });
        
        TextView day = (TextView) findViewById(R.id.activity_kii_launcher_calendar_day);
        day.setText("" + Calendar.getInstance().get(Calendar.DATE));
        
        View calendarButton = findViewById(R.id.activity_kii_launcher_calendar);
        calendarButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Intent i;
                PackageManager manager = getPackageManager();
                i = manager.getLaunchIntentForPackage("kii.kiibook.Student");
                if (i == null) {
                    i = manager.getLaunchIntentForPackage("com.android.calendar");
                    
                    if (i == null) {
                        Toast.makeText(getApplicationContext(), "Agenda não instalada...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_up, android.R.anim.fade_out)
                                                .toBundle();
                
                startActivity(i, bundle);
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
                
                Intent broadcast = new Intent();
                broadcast.setAction(KiiLauncherService.CalendarBroadcastClear);
                sendBroadcast(broadcast);
                
                Intent i;
                PackageManager manager = getPackageManager();
                i = manager.getLaunchIntentForPackage("kii.kiibook.Student");
                if (i == null) {
                    i = manager.getLaunchIntentForPackage("com.android.calendar");
                    
                    if (i == null) {
                        Toast.makeText(getApplicationContext(), "Agenda não instalada...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_up, android.R.anim.fade_out)
                                                .toBundle();
                
                startActivity(i, bundle);
            }
        });
        
        View messagesButton = findViewById(R.id.activity_kii_launcher_messages);
        messagesButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Intent i;
                PackageManager manager = getPackageManager();
                i = manager.getLaunchIntentForPackage("com.google.android.talk");
                
                if (i == null) {
                    Toast.makeText(getApplicationContext(), "GTalk não instalado...", Toast.LENGTH_SHORT).show();
                    return;
                }
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_up, android.R.anim.fade_out)
                                                .toBundle();
                
                startActivity(i, bundle);
            }
        });
        
        messagesButton = findViewById(R.id.activity_kii_launcher_messages_notification);
        messagesButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                findViewById(R.id.activity_kii_launcher_messages).setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);
                ((TextView) v.findViewById(R.id.activity_kii_launcher_messages_notification_count)).setText("0");
                
                Intent i;
                PackageManager manager = getPackageManager();
                i = manager.getLaunchIntentForPackage("com.google.android.talk");
                
                if (i == null) {
                    Toast.makeText(getApplicationContext(), "GTalk não instalado...", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                Intent broadcast = new Intent();
                broadcast.setAction(KiiLauncherService.MesssageBroadcastClear);
                sendBroadcast(broadcast);
                
                Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_up, android.R.anim.fade_out)
                                                .toBundle();
                
                startActivity(i, bundle);
                
            }
        });
        
        View homeworkButton = findViewById(R.id.activity_kii_launcher_homework);
        homeworkButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Intent i;
                PackageManager manager = getPackageManager();
                i = manager.getLaunchIntentForPackage("kii.kiibook.Student");
                if (i == null) {
                    i = manager.getLaunchIntentForPackage("com.android.calendar");
                    
                    if (i == null) {
                        Toast.makeText(getApplicationContext(), "Google Calendar não instalado...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_up, android.R.anim.fade_out)
                                                .toBundle();
                startActivity(i, bundle);
            }
        });
        
        homeworkButton = findViewById(R.id.activity_kii_launcher_homework_notification);
        homeworkButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                findViewById(R.id.activity_kii_launcher_homework).setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);
                ((TextView) v.findViewById(R.id.activity_kii_launcher_homework_notification_count)).setText("0");
                
                Intent i;
                PackageManager manager = getPackageManager();
                i = manager.getLaunchIntentForPackage("kii.kiibook.Student");
                if (i == null) {
                    i = manager.getLaunchIntentForPackage("com.android.calendar");
                    
                    if (i == null) {
                        Toast.makeText(getApplicationContext(), "Google Calendar não instalado...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_up, android.R.anim.fade_out)
                                                .toBundle();
                startActivity(i, bundle);
                
                Intent broadcast = new Intent();
                broadcast.setAction(KiiLauncherService.HomeworkBroadcastClear);
                sendBroadcast(broadcast);
            }
        });
        
        View newsButton = findViewById(R.id.activity_kii_launcher_news);
        newsButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Intent i;
                PackageManager manager = getPackageManager();
                i = manager.getLaunchIntentForPackage("com.google.android.apps.currents");
                if (i == null) {
                    Toast.makeText(getApplicationContext(), "Google Currents não instalado...", Toast.LENGTH_SHORT).show();
                    return;
                }
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_up, android.R.anim.fade_out)
                                                .toBundle();
                startActivity(i, bundle);
            }
        });
        
        newsButton = findViewById(R.id.activity_kii_launcher_news_notification);
        newsButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                findViewById(R.id.activity_kii_launcher_news).setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);
                ((TextView) v.findViewById(R.id.activity_kii_launcher_news_notification_count)).setText("0");
                
                Intent i;
                PackageManager manager = getPackageManager();
                i = manager.getLaunchIntentForPackage("com.google.android.apps.currents");
                
                if (i == null) {
                    Toast.makeText(getApplicationContext(), "Google Currents não instalado...", Toast.LENGTH_SHORT).show();
                    return;
                }
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_up, android.R.anim.fade_out)
                                                .toBundle();
                startActivity(i, bundle);
                Intent broadcast = new Intent();
                broadcast.setAction(KiiLauncherService.NewsBroadcastClear);
                sendBroadcast(broadcast);
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
    
    @Override
    protected void onStop() {
    
        super.onStop();
        
        dialog = null;
    }
    
    private void playNotify() {
    
        /*try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    
    @Override
    public void onBackPressed() {
    
    }
    
    private class LauncherHandler extends Handler {
        
        @Override
        public void handleMessage( Message msg ) {
        
            switch (msg.what) {
            
                case KiiLauncherService.MSG_NEW_CALENDAR_NOTIFICATION: {
                    
                    if (msg.arg1 == 0) {
                        findViewById(R.id.activity_kii_launcher_calendar_notification).setVisibility(View.GONE);
                        findViewById(R.id.activity_kii_launcher_calendar).setVisibility(View.VISIBLE);
                        break;
                    }
                    
                    findViewById(R.id.activity_kii_launcher_calendar_notification).setVisibility(View.VISIBLE);
                    TextView tv = (TextView) findViewById(R.id.activity_kii_launcher_calendar_notification_count);
                    
                    if (msg.arg1 >= 99) {
                        tv.setText(">99");
                    } else {
                        tv.setText(msg.arg1 + "");
                    }
                    
                    findViewById(R.id.activity_kii_launcher_calendar).setVisibility(View.GONE);
                    playNotify();
                    break;
                }
                case KiiLauncherService.MSG_NEW_MESSAGES_NOTIFICATION: {
                    
                    if (msg.arg1 == 0) {
                        findViewById(R.id.activity_kii_launcher_messages_notification).setVisibility(View.GONE);
                        findViewById(R.id.activity_kii_launcher_messages).setVisibility(View.VISIBLE);
                        break;
                    }
                    
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
                    playNotify();
                    break;
                }
                case KiiLauncherService.MSG_NEW_HOMEWORK_NOTIFICATION: {
                    
                    if (msg.arg1 == 0) {
                        findViewById(R.id.activity_kii_launcher_homework_notification).setVisibility(View.GONE);
                        findViewById(R.id.activity_kii_launcher_homework).setVisibility(View.VISIBLE);
                        break;
                    }
                    
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
                    playNotify();
                    break;
                }
                case KiiLauncherService.MSG_NEW_NEWS_NOTIFICATION: {
                    
                    if (msg.arg1 == 0) {
                        findViewById(R.id.activity_kii_launcher_news_notification).setVisibility(View.GONE);
                        findViewById(R.id.activity_kii_launcher_news).setVisibility(View.VISIBLE);
                        break;
                    }
                    
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
                    playNotify();
                    break;
                }
                default:
                    super.handleMessage(msg);
                    return;
            }
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
        
        int num = getResources().getInteger(R.integer.homescreen_horizontal_count);
        
        @Override
        public int getCount() {
        
            return 3;
        }
        
        @Override
        public Object instantiateItem( View collection, int position ) {
        
            LayoutInflater li = getLayoutInflater();
            GridView gridview = (GridView) li.inflate(R.layout.activity_kii_homescreen_tabs, (ViewGroup) collection, false);
            gridview.setStackFromBottom(true);
            
            ArrayList<PackagePermissions> list = null;
            switch (position) {
                case 0:
                    list = leftScreen;
                    break;
                case 1:
                    list = centerScreen;
                    break;
                case 2:
                    list = rightScreen;
                    break;
            }
            
            gridview.setAdapter(new DesktopIconAdapter(KiiLaucher.this, R.layout.activity_kii_homescreen_iconlayout, list));
            
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
        
            if (dialog != null) {
                dialog.dismiss();
            }
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
