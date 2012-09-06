
package com.kii.launcher;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.launcher.drawer.DrawerActivity;
import com.kii.launcher.drawer.apps.AppsFragmentIconAdapter;
import com.kii.launcher.drawer.apps.database.AppsListDataSource;
import com.kii.launcher.drawer.apps.database.KiiListDataSource;
import com.kii.launcher.wall.popups.CalendarPopUp;
import com.kii.launcher.wall.popups.HomeWorkPopUp;
import com.kii.launcher.wall.popups.MessagePopUp;
import com.kii.launcher.wall.popups.NewsPopUp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class KiiLaucher extends Activity {
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kii_launcher);
        
        new InstalledAppsWorker().execute(this);
        
        View v = findViewById(R.id.activity_kii_launcher_layout);
        v.setOnLongClickListener(new OnLongClickListener() {
            
            @Override
            public boolean onLongClick( View v ) {
            
                Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
                startActivity(Intent.createChooser(intent, "Select Wallpaper"));
                
                return true;
            }
        });
        
        GridView gridview = (GridView) findViewById(R.id.activity_kii_launcher_gridview);
        gridview.setAdapter(new AppsFragmentIconAdapter(this, R.layout.activity_kii_launcher_iconlayout,
                                        new ArrayList<PackagePermissions>()));
        gridview.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick( AdapterView<?> parent, View v, int position, long id ) {
            
                PackagePermissions app = (PackagePermissions) v.getTag();
                
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setClassName(app.getPackage(), app.getIntentActivity());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                startActivity(intent);
            }
        });
        
        ImageView logo = (ImageView) findViewById(R.id.activity_kii_launcher_logo);
        logo.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                startActivity(new Intent(getApplicationContext(), DrawerActivity.class));
            }
        });
        View profile = findViewById(R.id.activity_kii_launcher_profile);
        profile.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Toast.makeText(getApplicationContext(), "mostrar prefil...", Toast.LENGTH_SHORT).show();
            }
        });
        
        Calendar cal = Calendar.getInstance();
        
        TextView day = (TextView) findViewById(R.id.activity_kii_wall_calendar_day);
        TextView month = (TextView) findViewById(R.id.activity_kii_wall_calendar_month);
        
        day.setText("" + cal.get(Calendar.DATE));
        month.setText(new SimpleDateFormat("MMM").format(new Date()));
        
        View calendarButton = findViewById(R.id.activity_kii_wall_calendar);
        calendarButton.setOnClickListener(new OnClickListener() {
            
            PopupWindow pw;
            
            @Override
            public void onClick( final View v ) {
            
                if (pw != null) {
                    pw.dismiss();
                    pw = null;
                    return;
                }
                
                int popupWidth = 400, popupHeight = 400;
                
                pw = new CalendarPopUp(KiiLaucher.this, v, popupWidth, popupHeight);
                
            }
        });
        
        View messagesButton = findViewById(R.id.activity_kii_wall_messages);
        messagesButton.setOnClickListener(new OnClickListener() {
            
            PopupWindow pw;
            
            @Override
            public void onClick( final View v ) {
            
                if (pw != null) {
                    pw.dismiss();
                    pw = null;
                    return;
                }
                
                int popupWidth = 250, popupHeight = 300;
                
                pw = new MessagePopUp(KiiLaucher.this, v, popupWidth, popupHeight);
            }
        });
        
        View homeworkButton = findViewById(R.id.activity_kii_wall_homework);
        homeworkButton.setOnClickListener(new OnClickListener() {
            
            PopupWindow pw;
            
            @Override
            public void onClick( final View v ) {
            
                if (pw != null) {
                    pw.dismiss();
                    pw = null;
                    return;
                }
                
                int popupWidth = 250, popupHeight = 300;
                
                pw = new HomeWorkPopUp(KiiLaucher.this, v, popupWidth, popupHeight);
            }
        });
        
        View newsButton = findViewById(R.id.activity_kii_wall_news);
        newsButton.setOnClickListener(new OnClickListener() {
            
            PopupWindow pw;
            
            @Override
            public void onClick( final View v ) {
            
                if (pw != null) {
                    pw.dismiss();
                    pw = null;
                    return;
                }
                
                int popupWidth = 250, popupHeight = 300;
                
                pw = new NewsPopUp(KiiLaucher.this, v, popupWidth, popupHeight);
            }
        });
        
        View searchButton = findViewById(R.id.activity_kii_wall_search);
        searchButton.setOnClickListener(new OnClickListener() {
            
            PopupWindow pw;
            
            @Override
            public void onClick( final View v ) {
            
                if (pw != null) {
                    pw.dismiss();
                    pw = null;
                    return;
                }
                
                int popupWidth = 300, popupHeight = 50;
                int showX, showY;
                int viewLocation[] = new int[2];
                Point screenSize = new Point();
                
                Display display = getWindowManager().getDefaultDisplay();
                display.getSize(screenSize);
                
                v.getLocationOnScreen(viewLocation);
                
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                
                View pop = inflater.inflate(R.layout.activity_kii_wall_search, null, false);
                pw = new PopupWindow(pop, popupWidth, popupHeight, false);
                pw.setAnimationStyle(R.style.WallPopUpAnimation);
                pw.setBackgroundDrawable(new BitmapDrawable());
                pw.setOutsideTouchable(true);
                pw.setFocusable(true);
                
                showX = viewLocation[0] - screenSize.x / 2;
                showY = 0 - screenSize.y / 2 + 60 + popupHeight / 2;
                pw.showAtLocation(v.getRootView(), Gravity.CENTER, showX, showY);
                
                pop.requestFocus();
                EditText search = (EditText) pop.findViewById(R.id.activity_kii_wall_search_edittext);
                search.requestFocus();
            }
        });
    }
    
    @Override
    public void onBackPressed() {
    
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
        }
    }
    
    private void getInstalledApps( Context context, AppsListDataSource appsDataSource ) {
    
        KiiListDataSource kds = new KiiListDataSource(context);
        kds.open();
        
        PackageManager pm = context.getPackageManager();
        
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = pm.queryIntentActivities(mainIntent, 0);
        
        for (ResolveInfo ri : pkgAppsList) {
            
            PackagePermissions pp = appsDataSource.createPackagePermissions(ri.activityInfo.packageName, ri.activityInfo.name, ri
                                            .loadLabel(pm).toString(), ri.loadIcon(pm));
            
            if (pp.getLabel().contains("Chrome") || pp.getLabel().contains("Dropbox") || pp.getLabel().contains("Facebook")
                                            || pp.getLabel().contains("Drive") || pp.getLabel().contains("Skype")
                                            || pp.getLabel().contains("Kii")) {
                kds.createKiiApp(pp.getId());
            }
        }
        
        kds.close();
    }
}
