
package falconeye.tcp.server;

import android.os.Handler;

import falconeye.udp.UdpListener;
import interfaces.ITcpServer;
import messages.tcp.internal.InternalTcpMessage;
import messages.udp.UdpMessage;
import util.Constants;

public class ComunicationManager implements Constants {
    
    private final ITcpServer    tcpServer;
    private final UdpListener   udpServer;
    private final MessageSender mMsgSender;
    
    public ComunicationManager( String myIp, Handler mHandler ) {
    
        tcpServer = new TcpServer_Mina();
        udpServer = new UdpListener(UDP_PORT, myIp);
        mMsgSender = new MessageSender(tcpServer, udpServer);
        
        tcpServer.setMsgHandler(mHandler);
        udpServer.setMsgHandler(mHandler);
        
        udpServer.start();
        mMsgSender.start();
    }
    
    public void sendTCPMessage( InternalTcpMessage message ) {
    
        mMsgSender.sendTcpMessage(message);
    }
    
    public void sendUdpMessage( UdpMessage msg ) {
    
        mMsgSender.sendUdpMessage(msg);
    }
    
    public int getTcpPort() {
    
        return tcpServer.getPort();
    }
    
    public void close() {
    
        tcpServer.close();
        udpServer.close();
    }
    
}
