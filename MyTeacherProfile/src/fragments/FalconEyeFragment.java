
package fragments;

import TestObjectsResources.ObjectCreator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.myteacherprofile.R;
import com.example.myteacherprofile.adapters.AdapterFalconEyeView;

import objects.Student;
import utils.ItemFalconEye;

import java.util.ArrayList;
import java.util.Iterator;

public class FalconEyeFragment extends Fragment implements OnCheckedChangeListener, OnItemClickListener {
    
    public static final String TAG  = "FalconEyeFragment";
    public static final String FRAG = "fragment";
    
    private View               view;
    private ListView           listSlaves;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.falconeye_frame, container, false);
        Bundle bundle = getArguments();
        
        ToggleButton toggle = (ToggleButton) view.findViewById(R.id.toggleButton_falcon_eye);
        toggle.setOnCheckedChangeListener(this);
        
        ArrayList<ItemFalconEye> arrayList = new ArrayList<ItemFalconEye>();
        
        // Test Code
        Iterator<Student> it = ObjectCreator.getInstance().getClasses().get(bundle.getInt(ClassBookFragment.FRAG)).getStudents().iterator();
        
        while (it.hasNext()) {
            arrayList.add(new ItemFalconEye(0, it.next()));
        }
        // end test code
        
        AdapterFalconEyeView adapter = new AdapterFalconEyeView(getActivity(), arrayList);
        
        listSlaves = (ListView) view.findViewById(R.id.falconEye_list_slaves);
        listSlaves.setAdapter(adapter);
        listSlaves.setOnItemClickListener(this);
        
        return view;
    }
    
    @Override
    public void onCheckedChanged( CompoundButton button, boolean isChecked ) {
    
        if (isChecked) {
            // TODO do connections
            Toast.makeText(getActivity(), "Falcon Eye On", Toast.LENGTH_SHORT).show();
            listSlaves.setVisibility(View.VISIBLE);
        } else {
            // TODO turn off connections
            Toast.makeText(getActivity(), "Falcon Eye Off", Toast.LENGTH_SHORT).show();
            listSlaves.setVisibility(View.INVISIBLE);
        }
    }
    
    @Override
    public void onItemClick( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
    
        // TODO Auto-generated method stub
        
    }
    
}
