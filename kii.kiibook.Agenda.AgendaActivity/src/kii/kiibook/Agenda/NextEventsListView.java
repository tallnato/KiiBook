
package kii.kiibook.Agenda;

import objects.EventType;

public class NextEventsListView {
    
    private final String elemInHour;
    private String       hour;
    private EventType    eventType;
    
    public NextEventsListView( String hour, EventType eventType, String elemInHour ) {
    
        this.hour = hour;
        this.elemInHour = elemInHour;
        this.eventType = eventType;
    }
    
    public String getHour() {
    
        return hour;
    }
    
    public String getEventType() {
    
        return eventType.toString();
    }
    
    public void setEventType( EventType eventType ) {
    
        this.eventType = eventType;
    }
    
    public void setHour( String hour ) {
    
        this.hour = hour;
    }
    
    public String getElemInHour() {
    
        return elemInHour;
    }
    
    @Override
    public String toString() {
    
        return "HourCalendarListView [hour=" + hour + ", elemInHour=" + elemInHour + "]";
    }
    
}
