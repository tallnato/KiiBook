
package kii.kiibook.Student.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import kii.kiibook.Student.R;
import kii.kiibook.Student.adapters_items.AdapterListViewStudents;
import kii.kiibook.Student.adapters_items.ItemListViewStudent;

public class FriendsFragment extends Fragment {
    
    private View view;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        view = inflater.inflate(R.layout.fragment_friends, container, false);
        
        ListView mList = (ListView) view.findViewById(R.id.list_friends);
        // Iterator<Student> it =
        // DataShared.getInstance().getListFriendsOnline().iterator();
        ArrayList<ItemListViewStudent> items = new ArrayList<ItemListViewStudent>();
        /*
        while (it.hasNext()) {
            items.add(new ItemListViewStudent(it.next()));
        }*/
        
        mList.setAdapter(new AdapterListViewStudents(getActivity(), items));
        return view;
    }
}
