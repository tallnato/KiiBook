
package kii.kiibook.Agenda;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import objects.EventType;
import objects.NewEvent;
import kii.kiibook.Student.R;
import kii.kiibook.Student.database.DataShared;

class DialogNewEvent extends Dialog implements OnCheckedChangeListener {
    
    private final LinearLayout                      parent;
    private final Activity                          context;
    private EventType                               typeSelected;
    private final OnLongClickListener               clickListener;
    private EditText                                eventName;
    private EditText                                eventDesc;
    private RadioGroup                              radioGroup;
    private DatePicker                              datePicker;
    private TimePicker                              timePicker;
    private final android.view.View.OnClickListener click;
    private FragmentTransaction                     transaction;
    private FragmentWeek                            fragment;
    private int                                     filters;
    private long                                    timeStamp;
    
    public DialogNewEvent( Activity context, LinearLayout parent, OnLongClickListener longClickListener,
                                    android.view.View.OnClickListener clickLiestener, FragmentTransaction transaction, FragmentWeek frag,
                                    int filter, long time ) {
    
        super(context);
        this.parent = parent;
        this.context = context;
        this.transaction = transaction;
        clickListener = longClickListener;
        click = clickLiestener;
        fragment = frag;
        filters = filter;
        timeStamp = time;
        
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        
    }
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_event_dialog);
        setTitle("Novo Evento");
        setCancelable(true);
        
        eventName = (EditText) findViewById(R.id.dialog_new_event_edittext);
        eventDesc = (EditText) findViewById(R.id.dialog_new_event_edittext_desc);
        
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(eventName, InputMethodManager.SHOW_FORCED);
        
        radioGroup = (RadioGroup) findViewById(R.id.dialog_new_event_radioGroup);
        datePicker = (DatePicker) findViewById(R.id.datePicker_end);
        timePicker = (TimePicker) findViewById(R.id.timePicker_end);
        timePicker.setIs24HourView(true);
        
        radioGroup.setOnCheckedChangeListener(this);
        
        Button save = (Button) findViewById(R.id.dialog_new_event_button_save);
        save.setOnClickListener(new View.OnClickListener() {
            
            public void onClick( View v ) {
            
                TextView event = new TextView(context);
                if ((eventName.getText() != null) && (eventDesc.getText() != null)) {
                    event.setText(eventName.getText() + "\n" + eventDesc.getText() + "\n" + datePicker.getDayOfMonth() + "/"
                                                    + datePicker.getMonth() + "/" + datePicker.getYear() + " - "
                                                    + timePicker.getCurrentHour() + "h");
                    event.setOnClickListener(click);
                    event.setTextColor(Color.BLACK);
                    event.setGravity(Gravity.CENTER);
                    event.setOnLongClickListener(clickListener);
                    EventType types = typeSelected;
                    event.setBackgroundResource(getColor(typeSelected));
                    event.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
                    
                    // parent.addView(event);
                    
                    NewEvent ev = new NewEvent(eventName.getText().toString(), eventDesc.getText() + "\n" + datePicker.getDayOfMonth()
                                                    + "/" + datePicker.getMonth() + "/" + datePicker.getYear() + " - "
                                                    + timePicker.getCurrentHour() + "h", types, datePicker.getCalendarView().getDate(),
                                                    timePicker.getCurrentHour());
                    DataShared.getInstance().getListEvents().add(ev);
                    
                    transaction.detach(fragment);
                    transaction.remove(fragment);
                    fragment = (FragmentWeek) FragmentWeek.newInstance();
                    Bundle args = new Bundle();
                    args.putLong("time", timeStamp);
                    args.putInt("filters", filters);
                    fragment.setArguments(args);
                    transaction.add(R.id.pager_calendar, fragment, "frag");
                    transaction.commit();
                    
                }
                dismiss();
            }
        });
        
        Button exit = (Button) findViewById(R.id.dialog_new_event_button_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            
            public void onClick( View v ) {
            
                dismiss();
            }
        });
    }
    
    public void onCheckedChanged( RadioGroup group, int checkedId ) {
    
        switch (checkedId) {
            case R.id.dialog_new_event_work:
                typeSelected = EventType.Trabalho;
                break;
            case R.id.dialog_new_event_tpc:
                typeSelected = EventType.TPC;
                break;
            case R.id.dialog_new_event_school:
                typeSelected = EventType.Escolar;
                break;
            case R.id.dialog_new_event_other:
                typeSelected = EventType.Outro;
                break;
        }
    }
    
    public static int getColor( EventType type ) {
    
        switch (type) {
            case Aula:
                return R.drawable.cell_blue;
                
            case Teste:
                return R.drawable.cell_red;
                
            case TPC:
                return R.drawable.cell_orange_light;
                
            case Trabalho:
                return R.drawable.cell_orange;
                
            case Escolar:
                return R.drawable.cell_green;
        }
        return R.drawable.cell_gray;
    }
}
