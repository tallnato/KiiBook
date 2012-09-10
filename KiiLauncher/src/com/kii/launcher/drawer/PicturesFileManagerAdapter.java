
package com.kii.launcher.drawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
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
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class PicturesFileManagerAdapter extends ArrayAdapter<File> {
    
    private final PositionHelper        helper;
    private final TextView              path;
    private final String                rootName;
    private final HashMap<File, Bitmap> map;
    private final File                  cameraPath;
    
    public PicturesFileManagerAdapter( Context context, PositionHelper helper, TextView path, HashMap<File, Bitmap> map ) {
    
        super(context, R.layout.fragment_kii_drawer_menu_item, new ArrayList<File>());
        
        this.helper = helper;
        this.path = path;
        this.map = map;
        rootName = context.getResources().getString(R.string.drawer_menu_pictures);
        
        cameraPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera");
        
        addAll(helper.getCurrentPath().listFiles(ff));
        addAll(cameraPath.listFiles(ff));
        sort(comparator);
        
        path.setText(getCurrentPath());
        
        new ImageGetter().execute();
    }
    
    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
    
        View view = convertView;
        TextView text;
        final ImageView icon;
        
        if (view == null) {
            view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.fragment_kii_drawer_pictures_item, parent, false);
        }
        
        text = (TextView) view.findViewById(R.id.fragment_kii_drawer_pictures_item_name);
        icon = (ImageView) view.findViewById(R.id.fragment_kii_drawer_pictures_item_image);
        
        final File item = getItem(position);
        
        if (item.isDirectory()) {
            icon.setImageResource(R.drawable.ic_drawer_folder);
        } else {
            if (map.containsKey(item)) {
                icon.setImageBitmap(map.get(item));
            } else {
                icon.setImageResource(R.drawable.ic_drawer_pictures_small);
            }
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
                    
                    new ImageGetter().execute();
                    notifyDataSetChanged();
                    
                    ((Activity) getContext()).invalidateOptionsMenu();
                    
                    return;
                }
                
                Intent i = new Intent();
                i.setAction(android.content.Intent.ACTION_VIEW);
                i.setDataAndType(Uri.fromFile(item), "image/*");
                getContext().startActivity(i);
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
        opts.inSampleSize = originalSize / 300;
        return BitmapFactory.decodeFile(image.getPath(), opts);
    }
    
    private String getCurrentPath() {
    
        String result = helper.getCurrentPath().getAbsolutePath();
        if (!result.contains(cameraPath.getAbsolutePath())) {
            result = result.replace(helper.getRoot().getAbsolutePath(), rootName);
            result = result.replace("/", " > ");
        } else {
            result = result.replace(cameraPath.getAbsolutePath(), rootName);
            result = result.replace("/", " > ");
        }
        
        return result;
    }
    
    public boolean isRoot() {
    
        return helper.equal();
    }
    
    public void goUp() {
    
        if (helper.getCurrentPath().getParentFile().equals(cameraPath)) {
            helper.setCurrentPath(helper.getRoot());
            clear();
            addAll(helper.getCurrentPath().listFiles(ff));
            addAll(cameraPath.listFiles(ff));
            sort(comparator);
            path.setText(getCurrentPath());
            
            new ImageGetter().execute();
            notifyDataSetChanged();
            
            ((Activity) getContext()).invalidateOptionsMenu();
        } else if (!isRoot()) {
            helper.setCurrentPath(helper.getCurrentPath().getParentFile());
            clear();
            addAll(helper.getCurrentPath().listFiles(ff));
            if (isRoot()) {
                addAll(cameraPath.listFiles(ff));
            }
            sort(comparator);
            path.setText(getCurrentPath());
            
            new ImageGetter().execute();
            notifyDataSetChanged();
            
            ((Activity) getContext()).invalidateOptionsMenu();
        }
    }
    
    private class ImageGetter extends AsyncTask<Void, Void, Void> {
        
        @Override
        protected Void doInBackground( Void... params ) {
        
            for (int i = 0; i < getCount(); i++) {
                File item = getItem(i);
                
                if (map.containsKey(item)) {
                    continue;
                }
                
                Bitmap b = getPreview(item.toURI());
                if (b != null) {
                    map.put(item, b);
                }
                publishProgress();
            }
            return null;
        }
        
        @Override
        public void onProgressUpdate( Void... params ) {
        
            notifyDataSetChanged();
        }
    }
    
    private static final Comparator<File> comparator = new Comparator<File>() {
                                                         
                                                         @Override
                                                         public int compare( File f1, File f2 ) {
                                                         
                                                             return f1.getName().compareToIgnoreCase(f2.getName());
                                                         }
                                                     };
    
    private static final FileFilter       ff         = new FileFilter() {
                                                         
                                                         @Override
                                                         public boolean accept( File pathname ) {
                                                         
                                                             String extension, mimeType;
                                                             
                                                             if (pathname.isDirectory()) {
                                                                 return true;
                                                             }
                                                             
                                                             if (pathname.isHidden()) {
                                                                 return false;
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
                                                             
                                                             return mimeType.contains("image");
                                                             
                                                         }
                                                     };
}
