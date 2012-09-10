
package objects;

import comchannels.ComChannel;

import util.SlaveStatus;

import java.io.Serializable;
import java.util.List;

public class Student extends Profile implements Serializable {
    
    private static final long        serialVersionUID = -6548843282960499555L;
    
    private String                   ipAdrress        = "";
    private long                     id               = 0;
    private boolean                  attendance       = false;
    private final int                numStudent       = 12;
    private final double             average          = 4.5;
    private SlaveStatus              status           = SlaveStatus.DISCONNECT;
    private List<PackagePermissions> packages;
    private ComChannel               com              = null;
    
    public Student( String name, Data aniversary, int bi, int numTelf, String email, Sex sex, Address addr, String ipAdd, long id ) {
    
        super(name, aniversary, bi, numTelf, email, sex, addr);
        ipAdrress = ipAdd;
        this.id = id;
        packages = null;
    }
    
    public Student( String name, Data aniversary, int bi, int numTelf, String email, Sex sex, Address addr ) {
    
        super(name, aniversary, bi, numTelf, email, sex, addr);
    }
    
    public int getNumStudent() {
    
        return numStudent;
    }
    
    public double getAverage() {
    
        return average;
    }
    
    public ComChannel getComChannel() {
    
        return com;
    }
    
    public void setComChannel( ComChannel com ) {
    
        this.com = com;
    }
    
    public boolean isAttendance() {
    
        return attendance;
    }
    
    public void setAttendance( boolean attd ) {
    
        attendance = attd;
    }
    
    public String getIpAdrress() {
    
        return ipAdrress;
    }
    
    public void setIpAdrress( String ipAdrress ) {
    
        this.ipAdrress = ipAdrress;
    }
    
    public long getId() {
    
        return id;
    }
    
    public void setId( long id ) {
    
        this.id = id;
    }
    
    public SlaveStatus getStatus() {
    
        return status;
    }
    
    public void setStatus( SlaveStatus status ) {
    
        this.status = status;
    }
    
    public List<PackagePermissions> getPackages() {
    
        return packages;
    }
    
    public void setPackages( List<PackagePermissions> packages ) {
    
        this.packages = packages;
    }
    
    @Override
    public String toString() {
    
        return "Student [" + super.toString() + "ipAdrress=" + ipAdrress + ", nickname=" + id + ", getIpAdrress()=" + getIpAdrress()
                                        + ", getNickname()=" + getId() + "]";
    }
    
}
