
package messages.udp;

public class Connect implements UdpMessage {
    
    private static final long serialVersionUID = 1259163380943018732L;
    
    private final String      masterIp;
    private final String      masterName;
    private final int         port;
    private final String      destIp;
    
    public Connect( String masterIp, String masterName, int port, String destIp ) {
    
        super();
        this.masterIp = masterIp;
        this.masterName = masterName;
        this.port = port;
        this.destIp = destIp;
    }
    
    public String getMasterIp() {
    
        return masterIp;
    }
    
    public String getMasterName() {
    
        return masterName;
    }
    
    public String getDestIp() {
    
        return destIp;
    }
    
    public int getPort() {
    
        return port;
    }
    
    @Override
    public String toString() {
    
        return "Connect [masterIp=" + masterIp + ", masterName=" + masterName + ", port=" + port + "]";
    }
}
