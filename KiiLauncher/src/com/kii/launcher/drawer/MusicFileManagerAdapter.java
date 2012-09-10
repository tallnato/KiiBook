
package com.kii.launcher.drawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kii.launcher.R;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Comparator;

public class MusicFileManagerAdapter extends ArrayAdapter<File> {
    
    private final PositionHelper helper;
    private final TextView       path;
    private final String         rootName;
    
    public MusicFileManagerAdapter( Context context, PositionHelper helper, TextView path ) {
    
        super(context, R.layout.fragment_kii_drawer_menu_item, new ArrayList<File>());
        
        this.helper = helper;
        this.path = path;
        rootName = context.getResources().getString(R.string.drawer_menu_music);
        
        addAll(helper.getCurrentPath().listFiles());
        sort(comparator);
        
        path.setText(getCurrentPath());
    }
    
    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
    
        View view = convertView;
        TextView text;
        final ImageView icon;
        
        if (view == null) {
            view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.fragment_kii_drawer_music_item, parent, false);
        }
        
        text = (TextView) view.findViewById(R.id.fragment_kii_drawer_music_item_text);
        icon = (ImageView) view.findViewById(R.id.fragment_kii_drawer_music_item_icon);
        
        final File item = getItem(position);
        
        if (item.isDirectory()) {
            icon.setImageResource(R.drawable.ic_drawer_folder_small);
        } else {
            icon.setImageResource(R.drawable.ic_drawer_music);
        }
        
        text.setText(item.getName());
        
        view.setTag(item);
        view.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                File item = (File) v.getTag();
                
                if (item.isDirectory()) {
                    helper.setCurrentPath(item);
                    
                    clear();
                    addAll(helper.getCurrentPath().listFiles(ff));
                    sort(comparator);
                    path.setText(getCurrentPath());
                    
                    notifyDataSetChanged();
                    
                    ((Activity) getContext()).invalidateOptionsMenu();
                    
                    return;
                }
                
                Intent i = new Intent();
                i.setAction(android.content.Intent.ACTION_VIEW);
                i.setDataAndType(Uri.fromFile(item), "music/*");
                getContext().startActivity(i);
            }
        });
        
        return view;
    }
    
    private String getCurrentPath() {
    
        String result = helper.getCurrentPath().getAbsolutePath();
        result = result.replace(helper.getRoot().getAbsolutePath(), rootName);
        result = result.replace("/", " > ");
        
        return result;
    }
    
    public boolean isRoot() {
    
        return helper.equal();
    }
    
    public void goUp() {
    
        if (!isRoot()) {
            helper.setCurrentPath(helper.getCurrentPath().getParentFile());
            clear();
            addAll(helper.getCurrentPath().listFiles());
            sort(comparator);
            path.setText(getCurrentPath());
            
            notifyDataSetChanged();
            
            ((Activity) getContext()).invalidateOptionsMenu();
        }
    }
    
    private static final FileFilter       ff         = new FileFilter() {
                                                         
                                                         @Override
                                                         public boolean accept( File pathname ) {
                                                         
                                                             String extension, mimeType;
                                                             
                                                             if (pathname.isDirectory()) {
                                                                 return true;
                                                             }
                                                             extension = MimeTypeMap.getFileExtensionFromUrl(pathname.getAbsolutePath())
                                                                                             .toLowerCase();
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
    
    private static final Comparator<File> comparator = new Comparator<File>() {
                                                         
                                                         @Override
                                                         public int compare( File f1, File f2 ) {
                                                         
                                                             return f1.getName().compareToIgnoreCase(f2.getName());
                                                         }
                                                     };
}
