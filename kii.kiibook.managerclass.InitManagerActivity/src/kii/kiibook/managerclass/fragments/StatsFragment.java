
package kii.kiibook.managerclass.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import kii.kiibook.teacher.R;

public class StatsFragment extends Fragment implements OnTabChangeListener, OnClickListener {
    
    public static final String TAG  = "StatsFragment";
    public static final String FRAG = "fragment";
    
    private View               mRoot;
    private TabHost            mTabHost;
    private int                classId;
    
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
        TableLayout table_diaries = (TableLayout) mTabHost.findViewById(R.id.table_diaries_eval);
        setupTable(table_diaries);
        
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("testes").setContent(R.id.stats_tab2));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator("trabalhos").setContent(R.id.stats_tab3));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test4").setIndicator("tpc").setContent(R.id.stats_tab4));
        
        mTabHost.setCurrentTab(0);
    }
    
    public void onTabChanged( String tabId ) {
    
        // TODO Auto-generated method stub
        
    }
    
    private void setupTable( TableLayout table ) {
    
        for (int h = 0; h < 6; h++) {
            
            TableRow row = (TableRow) LayoutInflater.from(getActivity()).inflate(R.layout.tablerow_dialy_evaluation, null);
            String s = "row" + h;
            row.setTag(s);
            
            TextView text = ((TextView) row.findViewById(R.id.textView_cell1));
            text.setOnClickListener(this);
            
            text = ((TextView) row.findViewById(R.id.textView_cell2));
            text.setOnClickListener(this);
            
            text = ((TextView) row.findViewById(R.id.textView_cell3));
            text.setOnClickListener(this);
            
            text = ((TextView) row.findViewById(R.id.textView_cell4));
            text.setOnClickListener(this);
            
            table.addView(row);
        }
        table.requestLayout();
    }
    
    public void onClick( View v ) {
    
        View parent = (View) v.getParent();
        
        TableLayout table = (TableLayout) parent.getParent();
        String tag = (String) parent.getTag();
        int row = 1;
        
        if (tag.equalsIgnoreCase("row1")) {
            row = 2;
        }
        if (tag.equalsIgnoreCase("row2")) {
            row = 3;
        }
        if (tag.equalsIgnoreCase("row3")) {
            row = 4;
        }
        if (tag.equalsIgnoreCase("row4")) {
            row = 5;
        }
        if (tag.equalsIgnoreCase("row5")) {
            row = 6;
        }
        Log.d(getTag(), "Row:" + row);
        startAnimation(row, table, v);
        
    }
    
    private void startAnimation( int row, TableLayout table, View v ) {
    
        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(1500);
        
        Log.w(getTag(), "TableCount: " + table.getChildCount());
        
        for (int idx = 1; idx < table.getChildCount(); idx++) {
            if (idx == row) {
                View parent = (View) v.getParent();
                switch (v.getId()) {
                    case R.id.textView_cell1:
                        
                        TextView cell = (TextView) parent.findViewById(R.id.textView_cell2);
                        
                        cell.startAnimation(animation);
                        cell.setVisibility(View.INVISIBLE);
                        
                        cell = (TextView) parent.findViewById(R.id.textView_cell3);
                        
                        cell.startAnimation(animation);
                        cell.setVisibility(View.INVISIBLE);
                        
                        cell = (TextView) parent.findViewById(R.id.textView_cell4);
                        
                        cell.startAnimation(animation);
                        cell.setVisibility(View.INVISIBLE);
                        
                        break;
                    case R.id.textView_cell2:
                        
                        cell = (TextView) parent.findViewById(R.id.textView_cell1);
                        
                        cell.startAnimation(animation);
                        cell.setVisibility(View.INVISIBLE);
                        
                        cell = (TextView) parent.findViewById(R.id.textView_cell3);
                        
                        cell.startAnimation(animation);
                        cell.setVisibility(View.INVISIBLE);
                        
                        cell = (TextView) parent.findViewById(R.id.textView_cell4);
                        
                        cell.startAnimation(animation);
                        cell.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.textView_cell3:
                        
                        cell = (TextView) parent.findViewById(R.id.textView_cell2);
                        
                        cell.startAnimation(animation);
                        cell.setVisibility(View.INVISIBLE);
                        
                        cell = (TextView) parent.findViewById(R.id.textView_cell1);
                        
                        cell.startAnimation(animation);
                        cell.setVisibility(View.INVISIBLE);
                        
                        cell = (TextView) parent.findViewById(R.id.textView_cell4);
                        
                        cell.startAnimation(animation);
                        cell.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.textView_cell4:
                        
                        cell = (TextView) parent.findViewById(R.id.textView_cell2);
                        
                        cell.startAnimation(animation);
                        cell.setVisibility(View.INVISIBLE);
                        
                        cell = (TextView) parent.findViewById(R.id.textView_cell3);
                        
                        cell.startAnimation(animation);
                        cell.setVisibility(View.INVISIBLE);
                        
                        cell = (TextView) parent.findViewById(R.id.textView_cell1);
                        
                        cell.startAnimation(animation);
                        cell.setVisibility(View.INVISIBLE);
                        break;
                }
            } else {
                View parent = (View) table.getChildAt(idx);
                
                TextView cell = (TextView) parent.findViewById(R.id.textView_cell1);
                
                cell.startAnimation(animation);
                cell.setVisibility(View.INVISIBLE);
                
                cell = (TextView) parent.findViewById(R.id.textView_cell2);
                
                cell.startAnimation(animation);
                cell.setVisibility(View.INVISIBLE);
                
                cell = (TextView) parent.findViewById(R.id.textView_cell3);
                
                cell.startAnimation(animation);
                cell.setVisibility(View.INVISIBLE);
                
                cell = (TextView) parent.findViewById(R.id.textView_cell4);
                
                cell.startAnimation(animation);
                cell.setVisibility(View.INVISIBLE);
            }
        }
    }
}
