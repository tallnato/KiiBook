
package managerclass;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.myteacherprofile.R;

import fragments.CalendarFragment;
import fragments.ClassBookFragment;
import fragments.DocFragment;
import fragments.FalconEyeFragment;
import fragments.NewsFragment;
import fragments.StatsFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    
    private final int      classId;
    private final String[] list = { "Livro de Ponto", "Olho de Falcão", "Documentos", "Calendario", "Estatisticas", "Noticias" };
    
    public PagerAdapter( FragmentManager fm, int classId ) {
    
        super(fm);
        this.classId = classId;
    }
    
    @Override
    public Fragment getItem( int i ) {
    
        Fragment fragment = null;
        Bundle args = new Bundle();
        
        switch (i) {
            case 0:
                fragment = new ClassBookFragment();
                args.putInt(ClassBookFragment.FRAG, classId);
                fragment.setArguments(args);
                break;
            case 1:
                fragment = new FalconEyeFragment();
                args.putInt(FalconEyeFragment.FRAG, classId);
                fragment.setArguments(args);
                break;
            case 2:
                fragment = new DocFragment();
                args.putInt(DocFragment.FRAG, classId);
                fragment.setArguments(args);
                break;
            case 3:
                fragment = new CalendarFragment();
                args.putInt(CalendarFragment.FRAG, classId);
                fragment.setArguments(args);
                break;
            case 4:
                fragment = new NewsFragment();
                args.putInt(NewsFragment.FRAG, classId);
                fragment.setArguments(args);
                break;
            case 5:
                fragment = new StatsFragment();
                args.putInt(NewsFragment.FRAG, classId);
                fragment.setArguments(args);
                break;
        }
        
        return fragment;
    }
    
    @Override
    public int getCount() {
    
        return 6;
    }
    
    public int getFragmentIcon( int i ) {
    
        int resources = 0;
        
        switch (i) {
            case 0:
                resources = android.R.drawable.ic_menu_agenda;
                break;
            case 1:
                resources = android.R.drawable.ic_menu_view;
                break;
            case 2:
                resources = android.R.drawable.ic_menu_slideshow;
                break;
            case 3:
                resources = android.R.drawable.ic_menu_my_calendar;
                break;
            case 4:
                resources = android.R.drawable.ic_menu_sort_by_size;
                break;
            case 5:
                resources = R.drawable.ic_menu_messages;
                break;
        }
        
        return resources;
    }
}
