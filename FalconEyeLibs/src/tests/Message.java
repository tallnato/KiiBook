package tests;
import java.io.Serializable;

public class Message implements Serializable {
    
    private static final long serialVersionUID = -6578383801742695522L;
    private final String      cena;
    
    public Message( String cena ) {
    
        super();
        this.cena = cena;
    }
    
    public String getCena() {
    
        return cena;
    }
    
    @Override
    public String toString() {
    
        return "Message [cena=" + cena + "]";
    }
    
}
