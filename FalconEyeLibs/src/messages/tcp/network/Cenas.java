
package messages.tcp.network;


public class Cenas implements NetworkTcpMessage {
    
    private static final long serialVersionUID = -1763521588705904603L;
    
    public final String       cenas;
    
    public Cenas( String cenas ) {
    
        this.cenas = cenas;
    }
    
    @Override
    public String toString() {
    
        return "Cenas [" + cenas + "]";
    }
}
