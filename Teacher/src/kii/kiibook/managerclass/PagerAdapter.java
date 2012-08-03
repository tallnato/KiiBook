
package kii.kiibook.managerclass;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kii.kiibook.managerclass.fragments.CalendarFragment;
import kii.kiibook.managerclass.fragments.ClassBookFragment;
import kii.kiibook.managerclass.fragments.ClassModeFragment;
import kii.kiibook.managerclass.fragments.DocFragment;
import kii.kiibook.managerclass.fragments.StatsFragment;


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
                fragment = new ClassModeFragment();
                args.putInt(ClassModeFragment.FRAG, classId);
                fragment.setArguments(args);
                break;
                
 
            case 2:
                fragment = new CalendarFragment();
                args.putInt(CalendarFragment.FRAG, classId);
                fragment.setArguments(args);
                break;
            case 3:
                fragment = new StatsFragment();
                args.putInt(StatsFragment.FRAG, classId);
                fragment.setArguments(args);
                break; 
            case 4:
                fragment = new DocFragment();
                args.putInt(DocFragment.FRAG, classId);
                fragment.setArguments(args);
                break;
            
        }
        
        return fragment;
    }
    
    @Override
    public int getCount() {

        return 3;
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
        }
        
        return resources;
    }
}
