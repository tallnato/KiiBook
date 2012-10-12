
package deprecated;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import falconeye.udp.UdpUtil;
import messages.KiiMessage;
import messages.udp.Connect;
import messages.udp.Discover;
import messages.udp.UdpMessage;
import util.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Deprecated
public class ComunicationServer extends Thread implements Constants {
    
    private Selector            selector;
    private ServerSocketChannel tcpChannel;
    private DatagramChannel     udpChannel;
    private List<SocketChannel> sockets;
    
    private int                 tcpPort;
    private final String        ip;
    
    private Handler             msgHandler; // Handler of the others activities
    private final MyHandler     mHandler;  // Thread to send network messages
                                            
    public ComunicationServer( String ip ) {
    
        this.ip = ip;
        sockets = new LinkedList<SocketChannel>();
        mHandler = new MyHandler();
    }
    
    public void setMsgHandler( Handler msgHandler ) {
    
        this.msgHandler = msgHandler;
    }
    
    public void sendTCPMessage( KiiMessage message, String address ) {
    
        // Message iMessage = Message.obtain(null, TCP_MSG, new
        // UdpMessageeeeeeeeeeeeeeeeeee(message, address));
        // mHandler.handleMessage();
    }
    
    public void sendUdpMessage( UdpMessage msg ) {
    
        mHandler.sendUdpMessage(msg);
    }
    
    public int getTcpPort() {
    
        return tcpPort;
    }
    
    public void close() {
    
        try {
            tcpChannel.close();
            udpChannel.close();
            selector.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
    
        Iterator<SelectionKey> it;
        
        mHandler.start();
        
        try {
            
            selector = SelectorProvider.provider().openSelector();
            
            tcpChannel = ServerSocketChannel.open();
            
            InetSocketAddress iaddr = new InetSocketAddress("0.0.0.0", 5000);
            
            tcpChannel.configureBlocking(false);
            tcpChannel.socket().bind(iaddr);
            tcpChannel.register(selector, SelectionKey.OP_ACCEPT);
            tcpPort = tcpChannel.socket().getLocalPort();
            
            udpChannel = DatagramChannel.open();
            udpChannel.configureBlocking(false);
            udpChannel.socket().bind(new InetSocketAddress(UDP_PORT));
            udpChannel.register(selector, SelectionKey.OP_READ);
            
            System.out.println("TCP running on port:" + tcpChannel.socket().getLocalPort());
            
            sockets = new LinkedList<SocketChannel>();
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
            while (true) {
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
                                              // with procedure to add the
                                              // peer's socket to the list
                        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                        final SocketChannel newClient = ssc.accept();
                        
                        newClient.configureBlocking(false); // configure socket
                                                            // to be non
                                                            // blocking
                        newClient.register(selector, SelectionKey.OP_READ);
                        sockets.add(newClient);
                        
                        System.out.println("new client: " + newClient.socket().getInetAddress().getHostAddress());
                        
                        Thread t = new Thread(new Runnable() {
                            
                            @Override
                            public void run() {
                            
                                ByteBuffer bb = ByteBuffer.wrap("fodassse".getBytes());
                                try {
                                    newClient.write(bb);
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        t.run();
                    }
                    
                    if (key.isReadable() && key.channel() == tcpChannel) {
                        System.out.println("new message: ");
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer data = ByteBuffer.allocate(sc.socket().getReceiveBufferSize());
                        
                        System.out.println("new message: " + sc.socket().getInetAddress().getHostAddress());
                        
                        if (sc.read(data) == -1) {
                            continue; // bad packet
                        }
                        
                        data.flip();
                        
                        // Teste m = (Teste)
                        // UdpUtil.byteToMessage(data.array());
                        
                        System.out.println("message: " + data.array());
                    }
                    
                    if (key.isReadable() && key.channel() == udpChannel) {
                        
                        DatagramChannel chan = (DatagramChannel) key.channel();
                        ByteBuffer data = ByteBuffer.allocate(chan.socket().getSendBufferSize());
                        
                        InetSocketAddress add = (InetSocketAddress) chan.receive(data);
                        
                        data.flip();
                        
                        KiiMessage msgReceived = UdpUtil.byteToMessage(data.array());
                        
                        if (msgReceived instanceof Discover) {
                            ((Discover) msgReceived).getStudent().setIpAdrress(add.getHostName());
                            
                            Message iMessage = Message.obtain(msgHandler, UDP_MSG, msgReceived);
                            msgHandler.sendMessage(iMessage);
                        } else {
                            Log.d(TAG, "Wrong message received: " + msgReceived.toString());
                        }
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private class MyHandler extends Thread {
        
        private final ArrayList<KiiMessage> fifo;
        
        public MyHandler() {
        
            fifo = new ArrayList<KiiMessage>();
        }
        
        public void sendUdpMessage( UdpMessage msg ) {
        
            synchronized (fifo) {
                fifo.add(msg);
                fifo.notifyAll();
            }
        }
        
        @Override
        public void run() {
        
            while (true) {
                while (!fifo.isEmpty()) {
                    
                    KiiMessage msg = fifo.remove(0);
                    
                    if (msg instanceof UdpMessage) {
                        
                        if (msg instanceof Connect) {
                            handleConnect((Connect) msg);
                        }
                        
                    } else {
                        Log.d(TAG, "Wrong message type received: " + msg.toString());
                    }
                    
                }
                try {
                    synchronized (fifo) {
                        fifo.wait();
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
        private void handleConnect( Connect msg ) {
        
            /*
                try {
                    SocketAddress IPAddress = new InetSocketAddress(msg.getDestIp(), UDP_PORT);
                    
                    byte[] sendData = UdpUtil.messageToByteMessage(msg);
                    ByteBuffer bb = ByteBuffer.wrap(sendData);
                    
                    udpChannel.send(bb, IPAddress);
                }
                catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }*/
        }
    }
}
