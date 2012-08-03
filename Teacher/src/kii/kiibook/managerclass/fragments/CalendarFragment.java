package kii.kiibook.managerclass.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kii.kiibook.managerclass.TestObjectsResources.ObjectCreator;
import kii.kiibook.managerclass.objects.MyCalendar;
import kii.kiibook.managerclass.objects.TextElem;
import kii.kiibook.managerclass.utils.Data;
import kii.kiibook.managerclass.utils.HourCalendarListView;
import kii.kiibook.teacher.R;
import kii.kiibook.teacher.adapters.AdapterCalendarView;

public class CalendarFragment extends Fragment implements OnItemLongClickListener, OnItemClickListener, OnDateChangeListener, OnCheckedChangeListener {
    
    public static final String              TAG  = "CalendarFragment";
    public static final String              FRAG = "fragment";
    
    private View                            view;
    private CalendarView                    calendar;
    private TextView                        textViewDay;
    private ListView                        listViewDay;
    private ArrayList<MyCalendar>           myCalendar;
    private AdapterCalendarView             adapter;
    private ArrayList<HourCalendarListView> hoursElem;
    private int                             colorSelected;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.calendar_fragment, container, false);
        Bundle bundle = getArguments();
        myCalendar = ObjectCreator.getInstance().getMyCalendar();
        
        calendar = (CalendarView) view.findViewById(R.id.calendarView_fragment_profile);
        textViewDay = (TextView) view.findViewById(R.id.calendar_fragment_text_title);
        listViewDay = (ListView) view.findViewById(R.id.calendar_fragment_listView);
        
        updateList();
        
        listViewDay.setOnItemLongClickListener(this);
        listViewDay.setOnItemClickListener(this);
        calendar.setOnDateChangeListener(this);
        
        textViewDay.setText(Data.dataExtensive(new Data()));
        
        return view;
    }
    
    
    public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth ) {
    
        textViewDay.setText(Data.dataExtensive(dayOfMonth, month + 1, year));
        updateList();
        
    }
    
    private ArrayList<HourCalendarListView> search( long time ) {
    
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String str = formatter.format(calendar.getTime());
        Data data = new Data(str);
        
        for (int i = 0; i < myCalendar.size(); i++) {
            if (myCalendar.get(i).getYear() == data.getAno() && myCalendar.get(i).getMonth() == data.getMes()
                                            && myCalendar.get(i).getDay() == data.getDia()) {
                Log.d(TAG, myCalendar.get(i).toString());
                return myCalendar.get(i).getListDay();
            }
        }
        return null;
    }
    
    private ArrayList<HourCalendarListView> search( int year, int month, int day ) {
    
        for (int i = 0; i < myCalendar.size(); i++) {
            if (myCalendar.get(i).getYear() == year && myCalendar.get(i).getMonth() == month && myCalendar.get(i).getDay() == day) {
                Log.d(TAG, myCalendar.get(i).toString());
                return myCalendar.get(i).getListDay();
            }
            
        }
        return null;
    }
    
  
    public boolean onItemLongClick( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
    
        hoursElem = search(calendar.getDate());
        final HourCalendarListView hourElem = hoursElem.get(arg2);
        
        // set up dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.new_event_dialog);
        dialog.setTitle("Novo Evento " + hourElem.getHour());
        dialog.setCancelable(true);
        
        final EditText event = (EditText) dialog.findViewById(R.id.dialog_new_event_edittext);
        
        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.dialog_new_event_radioGroup);
        
        radioGroup.setOnCheckedChangeListener(this);
        
        Button save = (Button) dialog.findViewById(R.id.dialog_new_event_button);
        save.setOnClickListener(new View.OnClickListener() {
            
        
            public void onClick( View v ) {
            
                hourElem.getElemInHour().add(new TextElem(colorSelected, event.getText().toString()));
                updateList();
                dialog.dismiss();
            }
        });
        dialog.show();
        return true;
    }
    

    public void onItemClick( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
    
        for (int i = 0; i < hoursElem.get(arg2).getElemInHour().size(); i++) {
            hoursElem.get(arg2).getElemInHour().remove(i);
        }
        updateList();
    }
    
    private void updateList() {
    
        hoursElem = search(calendar.getDate());
        adapter = new AdapterCalendarView(getActivity(), hoursElem);
        listViewDay.setAdapter(adapter);
    }
    
 
    public void onCheckedChanged( RadioGroup group, int checkedId ) {
    
        switch (checkedId) {
            case R.id.dialog_new_event_black:
                colorSelected = Color.BLACK;
                break;
            case R.id.dialog_new_event_blue:
                colorSelected = Color.BLUE;
                break;
            case R.id.dialog_new_event_red:
                colorSelected = Color.RED;
                break;
            case R.id.dialog_new_event_green:
                colorSelected = Color.GREEN;
                break;
            default:
                colorSelected = Color.BLACK;
        }
        
    }
}
