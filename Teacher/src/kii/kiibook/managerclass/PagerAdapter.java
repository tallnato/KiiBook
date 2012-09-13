
package kii.kiibook.managerclass;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kii.kiibook.managerclass.fragments.ClassModeFragment;
import kii.kiibook.managerclass.fragments.DocFragment;
import kii.kiibook.managerclass.fragments.SkillsFragment;
import kii.kiibook.managerclass.fragments.StatsFragment;
import kii.kiibook.managerclass.fragments.SummariesFragment;

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
                fragment = new ClassModeFragment();
                args.putInt(ClassModeFragment.FRAG, classId);
                fragment.setArguments(args);
                break;
            case 1:
                
                fragment = new SummariesFragment();
                break;
            
            case 2:
                fragment = new StatsFragment();
                args.putInt(StatsFragment.FRAG, classId);
                fragment.setArguments(args);
                break;
            
            case 3:
                fragment = new SkillsFragment();
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
    
        return 5;
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
                resources = android.R.drawable.ic_menu_info_details;
                break;
            case 3:
                resources = android.R.drawable.ic_menu_myplaces;
                break;
        }
        
        return resources;
    }
    
    public String getFragmentTitle( int i ) {
    
        switch (i) {
            case 0:
                return "Livro de Ponto";
                
            case 1:
                return "H. Sumários";
                
            case 2:
                return "Avaliações";
                
            case 3:
                return "Competencias";
                
            case 4:
                return "Documentos";
                
        }
        return "";
    }
}
