
package kii.kiibook.managerclass.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import objects.Student;

import java.util.Iterator;
import java.util.Random;

import kii.kiibook.managerclass.database.DataShared;
import kii.kiibook.teacher.R;

public class StatsFragment extends Fragment implements OnTabChangeListener, OnClickListener {
    
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
        reachTable(inflater);
        
        return mRoot;
    }
    
    private void createTabs() {
    
        mTabHost = (TabHost) mRoot.findViewById(R.id.tabhost_stats);
        mTabHost.setup();
        mTabHost.setOnTabChangedListener(this);
        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("Atitudes").setContent(R.id.stats_tab1));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("Trabalhos").setContent(R.id.stats_tab2));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator("Testes").setContent(R.id.stats_tab3));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test4").setIndicator("Gerais").setContent(R.id.stats_tab4));
        
        mTabHost.setCurrentTab(0);
    }
    
    public void onTabChanged( String tabId ) {
    
        // TODO Auto-generated method stub
        
    }
    
    private void reachTable( LayoutInflater inflater ) {
    
        Iterator<Student> it = DataShared.getInstance().getClasses().get(classId).getStudents().iterator();
        
        while (it.hasNext()) {
            Student student = it.next();
            
            /********** Pauta - Atitudes ***********/
            
            mTableLayout = (TableLayout) mRoot.findViewById(R.id.table_body);
            
            TableRow row = (TableRow) inflater.inflate(R.layout.statstable_tablerow, mTableLayout, false);
            
            TableLayout internalTable = (TableLayout) row.findViewById(R.id.table_att);
            TableRow internalRow = (TableRow) internalTable.findViewById(R.id.table_att_titles);
            
            TextView name = (TextView) row.findViewById(R.id.table_name);
            name.setText(student.getName());
            name.setClickable(true);
            name.setOnClickListener(this);
            
            Random r = new Random();
            
            TextView attAss = (TextView) internalRow.findViewById(R.id.att1);
            attAss.setText(String.valueOf(r.nextInt(6 - 3) + 3));
            
            TextView attPont = (TextView) internalRow.findViewById(R.id.att2);
            attPont.setText(String.valueOf(r.nextInt(6 - 3) + 3));
            
            TextView attComp = (TextView) internalRow.findViewById(R.id.att3);;
            attComp.setText(String.valueOf(r.nextInt(6 - 3) + 3));
            
            TextView attPart = (TextView) internalRow.findViewById(R.id.att4);
            attPart.setText(String.valueOf(r.nextInt(6 - 3) + 3));
            
            mTableLayout.addView(row);
            
            /********** Pauta - Trabalhos ***********/
            
            mTableLayoutWork = (TableLayout) mRoot.findViewById(R.id.table_body_works);
            row = (TableRow) inflater.inflate(R.layout.statstable_tablerow, mTableLayout, false);
            
            internalTable = (TableLayout) row.findViewById(R.id.table_att);
            internalRow = (TableRow) internalTable.findViewById(R.id.table_att_titles);
            
            name = (TextView) row.findViewById(R.id.table_name);
            name.setText(student.getName());
            name.setClickable(true);
            name.setOnClickListener(this);
            
            attAss = (TextView) internalRow.findViewById(R.id.att1);
            attAss.setText(String.valueOf(r.nextInt(6 - 3) + 3));
            
            attPont = (TextView) internalRow.findViewById(R.id.att2);
            attPont.setText(String.valueOf(r.nextInt(6 - 3) + 3));
            
            attComp = (TextView) internalRow.findViewById(R.id.att3);;
            attComp.setText(String.valueOf(r.nextInt(6 - 3) + 3));
            
            attPart = (TextView) internalRow.findViewById(R.id.att4);
            attPart.setText(String.valueOf(r.nextInt(6 - 3) + 3));
            
            mTableLayoutWork.addView(row);
            
            /********** Pauta - Testes ***********/
            
            mTableLayoutTests = (TableLayout) mRoot.findViewById(R.id.table_body_tests);
            row = (TableRow) inflater.inflate(R.layout.statstable_tablerow, mTableLayout, false);
            
            internalTable = (TableLayout) row.findViewById(R.id.table_att);
            internalRow = (TableRow) internalTable.findViewById(R.id.table_att_titles);
            
            name = (TextView) row.findViewById(R.id.table_name);
            name.setText(student.getName());
            name.setClickable(true);
            name.setOnClickListener(this);
            
            attAss = (TextView) internalRow.findViewById(R.id.att1);
            attAss.setText(String.valueOf(r.nextInt(21 - 6) + 6));
            
            attPont = (TextView) internalRow.findViewById(R.id.att2);
            attPont.setText(String.valueOf(r.nextInt(21 - 6) + 6));
            
            attComp = (TextView) internalRow.findViewById(R.id.att3);;
            attComp.setText(String.valueOf(r.nextInt(21 - 6) + 6));
            
            mTableLayoutTests.addView(row);
        }
    }
    
    public void onClick( final View v ) {
    
        if (v.getId() == R.id.table_name) {
            
            v.setBackgroundResource(android.R.color.holo_blue_light);
            new Handler().postDelayed(new Runnable() {
                
                public void run() {
                
                    v.setBackgroundResource(R.drawable.cell_shape);
                }
            }, 300);
            Toast.makeText(getActivity(), "Click Subject", Toast.LENGTH_SHORT).show();
        }
    }
}
