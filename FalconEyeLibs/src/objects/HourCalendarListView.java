
package objects;

import java.util.ArrayList;

public class HourCalendarListView {
    
    private final ArrayList<TextElem> elemInHour;
    
    public HourCalendarListView( ArrayList<TextElem> elemInHour ) {
    
        this.elemInHour = elemInHour;
    }
    
    public ArrayList<TextElem> getElemInHour() {
    
        return elemInHour;
    }
    
}
