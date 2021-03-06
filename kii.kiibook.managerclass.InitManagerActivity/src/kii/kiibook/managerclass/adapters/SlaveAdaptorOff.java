
package kii.kiibook.managerclass.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.util.Log;
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

import kii.kiibook.teacher.R;

public class SlaveAdaptorOff extends ArrayAdapter<Student> implements OnClickListener {
    
    private final int           layoutResourceId;
    private final Context       context;
    private final List<Student> objects;
    private Student             slave;
    
    public SlaveAdaptorOff( Context context, int viewResourceId, List<Student> objects ) {
    
        super(context, viewResourceId, objects);
        
        layoutResourceId = viewResourceId;
        this.context = context;
        this.objects = objects;
        
    }
    
    @Override
    public View getView( final int position, View convertView, ViewGroup parent ) {
    
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
        image.setOnClickListener(new OnClickListener() {
            
            public void onClick( View v ) {
            
                showDialogProfile(slave, position);
                
            }
        });
        image.setBackgroundResource(slave.getPic());
        check.setChecked(false);
        name.setText(slave.getName());
        if (slave.getStatus() == SlaveStatus.CONNECTED) {
            status.setText("Online");
            status.setTextColor(Color.GREEN);
        }
        
        return row;
    }
    
    @Override
    public void notifyDataSetChanged() {
    
        super.notifyDataSetChanged();
    }
    
    public Student getSlave( String ip ) {
    
        for (Student s : objects) {
            if (s.getIpAdrress().equals(ip)) {
                return s;
            }
        }
        return null;
    }
    
    public void onClick( View v ) {
    
        showDialogProfile(slave, 0);
        
    }
    
    private void showDialogProfile( Student student, int ind ) {
    
        Intent i;
        PackageManager manager = context.getPackageManager();
        
        i = manager.getLaunchIntentForPackage("kii.profile");
        
        if (i == null) {
            
            Toast.makeText(context.getApplicationContext(), "Perfil não instalado...", Toast.LENGTH_SHORT).show();
            
            return;
            
        }
        
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.putExtra("index", ind);
        Log.d("Index intent profile", "Ind: " + ind + "Std:" + student.toString());
        context.startActivity(i);
    }
}
