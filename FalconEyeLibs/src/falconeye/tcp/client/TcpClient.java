
package falconeye.tcp.client;

import android.os.Handler;
import android.os.Message;

import messages.tcp.network.NetworkTcpMessage;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import util.Constants;

import java.net.InetSocketAddress;

public class TcpClient extends Thread implements Constants {
    
    private IoSession     session;
    private final Handler myHandler;
    private final String  hostIp;
    private final int     port;
    
    public TcpClient( String hostIp, int port, Handler handler ) {
    
        myHandler = handler;
        this.hostIp = hostIp;
        this.port = port;
        
        start();
    }
    
    public void close() {
    
        session.close(false);
    }
    
    public void sendMessage( NetworkTcpMessage message ) {
    
        session.write(message);
    }
    
    @Override
    public void run() {
    
        NioSocketConnector connector = new NioSocketConnector();
        
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        
        connector.setHandler(new ClientHandler());
        
        try {
            ConnectFuture future = connector.connect(new InetSocketAddress(hostIp, port));
            future.awaitUninterruptibly();
            session = future.getSession();
        }
        catch (RuntimeIoException e) {
            System.err.println("Failed to connect.");
            e.printStackTrace();
        }
        
        session.getCloseFuture().awaitUninterruptibly();
    }
    
    private class ClientHandler extends IoHandlerAdapter {
        
        @Override
        public void sessionOpened( IoSession session ) {
        
            System.out.println("session opened");
        }
        
        @Override
        public void messageReceived( IoSession session, Object message ) {
        
            if (message instanceof NetworkTcpMessage) {
                NetworkTcpMessage msg = (NetworkTcpMessage) message;
                
                Message iMessage = Message.obtain(myHandler, TCP_MSG_INTERNAL, msg);
                myHandler.sendMessage(iMessage);
                
            } else {
                System.out.println("Wrong message format received: " + message);
            }
        }
        
        @Override
        public void exceptionCaught( IoSession session, Throwable cause ) {
        
            session.close(true);
            cause.printStackTrace();
        }
    }
}
