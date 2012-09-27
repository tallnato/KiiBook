
package kii.kiibook.managerclass.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import kii.kiibook.managerclass.database.DataShared;
import kii.kiibook.managerclass.utils.FileListView;
import kii.kiibook.teacher.R;
import kii.kiibook.teacher.adapters.AdapterListView;

public class DocFragment extends Fragment implements OnItemClickListener {
    
    public static final String      TAG      = "DocFragment";
    public static final String      FRAG     = "fragment";
    
    private final CharSequence[]    items    = { "Partilhar", "Eliminar", "Cancelar" };
    
    private View                    view;
    private ArrayList<FileListView> files;
    private ListView                listView;
    private File                    currentDir;
    private AdapterListView         adapter;
    private int                     classId;
    private String                  className;
    private final String            title    = "Documentos - ";
    private TextView                titleView;
    private boolean                 portrait = false;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView( final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.doc_frame, container, false);
        
        setHasOptionsMenu(true);
        registerForContextMenu(container);
        
        titleView = (TextView) view.findViewById(R.id.doc_title);
        classId = getArguments().getInt(FRAG);
        className = DataShared.getInstance().getClasses().get(classId).getName();
        
        checkExternalMedia();
        checkFilesList(null);
        
        adapter = new AdapterListView(getActivity(), files);
        listView = (ListView) view.findViewById(R.id.mylist_doc);
        
        // Assign adapter to ListView
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(this);
        
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            portrait = true;
        }
        return view;
    }
    
    private void showCustomDialog( final File file ) {
    
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ficheiro");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            
            public void onClick( DialogInterface dialog, int item ) {
            
                switch (item) {
                    case 0:
                        shareFile(file);
                        break;
                    case 1:
                        removeFile(file);
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    private void removeFile( final File file ) {
    
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Tem a certeza que pretende eliminar este ficheiro?").setCancelable(false);
        
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            
            public void onClick( DialogInterface dialog, int id ) {
            
                if (file.delete()) {
                    Log.d(TAG, "File deleted!");
                    checkFilesList(currentDir);
                    adapter = new AdapterListView(getActivity(), files);
                    listView.setAdapter(adapter);
                    
                } else {
                    Log.d(TAG, "File not deleted! :(");
                }
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            
            public void onClick( DialogInterface dialog, int id ) {
            
                dialog.cancel();
            }
        });
        
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    private void shareFile( final File file ) {
    
        Toast.makeText(getActivity(), "Share File", Toast.LENGTH_LONG).show();
        
    }
    
    private void checkExternalMedia() {
    
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        
        String state = Environment.getExternalStorageState();
        
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or writeradio
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        Log.d(TAG, "\n\nExternal Media: readable=" + mExternalStorageAvailable + " writable=" + mExternalStorageWriteable);
    }
    
    private void checkFilesList( File path ) {
    
        files = new ArrayList<FileListView>();
        File dir;
        
        if (path == null) {
            File root = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/KiiDocs/");
            Log.d(TAG, "\nExternal file system root: " + root);
            
            dir = new File(root.getAbsolutePath() + "/" + className); // +
                                                                      // "/KiiDocs"
            if (!dir.mkdirs()) {
                Log.d(TAG, "mkdir failed");
            }
            
        } else {
            Log.d(TAG, "change path");
            dir = new File(path.getAbsolutePath());
            dir.mkdirs();
        }
        currentDir = new File(dir.getAbsolutePath());
        
        File[] listFiles = currentDir.listFiles();
        
        for (File file : listFiles) {
            if (!file.isDirectory()) {
                files.add(new FileListView(file, R.drawable.document, false));
            } else {
                files.add(new FileListView(file, R.drawable.folder, true));
            }
        }
        
        titleView.setText(title + "KiiDocs/" + className);
    }
    
    public void onItemClick( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
    
        Log.d(TAG, "onItemClick");
        
        if (!files.get(arg2).isDirectory()) {
            showCustomDialog(files.get(arg2).getFile());
        } else {
            checkFilesList(files.get(arg2).getFile());
            adapter = new AdapterListView(getActivity(), files);
            listView.setAdapter(adapter);
            Log.d(TAG, "is a Directory");
        }
    }
    
    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
    
        if (portrait) {
            
            inflater.inflate(R.menu.doc_return_portrait, menu);
        } else {
            
            inflater.inflate(R.menu.doc_return, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    
        if (item.getItemId() == R.id.menu_doc_return) {
            Log.d(TAG, "onOptionsItemSelected - get Return");
            Log.d(TAG, currentDir.getParentFile().getAbsolutePath());
            if (!currentDir.getParentFile().getAbsolutePath().equals("/mnt")) {
                checkFilesList(currentDir.getParentFile());
                
                adapter = new AdapterListView(getActivity(), files);
                listView.setAdapter(adapter);
                return true;
            }
            
        } else {
            return super.onOptionsItemSelected(item);
        }
        return false;
    }
    
}
