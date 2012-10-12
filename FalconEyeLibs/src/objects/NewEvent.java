
package objects;

import java.io.Serializable;
import java.util.ArrayList;

public class NewEvent implements Serializable {
    
    private static final long    serialVersionUID = -6849186650737564963L;
    private String               description;
    private EventType            type;
    private long                 date;
    private int                  hour;
    private ArrayList<MediaBook> links            = new ArrayList<MediaBook>();
    
    public NewEvent( String what, EventType typeSelected, long date, int hour, ArrayList<MediaBook> links ) {
    
        this.description = what;
        this.type = typeSelected;
        this.date = date;
        this.hour = hour;
        this.links = links;
    }
    
    public String getDescription() {
    
        return description;
    }
    
    public void setDescription( String description ) {
    
        this.description = description;
    }
    
    public EventType getType() {
    
        return type;
    }
    
    public void setType( EventType type ) {
    
        this.type = type;
    }
    
    public long getDate() {
    
        return date;
    }
    
    public void setDate( long date ) {
    
        this.date = date;
    }
    
    public int getHour() {
    
        return hour;
    }
    
    public void setHour( int hour ) {
    
        this.hour = hour;
    }
    
    public ArrayList<MediaBook> getLinks() {
    
        return links;
    }
    
    public void setLinks( ArrayList<MediaBook> links ) {
    
        this.links = links;
    }
    
    @Override
    public String toString() {
    
        return "NewEvent [description=" + description + ", type=" + type + ", date=" + date + ", hour=" + hour + ", links="
                                        + links.toString() + "]";
    }
    
}
