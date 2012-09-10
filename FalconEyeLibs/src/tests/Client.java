
package tests;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

public class Client {
    
    private static final String HOSTNAME = "192.168.2.105";
    
    private static final int    PORT     = 46184;
    
    public static void main( String[] args ) throws Throwable {
    
        NioSocketConnector connector = new NioSocketConnector();
        
        // Configure the service.
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        
        connector.setHandler(new ClientSessionHandler());
        
        IoSession session = null;
        try {
            ConnectFuture future = connector.connect(new InetSocketAddress(HOSTNAME, PORT));
            future.awaitUninterruptibly();
            session = future.getSession();
        }
        catch (RuntimeIoException e) {
            System.err.println("Failed to connect.");
            e.printStackTrace();
        }
        
        // wait until the summation is done
        session.getCloseFuture().awaitUninterruptibly();
        connector.dispose();
    }
}
