
package kii.kiibook.Student;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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

import falconeye.tcp.client.TcpClient;
import falconeye.udp.UdpListener;
import falconeye.udp.UdpUtil;
import messages.tcp.network.ApplicationList;
import messages.tcp.network.ApplicationList_Response;
import messages.tcp.network.ApplicationList_Response_ACK;
import messages.tcp.network.CloseConnectionAck;
import messages.tcp.network.CloseConnectionNetwork;
import messages.tcp.network.NewEventNetwork;
import messages.tcp.network.SummaryNetwork;
import messages.udp.Connect;
import messages.udp.Discover;
import objects.NewEvent;
import objects.PackagePermissions;
import objects.Student;
import objects.Summary;
import util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import kii.kiibook.KiiClass.MainActivity;
import kii.kiibook.Student.database.DataShared;

public class CommunicationService extends Service implements Constants {
    
    private WifiManager             wifi;
    private String                  ip;
    private UdpListener             ul;
    private MyHandle                myHandler;
    private Thread                  t;
    private Student                 me;
    
    public static final int         MSG_REGISTER_CLIENT     = 1;
    public static final int         MSG_UNREGISTER_CLIENT   = 2;
    public static final int         MSG_FALCONEYE_TURNOFF   = 3;
    private static final int        MSG_FALCONEYE_TRY_AGAIN = 3;
    private static final String     APPLOCKER_BROADCAST     = "kiibook.applocker";
    
    ArrayList<Messenger>            mClients                = new ArrayList<Messenger>();
    final Messenger                 mMessenger              = new Messenger(new ServiceActivityHandler());
    private final ServiceReceiver   myReceiver              = new ServiceReceiver();
    protected Messenger             mService;
    protected Toast                 textStatus;
    private NotificationManager     nm;
    private Notification            notification;
    private static boolean          connected               = false;
    private final ServiceConnection mConnection             = new ServiceConnection() {
                                                                
                                                                public void onServiceConnected( ComponentName className, IBinder service ) {
                                                                
                                                                    mService = new Messenger(service);
                                                                    
                                                                    try {
                                                                        Message msg = Message.obtain(null,
                                                                                                        CommunicationService.MSG_REGISTER_CLIENT);
                                                                        msg.replyTo = mMessenger;
                                                                        mService.send(msg);
                                                                        Log.i(TAG, "Activity - MSG_REGISTER_CLIENT send");
                                                                    }
                                                                    catch (RemoteException e) {
                                                                        // In
                                                                        // this
                                                                        // case
                                                                        // the
                                                                        // service
                                                                        // has
                                                                        // crashed
                                                                        // before
                                                                        // we
                                                                        // could
                                                                        // even
                                                                        // do
                                                                        // anything
                                                                        // with
                                                                        // it
                                                                    }
                                                                }
                                                                
                                                                public void onServiceDisconnected( ComponentName className ) {
                                                                
                                                                    // This is
                                                                    // called
                                                                    // when
                                                                    // the
                                                                    // connection
                                                                    // with
                                                                    // the
                                                                    // service
                                                                    // has been
                                                                    // unexpectedly
                                                                    // disconnected
                                                                    // -
                                                                    // process
                                                                    // crashed.
                                                                    mService = null;
                                                                    textStatus.setText("Disconnected.");
                                                                }
                                                                
                                                            };
    private ServiceActivityHandler  handlerInternal;
    public TcpClient                con;
    private ServiceActivityHandler  handlerService;
    private boolean                 thisService;
    public ArrayList<String>        setApps;
    private static boolean          isRunning               = false;
    private CommunicationService    servico;
    
    @Override
    public IBinder onBind( Intent intent ) {
    
        return mMessenger.getBinder();
    }
    
    public static boolean isRunning() {
    
        return isRunning;
    }
    
    public static boolean isOnline() {
    
        return connected;
    }
    
    public static void onFalconEye( boolean on ) {
    
        connected = on;
        
    }
    
    @Override
    public void onCreate() {
    
        thisService = true;
        isRunning = true;
        me = DataShared.getInstance().getMyProfile();
        servico = this;
        // initialization Falcon Eye
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        ip = Formatter.formatIpAddress(wifiInfo.getIpAddress());
        ul = new UdpListener(UDP_PORT, ip);
        myHandler = new MyHandle(getInstalledApps());
        ul.setMsgHandler(myHandler);
        ul.start();
        
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(APPLOCKER_BROADCAST);
        registerReceiver(myReceiver, filter);
        
    }
    
    @Override
    public void onStart( Intent intent, int startId ) {
    
        enableFalconEye();
        super.onStart(intent, startId);
    }
    
    @Override
    public void onDestroy() {
    
        ul.close();
        thisService = false;
        handlerService = null;
        sendMessageToUIFalconEyeOff();
        if (con != null) {
            con.close();
            con = null;
        }
        ul = null;
        myHandler = null;
        super.onDestroy();
    }
    
    private void showNotification() {
    
        // In this sample, we'll use the same text for the ticker and the
        // expanded notification
        CharSequence text = "Estas em aula!";
        
        // Set the icon, scrolling text and timestamp
        notification = new Notification(R.drawable.area_disciplina_small, text, System.currentTimeMillis());
        
        // The PendingIntent to launch our activity if the user selects this
        // notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        
        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, "Olho de Falcão", text, contentIntent);
        notification.flags |= Notification.FLAG_NO_CLEAR;
        // Send the notification.
        // We use a layout id because it is a unique number. We use it later to
        // cancel.
        nm.notify("Started", 11, notification);
    }
    
    private void changeNotification() {
    
        nm.cancelAll();
        
        CharSequence text = "Aula acabou!";
        
        notification = new Notification(R.drawable.area_disciplina_small, text, System.currentTimeMillis());
        
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        
        notification.setLatestEventInfo(this, "Olho de Falcão", text, contentIntent);
        
        nm.notify("Started", 0, notification);
        
    }
    
    private void addNotification( CharSequence text, CharSequence title ) {
    
        notification = new Notification(R.drawable.area_disciplina_small, text, System.currentTimeMillis());
        
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
        
        notification.setLatestEventInfo(this, title, text, contentIntent);
        
        nm.notify("Started", 0, notification);
        
    }
    
    private void enableFalconEye() {
    
        final String broadcast = UdpUtil.getBroadcastAddress(wifi.getDhcpInfo());
        
        t = new Thread(new Runnable() {
            
            public void run() {
            
                ul.sendMessage(new Discover(me), broadcast);
                Log.d(TAG, "send Discover");
            }
        });
        t.start();
        sendMessageTime();
        
    }
    
    private ArrayList<PackagePermissions> getInstalledApps() {
    
        PackageManager pm = getPackageManager();
        ArrayList<PackagePermissions> res = new ArrayList<PackagePermissions>();
        
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        
        List<ResolveInfo> pkgAppsList = pm.queryIntentActivities(mainIntent, 0);
        
        for (ResolveInfo ri : pkgAppsList) {
            
            if (ri.loadLabel(pm).toString().contains("KiiLauncher") || ri.loadLabel(pm).toString().contains("Parental Control")) {
                continue;
            }
            PackagePermissions newInfo = new PackagePermissions(ri.loadLabel(pm).toString(), ri.activityInfo.packageName, ri.loadIcon(pm));
            newInfo.setBlocked(true);
            
            res.add(newInfo);
        }
        
        Collections.sort(res);
        
        return res;
    }
    
    class MyHandle extends Handler implements Constants {
        
        private final ArrayList<PackagePermissions> packages;
        
        public MyHandle( ArrayList<PackagePermissions> packages ) {
        
            this.packages = packages;
            
        }
        
        @Override
        public void handleMessage( Message msg ) {
        
            switch (msg.what) {
                case UDP_MSG:
                    
                    // Toast.makeText(getApplicationContext(),
                    // msg.obj.toString(),
                    // Toast.LENGTH_SHORT).show();
                    
                    if (msg.obj instanceof Connect) {
                        connected = true;
                        final Connect c = (Connect) msg.obj;
                        
                        con = new TcpClient(c.getMasterIp(), c.getPort(), this);
                        
                        showNotification();
                        
                    }
                    
                    break;
                case TCP_MSG_INTERNAL:
                    
                    if (msg.obj instanceof ApplicationList) {
                        
                        Toast.makeText(CommunicationService.this, "ApplicationList", Toast.LENGTH_SHORT).show();
                        ApplicationList_Response resp = new ApplicationList_Response(packages);
                        
                        con.sendMessage(resp);
                        
                    }
                    if (msg.obj instanceof CloseConnectionNetwork) {
                        connected = false;
                        sendMessageTime();
                        sendMessageToUIFalconEyeOff();
                        
                        Intent off = new Intent();
                        off.setAction("com.kii.falconeye.parental");
                        sendBroadcast(off);
                        
                        CloseConnectionAck resp = new CloseConnectionAck();
                        con.sendMessage(resp);
                        
                    }
                    if (msg.obj instanceof ApplicationList_Response_ACK) {
                        Toast.makeText(CommunicationService.this, "ApplicationList_Response_ACK", Toast.LENGTH_SHORT).show();
                        ApplicationList_Response_ACK app_ack = (ApplicationList_Response_ACK) msg.obj;
                        
                        setApps = new ArrayList<String>();
                        Iterator<PackagePermissions> it = app_ack.getPackages().iterator();
                        while (it.hasNext()) {
                            PackagePermissions pack = it.next();
                            if (pack.isBlocked()) {
                                setApps.add(pack.getPackageName());
                            }
                        }
                        
                        Intent i = new Intent();
                        i.setAction("com.kii.falconeye.teacher");
                        i.putStringArrayListExtra("Key:BlockedApps", setApps);
                        sendBroadcast(i);
                        
                    }
                    if (msg.obj instanceof SummaryNetwork) {
                        Log.d("Service", "receive Summary Network");
                        SummaryNetwork summary = (SummaryNetwork) msg.obj;
                        
                        if (summary.getSummary() == null) {
                            Log.e(TAG, "dass summary");
                        }
                        Summary sum = summary.getSummary();
                        DataShared.getInstance().addListSummaries(sum);
                        Log.d(TAG + "Summary", sum.toString());
                        addNotification("Novo sumário recebido!", "Gestor de Disciplina");
                        
                    }
                    if (msg.obj instanceof NewEventNetwork) {
                        Log.d("Service", "receive NewEventNetwork");
                        NewEventNetwork summary = (NewEventNetwork) msg.obj;
                        
                        if (summary.getSummary() == null) {
                            Log.e(TAG, "dass summary");
                        }
                        NewEvent sum = summary.getSummary();
                        DataShared.getInstance().getListEvents().add(sum);
                        
                        Intent i = new Intent();;
                        
                        switch (sum.getType()) {
                            case TPC:
                                i.setAction("com.kii.broadcast.homework");
                                break;
                            case Trabalho:
                                i.setAction("com.kii.broadcast.homework");
                                break;
                            default:
                                i.setAction("com.kii.broadcast.calendar");
                                break;
                        }
                        sendBroadcast(i);
                        
                    } else {
                        // Toast.makeText(getApplicationContext(),
                        // "New Messsage: "
                        // + msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
    
    public class ServiceActivityHandler extends Handler {
        
        @Override
        public void handleMessage( Message msg ) {
        
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    Log.i(TAG, "Activity - MSG_REGISTER_CLIENT");
                    
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    
                    break;
                case MSG_FALCONEYE_TRY_AGAIN:
                    
                    if ((!connected)) {
                        Log.d(TAG, "TRY AGAIN");
                        enableFalconEye();
                    }
                    
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
    
    private void sendMessageTime() {
    
        Log.d("", "sendMessageTime");
        if (!connected) {
            
            handlerService = new ServiceActivityHandler();
            handlerService.sendEmptyMessageDelayed(MSG_FALCONEYE_TRY_AGAIN, 10000);
            
        }
    }
    
    private void sendMessageToUIFalconEyeOff() {
    
        Log.e(TAG, "sendMessageToUIFalconEyeOff - clients: " + mClients.size());
        
        changeNotification();
        
        for (int i = mClients.size() - 1; i >= 0; i--) {
            Log.e(TAG, "sendMessageToUIFalconEyeOff - " + i);
            // mClients.get(i).send(Message.obtain(null,
            // MSG_FALCONEYE_TURNOFF, 0, 0));
            
        }
        
    }
    
    class ServiceReceiver extends BroadcastReceiver {
        
        @Override
        public void onReceive( Context context, Intent intent ) {
        
            String action = intent.getAction();
            
            if (action.equals(APPLOCKER_BROADCAST)) {
                if (connected) {
                    Toast.makeText(servico, "Send broadcast to applocker @ connected", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent();
                    i.setAction("com.kii.falconeye.teacher");
                    i.putStringArrayListExtra("Key:BlockedApps", setApps);
                    sendBroadcast(i);
                } else {
                    Toast.makeText(servico, "Send broadcast to applocker @ disconnected", Toast.LENGTH_SHORT).show();
                    Intent off = new Intent();
                    off.setAction("com.kii.falconeye.parental");
                    sendBroadcast(off);
                }
            } else {
                Toast.makeText(servico, "BroadcastReceiver @CommunicationService - wrong action", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
