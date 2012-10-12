
package kii.kiibook.KiiClass.fragments;

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

import kii.kiibook.Student.R;

public class SkillsFragment extends Fragment implements OnItemSelectedListener {
    
    public static final String FRAG      = "fragment";
    private final String[]     itemsTab1 = { "Académicas", "Humanas" };
    
    private View               mRoot;
    private Spinner            spinner;
    private ImageView          image;
    private ImageView          image2;
    private ImageView          image3;
    private ImageView          image4;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        mRoot = inflater.inflate(R.layout.skills_fragment, container, false);
        
        spinner = (Spinner) mRoot.findViewById(R.id.spinner_tabs_skills1);
        image = (ImageView) mRoot.findViewById(R.id.imageView_skills1);
        image2 = (ImageView) mRoot.findViewById(R.id.imageView_skills2);
        image3 = (ImageView) mRoot.findViewById(R.id.imageView_skills3);
        image4 = (ImageView) mRoot.findViewById(R.id.imageView_skills4);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, itemsTab1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        
        return mRoot;
    }
    
    public void onItemSelected( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
    
        if (arg2 == 0) {
            
            image.setImageResource(R.drawable.teste1);
            image2.setImageResource(R.drawable.teste2);
            image3.setImageResource(R.drawable.teste3);
            image3.setVisibility(View.VISIBLE);
            image4.setImageResource(R.drawable.teste4);
            image4.setVisibility(View.VISIBLE);
            
        } else {
            image.setImageResource(R.drawable.humano1);
            image2.setImageResource(R.drawable.humanas2);
            image3.setVisibility(View.GONE);
            image4.setVisibility(View.GONE);
        }
    }
    
    public void onNothingSelected( AdapterView<?> arg0 ) {
    
    }
    
}
