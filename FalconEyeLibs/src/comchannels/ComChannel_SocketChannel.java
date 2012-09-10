
package comchannels;

import falconeye.udp.UdpUtil;
import messages.tcp.network.NetworkTcpMessage;
import objects.Student;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ComChannel_SocketChannel implements ComChannel {
    
    private final SocketChannel socket;
    private final SelectionKey  key;
    
    public ComChannel_SocketChannel( SocketChannel socket, SelectionKey key ) {
    
        this.socket = socket;
        this.key = key;
    }
    
    public SocketChannel getSocket() {
    
        return socket;
    }
    
    @Override
    public void sendMessage( NetworkTcpMessage message ) {
    
        byte[] msg = UdpUtil.messageToByteMessage(message);
        ByteBuffer data = ByteBuffer.wrap(msg);
        
        try {
            System.out.println("sending message..");
            socket.write(data);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    @Override
    public void setAttachment( Student s ) {
    
        key.attach(s);
    }
}
