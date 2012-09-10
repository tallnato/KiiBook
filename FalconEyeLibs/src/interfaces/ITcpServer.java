
package interfaces;

import android.os.Handler;

import comchannels.ComChannel;

import messages.tcp.network.NetworkTcpMessage;

public interface ITcpServer {
    
    public void setMsgHandler( Handler msgHandler );
    
    public void sendMessage( NetworkTcpMessage message, ComChannel com );
    
    public int getPort();
    
    public void close();
}
