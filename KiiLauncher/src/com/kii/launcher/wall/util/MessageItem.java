
package com.kii.launcher.wall.util;

public class MessageItem {
    
    private final String from;
    private final String to;
    private final String message;
    
    public MessageItem( String from, String to, String message ) {
    
        this.from = from;
        this.to = to;
        this.message = message;
    }
    
    public String getFrom() {
    
        return from;
    }
    
    public String getTo() {
    
        return to;
    }
    
    public String getMessage() {
    
        return message;
    }
    
    @Override
    public String toString() {
    
        return "MessageItem [from=" + from + ", to=" + to + ", message=" + message + "]";
    }
    
}
