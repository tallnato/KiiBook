
package kii.kiibook.Agenda;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
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
import kii.kiibook.Student.R;

public class DialogNewEvent extends AlertDialog implements OnCheckedChangeListener {
    
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
    private final AlertDialog                       me;
    
    public DialogNewEvent( Activity context, LinearLayout parent, OnLongClickListener longClickListener,
                                    android.view.View.OnClickListener clickLiestener ) {
    
        super(context);
        this.parent = parent;
        this.context = context;
        clickListener = longClickListener;
        click = clickLiestener;
        me = this;
    }
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event_dialog);
        setTitle("Novo Evento");
        setCancelable(true);
        
        eventName = (EditText) findViewById(R.id.dialog_new_event_edittext);
        eventDesc = (EditText) findViewById(R.id.dialog_new_event_edittext_desc);
        eventName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            
            public void onFocusChange( View v, boolean hasFocus ) {
            
                if (hasFocus) {
                    me.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    
                }
            }
        });
        eventDesc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            
            public void onFocusChange( View v, boolean hasFocus ) {
            
                if (hasFocus) {
                    me.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    
                }
            }
        });
        
        radioGroup = (RadioGroup) findViewById(R.id.dialog_new_event_radioGroup);
        datePicker = (DatePicker) findViewById(R.id.datePicker_end);
        timePicker = (TimePicker) findViewById(R.id.timePicker_end);
        timePicker.setIs24HourView(true);
        
        radioGroup.setOnCheckedChangeListener(this);
        
        Button save = (Button) findViewById(R.id.dialog_new_event_button_save);
        save.setOnClickListener(new View.OnClickListener() {
            
            public void onClick( View v ) {
            
                // hourElem.getElemInHour().add(new TextElem(colorSelected,
                // event.getText().toString()));
                
                TextView event = new TextView(context);
                event.setText(typeSelected.toString() + " " + eventName.getText() + " - " + eventDesc.getText() + " - "
                                                + datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear()
                                                + " - " + timePicker.getCurrentHour() + "h");
                event.setOnClickListener(click);
                event.setTextColor(Color.WHITE);
                event.setOnLongClickListener(clickListener);
                event.setBackgroundResource(getColor(typeSelected));
                
                event.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
                parent.addView(event);
                
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
                return R.drawable.cell_green;
                
            case Teste:
                return R.drawable.cell_red;
                
            case TPC:
                return R.drawable.cell_orange_light;
                
            case Trabalho:
                return R.drawable.cell_orange;
                
            case Escolar:
                return R.drawable.cell_blue;
        }
        return R.drawable.cell_gray;
    }
}
