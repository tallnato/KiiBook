
package objects;

import java.util.Comparator;

public class SummaryComparator implements Comparator {
    
    @Override
    public int compare( Object emp1, Object emp2 ) {
    
        int emp1Num = ((Summary) emp1).getNumber();
        int emp2Num = ((Summary) emp2).getNumber();
        
        if (emp1Num < emp2Num)
            return 1;
        else if (emp1Num > emp2Num)
            return -1;
        else
            return 0;
        
    }
    
}
