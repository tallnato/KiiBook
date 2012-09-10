
package messages.udp;

import objects.Student;

public class Discover implements UdpMessage {
    
    private static final long serialVersionUID = -6548843282960499555L;
    
    private String            ip;
    
    private Student           student;
    
    public Discover( Student std ) {
    
        this.student = std;
    }
    
    public Student getStudent() {
    
        return student;
    }
    
    public void setStudent( Student student ) {
    
        this.student = student;
    }
    
}
