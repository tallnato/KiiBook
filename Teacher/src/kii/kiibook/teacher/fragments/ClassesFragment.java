
package kii.kiibook.teacher.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import objects.ClassPeople;
import objects.ObjectCreator;

import java.util.ArrayList;
import java.util.Iterator;

import kii.kiibook.managerclass.ManagerClassActivity;
import kii.kiibook.managerclass.database.DataShared;
import kii.kiibook.managerclass.utils.ItemListView;
import kii.kiibook.teacher.R;
import kii.kiibook.teacher.adapters.AdapterListView;

public class ClassesFragment extends Fragment implements OnItemClickListener {
    
    private final String            TAG   = "ClassesFragment";
    public final static String      CLASS = "class";
    
    private ArrayList<ItemListView> itens;
    private AdapterListView         adapterListView;
    private ListView                listView;
    private Activity                myActivity;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        setHasOptionsMenu(true);
        registerForContextMenu(container);
        
        View view = inflater.inflate(R.layout.classes_fragment, container, false);
        
        myActivity = getActivity();
        
        listView = (ListView) view.findViewById(R.id.listClasses);
        listView.setOnItemClickListener(this);
        
        createListView();
        return view;
    }
    
    private void createListView() {
    
        itens = new ArrayList<ItemListView>();
        
        Iterator<ClassPeople> it = DataShared.getInstance().getClasses().iterator();
        
        while (it.hasNext()) {
            
            ItemListView item1 = new ItemListView(it.next().getName());
            itens.add(item1);
        }
        
        // Cria o adapter
        adapterListView = new AdapterListView(getActivity(), itens);
        
        // Define o Adapter
        listView.setAdapter(adapterListView);
        
        // Cor quando a lista é selecionada para ralagem.
        listView.setCacheColorHint(Color.TRANSPARENT);
    }
    
    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
    
        inflater.inflate(R.menu.menu_frame_classes, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    
        Log.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
        
            case R.id.menu_addClass:
                showDialogAddClass();
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void onItemClick( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
    
        final int pos = arg2;
        
        // Pega o item que foi selecionado.
        ItemListView item = adapterListView.getItem(arg2);
        
        final CharSequence[] items = { "Gerir", "Eliminar", "Cancelar" };
        
        AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
        builder.setTitle(item.getTexto());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            
            public void onClick( DialogInterface dialog, int item ) {
            
                switch (item) {
                    case 0:
                        Bundle b = new Bundle();
                        b.putInt(CLASS, pos);
                        Intent myIntent = new Intent(myActivity, ManagerClassActivity.class);
                        myIntent.putExtras(b);
                        myActivity.startActivity(myIntent);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        
    }
    
    private void showDialogAddClass() {
    
        // set up dialog
        final Dialog dialog = new Dialog(myActivity);
        dialog.setContentView(R.layout.new_class_dialog);
        dialog.setTitle(R.string.menu_newClass);
        dialog.setCancelable(true);
        
        // TODO here ObjectCreator
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(myActivity, android.R.layout.simple_dropdown_item_1line,
                                        ObjectCreator.getClassType());
        final AutoCompleteTextView textView = (AutoCompleteTextView) dialog.findViewById(R.id.newclass_autoComplete);
        textView.setAdapter(adapter);
        
        final EditText nameClass = (EditText) dialog.findViewById(R.id.newclass_editText_nameClass);
        
        Button save = (Button) dialog.findViewById(R.id.newclass_button);
        save.setOnClickListener(new View.OnClickListener() {
            
            public void onClick( View v ) {
            
                ClassPeople newClass = new ClassPeople(nameClass.getText().toString(), textView.getText().toString());
                DataShared.getInstance().getClasses().add(newClass);
                createListView();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
