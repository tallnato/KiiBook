
package kii.kiibook.managerclass.utils;

import java.util.ArrayList;

public class MediabookListView {
    
    private ArrayList<MediabooksList> list = new ArrayList<MediabooksList>();
    
    public ArrayList<MediabooksList> getList() {
    
        return list;
    }
    
    public void setList( ArrayList<MediabooksList> list ) {
    
        this.list = list;
    }
    
    public boolean addMediabook( MediabooksList media ) {
    
        return list.add(media);
    }
    
    public MediabookListView() {
    
    }
    
    @Override
    public String toString() {
    
        return "MediabookListView [list=" + list + "]";
    }
    
}
