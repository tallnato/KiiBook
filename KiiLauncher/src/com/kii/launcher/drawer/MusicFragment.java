
package com.kii.launcher.drawer;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.webkit.MimeTypeMap;

import com.kii.launcher.R;
import com.kii.launcher.drawer.util.IDrawerFragment;

import java.io.File;
import java.io.FileFilter;

public class MusicFragment extends ListFragment implements IDrawerFragment {
    
    private static final FileFilter ff = new FileFilter() {
                                           
                                           @Override
                                           public boolean accept( File pathname ) {
                                           
                                               String extension, mimeType;
                                               
                                               if (pathname.isDirectory()) {
                                                   return true;
                                               }
                                               extension = MimeTypeMap.getFileExtensionFromUrl(pathname.getAbsolutePath()).toLowerCase();
                                               if (extension == null) {
                                                   return false;
                                               }
                                               
                                               mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                                               
                                               if (mimeType == null) {
                                                   return false;
                                               }
                                               
                                               return mimeType.contains("audio");
                                               
                                           }
                                       };
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music");
        
        setListAdapter(new FileManagerAdapter(getActivity(), root, null));
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
