
package com.kii.launcher.drawer;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;

import com.kii.launcher.R;
import com.kii.launcher.drawer.util.IDrawerFragment;

import java.io.File;

public class DownloadsFragment extends ListFragment implements IDrawerFragment {
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download");
        
        setListAdapter(new FileManagerAdapter(getActivity(), root, null));
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
