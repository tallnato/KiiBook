
package kii.kiibook.managerclass.callbacks;

import objects.Student;

public interface MasterActivityCallback {
    
    public void onItemSelected( int position );
    
    public SlaveDetailsCallback getSlaveDetailsCallback();
    
    public void getPackages( Student slave );
}
