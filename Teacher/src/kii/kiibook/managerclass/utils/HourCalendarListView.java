
package kii.kiibook.managerclass.utils;

import objects.TextElem;

import java.util.ArrayList;

public class HourCalendarListView {
    
    private String                    hour;
    private final ArrayList<TextElem> elemInHour;
    
    public HourCalendarListView( String hour, ArrayList<TextElem> elemInHour ) {
    
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
