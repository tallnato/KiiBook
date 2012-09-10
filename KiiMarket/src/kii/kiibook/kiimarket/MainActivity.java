
package kii.kiibook.kiimarket;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class MainActivity extends FragmentActivity implements OnTabChangeListener {
    
    private ViewPager    mViewPager;
    private TabHost      mTabHost;
    private PagerAdapter pager;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        pager = new PagerAdapter(getSupportFragmentManager());
        
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
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
    
        mTabHost = (TabHost) findViewById(R.id.tabhost_fragments);
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setup();
        
        TabSpec tspec = mTabHost.newTabSpec("tab0");
        tspec.setIndicator("em destaque");
        tspec.setContent(R.id.tab0);
        mTabHost.addTab(tspec);
        
        tspec = mTabHost.newTabSpec("tab1");
        tspec.setIndicator("novidades");
        tspec.setContent(R.id.tab1);
        mTabHost.addTab(tspec);
        
        tspec = mTabHost.newTabSpec("tab2");
        tspec.setIndicator("populares pagas");
        tspec.setContent(R.id.tab2);
        mTabHost.addTab(tspec);
        
        tspec = mTabHost.newTabSpec("tab3");
        tspec.setIndicator("populares grátis");
        tspec.setContent(R.id.tab3);
        mTabHost.addTab(tspec);
        
        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(this);
        
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
        }
        
    }
}
