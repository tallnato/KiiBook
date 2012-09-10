package tests;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MinaTimeServer {
    
    private static final int PORT = 9123;
    
    public static void main( String[] args ) throws IOException {
    
        IoAcceptor acceptor = new NioSocketAcceptor();
        
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
        
        acceptor.setHandler(new TimeServerHandler());
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        acceptor.bind(new InetSocketAddress(PORT));
        System.out.println("MINA Time server started.");
        
    }
}
