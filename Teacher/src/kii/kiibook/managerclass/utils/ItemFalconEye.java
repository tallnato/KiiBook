package kii.kiibook.managerclass.utils;

import kii.kiibook.managerclass.objects.Student;

public class ItemFalconEye {
    
    private int     image;
    private Student student;
    
    public ItemFalconEye( int image, Student student ) {
    
        super();
        this.image = image;
        this.student = student;
    }
    
    public int getImage() {
    
        return image;
    }
    
    public void setImage( int image ) {
    
        this.image = image;
    }
    
    public Student getStudent() {
    
        return student;
    }
    
    public void setStudent( Student student ) {
    
        this.student = student;
    }
    
}
