
package com.kii.launcher.drawer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.kii.launcher.PackagePermissions;
import com.kii.launcher.R;
import com.kii.launcher.drawer.MenuFragment.ListCallbacks;

import java.util.ArrayList;

public class AppsTabFragment extends Fragment {
    
    private ListCallbacks                 topActivity;
    private ArrayList<PackagePermissions> apps = null;
    private final String                  title;
    
    public AppsTabFragment( ArrayList<PackagePermissions> apps, String fragmentTitle ) {
    
        this.apps = apps;
        title = fragmentTitle;
    }
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        View rootView = inflater.inflate(R.layout.fragment_kii_drawer_apps_tabs, container, false);
        
        GridView gridview = (GridView) rootView.findViewById(R.id.activity_kii_drawer_apps_gridview);
        gridview.setAdapter(new IconAdapter(getActivity(), R.layout.fragment_kii_drawer_apps_iconlayout, apps));
        gridview.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick( AdapterView<?> parent, View v, int position, long id ) {
            
                PackagePermissions app = (PackagePermissions) v.getTag();
                
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setClassName(app.getPackage(), app.getIntentActivity());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                startActivity(intent);
                
                topActivity.closeDrawer();
            }
        });
        
        return rootView;
    }
    
    @Override
    public void onAttach( Activity activity ) {
    
        super.onAttach(activity);
        
        topActivity = (ListCallbacks) activity;
    }
    
    public String getTitle() {
    
        return title;
    }
    
}
