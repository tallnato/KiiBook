
package falconeye.tcp.server;

import android.util.Log;

import falconeye.udp.UdpListener;
import interfaces.ITcpServer;
import messages.KiiMessage;
import messages.tcp.internal.GetSlaveApplicationList;
import messages.tcp.internal.InternalTcpMessage;
import messages.udp.Connect;
import messages.udp.UdpMessage;
import util.Constants;

import java.util.LinkedList;

public class MessageSender extends Thread implements Constants {
    
    private final LinkedList<KiiMessage> fifo;
    private final ITcpServer             tcpServer;
    private final UdpListener            udpServer;
    
    public MessageSender( ITcpServer tcpServer, UdpListener udpServer ) {
    
        fifo = new LinkedList<KiiMessage>();
        this.tcpServer = tcpServer;
        this.udpServer = udpServer;
    }
    
    public void sendTcpMessage( InternalTcpMessage msg ) {
    
        synchronized (fifo) {
            fifo.add(msg);
            fifo.notifyAll();
        }
    }
    
    public void sendUdpMessage( UdpMessage msg ) {
    
        synchronized (fifo) {
            fifo.add(msg);
            fifo.notifyAll();
        }
    }
    
    @Override
    public void run() {
    
        while (true) {
            while (!fifo.isEmpty()) {
                
                KiiMessage msg = fifo.removeFirst();
                
                if (msg instanceof UdpMessage) {
                    
                    handleUdp((UdpMessage) msg);
                } else if (msg instanceof InternalTcpMessage) {
                    
                    handleTcp((InternalTcpMessage) msg);
                } else {
                    
                    Log.d(TAG, "Wrong message type received: " + msg.toString());
                }
                
            }
            try {
                synchronized (fifo) {
                    fifo.wait();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void handleUdp( UdpMessage msg ) {
    
        if (msg instanceof Connect) {
            
            udpServer.sendMessage(msg, ((Connect) msg).getDestIp());
        } else {
            
            Log.d(TAG, "Wrong message type received: " + msg.toString());
        }
    }
    
    private void handleTcp( InternalTcpMessage msg ) {
    
        if (msg instanceof GetSlaveApplicationList) {
            GetSlaveApplicationList m = (GetSlaveApplicationList) msg;
            tcpServer.sendMessage(m.getMessage(), m.getComChannel());
        } else {
            
            Log.d(TAG, "Wrong message type received: " + msg.toString());
        }
    }
}
