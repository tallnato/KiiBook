
package kii.kiibook.managerclass.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import objects.Student;
import util.SlaveStatus;

import java.util.List;

import kii.kiibook.managerclass.PermissionsArrayAdapter;
import kii.kiibook.teacher.R;

public class SlaveAdaptorAll extends ArrayAdapter<Student> implements OnClickListener {
    
    private final int               layoutResourceId;
    private final Context           context;
    private final List<Student>     slaves;
    private Student                 slave;
    private Dialog                  dialog;
    private PermissionsArrayAdapter mAdapter;
    
    public SlaveAdaptorAll( Context context, int viewResourceId, List<Student> objects ) {
    
        super(context, viewResourceId, objects);
        
        layoutResourceId = viewResourceId;
        this.context = context;
        slaves = objects;
    }
    
    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
    
        View row = convertView;
        
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }
        
        slave = getItem(position);
        
        TextView name = (TextView) row.findViewById(R.id.classbook_item_name);
        TextView status = (TextView) row.findViewById(R.id.classbook_item_status);
        CheckBox check = (CheckBox) row.findViewById(R.id.checkBox_assiduity);
        ImageView image = (ImageView) row.findViewById(R.id.imageView_classmode);
        image.setOnClickListener(this);
        
        name.setText(slave.getName());
        if (slave.getStatus().equals(SlaveStatus.CONNECTED)) {
            Toast.makeText(getContext(), "Online carai", Toast.LENGTH_SHORT).show();
            status.setText("Online");
            status.setTextColor(Color.GREEN);
            check.setChecked(true);
        } else {
            status.setText("Offline");
            status.setTextColor(Color.RED);
            check.setChecked(false);
        }
        
        return row;
    }
    
    @Override
    public void add( Student slave ) {
    
        super.add(slave);
        
    }
    
    public Student getSlave( String ip ) {
    
        for (Student s : slaves) {
            if (s.getIpAdrress().equals(ip)) {
                return s;
            }
        }
        return null;
    }
    
    public void onClick( View v ) {
    
        showDialogProfile(slave);
        
    }
    
    private void showDialogProfile( Student student ) {
    
        // set up dialog
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.profile_student);
        dialog.setTitle(student.getName());
        dialog.setCancelable(true);
        
        // now that the dialog is set up, it's time to show it
        dialog.show();
    }
    
}
