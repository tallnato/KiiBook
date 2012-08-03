package kii.kiibook.managerclass.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import kii.kiibook.managerclass.AdapterClassModeView;
import kii.kiibook.managerclass.TestObjectsResources.ObjectCreator;
import kii.kiibook.managerclass.objects.Student;
import kii.kiibook.managerclass.utils.ItemListView;
import kii.kiibook.teacher.R;


public class ClassModeFragment extends Fragment implements OnItemClickListener{
    public static final String TAG  = "ClassModeFragment";
    public static final String FRAG = "fragment";
    
    private View               mRoot;
    private String             fragment;
    private ListView listView;
    private ArrayList<ItemListView> itens;
    private int classId;
    private AdapterClassModeView adapterListView;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.class_mode_frame, container, false);
        Bundle bundle = getArguments();
        
        classId = bundle.getInt(FRAG);
        
        listView = (ListView) mRoot.findViewById(R.id.list_class_mode);

        createListView();
        return mRoot;
    }

    public void onItemClick( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
    
        Toast.makeText(this.getActivity(), "Click", Toast.LENGTH_SHORT).show();
    }
    
    private void createListView() {
    
        // Criamos nossa lista que preenchera o ListView
        itens = new ArrayList<ItemListView>();
        
        Iterator it = ObjectCreator.getInstance().getClasses().get(classId).getStudents().iterator();
        
        while (it.hasNext()) {
            
            ItemListView item1 = new ItemListView(((Student) it.next()).getName(), android.R.drawable.checkbox_off_background);
            itens.add(item1);
        }
        
        // Cria o adapter
        adapterListView = new AdapterClassModeView(getActivity(), itens);
        
        // Define o Adapter
        listView.setAdapter(adapterListView);
        // Cor quando a lista é selecionada para ralagem.
        listView.setCacheColorHint(Color.TRANSPARENT);
    }
}
