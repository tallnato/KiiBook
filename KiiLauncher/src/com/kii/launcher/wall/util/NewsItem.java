
package com.kii.launcher.wall.util;

public class NewsItem {
    
    private final String from;
    private final String message;
    
    public NewsItem( String from, String message ) {
    
        this.from = from;
        this.message = message;
    }
    
    public String getFrom() {
    
        return from;
    }
    
    public String getMessage() {
    
        return message;
    }
    
    @Override
    public String toString() {
    
        return "NewsItem [from=" + from + ",  message=" + message + "]";
    }
}
