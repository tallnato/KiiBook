
package com.kii.launcher.drawer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.kii.launcher.PackagePermissions;
import com.kii.launcher.R;
import com.kii.launcher.drawer.apps.AppsFragmentIconAdapter;
import com.kii.launcher.drawer.apps.database.AppsListDataSource;
import com.kii.launcher.drawer.util.IDrawerFragment;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

public class AppsFragment extends Fragment implements IDrawerFragment {
    
    private int                             NUM_HORIZONTAL_APPS;
    private int                             NUM_VERTICAL_APPS;
    
    private static List<PackagePermissions> installedApps = null;
    
    private ViewPager                       mViewPager;
    private PageViewAdapter                 mPagerAdapter;
    private CirclePageIndicator             circleIndicator;
    
    private int                             appsPerView;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        
        setHasOptionsMenu(true);
        
        NUM_HORIZONTAL_APPS = getResources().getInteger(R.integer.drawer_apps_horizontal_count);
        NUM_VERTICAL_APPS = getResources().getInteger(R.integer.drawer_apps_vertical_count);
        
        appsPerView = NUM_HORIZONTAL_APPS * NUM_VERTICAL_APPS;
        
        if (installedApps == null || installedApps.isEmpty()) {
            AppsListDataSource appsDataSource = new AppsListDataSource(getActivity());
            appsDataSource.open();
            installedApps = appsDataSource.getAllApps();
            appsDataSource.close();
        }
        
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        View rootView = inflater.inflate(R.layout.fragment_kii_drawer_apps, container, false);
        
        circleIndicator = (CirclePageIndicator) rootView.findViewById(R.id.fragment_kii_drawer_apps_indicator);
        
        mPagerAdapter = new PageViewAdapter();
        mViewPager = (ViewPager) rootView.findViewById(R.id.fragment_kii_drawer_apps_view_pager);
        
        mViewPager.setAdapter(mPagerAdapter);
        
        circleIndicator.setViewPager(mViewPager);
        
        return rootView;
    }
    
    @Override
    public void onSaveInstanceState( Bundle savedInstanceState ) {
    
        super.onSaveInstanceState(savedInstanceState);
    }
    
    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
    
        inflater.inflate(R.menu.apps_fragment_menu, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    
        switch (item.getItemId()) {
            case R.id.apps_fragment_menu_kiimarket:
                
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details")));
                
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private class PageViewAdapter extends PagerAdapter {
        
        @Override
        public int getCount() {
        
            return (int) Math.ceil(installedApps.size() / (double) (NUM_HORIZONTAL_APPS * NUM_VERTICAL_APPS));
        }
        
        @Override
        public Object instantiateItem( View collection, int position ) {
        
            List<PackagePermissions> list;
            
            int start, nElem;
            start = position * appsPerView;
            
            nElem = Math.min(installedApps.size() - start, appsPerView);
            
            list = installedApps.subList(start, start + nElem);
            
            LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            GridView gridview = (GridView) li.inflate(R.layout.fragment_kii_drawer_apps_tabs, (ViewGroup) collection, false);
            gridview.setAdapter(new AppsFragmentIconAdapter(getActivity(), R.layout.fragment_kii_drawer_apps_iconlayout, list));
            
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
    
    @Override
    public int getNameResource() {
    
        return R.string.drawer_menu_apps;
    }
    
    @Override
    public int getIconResource() {
    
        return R.drawable.ic_drawer_applications;
    }
}
