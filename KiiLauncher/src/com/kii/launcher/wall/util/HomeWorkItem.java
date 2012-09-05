
package com.kii.launcher.wall.util;

public class HomeWorkItem {
    
    private final String deliveryDate;
    private final String course;
    private final String homework;
    
    public HomeWorkItem( String deliveryDate, String course, String homework ) {
    
        this.deliveryDate = deliveryDate;
        this.course = course;
        this.homework = homework;
    }
    
    public String getDeliveryDate() {
    
        return deliveryDate;
    }
    
    public String getCourse() {
    
        return course;
    }
    
    public String getHomework() {
    
        return homework;
    }
    
    @Override
    public String toString() {
    
        return "HomeWorkItem [deliveryDate=" + deliveryDate + ", course=" + course + ", homework=" + homework + "]";
    }
}
