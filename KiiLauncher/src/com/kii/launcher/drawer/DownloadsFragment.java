
package com.kii.launcher.drawer;

import android.graphics.Bitmap;
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
import java.util.HashMap;

public class DownloadsFragment extends Fragment implements IDrawerFragment {
    
    private static DownloadsFileManagerAdapter mAdapter;
    private static PositionHelper              helper = null;
    private static final HashMap<File, Bitmap> map    = new HashMap<File, Bitmap>();
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        
        setHasOptionsMenu(true);
        
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download");
        
        if (helper == null) {
            helper = new PositionHelper(root, root);
        }
        
        View rootView = inflater.inflate(R.layout.fragment_kii_drawer_downloads, container, false);
        TextView path = (TextView) rootView.findViewById(R.id.fragment_kii_drawer_downloads_path);
        
        ListView grid = (ListView) rootView.findViewById(R.id.fragment_kii_drawer_downloads_list);
        grid.setAdapter(mAdapter = new DownloadsFileManagerAdapter(getActivity(), helper, path, map));
        
        return rootView;
    }
    
    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
    
        inflater.inflate(R.menu.downloads_fragment_menu, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    
        switch (item.getItemId()) {
            case R.id.downloads_fragment_menu:
                mAdapter.goUp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onPrepareOptionsMenu( Menu menu ) {
    
        if (mAdapter == null || mAdapter.isRoot()) {
            menu.findItem(R.id.downloads_fragment_menu).setVisible(false);
        } else {
            menu.findItem(R.id.downloads_fragment_menu).setVisible(true);
        }
    }
    
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
    
        return R.string.drawer_menu_downloads;
    }
    
    @Override
    public int getIconResource() {
    
        return R.drawable.ic_drawer_downloads;
    }
}
