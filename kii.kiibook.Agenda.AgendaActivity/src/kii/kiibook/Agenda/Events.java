
package kii.kiibook.Agenda;

import objects.NewEvent;

public class Events {
    
    private int      index;
    private int      hour;
    private NewEvent event;
    
    public Events( int index, int hour, NewEvent event ) {
    
        super();
        this.index = index;
        this.hour = hour;
        this.event = event;
    }
    
    public int getHour() {
    
        return hour;
    }
    
    public void setHour( int hour ) {
    
        this.hour = hour;
    }
    
    public int getIndex() {
    
        return index;
    }
    
    public void setIndex( int index ) {
    
        this.index = index;
    }
    
    public NewEvent getEvent() {
    
        return event;
    }
    
    public void setEvent( NewEvent event ) {
    
        this.event = event;
    }
    
    @Override
    public String toString() {
    
        return "Events [index=" + index + ", hour=" + hour + ", event=" + event + "]";
    }
    
}
