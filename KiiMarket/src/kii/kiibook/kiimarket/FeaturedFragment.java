
package kii.kiibook.kiimarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FeaturedFragment extends Fragment {
    
    private View mRoot;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        mRoot = inflater.inflate(R.layout.feature_fragment, container, false);
        
        return mRoot;
    }
}
