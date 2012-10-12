
package kii.kiibook.managerclass.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TableLayout;

import kii.kiibook.teacher.R;

public class StatsFragment extends Fragment implements OnTabChangeListener {
    
    public static final String TAG  = "StatsFragment";
    public static final String FRAG = "fragment";
    
    private View               mRoot;
    private TabHost            mTabHost;
    private TableLayout        mTableLayout;
    private int                classId;
    private TableLayout        mTableLayoutWork;
    private TableLayout        mTableLayoutTests;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.stats_frame, container, false);
        Bundle bundle = getArguments();
        
        classId = bundle.getInt(FRAG);
        
        createTabs();
        
        return mRoot;
    }
    
    private void createTabs() {
    
        mTabHost = (TabHost) mRoot.findViewById(R.id.tabhost_stats);
        mTabHost.setup();
        mTabHost.setOnTabChangedListener(this);
        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("avaliações diárias").setContent(R.id.stats_tab1));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("testes").setContent(R.id.stats_tab2));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator("trabalhos").setContent(R.id.stats_tab3));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test4").setIndicator("tpc").setContent(R.id.stats_tab4));
        
        mTabHost.setCurrentTab(0);
    }
    
    public void onTabChanged( String tabId ) {
    
    }
    
}
