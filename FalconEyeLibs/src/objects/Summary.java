
package objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Summary implements Serializable {
    
    private static final long    serialVersionUID = -434654396391436685L;
    private ArrayList<MediaBook> listMedia;
    private String               text;
    private Date                 date;
    private int                  number;
    
    public Summary( ArrayList<MediaBook> list, String text, Date date, int num ) {
    
        this.listMedia = list;
        this.text = text;
        this.date = date;
        number = num + 1;
    }
    
    public int getNumber() {
    
        return number;
    }
    
    public void setNumber( int number ) {
    
        this.number = number;
    }
    
    public ArrayList<MediaBook> getListMedia() {
    
        return listMedia;
    }
    
    public void setListMedia( ArrayList<MediaBook> listMedia ) {
    
        this.listMedia = listMedia;
    }
    
    public String getText() {
    
        return text;
    }
    
    public void setText( String text ) {
    
        this.text = text;
    }
    
    public Date getDate() {
    
        return date;
    }
    
    public void setDate( Date date ) {
    
        this.date = date;
    }
    
    @Override
    public String toString() {
    
        return "title=" + listMedia + ", text=" + text + ", date=" + date;
    }
    
}
