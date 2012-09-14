
package kii.kiibook.KiiClass.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import objects.Summary;

import java.util.ArrayList;
import java.util.Collections;

import kii.kiibook.KiiClass.AdapterSummary;
import kii.kiibook.Student.R;
import kii.kiibook.Student.database.DataShared;

public class SummariesFragment extends Fragment {
    
    private View           mRoot;
    private AdapterSummary adapter;
    private ListView       list;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        setHasOptionsMenu(true);
        
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.fragment_summary, container, false);
        
        ArrayList<Summary> arraylist = DataShared.getInstance().getListSummaries();
        Collections.reverse(arraylist);
        
        adapter = new AdapterSummary(getActivity(), R.layout.layout_item_list_summary, arraylist);
        list = (ListView) mRoot.findViewById(R.id.list_summaries);
        list.setAdapter(adapter);
        return mRoot;
    }
    
    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
    
        inflater.inflate(R.menu.refresh_list_summary, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    
        switch (item.getItemId()) {
            case R.id.menu_refresh_list:
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
}
