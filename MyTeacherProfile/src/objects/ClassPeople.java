
package objects;

import java.util.ArrayList;

public class ClassPeople implements Comparable<ClassPeople> {
    
    private String             name;
    private String             classType;
    private ArrayList<Student> students;
    
    public ClassPeople( String name, String classType ) {
    
        this.name = name;
        students = new ArrayList<Student>();
    }
    
    public String getClassType() {
    
        return classType;
    }
    
    public void setClassType( String classType ) {
    
        this.classType = classType;
    }
    
    public String getName() {
    
        return name;
    }
    
    public int numberStudents() {
    
        return students.size();
    }
    
    public ArrayList<Student> getStudents() {
    
        return students;
    }
    
    public void setName( String name ) {
    
        this.name = name;
    }
    
    public void setStudents( ArrayList<Student> students ) {
    
        this.students = students;
    }
    
    public boolean addStudent( Student std ) {
    
        if (std == null) {
            return false;
        }
        
        return students.add(std);
    }
    
    public boolean remStudent( Student std ) {
    
        return students.remove(std);
    }
    
    @Override
    public int compareTo( ClassPeople another ) {
    
        return getName().compareTo(another.getName());
    }
    
    @Override
    public String toString() {
    
        return "ClassPeople [name=" + name + ", students=" + students + "]";
    }
    
}
