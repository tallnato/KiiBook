
package comchannels;

import messages.tcp.network.NetworkTcpMessage;
import objects.Student;

import org.apache.mina.core.session.IoSession;

public class ComChannel_IoSession implements ComChannel {
    
    private IoSession session;
    
    public ComChannel_IoSession( IoSession session ) {
    
        this.session = session;
    }
    
    public IoSession getSession() {
    
        return session;
    }
    
    public void setSession( IoSession session ) {
    
        this.session = session;
    }
    
    @Override
    public void sendMessage( NetworkTcpMessage msg ) {
    
        session.write(msg);
    }
    
    @Override
    public void setAttachment( Student s ) {
    
        session.setAttribute(SLAVE_KEY, s);
    }
}
