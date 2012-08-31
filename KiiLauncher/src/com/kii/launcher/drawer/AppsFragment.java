
package com.kii.launcher.drawer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.kii.launcher.PackagePermissions;
import com.kii.launcher.R;
import com.kii.launcher.drawer.util.IDrawerFragment;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppsFragment extends Fragment implements IDrawerFragment {
    
    private static final String                  APPSFRAGMENTSHOWALLAPPS = "AppsFragmentShowAllApps";
    private static final int                     NUM_HORIZONTAL_APPS     = 7;
    private static final int                     NUM_VERTICAL_APPS       = 6;
    
    private static ArrayList<PackagePermissions> installedApps           = null;
    private static ArrayList<PackagePermissions> kiiApps                 = null;
    
    private ViewPager                            mViewPager;
    private PageViewAdapter                      mPagerAdapter;
    private CirclePageIndicator                  circleIndicator;
    
    private boolean                              allApps                 = true;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        
        if (getArguments() != null) {
            allApps = getArguments().getBoolean(APPSFRAGMENTSHOWALLAPPS, true);
        }
        
        if (installedApps == null) {
            installedApps = getInstalledApps();
        }
        
        if (kiiApps == null) {
            kiiApps = new ArrayList<PackagePermissions>();
            
            for (PackagePermissions pp : installedApps) {
                if (pp.getLabel().contains("Chrome") || pp.getLabel().contains("Dropbox") || pp.getLabel().contains("Facebook")
                                                || pp.getLabel().contains("Drive") || pp.getLabel().contains("Skype")) {
                    kiiApps.add(pp);
                }
            }
        }
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        View rootView = inflater.inflate(R.layout.fragment_kii_drawer_apps, container, false);
        
        List<AppsTabFragment> fragments = new ArrayList<AppsTabFragment>();
        fragments.add(new AppsTabFragment(installedApps, "Todas"));
        fragments.add(new AppsTabFragment(kiiApps, "Aplicações Kii"));
        
        circleIndicator = (CirclePageIndicator) rootView.findViewById(R.id.fragment_kii_drawer_apps_indicator);
        
        mPagerAdapter = new PageViewAdapter();
        mViewPager = (ViewPager) rootView.findViewById(R.id.fragment_kii_drawer_apps_view_pager);
        
        mViewPager.setAdapter(mPagerAdapter);
        
        circleIndicator.setViewPager(mViewPager);
        
        if (allApps) {
            if (installedApps.size() <= NUM_HORIZONTAL_APPS * NUM_VERTICAL_APPS) {
                circleIndicator.setVisibility(View.INVISIBLE);
            }
        } else {
            if (kiiApps.size() <= NUM_HORIZONTAL_APPS * NUM_VERTICAL_APPS) {
                circleIndicator.setVisibility(View.INVISIBLE);
            }
        }
        return rootView;
    }
    
    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
    
        inflater.inflate(R.menu.apps_fragment_menu, menu);
        
        if (allApps) {
            menu.findItem(R.id.apps_fragment_menu_all).setChecked(true);
        } else {
            menu.findItem(R.id.apps_fragment_menu_kii).setChecked(true);
        }
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    
        Fragment frag = new AppsFragment();
        Bundle b = new Bundle();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, 0);
        ft.replace(R.id.activity_kii_launcher_drawer_container, frag);
        
        switch (item.getItemId()) {
            case R.id.apps_fragment_menu_all:
                if (allApps) {
                    return true;
                }
                b.putBoolean(APPSFRAGMENTSHOWALLAPPS, true);
                frag.setArguments(b);
                
                ft.commit();
                
                return true;
            case R.id.apps_fragment_menu_kii:
                if (!allApps) {
                    return true;
                }
                b.putBoolean(APPSFRAGMENTSHOWALLAPPS, false);
                frag.setArguments(b);
                
                ft.commit();
                
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private class PageViewAdapter extends PagerAdapter {
        
        @Override
        public int getCount() {
        
            if (allApps) {
                return (int) Math.ceil(installedApps.size() / (double) (NUM_HORIZONTAL_APPS * NUM_VERTICAL_APPS));
            } else {
                return (int) Math.ceil(kiiApps.size() / (double) (NUM_HORIZONTAL_APPS * NUM_VERTICAL_APPS));
            }
        }
        
        @Override
        public Object instantiateItem( View collection, int position ) {
        
            List<PackagePermissions> list;
            if (allApps) {
                int start, nElem;
                start = position * NUM_HORIZONTAL_APPS * NUM_VERTICAL_APPS;
                
                if (position == getCount() - 1) {
                    nElem = installedApps.size() - start;
                } else {
                    nElem = (position + 1) * NUM_HORIZONTAL_APPS * NUM_VERTICAL_APPS;
                }
                
                list = installedApps.subList(start, start + nElem);
            } else {
                list = kiiApps;
            }
            
            LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            GridView gridview = (GridView) li.inflate(R.layout.fragment_kii_drawer_apps_tabs, (ViewGroup) collection, false);
            gridview.setAdapter(new IconAdapter(getActivity(), R.layout.fragment_kii_drawer_apps_iconlayout, list));
            gridview.setOnItemClickListener(new OnItemClickListener() {
                
                @Override
                public void onItemClick( AdapterView<?> parent, View v, int position, long id ) {
                
                    PackagePermissions app = (PackagePermissions) v.getTag();
                    
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setClassName(app.getPackage(), app.getIntentActivity());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    startActivity(intent);
                    
                    getActivity().finish();
                }
            });
            
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
    
    private ArrayList<PackagePermissions> getInstalledApps() {
    
        PackageManager pm = getActivity().getPackageManager();
        
        ArrayList<PackagePermissions> res = new ArrayList<PackagePermissions>();
        
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = pm.queryIntentActivities(mainIntent, 0);
        
        for (ResolveInfo ri : pkgAppsList) {
            
            res.add(new PackagePermissions(ri.activityInfo.packageName, ri.activityInfo.name, ri.loadLabel(pm).toString(), ri.loadIcon(pm)));
        }
        Collections.sort(res);
        
        return res;
    }
    
    @Override
    public int getNameResource() {
    
        return R.string.drawer_menu_apps;
    }
    
    @Override
    public int getIconResource() {
    
        return R.drawable.ic_drawer_appsfragment;
    }
}
