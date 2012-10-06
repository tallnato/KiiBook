
package objects;

import java.io.Serializable;

public class MediaBook implements Serializable {
    
    private final String      name;
    private final int         page;
    private final String      pages;
    
    private static final long serialVersionUID = -434654396391436685L;
    
    public MediaBook( String name, int page, String pageName ) {
    
        this.name = name;
        this.page = page;
        this.pages = pageName;
    }
    
    public String getPageName() {
    
        return pages;
    }
    
    public String getName() {
    
        return name;
    }
    
    public int getPage() {
    
        return page;
    }
    
    @Override
    public String toString() {
    
        return "MediaBook [name=" + name + ", page=" + page + "]";
    }
    
}
