
package kii.kiibook.ParentalControl;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import kii.kiibook.Student.R;
import kii.kiibook.Student.database.DataShared;

public class PermissionsExternal extends ListActivity implements ParentalConstants {
    
    private PermissionsArrayAdapter mAdapter;
    private ServiceCon              mConnection;
    private final int               PASSWORD_DIALOG = 0x01;
    
    @Override
    public void onCreate( Bundle icicle ) {
    
        super.onCreate(icicle);
        setContentView(R.layout.permissions);
        
        mConnection = new ServiceCon();
        
        startActivityForResult(new Intent(getApplicationContext(), PasswordDialog.class), PASSWORD_DIALOG);
        
        Set<String> blocked = DataShared.getInstance().getBlockedApps();
        
        mAdapter = new PermissionsArrayAdapter(this, R.layout.fragment_permissions_item_list, getPackages(), blocked);
        setListAdapter(mAdapter);
        
        registerForContextMenu(getListView());
        
        Intent i = new Intent("kii.kiibook.ParentalControl.FalconEyeService");
        startService(i);
        
        bindService(i, mConnection, Context.BIND_AUTO_CREATE);
    }
    
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
    
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_fragment_premissions, menu);
        
        SearchView searchView = (SearchView) menu.findItem(R.id.fragment_permissions_menu_search).getActionView();
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            
            public boolean onQueryTextSubmit( String query ) {
            
                mAdapter.searchPackages(query);
                return true;
            }
            
            public boolean onQueryTextChange( String newText ) {
            
                mAdapter.searchPackages(newText);
                return true;
            }
        });
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    
        switch (item.getItemId()) {
            case R.id.fragment_permissions_menu_save:
                
                DataShared.getInstance().setBlockedApps(mAdapter.getBlockedApps());
                
                Messenger mService = mConnection.getService();
                
                try {
                    Message msg = Message.obtain(null, FalconEyeService.MSG_UPDATE_BLOCKED_APPS);
                    mService.send(msg);
                    
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
                
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
    
        if (requestCode == PASSWORD_DIALOG) {
            
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }
    
    private ArrayList<PackagePermissions> getPackages() {
    
        ArrayList<PackagePermissions> apps = getInstalledApps(false);
        
        return apps;
    }
    
    private ArrayList<PackagePermissions> getInstalledApps( boolean getSysPackages ) {
    
        ArrayList<PackagePermissions> res = new ArrayList<PackagePermissions>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if (!getSysPackages && p.versionName == null) {
                continue;
            }
            PackagePermissions newInfo = new PackagePermissions(p.applicationInfo.loadLabel(getPackageManager()).toString(), p.packageName,
                                            p.applicationInfo.loadIcon(getPackageManager()));
            
            res.add(newInfo);
        }
        return res;
    }
    
    @Override
    public void onDestroy() {
    
        super.onDestroy();
        
        unbindService(mConnection);
        
        // Intent i = new Intent("falconeye.FalconEyeService");
        // getActivity().stopService(i);
    }
    
}
