
package kii.kiibook.Agenda;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ListView;
import android.widget.Spinner;

import objects.MyCalendar;
import objects.NewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import kii.kiibook.Student.CommunicationService;
import kii.kiibook.Student.R;
import kii.kiibook.Student.database.DataShared;

public class AgendaActivity extends FragmentActivity implements OnDateChangeListener, OnItemSelectedListener {
    
    private static final String   TAG      = "CalendarMyStudent";
    private CalendarView          calendar;
    private ListView              listViewDay;
    private ArrayList<MyCalendar> myCalendar;
    private AdapterCalendarView   adapter;
    private ActionBar             actionBar;
    private FragmentWeek          fragment;
    private Intent                serviceIntent;
    private boolean               mIsBound = false;
    private Spinner               spinner;
    private final String[]        itemsTab = { "Todos", "Testes", "Trabalhos", "TPC" };
    private int                   filter   = FragmentWeek.filter_all;
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.calendar_fragment);
        
        serviceIntent = new Intent(this, CommunicationService.class);
        
        startService(serviceIntent);
        
        mIsBound = true;
        
        actionBar = getActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_actionbar));
        actionBar.setIcon(R.drawable.agenda);
        actionBar.setDisplayShowTitleEnabled(true);
        
        myCalendar = DataShared.getInstance().getMyCalendar();
        
        calendar = (CalendarView) findViewById(R.id.calendarView_agenda);
        calendar.setShowWeekNumber(true);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        
        listViewDay = (ListView) findViewById(R.id.calendar_listView);
        spinner = (Spinner) findViewById(R.id.spinner_filters);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsTab);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        
        updateList();
        
        calendar.setOnDateChangeListener(this);
        
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment = (FragmentWeek) FragmentWeek.newInstance();
        Bundle args = new Bundle();
        args.putLong("time", calendar.getDate());
        args.putInt("filters", filter);
        fragment.setArguments(args);
        transaction.add(R.id.pager_calendar, fragment, "frag");
        transaction.commit();
        
    }
    
    public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth ) {
    
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.detach(fragment);
        transaction.remove(fragment);
        fragment = (FragmentWeek) FragmentWeek.newInstance();
        Bundle args = new Bundle();
        args.putLong("time", calendar.getDate());
        args.putInt("filters", filter);
        fragment.setArguments(args);
        transaction.add(R.id.pager_calendar, fragment, "frag");
        transaction.commit();
        
    }
    
    private String getDateString( long time ) {
    
        Date date = new Date();
        date.setTime(time);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        return sdf.format(date);
    }
    
    private void updateList() {
    
        Iterator<NewEvent> it = DataShared.getInstance().getListEvents().iterator();
        
        ArrayList<NextEventsListView> events = new ArrayList<NextEventsListView>();
        while (it.hasNext()) {
            NewEvent ev = it.next();
            events.add(new NextEventsListView(getDateString(ev.getDate()), ev.getType(), ev.getDescription()));
        }
        adapter = new AdapterCalendarView(this, events);
        listViewDay.setAdapter(adapter);
    }
    
    public void onItemSelected( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
    
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.detach(fragment);
        transaction.remove(fragment);
        fragment = (FragmentWeek) FragmentWeek.newInstance();
        Bundle args = new Bundle();
        args.putLong("time", calendar.getDate());
        args.putInt("filters", arg2 + 1);
        filter = arg2 + 1;
        fragment.setArguments(args);
        transaction.add(R.id.pager_calendar, fragment, "frag");
        transaction.commit();
    }
    
    public void onNothingSelected( AdapterView<?> arg0 ) {
    
    }
    
}
