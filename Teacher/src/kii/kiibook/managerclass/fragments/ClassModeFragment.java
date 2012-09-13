
package kii.kiibook.managerclass.fragments;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import falconeye.tcp.server.ComunicationManager;
import objects.MediaBook;
import objects.Summary;
import util.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import kii.kiibook.managerclass.adapters.SlaveAdaptor;
import kii.kiibook.managerclass.adapters.SlaveAdaptorAll;
import kii.kiibook.managerclass.adapters.SlaveAdaptorOff;
import kii.kiibook.managerclass.database.DataShared;
import kii.kiibook.managerclass.utils.FileListView;
import kii.kiibook.managerclass.utils.MediabookListView;
import kii.kiibook.managerclass.utils.MediabooksList;
import kii.kiibook.teacher.CommunicationService;
import kii.kiibook.teacher.R;

public class ClassModeFragment extends Fragment implements OnItemClickListener, OnTabChangeListener, Constants {
    
    public static final String         TAG            = "ClassModeFragment";
    public static final String         FRAG           = "fragment";
    
    private View                       mRoot;
    private TabHost                    mTabHost;
    private Menu                       menu;
    private boolean                    finalized      = false;
    private boolean                    portrait       = false;
    
    public ComunicationManager         mComManager;
    protected Messenger                mService       = null;
    final Messenger                    mMessenger     = new Messenger(new ActivityServiceHandler());
    private boolean                    mIsBound;
    private final String               ON             = "ON";
    private final String               OFF            = "OFF";
    private final String               ALL            = "ALL";
    
    private SlaveAdaptor               mSlaveAdaptorOn;
    private SlaveAdaptorAll            mSlaveAdaptorAll;
    private SlaveAdaptorOff            mSlaveAdaptorOff;
    private SlaveList                  slave;
    private SlaveListOff               slaveoff;
    private SlaveListAll               slaveall;
    private EditText                   subject;
    private EditText                   text;
    private Intent                     serviceIntent;
    private View                       button;
    private View                       buttonAddBook;
    private ArrayList<FileListView>    files;
    private File                       currentDir;
    private Spinner                    spinnerBook;
    private Spinner                    spinnerMedia;
    private final ArrayList<MediaBook> listMediaBooks = new ArrayList<MediaBook>();
    private final ArrayList<String>    links          = new ArrayList<String>();
    private ArrayAdapter<String>       adapterListLinks;
    private ListView                   listLinks;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "onCreate");
        serviceIntent = new Intent(getActivity(), CommunicationService.class);
        
        if (!CommunicationService.isRunning()) {
            
            getActivity().startService(serviceIntent);
        }
        
        getActivity().bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();
        
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            portrait = true;
            
        } else {
            portrait = false;
        }
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.class_mode_frame, container, false);
        
        mRoot.requestFocus();
        
        Log.d(TAG, "onCreateView");
        mSlaveAdaptorOn = new SlaveAdaptor(getActivity(), R.layout.item_class_mode, DataShared.getInstance().getListOnline());
        mSlaveAdaptorOff = new SlaveAdaptorOff(getActivity(), R.layout.item_class_mode, DataShared.getInstance().getListOffline());
        mSlaveAdaptorAll = new SlaveAdaptorAll(getActivity(), R.layout.item_class_mode, DataShared.getInstance().getListAll());
        adapterListLinks = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, links);
        
        listLinks = (ListView) mRoot.findViewById(R.id.list_summary_mediabooks);
        listLinks.setAdapter(adapterListLinks);
        listLinks.setOnItemClickListener(this);
        
        text = (EditText) mRoot.findViewById(R.id.sum_text);
        buttonAddBook = mRoot.findViewById(R.id.button_add_link);
        buttonAddBook.setOnClickListener(new OnClickListener() {
            
            public void onClick( View v ) {
            
                addMediabook();
                
            }
        });
        createTabs();
        
        if (portrait) {
            button = mRoot.findViewById(R.id.button_send_summary);
            button.setOnClickListener(new OnClickListener() {
                
                public void onClick( View v ) {
                
                    catchSummary();
                    
                }
            });
        }
        
        return mRoot;
    }
    
    private void addMediabook() {
    
        checkExternalMedia();
        dialogSelectMediaBook(checkFilesList());
        
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
            // Can't read or writeradio
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        Log.d(TAG, "\n\nExternal Media: readable=" + mExternalStorageAvailable + " writable=" + mExternalStorageWriteable);
    }
    
    private MediabookListView checkFilesList() {
    
        files = new ArrayList<FileListView>();
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
    
    private void createTabs() {
    
        FragmentManager fm;
        
        mTabHost = (TabHost) mRoot.findViewById(R.id.tabhost);
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setup();
        
        TabSpec tspecAll = mTabHost.newTabSpec(ALL);
        tspecAll.setIndicator("Todos");
        tspecAll.setContent(R.id.tab_all);
        mTabHost.addTab(tspecAll);
        
        TabSpec tspecOn = mTabHost.newTabSpec(ON);
        tspecOn.setIndicator("Online");
        tspecOn.setContent(R.id.tab_on);
        mTabHost.addTab(tspecOn);
        
        TabSpec tspecOff = mTabHost.newTabSpec(OFF);
        tspecOff.setIndicator("Offline");
        tspecOff.setContent(R.id.tab_off);
        mTabHost.addTab(tspecOff);
        
        mTabHost.setCurrentTab(0);
    }
    
    private void dialogSelectMediaBook( final MediabookListView media ) {
    
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_add_mediabook, (ViewGroup) getActivity().findViewById(R.id.layout_root));
        Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Adicionar Mediabook");
        builder.setView(layout);
        final Dialog dialog = builder.create();
        dialog.show();
        
        spinnerBook = (Spinner) layout.findViewById(R.id.spinner_book);
        spinnerMedia = (Spinner) layout.findViewById(R.id.spinner_page);
        
        Button button = (Button) layout.findViewById(R.id.button_add_mediabook);
        button.setOnClickListener(new OnClickListener() {
            
            public void onClick( View v ) {
            
                String book = (String) spinnerBook.getSelectedItem();
                String cap = (String) spinnerMedia.getSelectedItem();
                int page = spinnerMedia.getSelectedItemPosition() + 1;
                listMediaBooks.add(new MediaBook(book, page, cap));
                links.add(book + " - " + cap);
                adapterListLinks.notifyDataSetChanged();
                dialog.dismiss();
                
            }
        });
        
        String[] items = new String[media.getList().size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = media.getList().get(i).getName();
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBook.setAdapter(adapter);
        
        spinnerBook.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
            
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, media
                                                .getList().get(arg2).getPages());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMedia.setAdapter(adapter);
                
            }
            
            public void onNothingSelected( AdapterView<?> arg0 ) {
            
                // TODO Auto-generated method stub
                
            }
        });
        
    }
    
    public void onItemClick( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
    
        listMediaBooks.remove(arg2);
        links.remove(arg2);
        adapterListLinks.notifyDataSetChanged();
        
    }
    
    public void onTabChanged( String arg0 ) {
    
        FragmentManager fm = getFragmentManager();
        if (arg0.equals(ON)) {
            slave = new SlaveList();
            slave.setListAdapter(mSlaveAdaptorOn);
            fm.beginTransaction().replace(R.id.tab_on, slave, arg0).commit();
            
        } else if (arg0.equals(OFF)) {
            slaveoff = new SlaveListOff();
            slaveoff.setListAdapter(mSlaveAdaptorOff);
            fm.beginTransaction().replace(R.id.tab_off, slaveoff, arg0).commit();
            
        } else {
            slaveall = new SlaveListAll();
            slaveall.setListAdapter(mSlaveAdaptorAll);
            fm.beginTransaction().replace(R.id.tab_all, slaveall, arg0).commit();
            
        }
        
    }
    
    void doUnbindService() {
    
        if (mIsBound) {
            // If we have received the service, and hence registered with it,
            // then now is the time to unregister.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null, CommunicationService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                }
                catch (RemoteException e) {
                    // There is nothing special we need to do if the service has
                    // crashed.
                }
            }
            // Detach our existing connection.
            getActivity().unbindService(mConnection);
            mIsBound = false;
            
        }
    }
    
    @Override
    public void onDestroy() {
    
        doUnbindService();
        super.onDestroy();
    }
    
    @Override
    public void onPrepareOptionsMenu( Menu menu ) {
    
        super.onPrepareOptionsMenu(menu);
        
        mSlaveAdaptorOn.notifyDataSetChanged();
        mSlaveAdaptorOff.notifyDataSetChanged();
        DataShared.getInstance().getListAll();
        mSlaveAdaptorAll.notifyDataSetChanged();
        
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    
        switch (item.getItemId()) {
            case R.id.menu_turn_off_falconEye:
                sendMsgtoService();
                break;
            case R.id.menu_class_send_summary:
                catchSummary();
                break;
            case R.id.menu_new_event:
                newEvent();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void newEvent() {
    
        DialogNewEvent dialog = new DialogNewEvent(this.getActivity(), mService);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.show();
        
    }
    
    private void catchSummary() {
    
        if ((text.getText().toString().equals(""))) {
            alertDialogSummaryEmpty("Sumário", "O sumário não foi preenchido!");
            
        } else {
            if (DataShared.getInstance().getListOnline().isEmpty()) {
                alertDialogSummaryEmpty("Olho Falcão", "Não existem alunos online!");
            } else {
                
                Calendar cal = Calendar.getInstance();
                Summary sum = new Summary(listMediaBooks, text.getText().toString(), cal.getTime(), DataShared.getInstance()
                                                .getListSummaries().size());
                sendMsgtoServiceSummary(sum);
                DataShared.getInstance().getListSummaries().add(sum);
            }
        }
    }
    
    private void alertDialogSummaryEmpty( String title, String msg ) {
    
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.alert_dialog_error, (ViewGroup) getActivity().findViewById(R.id.layout_root));
        
        TextView text = (TextView) layout.findViewById(R.id.alert_dialog_error_text);
        text.setText(msg);
        ImageView image = (ImageView) layout.findViewById(R.id.alert_dialog_error_image);
        image.setImageResource(R.drawable.alert_icon);
        
        Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setView(layout);
        builder.create().show();
        
    }
    
    private void sendMsgtoServiceSummary( Summary sum ) {
    
        Message msg = new Message();
        msg.obj = sum;
        msg.what = CommunicationService.MSG_SEND_SUMMARY;
        try {
            mService.send(msg);
        }
        catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
    
        this.menu = menu;
        Log.d(TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.turn_off_falcon_eye, menu);
        inflater.inflate(R.menu.menu_new_event, menu);
        if (!portrait) {
            inflater.inflate(R.menu.send_summary, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
        this.finalized = true;
    }
    
    private void sendMsgtoService() {
    
        Message msg = new Message();
        
        try {
            mService.send(msg.obtain(null, CommunicationService.MSG_DISCONNECT_SLAVES, 0, 0));
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
    public class ActivityServiceHandler extends Handler {
        
        public static final int UPDATE_LIST = 90;
        public static final int SEND_APPS   = 92;
        
        @Override
        public void handleMessage( Message msg ) {
        
            switch (msg.what) {
                case CommunicationService.MSG_UPDATE_LIST:
                    DataShared.getInstance().getListAll();
                    mSlaveAdaptorOn.notifyDataSetChanged();
                    mSlaveAdaptorOff.notifyDataSetChanged();
                    mSlaveAdaptorAll.notifyDataSetChanged();
                    
                    break;
                
                default:
                    // Toast.makeText(getApplicationContext(), "nothing... " +
                    // msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    super.handleMessage(msg);
            }
        }
        
    }
    
    private final ServiceConnection mConnection = new ServiceConnection() {
                                                    
                                                    public void onServiceConnected( ComponentName className, IBinder service ) {
                                                    
                                                        mService = new Messenger(service);
                                                        
                                                        try {
                                                            Message msg = Message.obtain(null, CommunicationService.MSG_REGISTER_CLIENT);
                                                            msg.replyTo = mMessenger;
                                                            mService.send(msg);
                                                        }
                                                        catch (RemoteException e) {
                                                            // In this case the
                                                            // service has
                                                            // crashed before we
                                                            // could even do
                                                            // anything with it
                                                        }
                                                    }
                                                    
                                                    public void onServiceDisconnected( ComponentName className ) {
                                                    
                                                        // This is called when
                                                        // the connection with
                                                        // the service has been
                                                        // unexpectedly
                                                        // disconnected -
                                                        // process crashed.
                                                        mService = null;
                                                        
                                                    }
                                                };
    
}
