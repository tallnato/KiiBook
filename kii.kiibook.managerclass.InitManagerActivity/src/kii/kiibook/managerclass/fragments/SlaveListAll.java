
package kii.kiibook.managerclass.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import objects.Student;

import java.util.ArrayList;

import kii.kiibook.managerclass.PermissionsArrayAdapter;
import kii.kiibook.managerclass.callbacks.MasterActivityCallback;
import kii.kiibook.managerclass.database.DataShared;
import kii.kiibook.teacher.R;

public class SlaveListAll extends ListFragment implements OnClickListener {
    
    private MasterActivityCallback  masterCallback;
    private ArrayList<Student>      slaves;
    private PermissionsArrayAdapter mAdapter;
    private Dialog                  dialog;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        slaves = DataShared.getInstance().getListAll();
    }
    
    @Override
    public void onDetach() {
    
        super.onDetach();
        masterCallback = null;
    }
    
    @Override
    public void onListItemClick( ListView listView, View view, int position, long id ) {
    
        showDialogItem(slaves.get(position));
        super.onListItemClick(listView, view, position, id);
        
    }
    
    private void showDialogItem( final Student student ) {
    
        String[] items = { "Ver Perfil", "Aplicações Bloqueadas" };
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            
            public void onClick( DialogInterface dialog, int item ) {
            
                switch (item) {
                    case 0:
                        showDialogProfile(student);
                        break;
                    case 1:
                        showPermissions(student);
                        break;
                    case 2:
                        
                        break;
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
    
    private void showPermissions( Student student ) {
    
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.permission_list);
        
        if (student.getPackages() == null) {
            Log.w(getTag(), "No Packages :(");
        }
        
        mAdapter = new PermissionsArrayAdapter(getActivity(), R.layout.fragment_permissions_item_list, student.getPackages(), null);
        ListView list = (ListView) dialog.findViewById(R.id.list_permissions);
        list.setAdapter(mAdapter);
        
        Button btn = (Button) dialog.findViewById(R.id.actionBar_button);
        btn.setOnClickListener(this);
        dialog.setCancelable(false);
        dialog.show();
    }
    
    public void onClick( View v ) {
    
        switch (v.getId()) {
            case R.id.actionBar_button:
                dialog.dismiss();
                break;
        }
        
    }
    
}
