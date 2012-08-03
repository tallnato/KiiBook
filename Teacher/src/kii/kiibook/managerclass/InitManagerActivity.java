package kii.kiibook.managerclass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;


import java.util.ArrayList;
import java.util.Iterator;

import kii.kiibook.managerclass.TestObjectsResources.ObjectCreator;
import kii.kiibook.managerclass.objects.ClassPeople;
import kii.kiibook.teacher.R;
import kii.kiibook.teacher.fragments.ClassesFragment;

public class InitManagerActivity extends Activity implements OnKeyListener {
    
    private AlertDialog alert;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_init_dialog_class);
        
        ArrayList<ClassPeople> list = ObjectCreator.getInstance().getClasses();
        Iterator<ClassPeople> it = list.iterator();
        String[] items = new String[list.size()];
        int pointer = 0;
        while (it.hasNext()) {
            
            items[pointer] = it.next().getName();
            pointer++;
        }
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(" Turma");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            
            public void onClick( DialogInterface dialog, int item ) {
            
                Bundle b = new Bundle();
                b.putInt(ClassesFragment.CLASS, item);
                Intent myIntent = new Intent(InitManagerActivity.this, ManagerClassActivity.class);
                myIntent.putExtras(b);
                startActivity(myIntent);
                finish();
            }
        });
        alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
        alert.setOnKeyListener(this);
        
    }
    
    public boolean onKey( DialogInterface dialog, int keyCode, KeyEvent event ) {
    
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            return true;
        }
        return false;
    }
}
