
package kii.kiibook.Agenda;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import objects.Data;
import objects.HourCalendarListView;
import objects.MyCalendar;
import objects.TextElem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kii.kiibook.Student.CommunicationService;
import kii.kiibook.Student.MyTabListener;
import kii.kiibook.Student.R;
import kii.kiibook.Student.adapters_items.AdapterCalendarView;
import kii.kiibook.Student.database.DataShared;

public class AgendaActivity extends FragmentActivity implements OnItemLongClickListener, OnDateChangeListener, OnTouchListener {
    
    private static final String             TAG      = "CalendarMyStudent";
    private CalendarView                    calendar;
    private ListView                        listViewDay;
    private ArrayList<MyCalendar>           myCalendar;
    private AdapterCalendarView             adapter;
    private ArrayList<HourCalendarListView> hoursElem;
    private int                             colorSelected;
    
    private ViewPager                       mViewPager;
    private ActionBar                       actionBar;
    
    private FragmentTransaction             ft;
    private WeekPagerAdapter                weekPagerAdapter;
    private MyTabListener                   mTabListener;
    private Calendar                        cal;
    private long                            time;
    private final int                       lastPage = 0;
    private FragmentWeek                    fragment;
    private FrameLayout                     frame;
    private Intent                          serviceIntent;
    private boolean                         mIsBound = false;
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.calendar_fragment);
        
        serviceIntent = new Intent(this, CommunicationService.class);
        
        startService(serviceIntent);
        
        mIsBound = true;
        
        actionBar = getActionBar();
        actionBar.setIcon(R.drawable.icon);
        actionBar.setDisplayShowTitleEnabled(true);
        
        myCalendar = DataShared.getInstance().getMyCalendar();
        
        calendar = (CalendarView) findViewById(R.id.calendarView_agenda);
        calendar.setShowWeekNumber(true);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        
        listViewDay = (ListView) findViewById(R.id.calendar_listView);
        
        updateList();
        listViewDay.setOnItemLongClickListener(this);
        calendar.setOnDateChangeListener(this);
        
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment = (FragmentWeek) FragmentWeek.newInstance();
        Bundle args = new Bundle();
        args.putLong("time", calendar.getDate());
        fragment.setArguments(args);
        transaction.add(R.id.pager_calendar, fragment, "frag");
        transaction.commit();
        
    }
    
    @Override
    protected void onDestroy() {
    
        Toast.makeText(this, "Destroy service", Toast.LENGTH_SHORT).show();
        // stopService(serviceIntent);
        super.onDestroy();
    }
    
    private int getWeek() {
    
        time = calendar.getDate();
        cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(cal.MONDAY);
        cal.setTimeInMillis(time);
        Date date = cal.getTime();
        cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
    
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
    
        getMenuInflater().inflate(R.menu.agenda_menu_views, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    
        switch (item.getItemId()) {
            case R.id.item_view_week:
                break;
            
            case R.id.item_view_day:
                break;
        
        }
        return true;
    }
    
    public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth ) {
    
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.detach(fragment);
        transaction.remove(fragment);
        fragment = (FragmentWeek) FragmentWeek.newInstance();
        Bundle args = new Bundle();
        args.putLong("time", calendar.getDate());
        fragment.setArguments(args);
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.add(R.id.pager_calendar, fragment, "frag");
        transaction.commit();
        
    }
    
    private ArrayList<HourCalendarListView> search( long time ) {
    
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String str = formatter.format(calendar.getTime());
        Log.d(TAG, str);
        Data data = new Data(str);
        
        long times = (System.currentTimeMillis());
        
        for (int i = 0; i < myCalendar.size(); i++) {
            if (myCalendar.get(i).getYear() == data.getAno() && myCalendar.get(i).getMonth() == data.getMes()
                                            && myCalendar.get(i).getDay() == data.getDia()) {
                Log.d(TAG, myCalendar.get(i).toString());
                return myCalendar.get(i).getListDay();
            }
            
        }
        return null;
    }
    
    public boolean onItemLongClick( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
    
        Toast.makeText(this, "What's up?!", Toast.LENGTH_SHORT).show();
        return true;
    }
    
    private void updateList() {
    
        // hoursElem = search(calendar.getDate());
        ArrayList<NextEventsListView> events = new ArrayList<NextEventsListView>();
        for (int i = 1; i < 15; i++) {
            events.add(new NextEventsListView(i + "-Set-2012", new ArrayList<TextElem>()));
        }
        adapter = new AdapterCalendarView(this, events);
        listViewDay.setAdapter(adapter);
    }
    
    public boolean onTouch( View v, MotionEvent event ) {
    
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Toast.makeText(this, "Action UP: " + event.getAction(), Toast.LENGTH_SHORT).show();
        }
        
        return false;
    }
    
}
