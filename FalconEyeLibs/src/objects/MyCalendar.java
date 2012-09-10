
package objects;

import java.io.Serializable;
import java.util.ArrayList;

public class MyCalendar implements Serializable {
    
    private static final long               serialVersionUID = -6548843282960499555L;
    
    private int                             day;
    private int                             month;
    private int                             year;
    private ArrayList<HourCalendarListView> listDay;
    
    public MyCalendar( int day, int month, int year, ArrayList<HourCalendarListView> listDay ) {
    
        this.day = day;
        this.month = month;
        this.year = year;
        this.listDay = listDay;
    }
    
    public int getDay() {
    
        return day;
    }
    
    public void setDay( int day ) {
    
        this.day = day;
    }
    
    public int getMonth() {
    
        return month;
    }
    
    public void setMonth( int month ) {
    
        this.month = month;
    }
    
    public int getYear() {
    
        return year;
    }
    
    public void setYear( int year ) {
    
        this.year = year;
    }
    
    public ArrayList<HourCalendarListView> getListDay() {
    
        return listDay;
    }
    
    public void setListDay( ArrayList<HourCalendarListView> listDay ) {
    
        this.listDay = listDay;
    }
    
    @Override
    public String toString() {
    
        return "MyCalendar [day=" + day + ", month=" + month + ", year=" + year + "]\n";
    }
    
}
