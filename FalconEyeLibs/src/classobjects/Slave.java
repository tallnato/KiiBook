
package classobjects;

import android.widget.BaseAdapter;

import objects.PackagePermissions;

import comchannels.ComChannel;

import util.SlaveStatus;

import java.io.Serializable;
import java.util.List;

public class Slave implements Serializable {
    
    private static final long        serialVersionUID = 98943503969547208L;
    
    private final long               slaveId;
    private final String             firstName;
    private final String             lastName;
    
    private final String             ip;
    
    private SlaveStatus              status;
    
    private ComChannel               com;
    private List<PackagePermissions> packages;
    private final BaseAdapter        adaptor;
    
    public Slave( long slaveId, String firstName, String lastName, String ip, BaseAdapter adaptor ) {
    
        super();
        this.slaveId = slaveId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ip = ip;
        this.adaptor = adaptor;
        
        status = SlaveStatus.DISCOVERED;
        com = null;
        packages = null;
    }
    
    public long getSlaveId() {
    
        return slaveId;
    }
    
    public String getFirstName() {
    
        return firstName;
    }
    
    public String getLastName() {
    
        return lastName;
    }
    
    public String getIp() {
    
        return ip;
    }
    
    public SlaveStatus getStatus() {
    
        return status;
    }
    
    public void setStatus( SlaveStatus status ) {
    
        this.status = status;
        adaptor.notifyDataSetChanged();
    }
    
    public ComChannel getComChannel() {
    
        return com;
    }
    
    public void setComChannel( ComChannel com ) {
    
        this.com = com;
    }
    
    public List<PackagePermissions> getPackages() {
    
        return packages;
    }
    
    public void setPackages( List<PackagePermissions> packages ) {
    
        this.packages = packages;
    }
    
    @Override
    public String toString() {
    
        return "Slave [slaveId=" + slaveId + ", firstName=" + firstName + ", lastName=" + lastName + ", ip=" + ip + ", status=" + status
                                        + "]";
    }
}
