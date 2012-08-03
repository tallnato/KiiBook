
package objects;

public class TextElem {
    
    private int    colof;
    private String text;
    
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
