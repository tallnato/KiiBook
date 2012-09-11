
package kii.kiibook.kiimarket.games;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import kii.kiibook.kiimarket.R;

public class BlankFragmentGames extends Fragment {
    
    private View mRoot;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.feature_fragment, container, false);
        ImageView image = (ImageView) mRoot.findViewById(R.id.image);
        image.setBackgroundResource(R.drawable.page2);
        return mRoot;
    }
}
