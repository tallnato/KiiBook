
package kii.kiibook.managerclass.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import objects.EventType;
import objects.MediaBook;
import objects.NewEvent;

import java.io.File;
import java.util.ArrayList;

import kii.kiibook.managerclass.utils.MediabookListView;
import kii.kiibook.managerclass.utils.MediabooksList;
import kii.kiibook.teacher.CommunicationService;
import kii.kiibook.teacher.R;

public class DialogNewEvent extends Dialog implements OnCheckedChangeListener, OnItemSelectedListener, OnClickListener {
    
    final String[]               caps = { "Multiplicação de números racionais", "Potências de expoente natural",
                                    "Números inversos diferentes de zero", "Divisão de números racionais" };
    
    private ArrayList<MediaBook> links;
    private EventType            typeSelected;
    private final Messenger      mService;
    private final Context        context;
    
    private EditText             eventDesc;
    private RadioGroup           radioGroup;
    private DatePicker           datePicker;
    private TimePicker           timePicker;
    private LinearLayout         containerLinks;
    protected NewEvent           newEvent;
    private Spinner              spinnerBook;
    private Spinner              spinnerPage;
    private Button               buttonAddLink;
    private Button               buttonSave;
    
    public DialogNewEvent( Context context, Messenger mService ) {
    
        super(context);
        this.mService = mService;
        this.context = context;
        links = new ArrayList<MediaBook>();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_event_dialog);
        setCancelable(true);
        
        radioGroup = (RadioGroup) findViewById(R.id.dialog_new_event_radioGroup);
        eventDesc = (EditText) findViewById(R.id.dialog_new_event_edittext_desc);
        spinnerBook = (Spinner) findViewById(R.id.dialog_new_event_spinner_book);
        spinnerPage = (Spinner) findViewById(R.id.dialog_new_event_spinner_page);
        buttonAddLink = (Button) findViewById(R.id.dialog_new_event_add_link);
        buttonSave = (Button) findViewById(R.id.dialog_new_event_button_save);
        containerLinks = (LinearLayout) findViewById(R.id.dialog_new_event_container_links);
        datePicker = (DatePicker) findViewById(R.id.datePicker_end);
        timePicker = (TimePicker) findViewById(R.id.timePicker_end);
        
        timePicker.setIs24HourView(true);
        eventDesc.setFocusable(true);
        buttonSave.setOnClickListener(this);
        buttonAddLink.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        
        checkExternalMedia();
        MediabookListView media = checkFilesList();
        
        String[] items = new String[media.getList().size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = media.getList().get(i).getName();
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBook.setAdapter(adapter);
        
        ArrayAdapter<String> adapters = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, caps);
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPage.setAdapter(adapters);
        
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
    
    private void checkExternalMedia() {
    
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        
        String state = Environment.getExternalStorageState();
        
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        
    }
    
    private MediabookListView checkFilesList() {
    
        MediabookListView mediabooks = new MediabookListView();
        
        File root = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/kiibooks/");
        
        File[] listFiles = root.listFiles();
        
        for (File file : listFiles) {
            String name = file.getName();
            File dirMedia = new File(root.getAbsolutePath() + "/" + name + "/page/");
            File[] pages = dirMedia.listFiles();
            int numPages = pages.length / 2;
            String[] names = new String[numPages];
            for (int i = 0; i < numPages; i++) {
                names[i] = "Page " + (i + 1);
            }
            
            mediabooks.addMediabook(new MediabooksList(name, numPages, names));
        }
        return mediabooks;
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
    
    public void onItemSelected( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
    
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, caps);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPage.setAdapter(adapter);
        
    }
    
    public void onNothingSelected( AdapterView<?> arg0 ) {
    
    }
    
    public void onClick( View v ) {
    
        switch (v.getId()) {
            case R.id.dialog_new_event_add_link:
                TextView link = new TextView(getContext());
                String book = (String) spinnerBook.getSelectedItem();
                String page = (String) spinnerPage.getSelectedItem();
                link.setText(book + ": " + page);
                link.setTextColor(Color.BLUE);
                containerLinks.addView(link);
                int pageNum = 0;
                
                switch (spinnerPage.getSelectedItemPosition()) {
                    case 0:
                        pageNum = 4;
                        break;
                    case 1:
                        pageNum = 6;
                        break;
                    case 2:
                        pageNum = 7;
                        break;
                    case 3:
                        pageNum = 9;
                        break;
                }
                links.add(new MediaBook(book, pageNum, page));
                
                break;
            
            case R.id.dialog_new_event_button_save:
                long date = datePicker.getCalendarView().getDate();
                int hour = timePicker.getCurrentHour();
                
                /**
                 * TODO TESTING all of this code that send event with links
                 **/
                
                newEvent = new NewEvent(eventDesc.getText().toString(), typeSelected, date, hour, links);
                
                Message msg = new Message();
                msg.obj = newEvent;
                msg.what = CommunicationService.MSG_SEND_NEW_EVENT;
                try {
                    mService.send(msg);
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
                
                dismiss();
                break;
        }
        
    }
}
