
package com.kii.launcher.drawer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.kii.launcher.R;
import com.kii.launcher.drawer.util.IDrawerFragment;
import com.kii.launcher.drawer.util.LibraryItem;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment implements IDrawerFragment {
    
    private int                 NUM_HORIZONTAL_BOOKS;
    private int                 NUM_VERTICAL_BOOKS;
    private int                 booksPerView;
    
    private ViewPager           mViewPager;
    private PageViewAdapter     mPagerAdapter;
    private CirclePageIndicator circleIndicator;
    
    private List<LibraryItem>   books;
    
    private static int          currentPos = 0;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
    
        super.onCreate(savedInstanceState);
        
        setHasOptionsMenu(true);
        
        NUM_HORIZONTAL_BOOKS = getResources().getInteger(R.integer.drawer_books_horizontal_count);
        NUM_VERTICAL_BOOKS = getResources().getInteger(R.integer.drawer_books_vertical_count);
        
        booksPerView = NUM_HORIZONTAL_BOOKS * NUM_VERTICAL_BOOKS;
        
        books = new ArrayList<LibraryItem>();
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/kiibooks");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        
        new BookImageRetriver().execute(folder.listFiles());
    }
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        View rootView = inflater.inflate(R.layout.fragment_kii_drawer_library, container, false);
        
        circleIndicator = (CirclePageIndicator) rootView.findViewById(R.id.fragment_kii_drawer_books_indicator);
        
        mPagerAdapter = new PageViewAdapter();
        mViewPager = (ViewPager) rootView.findViewById(R.id.fragment_kii_drawer_books_view_pager);
        
        mViewPager.setAdapter(mPagerAdapter);
        
        circleIndicator.setViewPager(mViewPager);
        circleIndicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            
            @Override
            public void onPageSelected( int position ) {
            
                currentPos = position;
            }
        });
        
        return rootView;
    }
    
    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
    
        inflater.inflate(R.menu.apps_fragment_menu, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    
        switch (item.getItemId()) {
            case R.id.apps_fragment_menu_kiimarket:
                
                Intent i;
                PackageManager manager = getActivity().getPackageManager();
                i = manager.getLaunchIntentForPackage("kii.kiibook.kiimarket");
                if (i == null) {
                    Toast.makeText(getActivity(), "Market not installed...", Toast.LENGTH_SHORT).show();
                    return true;
                }
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(i);
                
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private class BookImageRetriver extends AsyncTask<File, LibraryItem, Void> {
        
        @Override
        protected Void doInBackground( File... params ) {
        
            for (File f : params) {
                if (!f.isDirectory()) {
                    continue;
                }
                
                File image = new File(f.getAbsolutePath() + "/cover.jpeg");
                if (!image.exists()) {
                    image = new File(f.getAbsolutePath() + "/cover.jpg");
                }
                if (!image.exists()) {
                    image = new File(f.getAbsolutePath() + "/cover.png");
                }
                
                Drawable icon;
                
                if (image.exists()) {
                    icon = Drawable.createFromPath(image.getAbsolutePath());
                } else {
                    icon = getResources().getDrawable(R.drawable.ic_drawer_library_small);
                }
                
                LibraryItem li = new LibraryItem(f.getName(), f.getAbsolutePath(), icon);
                
                System.out.println(li.getPath() + " " + li.getName());
                publishProgress(li);
            }
            return null;
        }
        
        @Override
        protected void onProgressUpdate( LibraryItem... progress ) {
        
            books.add(progress[0]);
            
            mPagerAdapter.notifyDataSetChanged();
            circleIndicator.notifyDataSetChanged();
        }
        
        @Override
        public void onPostExecute( Void result ) {
        
            circleIndicator.setCurrentItem(currentPos);
        }
        
    }
    
    public void setPage( int position ) {
    
        currentPos = position;
    }
    
    private class PageViewAdapter extends PagerAdapter {
        
        @Override
        public int getCount() {
        
            return (int) Math.ceil(books.size() / (double) booksPerView);
        }
        
        @Override
        public Object instantiateItem( View collection, int position ) {
        
            List<LibraryItem> list;
            
            int start, nElem;
            start = position * booksPerView;
            
            nElem = Math.min(books.size() - start, booksPerView);
            
            list = books.subList(start, start + nElem);
            
            LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            GridView gridview = (GridView) li.inflate(R.layout.fragment_kii_drawer_library_tabs, (ViewGroup) collection, false);
            gridview.setAdapter(new LibraryAdapter(getActivity(), list));
            
            ((ViewPager) collection).addView(gridview);
            
            return gridview;
        }
        
        @Override
        public void destroyItem( View collection, int position, Object view ) {
        
            ((ViewPager) collection).removeView((View) view);
        }
        
        @Override
        public boolean isViewFromObject( View view, Object object ) {
        
            return view == object;
        }
        
        @Override
        public int getItemPosition( Object object ) {
        
            return POSITION_NONE;
        }
    }
    
    @Override
    public int getNameResource() {
    
        return R.string.drawer_menu_library;
    }
    
    @Override
    public int getIconResource() {
    
        return R.drawable.ic_drawer_library;
    }
    
    @Override
    public boolean goUp() {
    
        return false;
    }
}
