
package kii.kiibook.managerclass;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import objects.ClassPeople;

import java.util.ArrayList;
import java.util.Iterator;

import kii.kiibook.managerclass.adapters.AdapterSummary;
import kii.kiibook.managerclass.database.DataShared;
import kii.kiibook.managerclass.fragments.SummariesFragment;
import kii.kiibook.managerclass.utils.MyTabListener;
import kii.kiibook.teacher.R;

public class ManagerClassActivity extends FragmentActivity {
    
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private static final String TAG                            = "MainActivity";
    public final static String  CLASS                          = "class";
    
    public static final int     BOOK                           = 0;
    public static final int     FALCON_EYE                     = 1;
    public static final int     DOC                            = 2;
    public static final int     CALENDAR                       = 3;                          ;
    public static final int     STAT                           = 4;
    public static final int     NEWS                           = 5;
    
    private ViewPager           mViewPager;
    private int                 classId;
    private PagerAdapter        mSectionsPagerAdapter;
    private MyTabListener       mTabListener;
    private ClassPeople         classPeople;
    private final boolean[]     checkedItems                   = { false, false };
    private Intent              service;
    private SummariesFragment   fragmentSummary;
    private AdapterSummary      adapter;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_class);
        
        Bundle bundle = getIntent().getExtras();
        classId = bundle.getInt(CLASS);
        classPeople = DataShared.getInstance().getClasses().get(classId);
        
        // Create the adapter that will return a fragment for each section
        mSectionsPagerAdapter = new PagerAdapter(getSupportFragmentManager(), classId);
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_actionbar_manager));
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle(classPeople.getName());
        
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.manage_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setDrawingCacheEnabled(false);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            
            @Override
            public void onPageSelected( int position ) {
            
                // adapter.notifyDataSetChanged();
                actionBar.setSelectedNavigationItem(position);
                
            }
        });
        
        mTabListener = new MyTabListener(mViewPager);
        
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setTabListener(mTabListener).setText(mSectionsPagerAdapter.getFragmentTitle(i)));
        }
    }
    
    @Override
    public void onAttachFragment( Fragment fragment ) {
    
        if (fragment instanceof SummariesFragment) {
            fragmentSummary = (SummariesFragment) fragment;
        }
        super.onAttachFragment(fragment);
    }
    
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
    
        getMenuInflater().inflate(R.menu.menu_managerclass, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    
        Log.d(TAG, "onOptionsItemSelected");
        
        switch (item.getItemId()) {
        
            case R.id.menu_managerclass_changeClass:
                showDialogChangeClass();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }
    
    private void showDialogChangeClass() {
    
        ArrayList<ClassPeople> list = DataShared.getInstance().getClasses();
        Iterator<ClassPeople> it = list.iterator();
        String[] items = new String[list.size()];
        int pointer = 0;
        while (it.hasNext()) {
            
            items[pointer] = it.next().getName();
            pointer++;
        }
        
        for (String str : items) {
            Log.e(TAG, "TAG - Turmas { " + str);
        }
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mudar Turma");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            
            public void onClick( DialogInterface dialog, int item ) {
            
                Bundle b = new Bundle();
                b.putInt(CLASS, item);
                Intent myIntent = new Intent(ManagerClassActivity.this, ManagerClassActivity.class);
                myIntent.putExtras(b);
                startActivity(myIntent);
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    private void showDialogSettings() {
    
        String[] items = { "Marcação de faltas automática", "Outras cenas" };
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Definições");
        builder.setMultiChoiceItems(items, checkedItems, new OnMultiChoiceClickListener() {
            
            public void onClick( DialogInterface dialog, int which, boolean isChecked ) {
            
                if (isChecked) {
                    checkedItems[which] = true;
                } else {
                    checkedItems[which] = false;
                }
            }
            
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
