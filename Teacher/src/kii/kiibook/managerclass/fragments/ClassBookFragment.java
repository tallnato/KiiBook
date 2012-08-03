
package kii.kiibook.managerclass.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.Iterator;

import kii.kiibook.managerclass.AdapterClassBookView;
import kii.kiibook.managerclass.TestObjectsResources.ObjectCreator;
import kii.kiibook.managerclass.objects.Student;
import kii.kiibook.managerclass.utils.ItemListView;
import kii.kiibook.teacher.R;

public class ClassBookFragment extends Fragment implements OnItemClickListener {
    
    public static final String      TAG  = "ClassBookFragment";
    public static final String      FRAG = "fragment"; 
    
    private View                    mRoot;
    private String                  fragment;
    private ArrayList<ItemListView> itens;
    private int                     classId;
    private AdapterClassBookView         adapterListView;
    private ListView                listView;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.classbook_frame, container, false);
        Bundle bundle = getArguments();
        classId = bundle.getInt(FRAG);
        
        listView = (ListView) mRoot.findViewById(R.id.listView1);
        
        listView.setOnItemClickListener(this);
        
        createListView();
        
        return mRoot;
    }
    
  
    public void onItemClick( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
    
        showDialogStudent(arg2);
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
        adapterListView = new AdapterClassBookView(getActivity(), itens);
        
        // Define o Adapter
        listView.setAdapter(adapterListView);
        // Cor quando a lista é selecionada para ralagem.
        listView.setCacheColorHint(Color.TRANSPARENT);
    }
    
    private void showDialogStudent( final int StudentId ) {
    
        String[] items = { "Ver Perfil", "Marcar/Desmarcar Presença" };
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(adapterListView.getItem(StudentId).getTexto());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            
     
            public void onClick( DialogInterface dialog, int item ) {
            
                Student std = ObjectCreator.getInstance().getClasses().get(classId).getStudents().get(StudentId);
                if (item == 0) {
                    showDialogProfile(std);
                } else {
                    
                    if (std.isAttendance()) {
                        
                        adapterListView.getItem(StudentId).setIconeRid(android.R.drawable.checkbox_off_background);
                        listView.setAdapter(adapterListView);
                        std.setAttendance(false);
                        
                    } else {
                        
                        adapterListView.getItem(StudentId).setIconeRid(android.R.drawable.checkbox_on_background);
                        listView.setAdapter(adapterListView);
                        std.setAttendance(true);
                    }
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    private void showDialogProfile( Student student ) {
    
        // set up dialog
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.profile_student);
        dialog.setTitle(student.getName());
        dialog.setCancelable(true);
        
        // now that the dialog is set up, it's time to show it
        dialog.show();
    }
}
