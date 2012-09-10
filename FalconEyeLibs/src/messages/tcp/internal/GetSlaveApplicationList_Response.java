
package messages.tcp.internal;

import messages.tcp.network.NetworkTcpMessage;

public class GetSlaveApplicationList_Response implements InternalTcpMessage {
    
    private static final long       serialVersionUID = -2767960606900035110L;
    private final NetworkTcpMessage message;
    
    public GetSlaveApplicationList_Response( NetworkTcpMessage message ) {
    
        this.message = message;
    }

    public NetworkTcpMessage getMessage() {
    
        return message;
    }
}
