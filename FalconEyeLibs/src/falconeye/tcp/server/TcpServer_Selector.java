
package falconeye.tcp.server;

import android.os.Handler;
import android.os.Message;

import comchannels.ComChannel;
import comchannels.ComChannel_SocketChannel;

import falconeye.udp.UdpUtil;
import messages.tcp.internal.NewClient;
import messages.tcp.internal.NewTcpMessage;
import messages.tcp.network.NetworkTcpMessage;
import objects.Student;
import util.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

public class TcpServer_Selector extends Thread implements Constants {
    
    private Selector            selector;
    private ServerSocketChannel socketChan;
    
    private int                 port;
    
    private Handler             msgHandler;
    
    public TcpServer_Selector() {
    
        start();
    }
    
    public void setMsgHandler( Handler msgHandler ) {
    
        this.msgHandler = msgHandler;
    }
    
    public void sendMessage( NetworkTcpMessage message, ComChannel com ) {
    
        com.sendMessage(message);
    }
    
    public int getPort() {
    
        return port;
    }
    
    public void close() {
    
        try {
            socketChan.close();
            selector.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
    
        Iterator<SelectionKey> it;
        
        try {
            
            selector = SelectorProvider.provider().openSelector();
            socketChan = ServerSocketChannel.open();
            InetSocketAddress iaddr = new InetSocketAddress("0.0.0.0", 5000);
            
            socketChan.configureBlocking(false);
            socketChan.socket().bind(iaddr);
            port = socketChan.socket().getLocalPort();
            
            System.out.println("Running on port:" + socketChan.socket().getLocalPort());
            
            socketChan.register(selector, SelectionKey.OP_ACCEPT);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (ClosedChannelException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            while (selector.isOpen()) {
                selector.select();
                
                it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    
                    it.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    
                    // Finish connection in case of an error
                    if (key.isConnectable()) {
                        SocketChannel ssc = (SocketChannel) key.channel();
                        if (ssc.isConnectionPending()) {
                            ssc.finishConnect();
                        }
                    }
                    
                    if (key.isAcceptable()) { // Incoming connection, proceed
                                              // with to add the
                                              // peer's socket to the list
                        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                        SocketChannel newClient = ssc.accept();
                        
                        newClient.configureBlocking(false); // configure socket
                                                            // to be non
                                                            // blocking
                        SelectionKey k = newClient.register(selector, SelectionKey.OP_READ);
                        
                        System.out.println("new client: " + newClient.socket().getInetAddress().getHostAddress());
                        
                        NewClient nc = new NewClient(new ComChannel_SocketChannel(newClient, k), newClient.socket().getInetAddress()
                                                        .getHostAddress());
                        Message iMessage = Message.obtain(msgHandler, TCP_MSG_INTERNAL, nc);
                        msgHandler.sendMessage(iMessage);
                    }
                    
                    if (key.isReadable()) {
                        
                        SocketChannel sc = (SocketChannel) key.channel();
                        int totalRead = 0;
                        ByteArrayOutputStream totalMessage = new ByteArrayOutputStream();
                        ByteBuffer tmp = ByteBuffer.allocate(sc.socket().getReceiveBufferSize());
                        
                        while ((totalRead = sc.read(tmp)) > 0) {
                            tmp.flip();
                            totalMessage.write(tmp.array(), 0, totalRead);
                            System.out.println("read: " + totalRead + " buffersize: " + sc.socket().getReceiveBufferSize() + " array: "
                                                            + tmp.capacity());
                            tmp.clear();
                        }
                        System.out.println("last read:" + totalRead);
                        
                        System.out.println("total size: " + totalMessage.size());
                        NetworkTcpMessage m = (NetworkTcpMessage) UdpUtil.byteToMessage(totalMessage.toByteArray());
                        if (m != null) {
                            Message iMessage = Message.obtain(msgHandler, TCP_MSG_NETWORK, new NewTcpMessage(m, (Student) key.attachment()));
                            msgHandler.sendMessage(iMessage);
                            
                            System.out.println("message from " + sc.socket().getInetAddress().getHostAddress() + ":" + m.toString());
                        } else {
                            System.out.println("wrong message format :S");
                        }
                    }
                }
            }
        }
        catch (ClosedSelectorException e) {
            e.printStackTrace();
            return;
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        catch (ClosedChannelException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
