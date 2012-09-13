
package objects;

import java.io.Serializable;

public class NewEvent implements Serializable {
    
    private static final long serialVersionUID = -6849186650737564963L;
    private String            what;
    private String            description;
    private EventType         type;
    private long              date;
    private int               hour;
    
    public NewEvent( String what, String description, EventType type, long date, int hour ) {
    
        super();
        this.what = what;
        this.description = description;
        this.type = type;
        this.date = date;
        this.hour = hour;
    }
    
    public String getWhat() {
    
        return what;
    }
    
    public void setWhat( String what ) {
    
        this.what = what;
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
    
    @Override
    public String toString() {
    
        return "NewEvent [what=" + what + ", description=" + description + ", type=" + type + ", date=" + date + ", hour=" + hour + "]";
    }
    
}
