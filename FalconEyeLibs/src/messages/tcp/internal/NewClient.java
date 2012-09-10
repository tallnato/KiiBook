
package messages.tcp.internal;

import comchannels.ComChannel;

public class NewClient implements InternalTcpMessage {
    
    private static final long serialVersionUID = -790804163616633901L;
    private final ComChannel  com;
    private final String      ip;
    
    public NewClient( ComChannel com, String ip ) {
    
        this.com = com;
        this.ip = ip;
    }
    
    public ComChannel getComChannel() {
    
        return com;
    }
    
    public String getIp() {
    
        return ip;
    }
}
