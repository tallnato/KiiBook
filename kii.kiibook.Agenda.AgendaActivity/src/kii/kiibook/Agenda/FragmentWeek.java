
package kii.kiibook.Agenda;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import objects.Data;
import objects.EventType;
import objects.HourCalendarListView;
import objects.MyCalendar;
import objects.NewEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import kii.kiibook.Student.R;
import kii.kiibook.Student.database.DataShared;

public class FragmentWeek extends Fragment implements OnLongClickListener, OnClickListener, OnTouchListener {
    
    private View                            view;
    private TableLayout                     tableLayout;
    private boolean                         portrait = false;
    private LinearLayout                    cell;
    private int                             idCell   = 0;
    private ArrayList<MyCalendar>           myCalendar;
    private ArrayList<HourCalendarListView> hoursElem;
    private TableLayout                     table;
    
    private Calendar                        cal;
    private final String[]                  str      = { "SEG - ", "TER - ", "QUA - ", "QUI - ", "SEX - ", "SAB - ", "DOM - " };
    private final int[]                     ids      = { R.id.textView_seg, R.id.textView_ter, R.id.textView_qua, R.id.textView_qui,
                                    R.id.textView_sex, R.id.textView_sab, R.id.textView_dom };
    private long                            timeStamp;
    
    public static Fragment newInstance() {
    
        FragmentWeek mFrgment = new FragmentWeek();
        return mFrgment;
    }
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
    
    private int getDay( long time ) {
    
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(cal.MONDAY);
        cal.setTimeInMillis(time);
        Date date = cal.getTime();
        cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    
    private int getFirstDayofWeek( long time ) {
    
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(time);
        while (calendar.get(Calendar.DAY_OF_WEEK) > calendar.getFirstDayOfWeek()) {
            calendar.add(Calendar.DATE, -1);
        }
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        view = inflater.inflate(R.layout.fragment_week, container, false);
        timeStamp = getArguments().getLong("time");
        
        view.requestFocus();
        
        final ScrollView scroll = (ScrollView) view.findViewById(R.id.scrollView_calendar);
        
        scroll.post(new Runnable() {
            
            public void run() {
            
                scroll.scrollTo(0, 673);
            }
        });
        
        tableLayout = (TableLayout) view.findViewById(R.id.tablelayout_week);
        table = (TableLayout) view.findViewById(R.id.table_week_days);
        TableRow row = (TableRow) table.findViewById(R.id.tableRow_days);
        
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setFirstDayOfWeek(calendar.MONDAY);
        calendar.setTimeInMillis(timeStamp);
        while (calendar.get(Calendar.DAY_OF_WEEK) > calendar.getFirstDayOfWeek()) {
            calendar.add(Calendar.DATE, -1); // Substract 1 day until first day
                                             // of week.
        }
        calendar.setFirstDayOfWeek(calendar.MONDAY);
        
        int dayofWeek = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d(getTag(), dayofWeek + " - " + calendar.getFirstDayOfWeek());
        int cons = dayofWeek - 1;
        for (int i = 0; i < 7; i++) {
            cons++;
            
            if ((calendar.get(Calendar.MONTH) == Calendar.SEPTEMBER) || (calendar.get(Calendar.MONTH) == Calendar.APRIL)
                                            || (calendar.get(Calendar.MONTH) == Calendar.JUNE)
                                            || (calendar.get(Calendar.MONTH) == Calendar.NOVEMBER)) {
                
                if (cons > 30) {
                    cons = 1;
                }
            } else if ((calendar.get(Calendar.MONTH) == Calendar.FEBRUARY)) {
                if (cons > 28) {
                    cons = 1;
                }
            }
            if (cons > 31) {
                cons = 1;
            }
            ((TextView) row.findViewById(ids[i])).setText(str[i] + cons);
        }
        
        myCalendar = DataShared.getInstance().getMyCalendar();
        int i = inflater.getContext().getResources().getConfiguration().orientation;
        if (i == Configuration.ORIENTATION_PORTRAIT) {
            
            portrait = true;
        }
        createTable();
        
        return view;
    }
    
    private void createTable() {
    
        TableRow row;
        TextView textView;
        
        for (int hour = 0; hour < 24; hour++) {
            row = new TableRow(getActivity());
            row.setId(200 + hour);
            for (int day = 0; day < 8; day++) {
                if (day == 0) {
                    cell = new LinearLayout(getActivity());
                    cell.setBackgroundResource(R.drawable.cell_black_cell);
                    textView = new TextView(getActivity());
                    textView.setTextColor(Color.BLACK);
                    textView.setTextSize(18);
                    textView.setPadding(5, 4, 0, 0);
                    textView.setText(hour + "h");
                    cell.addView(textView);
                    
                } else {
                    cell = new LinearLayout(getActivity());
                    cell.setId(1000 + day);
                    cell.setTag("day" + day + "hour" + hour);
                    cell.setClickable(true);
                    cell.setId(idCell++);
                    cell.setOnLongClickListener(this);
                    cell.setBackgroundResource(R.drawable.cell_shape);
                    createCalendar(hour, day);
                    
                }
                if (!portrait) {
                    row.addView(cell, 105, 84);
                } else {
                    row.addView(cell, 96, 84);
                }
            }
            tableLayout.addView(row);
        }
        insertEvents();
    }
    
    private void insertEvents() {
    
        int indexDay;
        Iterator<NewEvent> it = DataShared.getInstance().getListEvents().iterator();
        ArrayList<Events> evList = new ArrayList<Events>();
        
        // timeStamp - time selected on calendar
        int dayOfweekTimeStamp = getFirstDayofWeek(timeStamp) - 1;
        
        while (it.hasNext()) {
            NewEvent event = it.next();
            long time = event.getDate();
            int dayOfweek = getFirstDayofWeek(time) - 1;
            if (dayOfweek == dayOfweekTimeStamp) {
                Log.w(getTag(), "FOOOOUUUNNNNNDDDDDD");
                indexDay = getDay(time) - dayOfweekTimeStamp;
                evList.add(new Events(indexDay, event.getHour(), event));
            }
        }
        
        Iterator<Events> iterator = evList.iterator();
        
        while (iterator.hasNext()) {
            Events ev = iterator.next();
            
            Log.w(getTag(), ev.toString());
            
            LinearLayout cello = (LinearLayout) view.findViewWithTag("day" + ev.getIndex() + "hour" + ev.getHour());
            TextView events = new TextView(this.getActivity());
            events.setText(ev.getEvent().getWhat() + " - " + ev.getEvent().getDescription());
            events.setOnClickListener(this);
            events.setTextColor(Color.BLACK);
            events.setOnLongClickListener(this);
            events.setGravity(Gravity.CENTER);
            events.setBackgroundResource(DialogNewEvent.getColor(ev.getEvent().getType()));
            events.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
            cello.addView(events);
        }
    }
    
    /**
     * Criador do horario
     * 
     * @param hour
     *            hora do dia
     * @param day
     *            dia da semana
     */
    private void createCalendar( int hour, int day ) {
    
        if ((hour == 9) || (hour == 10)) {
            if ((day == 1) || (day == 3)) {
                String str = EventType.Aula.toString() + "\n" + " de Lingua Portuguesa\nSala 23";
                TextView event = new TextView(this.getActivity());
                event.setText(str);
                event.setOnClickListener(this);
                event.setGravity(Gravity.CENTER);
                event.setTextColor(Color.BLACK);
                event.setOnLongClickListener(this);
                event.setBackgroundResource(DialogNewEvent.getColor(EventType.Aula));
                event.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
                cell.addView(event);
            }
            if ((day == 2) || (day == 4)) {
                String str = EventType.Aula.toString() + "\n" + " de Matemática\nSala 23";
                TextView event = new TextView(this.getActivity());
                event.setText(str);
                event.setOnClickListener(this);
                event.setGravity(Gravity.CENTER);
                event.setTextColor(Color.BLACK);
                event.setOnLongClickListener(this);
                event.setBackgroundResource(DialogNewEvent.getColor(EventType.Aula));
                event.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
                cell.addView(event);
            }
            if (day == 5) {
                String str = EventType.Aula.toString() + "\n" + " de Inglês\nSala 23";
                TextView event = new TextView(this.getActivity());
                event.setText(str);
                event.setOnClickListener(this);
                event.setGravity(Gravity.CENTER);
                event.setTextColor(Color.BLACK);
                event.setOnLongClickListener(this);
                event.setBackgroundResource(DialogNewEvent.getColor(EventType.Aula));
                event.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
                cell.addView(event);
            }
        }
        if ((hour == 11) || (hour == 12)) {
            if ((day == 1) || (day == 4)) {
                String str = EventType.Aula.toString() + "\n" + " de Ciencias\nSala 23";
                TextView event = new TextView(this.getActivity());
                event.setText(str);
                event.setOnClickListener(this);
                event.setTextColor(Color.BLACK);
                event.setGravity(Gravity.CENTER);
                event.setOnLongClickListener(this);
                event.setBackgroundResource(DialogNewEvent.getColor(EventType.Aula));
                event.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
                cell.addView(event);
            }
            if (day == 2) {
                String str = EventType.Aula.toString() + "\n" + " de Historia\nSala 23";
                TextView event = new TextView(this.getActivity());
                event.setText(str);
                event.setOnClickListener(this);
                event.setTextColor(Color.BLACK);
                event.setGravity(Gravity.CENTER);
                event.setOnLongClickListener(this);
                event.setBackgroundResource(DialogNewEvent.getColor(EventType.Aula));
                event.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
                cell.addView(event);
            }
            if (day == 3) {
                String str = EventType.Aula.toString() + "\n" + " de Inglês\nSala 23";
                TextView event = new TextView(this.getActivity());
                event.setText(str);
                event.setOnClickListener(this);
                event.setTextColor(Color.BLACK);
                event.setGravity(Gravity.CENTER);
                event.setOnLongClickListener(this);
                event.setBackgroundResource(DialogNewEvent.getColor(EventType.Aula));
                event.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
                cell.addView(event);
            }
            if (day == 5) {
                String str = EventType.Aula.toString() + "\n" + " de Educação Fisica\nSala Ginasio";
                TextView event = new TextView(this.getActivity());
                event.setText(str);
                event.setOnClickListener(this);
                event.setTextColor(Color.BLACK);
                event.setGravity(Gravity.CENTER);
                event.setOnLongClickListener(this);
                event.setBackgroundResource(DialogNewEvent.getColor(EventType.Aula));
                event.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
                cell.addView(event);
            }
        }
        if (hour == 15) {
            if (day == 2) {
                String str = EventType.Aula.toString() + "\n" + " de Mural\nSala 23";
                TextView event = new TextView(this.getActivity());
                event.setText(str);
                event.setOnClickListener(this);
                event.setTextColor(Color.BLACK);
                event.setGravity(Gravity.CENTER);
                event.setOnLongClickListener(this);
                event.setBackgroundResource(DialogNewEvent.getColor(EventType.Aula));
                event.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
                cell.addView(event);
            }
            if (day == 1) {
                String str = EventType.Aula.toString() + "\n" + " de E. Visual\nSala 23";
                TextView event = new TextView(this.getActivity());
                event.setText(str);
                event.setOnClickListener(this);
                event.setGravity(Gravity.CENTER);
                event.setTextColor(Color.BLACK);
                event.setOnLongClickListener(this);
                event.setBackgroundResource(DialogNewEvent.getColor(EventType.Aula));
                event.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
                cell.addView(event);
            }
            if (day == 4) {
                String str = EventType.Aula.toString() + "\n" + " de História\nSala 23";
                TextView event = new TextView(this.getActivity());
                event.setText(str);
                event.setOnClickListener(this);
                event.setTextColor(Color.BLACK);
                event.setGravity(Gravity.CENTER);
                event.setOnLongClickListener(this);
                event.setBackgroundResource(DialogNewEvent.getColor(EventType.Aula));
                event.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
                cell.addView(event);
            }
            if (day == 5) {
                String str = EventType.Aula.toString() + "\n" + " de História\nSala 23";
                TextView event = new TextView(this.getActivity());
                event.setText(str);
                event.setOnClickListener(this);
                event.setTextColor(Color.BLACK);
                event.setGravity(Gravity.CENTER);
                event.setOnLongClickListener(this);
                event.setBackgroundResource(DialogNewEvent.getColor(EventType.Aula));
                event.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
                cell.addView(event);
            }
        }
        if (hour == 16) {
            if (day == 4) {
                String str = EventType.Aula.toString() + "\n" + " de História\nSala 23";
                TextView event = new TextView(this.getActivity());
                event.setText(str);
                event.setOnClickListener(this);
                event.setTextColor(Color.BLACK);
                event.setGravity(Gravity.CENTER);
                event.setOnLongClickListener(this);
                event.setBackgroundResource(DialogNewEvent.getColor(EventType.Aula));
                event.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
                cell.addView(event);
            }
            if (day == 5) {
                String str = EventType.Aula.toString() + "\n" + " de História\nSala 23";
                TextView event = new TextView(this.getActivity());
                event.setText(str);
                event.setOnClickListener(this);
                event.setTextColor(Color.BLACK);
                event.setOnLongClickListener(this);
                event.setGravity(Gravity.CENTER);
                event.setBackgroundResource(DialogNewEvent.getColor(EventType.Aula));
                event.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
                cell.addView(event);
            }
        }
        
    }
    
    public boolean onLongClick( View v ) {
    
        LinearLayout parent;
        if (v.getClass() == LinearLayout.class) {
            parent = (LinearLayout) v;
        } else {
            TextView child = (TextView) v;
            parent = (LinearLayout) child.getParent();
        }
        dialogRequestElem(parent);
        return true;
    }
    
    public void onClick( View v ) {
    
        TextView text = (TextView) v;
        TextView txt = new TextView(getActivity());
        txt.setText(text.getText());
        
        txt.setBackgroundDrawable(text.getBackground());
        txt.setTextSize(30);
        txt.setTextColor(text.getTextColors());
        txt.setGravity(Gravity.CENTER);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(txt);
        builder.show();
    }
    
    private ArrayList<HourCalendarListView> search( long time ) {
    
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String str = formatter.format(calendar.getTime());
        
        Data data = new Data(str);
        
        long times = (System.currentTimeMillis());
        
        for (int i = 0; i < myCalendar.size(); i++) {
            if (myCalendar.get(i).getYear() == data.getAno() && myCalendar.get(i).getMonth() == data.getMes()
                                            && myCalendar.get(i).getDay() == data.getDia()) {
                return myCalendar.get(i).getListDay();
            }
            
        }
        return null;
    }
    
    private void dialogRequestElem( LinearLayout parent ) {
    
        view.clearFocus();
        
        DialogNewEvent dialog = new DialogNewEvent(getActivity(), parent, this, this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.show();
        
    }
    
    public boolean onTouch( View v, MotionEvent event ) {
    
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            
        }
        
        return false;
    }
    
}
