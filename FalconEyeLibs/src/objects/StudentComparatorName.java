
package objects;

import java.util.Comparator;

public class StudentComparatorName implements Comparator {
    
    @Override
    public int compare( Object emp1, Object emp2 ) {
    
        String emp1Age = ((Student) emp1).getName();
        String emp2Age = ((Student) emp2).getName();
        
        return emp1Age.compareToIgnoreCase(emp2Age);
        
    }
    
}
