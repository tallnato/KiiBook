
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

public class StatsFragment extends Fragment implements OnItemSelectedListener {
    
    public static final String TAG  = "StatsFragment";
    public static final String FRAG = "fragment";
    
    private View               mRoot;
    private Spinner            spinner;
    private ImageView          image;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.stats_frame, container, false);
        
        spinner = (Spinner) mRoot.findViewById(R.id.spinner_tabs_aval);
        image = (ImageView) mRoot.findViewById(R.id.imageView_stats);
        
        String[] itemsTab1 = { "Avaliações Diárias", "Testes", "Trabalhos", "TPC" };
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, itemsTab1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        
        return mRoot;
    }
    
    public void onItemSelected( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
    
        switch (arg2) {
            case 0:
                image.setImageResource(R.drawable.diarias_aluno);
                break;
            case 1:
                image.setImageResource(R.drawable.testes_aluno);
                break;
            case 2:
                image.setImageResource(R.drawable.trabalhos_aluno);
                break;
            case 3:
                image.setImageResource(R.drawable.tpc_alunos);
                break;
        }
        
    }
    
    public void onNothingSelected( AdapterView<?> arg0 ) {
    
    }
    
}
