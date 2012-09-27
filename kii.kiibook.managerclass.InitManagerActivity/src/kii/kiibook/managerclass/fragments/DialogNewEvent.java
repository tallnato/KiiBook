
package kii.kiibook.managerclass.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TimePicker;

import objects.EventType;
import objects.NewEvent;
import kii.kiibook.teacher.CommunicationService;
import kii.kiibook.teacher.R;

public class DialogNewEvent extends Dialog implements OnCheckedChangeListener {
    
    private final Messenger mService;
    private final Context   context;
    private EventType       typeSelected;
    private EditText        eventName;
    private EditText        eventDesc;
    private RadioGroup      radioGroup;
    private DatePicker      datePicker;
    private TimePicker      timePicker;
    private final Dialog    me;
    protected NewEvent      newEvent;
    
    public DialogNewEvent( Context context, Messenger mService ) {
    
        super(context);
        this.mService = mService;
        this.context = context;
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        me = this;
    }
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_event_dialog);
        setCancelable(true);
        
        radioGroup = (RadioGroup) findViewById(R.id.dialog_new_event_radioGroup);
        datePicker = (DatePicker) findViewById(R.id.datePicker_end);
        timePicker = (TimePicker) findViewById(R.id.timePicker_end);
        timePicker.setIs24HourView(true);
        eventName = (EditText) findViewById(R.id.dialog_new_event_edittext);
        eventDesc = (EditText) findViewById(R.id.dialog_new_event_edittext_desc);
        eventName.setFocusable(true);
        eventDesc.setFocusable(true);
        
        radioGroup.setOnCheckedChangeListener(this);
        
        Button save = (Button) findViewById(R.id.dialog_new_event_button_save);
        save.setOnClickListener(new View.OnClickListener() {
            
            public void onClick( View v ) {
            
                long date = datePicker.getCalendarView().getDate();
                int hour = timePicker.getCurrentHour();
                
                newEvent = new NewEvent(eventName.getText().toString(), eventDesc.getText().toString(), typeSelected, date, hour);
                
                Message msg = new Message();
                msg.obj = newEvent;
                msg.what = CommunicationService.MSG_SEND_NEW_EVENT;
                try {
                    mService.send(msg);
                }
                catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
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
            case R.id.dialog_new_event_test:
                typeSelected = EventType.Teste;
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
