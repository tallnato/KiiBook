
package kii.kiibook.kiimarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class BooksFragment extends Fragment implements OnTabChangeListener {
    
    private View              mRoot;
    private BooksPagerAdapter pager;
    private ViewPager         mViewPager;
    private TabHost           mTabHost;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        mRoot = inflater.inflate(R.layout.books_fragment, container, false);
        
        pager = new BooksPagerAdapter(inflater);
        
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) mRoot.findViewById(R.id.pager);
        
        mViewPager.setAdapter(pager);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            
            @Override
            public void onPageSelected( int position ) {
            
                mTabHost.setCurrentTab(position);
                
            }
        });
        createTabs();
        return mRoot;
    }
    
    private void createTabs() {
    
        mTabHost = (TabHost) mRoot.findViewById(R.id.tabhost_fragments);
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
