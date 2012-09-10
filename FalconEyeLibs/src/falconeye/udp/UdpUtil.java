
package falconeye.udp;

import android.net.DhcpInfo;

import messages.KiiMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UdpUtil {
    
    public static String getBroadcastAddress( DhcpInfo dhcp ) {
    
        int broadcast = dhcp.ipAddress & dhcp.netmask | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++) {
            quads[k] = (byte) (broadcast >> k * 8 & 0xFF);
        }
        try {
            return InetAddress.getByAddress(quads).getHostAddress();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static byte[] messageToByteMessage( KiiMessage object ) {
    
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
            
            return baos.toByteArray();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static KiiMessage byteToMessage( byte[] object ) {
    
        try {
            ByteArrayInputStream baos = new ByteArrayInputStream(object);
            ObjectInputStream oos = new ObjectInputStream(baos);
            
            return (KiiMessage) oos.readObject();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
