
package messages.tcp.network;

import objects.PackagePermissions;

import java.util.List;

public class ApplicationList_Response_ACK implements NetworkTcpMessage {
    
    private static final long              serialVersionUID = -5875987254284940977L;
    
    private final List<PackagePermissions> packages;
    
    public ApplicationList_Response_ACK( List<PackagePermissions> packages ) {
    
        this.packages = packages;
    }
    
    public List<PackagePermissions> getPackages() {
    
        return packages;
    }
}
