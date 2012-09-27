
package kii.kiibook.managerclass.adapters;

public class ItemSummaryListView {
    
    private String time;
    private String title;
    private String desc;
    
    public ItemSummaryListView( String time, String title, String desc ) {
    
        super();
        this.time = time;
        this.title = title;
        this.desc = desc;
    }
    
    public String getTime() {
    
        return time;
    }
    
    public void setTime( String time ) {
    
        this.time = time;
    }
    
    public String getTitle() {
    
        return title;
    }
    
    public void setTitle( String title ) {
    
        this.title = title;
    }
    
    public String getDesc() {
    
        return desc;
    }
    
    public void setDesc( String desc ) {
    
        this.desc = desc;
    }
    
}
