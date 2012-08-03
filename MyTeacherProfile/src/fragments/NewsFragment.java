
package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

import com.example.myteacherprofile.R;

public class NewsFragment extends Fragment implements OnTabChangeListener {
    
    public static final String TAG  = "NewsFragment";
    public static final String FRAG = "fragment";
    
    private View               mRoot;
    private String             fragment;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.news_frame, container, false);
        Bundle bundle = getArguments();
        
        int arg = bundle.getInt(FRAG);
        
        return mRoot;
    }
    
    @Override
    public void onTabChanged( String tabId ) {
    
        Toast.makeText(getActivity(), tabId, Toast.LENGTH_SHORT);
        // TODO Auto-generated method stub
        
    }
    
}
