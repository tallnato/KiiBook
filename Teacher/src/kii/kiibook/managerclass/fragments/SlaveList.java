
package kii.kiibook.managerclass.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

import objects.Student;

import java.util.ArrayList;

import kii.kiibook.managerclass.PermissionsArrayAdapter;
import kii.kiibook.managerclass.callbacks.MasterActivityCallback;
import kii.kiibook.managerclass.database.DataShared;

public class SlaveList extends ListFragment {
    
    private MasterActivityCallback  masterCallback;
    private ArrayList<Student>      slaves;
    private PermissionsArrayAdapter mAdapter;
    private Dialog                  dialog;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        slaves = DataShared.getInstance().getListOnline();
        
    }
    
    @Override
    public void onDetach() {
    
        super.onDetach();
        masterCallback = null;
    }
    
}
