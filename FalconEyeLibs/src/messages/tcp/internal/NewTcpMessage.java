
package messages.tcp.internal;

import messages.tcp.network.NetworkTcpMessage;
import objects.Student;

public class NewTcpMessage implements InternalTcpMessage {
    
    private static final long       serialVersionUID = 3686948055898622643L;
    private final NetworkTcpMessage msg;
    private final Student           slave;
    
    public NewTcpMessage( NetworkTcpMessage msg, Student slave ) {
    
        this.msg = msg;
        this.slave = slave;
    }
    
    public NetworkTcpMessage getMsg() {
    
        return msg;
    }
    
    public Student getSlave() {
    
        return slave;
    }
    
}
