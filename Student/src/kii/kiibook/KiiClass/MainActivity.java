
package kii.kiibook.KiiClass;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import java.util.ArrayList;

import kii.kiibook.ClassMode.adapters_items.AdapterListViewHistorical;
import kii.kiibook.ClassMode.adapters_items.AdapterListViewStudents;
import kii.kiibook.ClassMode.adapters_items.ItemListViewHistorical;
import kii.kiibook.ClassMode.adapters_items.ItemListViewStudent;
import kii.kiibook.Student.R;

public class MainActivity extends FragmentActivity implements OnTabChangeListener {
    
    private TextView                          title_summary;
    private TextView                          text_summary;
    private ListView                          list_historical;
    private ListView                          list_students;
    private ArrayList<ItemListViewStudent>    itens;
    private AdapterListViewStudents           adapterListView;
    private AdapterListViewHistorical         adapter;
    private ArrayList<ItemListViewHistorical> itens_hist;
    private TabHost                           mTabHost;
    private ViewPager                         mViewPager;
    private PagerAdapter                      pager;
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
    
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classmode);
        
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_actionbar_manager));
        
        pager = new PagerAdapter(getSupportFragmentManager());
        
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.manage_pager);
        mViewPager.setAdapter(pager);
        mViewPager.setDrawingCacheEnabled(false);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            
            @Override
            public void onPageSelected( int position ) {
            
                mTabHost.setCurrentTab(position);
                
            }
        });
        createTabs();
    }
    
    private void createTabs() {
    
        FragmentManager fm;
        
        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setOnTabChangedListener(this);
        tabHost.setup();
        
        TabSpec tspecProf = tabHost.newTabSpec("prof");
        tspecProf.setIndicator("professor");
        tspecProf.setContent(R.id.tab_prof);
        tabHost.addTab(tspecProf);
        
        TabSpec tspecEvent = tabHost.newTabSpec("eventos");
        tspecEvent.setIndicator("eventos");
        tspecEvent.setContent(R.id.tab_event);
        tabHost.addTab(tspecEvent);
        
        tabHost.setCurrentTab(0);
        initTabsAppearance(tabHost.getTabWidget());
        
        mTabHost = (TabHost) findViewById(R.id.tabhost_fragments);
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setup();
        
        TabSpec tspec = mTabHost.newTabSpec("tab0");
        tspec.setIndicator("sumarios");
        tspec.setContent(R.id.tab0);
        mTabHost.addTab(tspec);
        
        tspec = mTabHost.newTabSpec("tab1");
        tspec.setIndicator("avaliaçoes");
        tspec.setContent(R.id.tab1);
        mTabHost.addTab(tspec);
        
        tspec = mTabHost.newTabSpec("tab2");
        tspec.setIndicator("skills");
        tspec.setContent(R.id.tab2);
        mTabHost.addTab(tspec);
        
        tspec = mTabHost.newTabSpec("tab3");
        tspec.setIndicator("documentos");
        tspec.setContent(R.id.tab3);
        mTabHost.addTab(tspec);
        
        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(this);
        initTabsAppearance(mTabHost.getTabWidget());
        
    }
    
    private void initTabsAppearance( TabWidget tabWidget ) {
    
        // tabWidget.setStripEnabled(false);
        //
        // for (int i = 0; i < tabWidget.getChildCount(); i++)
        // tabWidget.getChildAt(i)
        //
    }
    
    public void onTabChanged( String tabId ) {
    
        if (tabId.equalsIgnoreCase("tab0")) {
            mViewPager.setCurrentItem(0, true);
        } else if (tabId.equalsIgnoreCase("tab1")) {
            mViewPager.setCurrentItem(1, true);
        } else if (tabId.equalsIgnoreCase("tab2")) {
            mViewPager.setCurrentItem(2, true);
        } else if (tabId.equalsIgnoreCase("tab3")) {
            mViewPager.setCurrentItem(3, true);
        } else if (tabId.equalsIgnoreCase("tab4")) {
            mViewPager.setCurrentItem(4, true);
        } else if (tabId.equalsIgnoreCase("tab5")) {
            mViewPager.setCurrentItem(5, true);
        }
        
    }
}
