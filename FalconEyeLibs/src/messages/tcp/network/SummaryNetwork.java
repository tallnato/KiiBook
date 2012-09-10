
package messages.tcp.network;

import objects.Summary;

public class SummaryNetwork implements NetworkTcpMessage {
    
    private static final long serialVersionUID = -1763521588705904603L;
    
    private Summary           summary;
    
    public SummaryNetwork( Summary sum ) {
    
        summary = sum;
    }
    
    public void setSummary( Summary summary ) {
    
        this.summary = summary;
    }
    
    public Summary getSummary() {
    
        return summary;
    }
    
}
