
package messages.tcp.network;

public class CloseConnectionACK implements NetworkTcpMessage {
    
    private static final long serialVersionUID = 4517668982969046823L;
    
    @Override
    public String toString() {
    
        return "CloseConnectionACK [getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
    }
    
}
