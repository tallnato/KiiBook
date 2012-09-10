
package falconeye.udp;

import android.os.Handler;
import android.os.Message;

import messages.KiiMessage;
import messages.udp.Connect;
import messages.udp.Discover;
import messages.udp.UdpMessage;
import util.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpListener extends Thread implements Constants {
    
    private DatagramSocket connectionSocket;
    private final int      port;
    private Handler        msgHandler;
    private final String   ip;
    
    public UdpListener( int port, String ip ) {
    
        this.port = port;
        msgHandler = null;
        this.ip = ip;
        
        try {
            connectionSocket = new DatagramSocket(port);
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
    }
    
    public void setMsgHandler( Handler msgHandler ) {
    
        this.msgHandler = msgHandler;
    }
    
    public void sendMessage( UdpMessage message, String address ) {
    
        try {
            
            InetAddress dest = InetAddress.getByName(address);
            byte[] sendData = UdpUtil.messageToByteMessage(message);
            
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, dest, port);
            
            connectionSocket.send(sendPacket);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void close() {
    
        connectionSocket.disconnect();
        connectionSocket.close();
    }
    
    @Override
    public void run() {
    
        DatagramPacket receivePacket;
        byte[] receiveData = new byte[1500];
        
        while (!connectionSocket.isClosed()) {
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                connectionSocket.receive(receivePacket);
                if (ip.equals(receivePacket.getAddress().getHostAddress())) {
                    continue;
                }
                
                KiiMessage msgReceived = UdpUtil.byteToMessage(receivePacket.getData());
                
                if (msgReceived instanceof Discover) {
                    ((Discover) msgReceived).getStudent().setIpAdrress(receivePacket.getAddress().getHostAddress());
                    
                    Message iMessage = Message.obtain(msgHandler, UDP_MSG, msgReceived);
                    msgHandler.sendMessage(iMessage);
                } else if (msgReceived instanceof Connect) {
                    Message iMessage = Message.obtain(msgHandler, UDP_MSG, msgReceived);
                    msgHandler.sendMessage(iMessage);
                } else {
                    System.out.println("Wrong message format:" + msgReceived.toString());
                }
                
            }
            catch (SocketException e) {
                e.printStackTrace();
                return;
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
