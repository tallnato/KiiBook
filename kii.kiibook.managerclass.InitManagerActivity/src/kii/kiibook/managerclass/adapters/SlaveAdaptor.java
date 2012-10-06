
package kii.kiibook.managerclass.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import objects.Student;

import java.util.List;

import kii.kiibook.managerclass.PermissionsArrayAdapter;
import kii.kiibook.teacher.CommunicationService;
import kii.kiibook.teacher.R;

public class SlaveAdaptor extends ArrayAdapter<Student> implements OnClickListener {
    
    final Messenger                 mMessenger = new Messenger(new ActivityServiceHandler());
    private final int               layoutResourceId;
    private final Context           context;
    private final List<Student>     objects;
    private Student                 slave;
    private Dialog                  dialog;
    private PermissionsArrayAdapter mAdapter;
    
    protected Messenger             mService;
    private int                     position;
    
    public SlaveAdaptor( Context context, int viewResourceId, List<Student> objects ) {
    
        super(context, viewResourceId, objects);
        
        layoutResourceId = viewResourceId;
        this.context = context;
        this.objects = objects;
        Intent serviceIntent = new Intent(context, CommunicationService.class);
        context.bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
        
    }
    
    @Override
    public View getView( final int position, View convertView, ViewGroup parent ) {
    
        View row = convertView;
        this.position = position;
        
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }
        
        slave = getItem(position);
        
        TextView name = (TextView) row.findViewById(R.id.classbook_item_name);
        ImageView image = (ImageView) row.findViewById(R.id.imageView_classmode);
        TextView status = (TextView) row.findViewById(R.id.classbook_item_status);
        CheckBox check = (CheckBox) row.findViewById(R.id.checkBox_assiduity);
        
        check.setChecked(true);
        name.setText(slave.getName());
        status.setText("Online");
        status.setTextColor(Color.GREEN);
        image.setImageDrawable(row.getResources().getDrawable(slave.getPic()));
        image.setOnClickListener(new OnClickListener() {
            
            public void onClick( View v ) {
            
                showDialogItem(slave, position);
                
            }
        });
        return row;
    }
    
    public Student getSlave( String ip ) {
    
        for (Student s : objects) {
            if (s.getIpAdrress().equals(ip)) {
                return s;
            }
        }
        return null;
    }
    
    public void onClick( View v ) {
    
        showDialogItem(slave, 0);
        
    }
    
    private void showDialogItem( final Student student, final int ind ) {
    
        String[] items = { "Ver Perfil", "Aplicações Bloqueadas" };
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            
            public void onClick( DialogInterface dialog, int item ) {
            
                switch (item) {
                    case 0:
                        showDialogProfile(student, ind);
                        break;
                    case 1:
                        showPermissions(student);
                        break;
                    case 2:
                        
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    private void showDialogProfile( Student student, int ind ) {
    
        Intent i;
        PackageManager manager = context.getPackageManager();
        
        i = manager.getLaunchIntentForPackage("kii.profile");
        
        if (i == null) {
            
            Toast.makeText(context.getApplicationContext(), "Perfil não instalado...", Toast.LENGTH_SHORT).show();
            
            return;
            
        }
        
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.putExtra("index", ind);
        context.startActivity(i);
        
    }
    
    private void showPermissions( final Student student ) {
    
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.permission_list);
        
        if (student.getPackages() == null) {
            Log.w("", "No Packages :(");
        }
        
        mAdapter = new PermissionsArrayAdapter(getContext(), R.layout.fragment_permissions_item_list, student.getPackages(), null);
        ListView list = (ListView) dialog.findViewById(R.id.list_permissions);
        list.setAdapter(mAdapter);
        
        Button btn = (Button) dialog.findViewById(R.id.actionBar_button);
        btn.setOnClickListener(new OnClickListener() {
            
            public void onClick( View v ) {
            
                student.setPackages(mAdapter.getBlockedApps());
                
                dialog.dismiss();
                sendMsgtoService();
                
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
    
    private void sendMsgtoService() {
    
        Message msg = new Message();
        
        try {
            mService.send(msg.obtain(null, CommunicationService.MSG_RECEIVE_APPS, position, 0));
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
    public class ActivityServiceHandler extends Handler {
        
        @Override
        public void handleMessage( Message msg ) {
        
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
