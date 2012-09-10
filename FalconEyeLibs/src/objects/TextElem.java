
package objects;

import java.io.Serializable;

public class TextElem implements Serializable {
    
    private static final long serialVersionUID = -6548843282960499555L;
    
    private int               colof;
    private String            text;
    
    public TextElem( int colof, String text ) {
    
        this.colof = colof;
        this.text = text;
    }
    
    public int getColof() {
    
        return colof;
    }
    
    public void setColof( int colof ) {
    
        this.colof = colof;
    }
    
    public String getText() {
    
        return text;
    }
    
    public void setText( String text ) {
    
        this.text = text;
    }
    
}
