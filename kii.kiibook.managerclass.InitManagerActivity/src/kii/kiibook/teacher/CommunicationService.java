
package kii.kiibook.teacher;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import falconeye.tcp.server.ComunicationManager;
import messages.tcp.internal.GetSlaveApplicationList;
import messages.tcp.internal.InternalTcpMessage;
import messages.tcp.internal.NewClient;
import messages.tcp.internal.NewTcpMessage;
import messages.tcp.network.ApplicationList;
import messages.tcp.network.ApplicationList_Response;
import messages.tcp.network.ApplicationList_Response_ACK;
import messages.tcp.network.CloseConnectionAck;
import messages.tcp.network.CloseConnectionNetwork;
import messages.tcp.network.NewEventNetwork;
import messages.tcp.network.SummaryNetwork;
import messages.udp.Connect;
import messages.udp.Discover;
import messages.udp.UdpMessage;
import objects.NewEvent;
import objects.PackagePermissions;
import objects.Student;
import objects.Summary;
import util.Constants;
import util.SlaveStatus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kii.kiibook.managerclass.callbacks.SlaveDetailsCallback;
import kii.kiibook.managerclass.database.DataShared;

public class CommunicationService extends Service implements Constants {
    
    public static final int     MSG_REGISTER_CLIENT   = 1;
    public static final int     MSG_UNREGISTER_CLIENT = 2;
    public static final int     MSG_UPDATE_LIST       = 3;
    public static final int     MSG_SEND_SUMMARY      = 4;
    public static final int     MSG_DISCONNECT_SLAVES = 5;
    public static final int     MSG_RECEIVE_APPS      = 6;
    public static final int     MSG_SEND_NEW_EVENT    = 7;
    
    ArrayList<Messenger>        mClients              = new ArrayList<Messenger>();
    final Messenger             mMessenger            = new Messenger(new ServiceActivityHandler());
    private ComunicationManager mComManager;
    private MyHandler           mHandler;
    private String              myIp;
    private DataShared          dataShared;
    protected Messenger         mService;
    protected Toast             textStatus;
    public Student              stdCurrentAdded;
    private boolean             enable;
    public boolean              firstTime             = false;
    private static boolean      isRunning             = false;
    
    @Override
    public IBinder onBind( Intent intent ) {
    
        enable = true;
        return mMessenger.getBinder();
    }
    
    @Override
    public void onCreate() {
    
        Toast.makeText(CommunicationService.this, "onCreate", Toast.LENGTH_SHORT).show();
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        myIp = Formatter.formatIpAddress(wifiInfo.getIpAddress());
        
        mHandler = new MyHandler();
        mComManager = new ComunicationManager(myIp, mHandler);
        
        dataShared = DataShared.getInstance();
        isRunning = true;
        
    }
    
    public static boolean isRunning() {
    
        return isRunning;
        
    }
    
    @Override
    public void onDestroy() {
    
        mComManager.close();
        mHandler = null;
        super.onDestroy();
        
    }
    
    @Override
    public void onRebind( Intent intent ) {
    
        enable = true;
        super.onRebind(intent);
    }
    
    public SlaveDetailsCallback getSlaveDetailsCallback() {
    
        return null;
    }
    
    @Override
    public boolean onUnbind( Intent intent ) {
    
        Log.w(TAG, "cenassssssssssss");
        return super.onUnbind(intent);
    }
    
    public void getPackages( Student slave ) {
    
        ApplicationList msg = new ApplicationList();
        mComManager.sendTCPMessage(new GetSlaveApplicationList(slave.getComChannel(), msg));
        
    }
    
    public void disconnectSlaves() {
    
        CloseConnectionNetwork msg = new CloseConnectionNetwork();
        Iterator<Student> it = DataShared.getInstance().getListOnline().iterator();
        while (it.hasNext()) {
            Student slave = it.next();
            if (slave.getStatus() == SlaveStatus.CONNECTED) {
                slave.setStatus(SlaveStatus.DISCONNECT);
                
                mComManager.sendTCPMessage(new GetSlaveApplicationList(slave.getComChannel(), msg));
                Log.d(TAG, "Disconnect: " + slave.getName() + " @ " + slave.getIpAdrress());
                sendMessageToUIUpdate();
                
            }
        }
    }
    
    private void handleSummarySend( Summary sum ) {
    
        SummaryNetwork msg = new SummaryNetwork(sum);
        Iterator<Student> it = DataShared.getInstance().getListOnline().iterator();
        while (it.hasNext()) {
            Student slave = it.next();
            if (slave.getStatus() == SlaveStatus.CONNECTED) {
                Log.d(TAG, "Send summary to: " + slave.getName() + " @" + slave.getIpAdrress() + "Msg: " + msg.toString());
                mComManager.sendTCPMessage(new GetSlaveApplicationList(slave.getComChannel(), msg));
            }
        }
    }
    
    private void handleNewEventSend( NewEvent event ) {
    
        NewEventNetwork msg = new NewEventNetwork(event);
        Iterator<Student> it = DataShared.getInstance().getListOnline().iterator();
        while (it.hasNext()) {
            Student slave = it.next();
            if (slave.getStatus() == SlaveStatus.CONNECTED) {
                mComManager.sendTCPMessage(new GetSlaveApplicationList(slave.getComChannel(), msg));
            }
        }
    }
    
    private Student checkDefaultPermissions( Student std, boolean unlock ) {
    
        List<PackagePermissions> list = std.getPackages();
        
        Iterator<PackagePermissions> it = list.iterator();
        if (unlock) {
            while (it.hasNext()) {
                it.next().setBlocked(false);
                
            }
        } else {
            while (it.hasNext()) {
                PackagePermissions app = it.next();
                Log.d("APPS", app.getAppName() + " # " + app.getPackageName());
                if (!(app.getPackageName().equalsIgnoreCase("kii.kiibook.Student"))) {
                    Log.w("APPS", "blocked");
                    app.setBlocked(true);
                } else {
                    app.setBlocked(false);
                }
            }
            
        }
        sendApplicationsList(std);
        return std;
    }
    
    private void sendApplicationsList( Student slave ) {
    
        Log.w(TAG, slave.getPackages().toString() + "");
        ApplicationList_Response_ACK msg = new ApplicationList_Response_ACK(slave.getPackages());
        
        if (slave.getStatus() == SlaveStatus.CONNECTED) {
            Log.d(TAG, "Send summary to: " + slave.getName() + " @" + slave.getIpAdrress() + "Msg: " + msg.toString());
            mComManager.sendTCPMessage(new GetSlaveApplicationList(slave.getComChannel(), msg));
            
        }
    }
    
    private class MyHandler extends Handler {
        
        @Override
        public void handleMessage( Message msg ) {
        
            switch (msg.what) {
                case UDP_MSG:
                    
                    handleUdpMessage((UdpMessage) msg.obj);
                    
                    break;
                
                case TCP_MSG_INTERNAL:
                    
                    handleInternalTcpMessage((InternalTcpMessage) msg.obj);
                    
                    break;
                
                case TCP_MSG_NETWORK:
                    handleNetworkTcpMessage((NewTcpMessage) msg.obj);
                    break;
                
                default:
                    super.handleMessage(msg);
            }
        }
        
        private void handleUdpMessage( UdpMessage msg ) {
        
            if (msg instanceof Discover) {
                final Discover d = (Discover) msg;
                if (enable) {
                    if (!dataShared.existConnectedStudents(d.getStudent().getIpAdrress())) {
                        
                        stdCurrentAdded = d.getStudent();
                        
                        for (int index = 0; index < dataShared.getListOffline().size(); index++) {
                            if (dataShared.getListOffline().get(index).getName().equals(stdCurrentAdded.getName())) {
                                
                                Student std = dataShared.getListOffline().get(index);
                                dataShared.getListOffline().remove(index);
                                ArrayList<Student> lists = dataShared.getListOnline();
                                stdCurrentAdded.setPic(std.getPic());
                                lists.add(stdCurrentAdded);
                                sendMessageToUIUpdate();
                                firstTime = true;
                                Toast.makeText(getApplicationContext(), "handleUdpMessage receive new slave", Toast.LENGTH_SHORT).show();
                            }
                        }
                        
                    }
                    mComManager.sendUdpMessage(new Connect(myIp, "Master of puppets :D", mComManager.getTcpPort(), d.getStudent()
                                                    .getIpAdrress()));
                }
            }
        }
        
        private void handleInternalTcpMessage( InternalTcpMessage msg ) {
        
            if (msg instanceof NewClient) {
                
                NewClient nc = (NewClient) msg;
                Student s = dataShared.getConnectedStudents(nc.getIp());
                
                s.setStatus(SlaveStatus.CONNECTED);
                
                s.setComChannel(nc.getComChannel());
                
                nc.getComChannel().setAttachment(s);
                
                if (firstTime) {
                    getPackages(stdCurrentAdded);
                    Toast.makeText(getApplicationContext(), "send ApplicationList", Toast.LENGTH_SHORT).show();
                    firstTime = false;
                }
            }
        }
        
        private void handleNetworkTcpMessage( NewTcpMessage msg ) {
        
            if (msg.getMsg() instanceof ApplicationList_Response) {
                Toast.makeText(getApplicationContext(), "ApplicationList_Response", Toast.LENGTH_SHORT).show();
                Student s = dataShared.getConnectedStudents(msg.getSlave().getIpAdrress());
                
                List<PackagePermissions> apps = ((ApplicationList_Response) msg.getMsg()).getPackages();
                s.setPackages(apps);
                checkDefaultPermissions(s, false);
                sendMessageToUIUpdate();
                Toast.makeText(getApplicationContext(), "ApplicationList_Response : send ACK", Toast.LENGTH_SHORT).show();
                
            }
            if (msg.getMsg() instanceof CloseConnectionAck) {
                
                Student s = dataShared.getConnectedStudents(msg.getSlave().getIpAdrress());
                dataShared.getListOnline().remove(s);
                dataShared.clearAllLists();
                Toast.makeText(getApplicationContext(), "Student removed ", Toast.LENGTH_SHORT).show();
                enable = false;
                sendMessageToUIUpdate();
                
            }
            sendMessageToUIUpdate();
        }
    }
    
    public class ServiceActivityHandler extends Handler {
        
        @Override
        public void handleMessage( Message msg ) {
        
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    enable = true;
                    mClients.add(msg.replyTo);
                    break;
                
                case MSG_UNREGISTER_CLIENT:
                    
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_DISCONNECT_SLAVES:
                    
                    disconnectSlaves();
                    
                    break;
                
                case MSG_SEND_SUMMARY:
                    
                    handleSummarySend((Summary) msg.obj);
                    break;
                
                case MSG_RECEIVE_APPS:
                    
                    sendApplicationsList(DataShared.getInstance().getListOnline().get(msg.arg1));
                    break;
                case MSG_SEND_NEW_EVENT:
                    
                    handleNewEventSend((NewEvent) msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
    
    private void sendMessageToUIUpdate() {
    
        for (int i = mClients.size() - 1; i >= 0; i--) {
            try {
                // Send data as an Integer
                mClients.get(i).send(Message.obtain(null, MSG_UPDATE_LIST, 0, 0));
                
            }
            catch (RemoteException e) {
                // The client is dead. Remove it from the list; we are going
                // through the list from back to front so this is safe to do
                // inside the loop.
                mClients.remove(i);
            }
        }
    }
    
    private final ServiceConnection mConnection = new ServiceConnection() {
                                                    
                                                    public void onServiceConnected( ComponentName className, IBinder service ) {
                                                    
                                                        mService = new Messenger(service);
                                                        textStatus.setText("Attached.");
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
                                                        textStatus.setText("Disconnected.");
                                                    }
                                                    
                                                };
}
