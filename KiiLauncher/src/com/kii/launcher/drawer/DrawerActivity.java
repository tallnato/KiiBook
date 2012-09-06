
package com.kii.launcher.drawer;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.kii.launcher.PackagePermissions;
import com.kii.launcher.R;
import com.kii.launcher.drawer.MenuFragment.ListCallbacks;
import com.kii.launcher.drawer.favorites.FavoriteItem;
import com.kii.launcher.drawer.util.MenuItem;

public class DrawerActivity extends FragmentActivity implements ListCallbacks {
    
    private static final String MENU_POSITION = "Menu:Position";
    private MenuItem            currentMenu   = null;
    private MenuAdapter         menuAdapter;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kii_drawer);
        
        ListFragment list = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.activity_kii_launcher_drawer_list);
        menuAdapter = (MenuAdapter) list.getListAdapter();
        
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (savedInstanceState != null && savedInstanceState.containsKey(MENU_POSITION)) {
            currentMenu = (MenuItem) list.getListAdapter().getItem(savedInstanceState.getInt(MENU_POSITION, 0));
            menuAdapter.setPosition(savedInstanceState.getInt(MENU_POSITION, 0));
            
            if (currentMenu.getFragment().isDetached()) {
                ft.replace(R.id.activity_kii_launcher_drawer_container, currentMenu.getFragment());
            }
        } else {
            currentMenu = (MenuItem) list.getListAdapter().getItem(0);
            menuAdapter.setPosition(0);
            ft.replace(R.id.activity_kii_launcher_drawer_container, currentMenu.getFragment());
        }
        ft.commit();
        
        final View view = findViewById(R.id.activity_kii_launcher_drawer_container);
        view.setOnDragListener(new OnDragListener() {
            
            @Override
            public boolean onDrag( View v, DragEvent event ) {
            
                final Drawable background = v.getBackground();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        
                        if (currentMenu.getFragment() instanceof FavoritesFragment) {
                            currentMenu.getFragment().getView().setBackgroundResource(android.R.color.holo_orange_light);
                        }
                        
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        if (currentMenu.getFragment() instanceof FavoritesFragment) {
                            currentMenu.getFragment().getView().setBackground(background);
                        }
                        break;
                    case DragEvent.ACTION_DROP:
                        if (currentMenu.getFragment() instanceof FavoritesFragment) {
                            
                            FavoritesFragment ff = (FavoritesFragment) currentMenu.getFragment();
                            ff.getView().setBackground(null);
                            ff.dropView(event.getLocalState());
                        }
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        
        ImageView trash = (ImageView) findViewById(R.id.fragment_kii_drawer_menu_trash);
        trash.setOnDragListener(new OnDragListener() {
            
            @Override
            public boolean onDrag( View v, DragEvent event ) {
            
                ImageView trash = (ImageView) v;
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        
                        trash.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_down));
                        
                        trash.setImageResource(R.drawable.ic_trash_close);
                        break;
                    
                    case DragEvent.ACTION_DRAG_ENTERED:
                        trash.setImageResource(R.drawable.ic_trash_open);
                        break;
                    
                    case DragEvent.ACTION_DRAG_EXITED:
                        trash.setImageResource(R.drawable.ic_trash_close);
                        break;
                    
                    case DragEvent.ACTION_DROP:
                        if (event.getLocalState() instanceof PackagePermissions) {
                            PackagePermissions pp = (PackagePermissions) event.getLocalState();
                            Uri packageUri = Uri.parse("package:" + pp.getPackage());
                            Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
                            startActivity(uninstallIntent);
                        } else if (event.getLocalState() instanceof FavoriteItem && currentMenu.getFragment() instanceof FavoritesFragment) {
                            
                            FavoritesFragment ff = (FavoritesFragment) currentMenu.getFragment();
                            ff.removeFavorite((FavoriteItem) event.getLocalState());
                        } else {
                            Toast.makeText(getApplicationContext(), "unkown shit... " + event.getLocalState().getClass(),
                                                            Toast.LENGTH_SHORT).show();
                        }
                        
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        trash.setImageDrawable(null);
                        menuAdapter.notifyDataSetChanged();
                    default:
                        break;
                }
                return true;
            }
        });
    }
    
    @Override
    public void onAttachFragment( Fragment fragment ) {
    
        super.onAttachFragment(fragment);
    }
    
    @Override
    public void onItemSelected( MenuItem menuItem ) {
    
        if (currentMenu == menuItem) {
            return;
        }
        
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (currentMenu.getNum() < menuItem.getNum()) {
            ft.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);
        } else {
            ft.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_up);
        }
        
        if (menuItem.getFragment() instanceof LibraryFragment) {
            ((LibraryFragment) menuItem.getFragment()).setPage(0);
        }
        
        ft.replace(R.id.activity_kii_launcher_drawer_container, menuItem.getFragment());
        ft.commit();
        
        currentMenu = menuItem;
    }
    
    @Override
    public void onSaveInstanceState( Bundle savedInstanceState ) {
    
        savedInstanceState.putInt(MENU_POSITION, menuAdapter.getCurrentPosition());
        super.onSaveInstanceState(savedInstanceState);
    }
    
    @Override
    public void closeDrawer() {
    
        finish();
    }
}
