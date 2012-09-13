
package messages.tcp.network;

import objects.NewEvent;

public class NewEventNetwork implements NetworkTcpMessage {
    
    private static final long serialVersionUID = -1763521588705904603L;
    
    private NewEvent          summary;
    
    public NewEventNetwork( NewEvent sum ) {
    
        summary = sum;
    }
    
    public void setSummary( NewEvent summary ) {
    
        this.summary = summary;
    }
    
    public NewEvent getSummary() {
    
        return summary;
    }
    
}
