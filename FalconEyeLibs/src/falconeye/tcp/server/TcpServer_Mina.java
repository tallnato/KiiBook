
package falconeye.tcp.server;

import android.os.Handler;
import android.os.Message;

import classobjects.Slave;

import comchannels.ComChannel;
import comchannels.ComChannel_IoSession;

import interfaces.ITcpServer;
import messages.tcp.internal.NewClient;
import messages.tcp.internal.NewTcpMessage;
import messages.tcp.network.NetworkTcpMessage;
import objects.Student;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import util.Constants;
import util.SlaveStatus;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class TcpServer_Mina extends IoHandlerAdapter implements ITcpServer, Constants {
    
    private final IoAcceptor acceptor;
    
    private int              port;
    private Handler          msgHandler;
    
    public TcpServer_Mina() {
    
        acceptor = new NioSocketAcceptor();
        
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        LoggingFilter loggingFilter = new LoggingFilter();
        loggingFilter.setExceptionCaughtLogLevel(LogLevel.NONE);
        loggingFilter.setMessageReceivedLogLevel(LogLevel.NONE);
        loggingFilter.setMessageSentLogLevel(LogLevel.NONE);
        loggingFilter.setSessionClosedLogLevel(LogLevel.NONE);
        loggingFilter.setSessionCreatedLogLevel(LogLevel.NONE);
        loggingFilter.setSessionIdleLogLevel(LogLevel.NONE);
        loggingFilter.setSessionOpenedLogLevel(LogLevel.NONE);
        
        acceptor.getFilterChain().addLast("logging", loggingFilter);
        
        acceptor.setHandler(this);
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        
        try {
            acceptor.bind(new InetSocketAddress(0));
            port = ((InetSocketAddress) acceptor.getLocalAddress()).getPort();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("The server has started on port " + port);
        
    }
    
    @Override
    public void setMsgHandler( Handler msgHandler ) {
    
        this.msgHandler = msgHandler;
    }
    
    @Override
    public void sendMessage( NetworkTcpMessage message, ComChannel com ) {
    
        System.out.println("sending message...");
        com.sendMessage(message);
    }
    
    @Override
    public int getPort() {
    
        return port;
    }
    
    @Override
    public void close() {
    
        acceptor.dispose();
    }
    
    @Override
    public void messageReceived( IoSession session, Object message ) throws Exception {
    
        NetworkTcpMessage m = (NetworkTcpMessage) message;
        if (m != null) {
            Message iMessage = Message.obtain(msgHandler, TCP_MSG_NETWORK, new NewTcpMessage(m, (Student) session.getAttribute(SLAVE_KEY)));
            msgHandler.sendMessage(iMessage);
            
            System.out.println("message from " + ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress() + ":"
                                            + m.toString());
        } else {
            System.out.println("wrong message format :S");
        }
    }
    
    @Override
    public void sessionOpened( IoSession session ) {
    
        InetAddress inetAddress = ((InetSocketAddress) session.getRemoteAddress()).getAddress();
        
        System.out.println("new client: " + inetAddress.getHostAddress());
        
        Message iMessage = Message.obtain(msgHandler, TCP_MSG_INTERNAL,
                                        new NewClient(new ComChannel_IoSession(session), inetAddress.getHostAddress()));
        msgHandler.sendMessage(iMessage);
    }
    
    @Override
    public void sessionClosed( IoSession session ) {
    
        InetAddress inetAddress = ((InetSocketAddress) session.getRemoteAddress()).getAddress();
        
        System.out.println("sessionClosed: " + inetAddress.getHostAddress());
    }
    
    @Override
    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception {
    
        System.out.println("IDLE " + session.getIdleCount(status));
    }
    
    @Override
    public void exceptionCaught( IoSession session, Throwable cause ) throws Exception {
    
        Slave s = (Slave) session.getAttribute(SLAVE_KEY);
        s.setStatus(SlaveStatus.DISCONNECT);
        
        System.out.println("Exeption: " + session + ". Slave: " + s);
        cause.printStackTrace();
    }
    
}
