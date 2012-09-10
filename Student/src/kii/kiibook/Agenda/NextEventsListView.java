
package kii.kiibook.Agenda;

import objects.TextElem;

import java.util.ArrayList;

public class NextEventsListView {
    
    private final ArrayList<TextElem> elemInHour;
    private String                    hour;
    
    public NextEventsListView( String hour, ArrayList<TextElem> elemInHour ) {
    
        this.hour = hour;
        this.elemInHour = elemInHour;
    }
    
    public String getHour() {
    
        return hour;
    }
    
    public void setHour( String hour ) {
    
        this.hour = hour;
    }
    
    public ArrayList<TextElem> getElemInHour() {
    
        return elemInHour;
    }
    
    @Override
    public String toString() {
    
        return "HourCalendarListView [hour=" + hour + ", elemInHour=" + elemInHour + "]";
    }
    
}
