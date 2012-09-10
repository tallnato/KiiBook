
package messages;

public class Teste implements KiiMessage {
    
    private static final long serialVersionUID = 3215350146540396867L;
    public final String       cenas;
    
    public Teste( String cenas ) {
    
        this.cenas = cenas;
    }
    
    @Override
    public String toString() {
    
        return "Teste [" + cenas + "]";
    }
}
