
package kii.kiibook.managerclass.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TableLayout;

import kii.kiibook.teacher.R;

public class SkillsFragment extends Fragment implements OnTabChangeListener {
    
    public static final String TAG  = "StatsFragment";
    public static final String FRAG = "fragment";
    
    private View               mRoot;
    private TabHost            mTabHost;
    private TableLayout        mTableLayout;
    private int                classId;
    private TableLayout        mTableLayoutWork;
    private TableLayout        mTableLayoutTests;
    private ImageView          image;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.skills_frame, container, false);
        Bundle bundle = getArguments();
        
        classId = bundle.getInt(FRAG);
        
        createTabs();
        
        image = (ImageView) mRoot.findViewById(R.id.imageView_tab2);
        Spinner spinnerTab1 = (Spinner) mRoot.findViewById(R.id.spinner_tab1);
        Spinner spinnerTab2 = (Spinner) mRoot.findViewById(R.id.spinner_tab2);
        
        String[] itemsTab1 = { "Números racionais não negativos", "Reflexão, rotação e translação", "Números naturais",
                                        "Relações e regularidades", "Volumes", "Representação e interpretação de dados", "Números inteiros" };
        
        String[] itemsTab2 = { "Criatividade", "Comunicação", "Pensamento critico e Resolução de problemas", "Liderança e Colaboração",
                                        "Planeamento e Organização", "Aprendizagem de competencias",
                                        "Empreendorismo e Responsabilidade social" };
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, itemsTab1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTab1.setAdapter(adapter);
        
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, itemsTab2);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTab2.setAdapter(adapt);
        
        spinnerTab2.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
            
                if (arg2 == 0) {
                    image.setImageResource(R.drawable.human_skills);
                } else {
                    image.setImageResource(R.drawable.human_skills2);
                }
                
            }
            
            public void onNothingSelected( AdapterView<?> arg0 ) {
            
            }
        });
        
        return mRoot;
    }
    
    private void createTabs() {
    
        mTabHost = (TabHost) mRoot.findViewById(R.id.tabhost_stats);
        mTabHost.setup();
        mTabHost.setOnTabChangedListener(this);
        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("académicas").setContent(R.id.stats_tab1));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("humanas").setContent(R.id.stats_tab2));
        
        mTabHost.setCurrentTab(0);
    }
    
    public void onTabChanged( String tabId ) {
    
    }
    
}
