
package kii.kiibook.kiimarket.books;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import kii.kiibook.kiimarket.R;

public class FeaturedFragmentBooks extends Fragment {
    
    private View mRoot;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        mRoot = inflater.inflate(R.layout.feature_fragment, container, false);
        ImageView image = (ImageView) mRoot.findViewById(R.id.image);
        image.setBackgroundResource(R.drawable.books_page1);
        
        return mRoot;
    }
}
