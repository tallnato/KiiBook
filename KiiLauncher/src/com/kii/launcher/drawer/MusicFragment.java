
package com.kii.launcher.drawer;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.kii.launcher.R;
import com.kii.launcher.drawer.util.IDrawerFragment;

import java.io.File;

public class MusicFragment extends Fragment implements IDrawerFragment {
    
    private static MusicFileManagerAdapter mAdapter;
    private static PositionHelper          helper = null;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        
        setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music");
        if (!root.exists()) {
            root.mkdirs();
        }
        
        if (helper == null) {
            helper = new PositionHelper(root, root);
        }
        
        View rootView = inflater.inflate(R.layout.fragment_kii_drawer_music, container, false);
        TextView path = (TextView) rootView.findViewById(R.id.fragment_kii_drawer_music_path);
        
        ListView list = (ListView) rootView.findViewById(R.id.fragment_kii_drawer_music_list);
        list.setAdapter(mAdapter = new MusicFileManagerAdapter(getActivity(), helper, path));
        list.setEmptyView(rootView.findViewById(R.id.fragment_kii_drawer_music_list_empty));
        
        return rootView;
    }
    
    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
    
        inflater.inflate(R.menu.videos_fragment_menu, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    
        switch (item.getItemId()) {
            case R.id.videos_fragment_menu:
                mAdapter.goUp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onPrepareOptionsMenu( Menu menu ) {
    
        if (mAdapter == null || mAdapter.isRoot()) {
            menu.findItem(R.id.videos_fragment_menu).setVisible(false);
        } else {
            menu.findItem(R.id.videos_fragment_menu).setVisible(true);
        }
    }
    
    @Override
    public boolean goUp() {
    
        if (mAdapter == null || mAdapter.isRoot()) {
            return false;
        } else {
            mAdapter.goUp();
            return true;
        }
    }
    
    @Override
    public int getNameResource() {
    
        return R.string.drawer_menu_music;
    }
    
    @Override
    public int getIconResource() {
    
        return R.drawable.ic_drawer_music;
    }
}
