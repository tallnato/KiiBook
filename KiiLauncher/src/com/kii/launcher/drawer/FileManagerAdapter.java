
package com.kii.launcher.drawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.launcher.R;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileManagerAdapter extends ArrayAdapter<File> {
    
    private final File       root;
    private File             currentPath;
    private final FileFilter fileFilter;
    
    public FileManagerAdapter( Context context, File root, FileFilter fileFilter ) {
    
        super(context, R.layout.fragment_kii_drawer_menu_item, getFiles(root, fileFilter));
        
        this.root = root;
        currentPath = root;
        this.fileFilter = fileFilter;
    }
    
    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
    
        View view = convertView;
        TextView text;
        final ImageView icon;
        
        if (view == null) {
            view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.fragment_kii_drawer_menu_item, parent, false);
        }
        
        text = (TextView) view.findViewById(R.id.fragment_kii_drawer_menu_item_text);
        icon = (ImageView) view.findViewById(R.id.fragment_kii_drawer_menu_item_icon);
        
        final File item = getItem(position);
        String extension = MimeTypeMap.getFileExtensionFromUrl(item.getAbsolutePath()).toLowerCase();
        String mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        
        if (item.equals(currentPath.getParentFile())) {
            icon.setImageResource(android.R.drawable.ic_media_previous);
            text.setText(R.string.drawer_menu_manager_back);
        } else {
            
            if (item.isDirectory()) {
                icon.setImageResource(R.drawable.ic_drawer_folder);
            } else if (mimetype != null && mimetype.contains("image")) {
                new Thread(new Runnable() {
                    
                    @Override
                    public void run() {
                    
                        Bitmap b = getPreview(item.toURI());
                        if (b != null) {
                            icon.setImageBitmap(b);
                        } else {
                            icon.setImageResource(R.drawable.ic_drawer_music);
                        }
                    }
                }).start();
            } else if (mimetype != null && mimetype.contains("video")) {
                Bitmap bMap = ThumbnailUtils.createVideoThumbnail(item.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                icon.setImageBitmap(bMap);
                
            } else {
                icon.setImageResource(android.R.drawable.ic_input_get);
            }
            
            text.setText(item.getName());
        }
        view.setTag(item);
        view.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick( View v ) {
            
                File item = (File) v.getTag();
                
                if (item.isDirectory()) {
                    currentPath = item;
                    clear();
                    
                    if (!currentPath.equals(root)) {
                        add(currentPath.getParentFile());
                    }
                    addAll(getFiles(currentPath, fileFilter));
                    notifyDataSetChanged();
                    
                    Toast.makeText(getContext(), currentPath.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    
                    // TextView tv = (TextView)
                    // v.getRootView().findViewById(R.id.fragment_kii_drawer_pictures_path);
                    // tv.setText("cenas");
                    
                    return;
                }
                
                String extension = MimeTypeMap.getFileExtensionFromUrl(item.getAbsolutePath()).toLowerCase();
                String mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                
                if (mimetype != null) {
                    Intent i = new Intent();
                    i.setAction(android.content.Intent.ACTION_VIEW);
                    i.setDataAndType(Uri.fromFile(item), mimetype);
                    getContext().startActivity(i);
                    
                } else {
                    Toast.makeText(getContext(), "não dá pá..." + mimetype, Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        return view;
    }
    
    private Bitmap getPreview( URI uri ) {
    
        File image = new File(uri);
        
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getPath(), bounds);
        if (bounds.outWidth == -1 || bounds.outHeight == -1) {
            return null;
        }
        
        int originalSize = bounds.outHeight > bounds.outWidth ? bounds.outHeight : bounds.outWidth;
        
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = originalSize / 50;
        return BitmapFactory.decodeFile(image.getPath(), opts);
    }
    
    private static List<File> getFiles( File f, FileFilter fileFilter ) {
    
        if (f.isDirectory()) {
            
            List<File> list = new ArrayList<File>(Arrays.asList(f.listFiles(fileFilter)));
            Collections.sort(list, new Comparator<File>() {
                
                @Override
                public int compare( File f1, File f2 ) {
                
                    return f1.getName().compareToIgnoreCase(f2.getName());
                }
            });
            
            return list;
        } else {
            return new ArrayList<File>();
        }
    }
}
