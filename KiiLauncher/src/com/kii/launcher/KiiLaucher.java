
package com.kii.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.kii.launcher.drawer.DrawerActivity;
import com.kii.launcher.drawer.IconAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KiiLaucher extends Activity {
    
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
        
        GridView gridview = (GridView) findViewById(R.id.activity_kii_launcher_gridview);
        gridview.setAdapter(new IconAdapter(this, R.layout.activity_kii_launcher_iconlayout, getInstalledApps()));
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
        final ImageView iv = (ImageView) findViewById(R.id.activity_kii_launcher_avatar);
        iv.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                Toast.makeText(getApplicationContext(), "mostrar popup", Toast.LENGTH_SHORT).show();
                
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                
                View pop = inflater.inflate(R.layout.popup_test, null, false);
                PopupWindow pw = new PopupWindow(pop, 200, 200, false);
                pw.setAnimationStyle(R.style.AnimationPopup);
                pw.setBackgroundDrawable(new BitmapDrawable());
                pw.setOutsideTouchable(true);
                pw.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 0);
                Button b = (Button) pop.findViewById(R.id.button1);
                
                b.setOnClickListener(new OnClickListener() {
                    
                    @Override
                    public void onClick( View v ) {
                    
                        Display display = getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        
                        int pos[] = new int[2];
                        iv.getLocationInWindow(pos);
                        Toast.makeText(getApplicationContext(), "aqui x:" + size.x + " y:" + size.y, Toast.LENGTH_SHORT).show();
                        
                    }
                });
                
            }
        });
        
        startActivity(new Intent(getApplicationContext(), DrawerActivity.class));
    }
    
    @Override
    public void onBackPressed() {
    
    }
    
    private ArrayList<PackagePermissions> getInstalledApps() {
    
        PackageManager pm = getPackageManager();
        
        ArrayList<PackagePermissions> res = new ArrayList<PackagePermissions>();
        
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);
        
        for (ResolveInfo ri : pkgAppsList) {
            
            res.add(new PackagePermissions(ri.activityInfo.packageName, ri.activityInfo.name, ri.loadLabel(pm).toString(), ri.loadIcon(pm)));
        }
        Collections.sort(res);
        
        return res;
    }
}
