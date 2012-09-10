
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
import android.widget.GridView;
import android.widget.TextView;

import com.kii.launcher.R;
import com.kii.launcher.drawer.util.IDrawerFragment;

import java.io.File;
import java.util.HashMap;

public class PicturesFragment extends Fragment implements IDrawerFragment {
    
    private static PicturesFileManagerAdapter  mAdapter;
    private static PositionHelper              helper = null;
    private static final HashMap<File, Bitmap> map    = new HashMap<File, Bitmap>();
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures");
        
        if (helper == null) {
            helper = new PositionHelper(root, root);
        }
        
        View rootView = inflater.inflate(R.layout.fragment_kii_drawer_pictures, container, false);
        TextView path = (TextView) rootView.findViewById(R.id.fragment_kii_drawer_pictures_path);
        
        GridView grid = (GridView) rootView.findViewById(R.id.fragment_kii_drawer_pictures_grid);
        grid.setAdapter(mAdapter = new PicturesFileManagerAdapter(getActivity(), helper, path, map));
        grid.setEmptyView(rootView.findViewById(R.id.fragment_kii_drawer_pictures_grid_empty));
        
        return rootView;
    }
    
    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
    
        inflater.inflate(R.menu.pictures_fragment_menu, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    
        switch (item.getItemId()) {
            case R.id.pictures_fragment_menu:
                mAdapter.goUp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onPrepareOptionsMenu( Menu menu ) {
    
        if (mAdapter == null || mAdapter.isRoot()) {
            menu.findItem(R.id.pictures_fragment_menu).setVisible(false);
        } else {
            menu.findItem(R.id.pictures_fragment_menu).setVisible(true);
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
    
        return R.string.drawer_menu_pictures;
    }
    
    @Override
    public int getIconResource() {
    
        return R.drawable.ic_drawer_pictures;
    }
}
