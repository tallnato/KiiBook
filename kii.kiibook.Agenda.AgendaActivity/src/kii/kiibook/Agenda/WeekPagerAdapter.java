
package kii.kiibook.Agenda;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.CalendarView;

public class WeekPagerAdapter extends FragmentPagerAdapter {
    
    private int                size = 52;
    private final CalendarView cal;
    
    public WeekPagerAdapter( FragmentManager fm, CalendarView calendar ) {
    
        super(fm);
        cal = calendar;
    }
    
    @Override
    public Fragment getItem( int i ) {
    
        return new FragmentWeek();
    }
    
    @Override
    public int getCount() {
    
        return size;
    }
    
    public void setCountIncrement() {
    
        ++this.size;
    }
    
    public void setCount( int num ) {
    
        this.size = num;
    }
}
