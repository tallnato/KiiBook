
package kii.kiibook.Student.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import kii.kiibook.Student.R;
import libslidemenu.SlideMenu;
import libslidemenu.SlideMenu.SlideMenuItem;
import libslidemenu.SlideMenuInterface.OnSlideMenuItemClickListener;

public class ProfileAcademicFragment extends Fragment implements OnSlideMenuItemClickListener {
    
    private SlideMenu        slidemenu;
    private static int       MYITEMID = 42;
    private static ArrayList itemsIds;
    
    private View             view;
    private View             mActionBarView;
    private String[]         classType;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        view = inflater.inflate(R.layout.fragment_classes, container, false);
        registerForContextMenu(view);
        
        setHasOptionsMenu(true);
        
        slidemenu = (SlideMenu) view.findViewById(R.id.slideMenu);
        slidemenu.init(getActivity(), this, 333, getResources().getDisplayMetrics());
        
        // set optional header image
        slidemenu.setHeaderImage(R.drawable.ic_launcher);
        
        // this demonstrates how to dynamically add menu items
        SlideMenuItem item;
        // TODO here
        classType = objects.ObjectCreator.getClassType();
        
        for (String mClass : classType) {
            item = new SlideMenuItem();
            item.id = MYITEMID++;
            item.icon = getResources().getDrawable(R.drawable.ic_launcher);
            item.label = mClass;
            slidemenu.addMenuItem(item);
        }
        
        // connect the fallback button in case there is no ActionBar
        Button b = (Button) view.findViewById(R.id.buttonMenu);
        b.setOnClickListener(new OnClickListener() {
            
            public void onClick( View v ) {
            
                slidemenu.show();
            }
        });
        
        return view;
    }
    
    public void onSlideMenuItemClick( int itemId ) {
    
        TextView txt = (TextView) getActivity().findViewById(R.id.texts);
        txt.setText(String.valueOf(itemId));
    }
    
}
