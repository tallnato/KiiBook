
package tests;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class TimeServerHandler extends IoHandlerAdapter {
    
    int x = 0;
    
    @Override
    public void exceptionCaught( IoSession session, Throwable cause ) throws Exception {
    
        cause.printStackTrace();
    }
    
    @Override
    public void messageReceived( IoSession session, Object message ) throws Exception {
    
        Message m = (Message) message;
        System.out.println("message server: " + m.getCena().length() + " " + m);
        
        session.write(new Message("ola do servidor " + x++));
    }
    
    @Override
    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception {
    
        System.out.println("IDLE " + session.getIdleCount(status));
    }
    
    @Override
    public void sessionOpened( IoSession session ) {
    
        InetSocketAddress socketAddress = (InetSocketAddress) session.getRemoteAddress();
        InetAddress inetAddress = socketAddress.getAddress();
        
        System.out.println("new session: " + inetAddress.getHostAddress());
    }
}
