
package com.kii.launcher.drawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.kii.launcher.R;
import com.kii.launcher.drawer.util.IDrawerFragment;
import com.kii.launcher.drawer.util.LibraryItem;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment implements IDrawerFragment {
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        GridView list = (GridView) inflater.inflate(R.layout.fragment_kii_drawer_library, container, false);
        List<LibraryItem> books = new ArrayList<LibraryItem>();
        books.add(new LibraryItem("Sr dos azeites", ""));
        books.add(new LibraryItem("Sermão do gajo aos peixes", ""));
        books.add(new LibraryItem("Os camões", ""));
        books.add(new LibraryItem("Lusidias, os Cavalos", ""));
        books.add(new LibraryItem("Harry Potter e o calhau filosofal", ""));
        books.add(new LibraryItem("Sr dos azeites", ""));
        books.add(new LibraryItem("Sermão do gajo aos peixes", ""));
        books.add(new LibraryItem("Os camões", ""));
        books.add(new LibraryItem("Lusidias, os Cavalos", ""));
        books.add(new LibraryItem("Harry Potter e o calhau filosofal", ""));
        books.add(new LibraryItem("Sr dos azeites", ""));
        books.add(new LibraryItem("Sermão do gajo aos peixes", ""));
        books.add(new LibraryItem("Os camões", ""));
        books.add(new LibraryItem("Lusidias, os Cavalos", ""));
        books.add(new LibraryItem("Harry Potter e o calhau filosofal", ""));
        books.add(new LibraryItem("Sr dos azeites", ""));
        books.add(new LibraryItem("Sermão do gajo aos peixes", ""));
        books.add(new LibraryItem("Os camões", ""));
        books.add(new LibraryItem("Lusidias, os Cavalos", ""));
        books.add(new LibraryItem("Harry Potter e o calhau filosofal", ""));
        
        list.setAdapter(new LibraryAdapter(getActivity(), books));
        
        return list;
    }
    
    @Override
    public int getNameResource() {
    
        return R.string.drawer_menu_library;
    }
    
    @Override
    public int getIconResource() {
    
        return R.drawable.ic_drawer_library;
    }
}
