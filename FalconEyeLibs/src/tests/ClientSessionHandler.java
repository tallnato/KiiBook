
package tests;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * {@link IoHandler} for SumUp client.
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class ClientSessionHandler extends IoHandlerAdapter {
    
    private boolean finished;
    
    public ClientSessionHandler() {
    
    }
    
    public boolean isFinished() {
    
        return finished;
    }
    
    @Override
    public void sessionOpened( IoSession session ) {
    
        String t = "";
        for (int i = 0; i < 10000; i++) {
            t += "AAAAAAAAAA";
            System.out.println("pos: " + i);
        }
        System.out.println("size: " + t.length() + " " + t);
        session.write(new Message(t));
    }
    
    @Override
    public void messageReceived( IoSession session, Object message ) {
    
        System.out.println("message received in client: " + message.toString());
        
        session.close(true);
        finished = true;
    }
    
    @Override
    public void exceptionCaught( IoSession session, Throwable cause ) {
    
        session.close(true);
    }
}
