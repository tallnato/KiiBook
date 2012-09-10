
package kii.kiibook.managerclass.utils;

import java.util.Arrays;

public class MediabooksList {
    
    private String   name;
    private int      numPages;
    private String[] pages;
    
    public MediabooksList( String name, int numPages, String[] pages ) {
    
        this.name = name;
        this.numPages = numPages;
        this.pages = pages;
    }
    
    public String getName() {
    
        return name;
    }
    
    public void setName( String name ) {
    
        this.name = name;
    }
    
    public int getNumPages() {
    
        return numPages;
    }
    
    public void setNumPages( int numPages ) {
    
        this.numPages = numPages;
    }
    
    public String[] getPages() {
    
        return pages;
    }
    
    public void setPages( String[] pages ) {
    
        this.pages = pages;
    }
    
    @Override
    public String toString() {
    
        return "MediabooksList [name=" + name + ", numPages=" + numPages + ", pages=" + Arrays.toString(pages) + "]";
    }
    
}
