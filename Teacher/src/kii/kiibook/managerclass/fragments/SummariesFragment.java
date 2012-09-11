
package kii.kiibook.managerclass.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import kii.kiibook.managerclass.adapters.AdapterSummary;
import kii.kiibook.managerclass.database.DataShared;
import kii.kiibook.teacher.R;

public class SummariesFragment extends Fragment {
    
    private View           mRoot;
    private AdapterSummary adapter;
    private ListView       list;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        setHasOptionsMenu(true);
        
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.fragment_summary, container, false);
        
        Log.d("", "onCreateView");
        adapter = new AdapterSummary(getActivity(), R.layout.layout_item_list_summary, DataShared.getInstance().getListSummaries());
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