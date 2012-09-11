
package kii.kiibook.kiimarket;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class BooksPagerAdapter extends PagerAdapter {
    
    private final LayoutInflater inflater;
    
    public BooksPagerAdapter( LayoutInflater inf ) {
    
        inflater = inf;
    }
    
    @Override
    public int getCount() {
    
        return 4;
    }
    
    @Override
    public Object instantiateItem( View collection, int position ) {
    
        int id;
        if ((position == 0) || (position == 2)) {
            
            id = R.drawable.books_page1;
        } else {
            id = R.drawable.books_page2;
            
        }
        
        View layout = inflater.inflate(R.layout.feature_fragment, null);
        
        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setBackgroundResource(id);
        
        ((ViewPager) collection).addView(layout);
        
        return layout;
    }
    
    @Override
    public void destroyItem( View collection, int position, Object view ) {
    
        ((ViewPager) collection).removeView((View) view);
    }
    
    @Override
    public boolean isViewFromObject( View view, Object object ) {
    
        return view == object;
    }
}
