
package messages.tcp.internal;

import comchannels.ComChannel;

import messages.tcp.network.NetworkTcpMessage;

public class GetSlaveSummary implements InternalTcpMessage {
    
    private static final long       serialVersionUID = 2895741792632099992L;
    
    private final ComChannel        com;
    private final NetworkTcpMessage message;
    
    public GetSlaveSummary( ComChannel com, NetworkTcpMessage message ) {
    
        this.com = com;
        this.message = message;
    }
    
    public ComChannel getComChannel() {
    
        return com;
    }
    
    public NetworkTcpMessage getMessage() {
    
        return message;
    }
}
